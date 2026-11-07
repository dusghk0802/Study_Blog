import { useEffect, useRef, useState } from 'react'
import type { FormEvent } from 'react'
import { streamChat } from './api'
import type { AssistantId, Source } from './api'

interface Turn {
  id: string
  question: string
  answer: string
  sources: Source[]
  insufficient: boolean
  status: 'streaming' | 'done' | 'failed'
  error?: string
}

const REFUSAL = '근거를 찾지 못해'

export default function ChatPanel({ assistant, example }: { assistant: AssistantId; example: string }) {
  const [question, setQuestion] = useState('')
  const [turns, setTurns] = useState<Turn[]>([])
  const [topK, setTopK] = useState(4)
  const [threshold, setThreshold] = useState(0.3)
  const closeRef = useRef<(() => void) | null>(null)
  const listRef = useRef<HTMLDivElement>(null)

  useEffect(() => () => closeRef.current?.(), [])

  useEffect(() => {
    const list = listRef.current
    if (list) list.scrollTop = list.scrollHeight
  }, [turns])

  const busy = turns.some((turn) => turn.status === 'streaming')

  const update = (id: string, patch: (turn: Turn) => Partial<Turn>) =>
    setTurns((prev) => prev.map((turn) => (turn.id === id ? { ...turn, ...patch(turn) } : turn)))

  const run = (id: string, text: string) => {
    closeRef.current = streamChat(assistant, text, topK, threshold, {
      onSources: (sources, insufficient) => update(id, () => ({ sources, insufficient })),
      onToken: (token) => update(id, (turn) => ({ answer: turn.answer + token })),
      onDone: () => update(id, () => ({ status: 'done' })),
      onError: (message) => update(id, () => ({ status: 'failed', error: message })),
    })
  }

  const ask = (text: string) => {
    const value = text.trim()
    if (!value || busy) return
    const id = crypto.randomUUID()
    setTurns((prev) => [...prev, { id, question: value, answer: '', sources: [], insufficient: false, status: 'streaming' }])
    setQuestion('')
    run(id, value)
  }

  const retry = (turn: Turn) => {
    if (busy) return
    update(turn.id, () => ({ answer: '', sources: [], insufficient: false, status: 'streaming', error: undefined }))
    run(turn.id, turn.question)
  }

  const handleSubmit = (event: FormEvent) => {
    event.preventDefault()
    ask(question)
  }

  return (
    <div className="panel chat">
      <div className="chat-list" ref={listRef} aria-live="polite">
        {turns.length === 0 && (
          <div className="chat-empty">
            <p>문서를 올린 뒤 질문하면 문서 내용을 근거로 답변하고 출처를 함께 보여 줍니다.</p>
            <button type="button" className="chip" onClick={() => ask(example)}>{example}</button>
          </div>
        )}

        {turns.map((turn) => {
          const refused = turn.insufficient || turn.answer.includes(REFUSAL)
          return (
            <article key={turn.id} className="turn">
              <p className="turn-question">{turn.question}</p>
              {turn.answer && (
                <p className={`turn-answer${refused ? ' is-refused' : ''}`}>{turn.answer.replace(/\*\*/g, '')}</p>
              )}
              {turn.status === 'streaming' && !turn.answer && (
                <span className="progress" role="progressbar" aria-label="답변 생성 중"><span /></span>
              )}
              {turn.status === 'failed' && (
                <div className="turn-error">
                  <span>{turn.error}</span>
                  <button type="button" className="btn-text" disabled={busy} onClick={() => retry(turn)}>다시 시도</button>
                </div>
              )}
              {!refused && turn.sources.length > 0 && (
                <details className="sources">
                  <summary>출처 {turn.sources.length}건: {turn.sources[0].fileName}{turn.sources[0].page ? ` ${turn.sources[0].page}쪽` : ''}</summary>
                  <ul>
                    {turn.sources.map((source, index) => (
                      <li key={index}>
                        <strong>{source.fileName}{source.page ? ` · ${source.page}쪽` : ''}</strong>
                        <small>유사도 {source.score.toFixed(3)}</small>
                        <p>{source.snippet}</p>
                      </li>
                    ))}
                  </ul>
                </details>
              )}
            </article>
          )
        })}
      </div>

      <form className="composer" onSubmit={handleSubmit}>
        <div className="composer-row">
          <label htmlFor={`q-${assistant}`} className="sr-only">질문</label>
          <input
            id={`q-${assistant}`}
            type="text"
            value={question}
            autoComplete="off"
            placeholder={`예: ${example}`}
            onChange={(event) => setQuestion(event.target.value)}
          />
          <button type="submit" className="btn-primary" disabled={busy || !question.trim()}>질문</button>
        </div>
        <div className="options">
          <label>
            검색 개수(topK)
            <input type="number" min={1} max={10} value={topK} onChange={(event) => setTopK(Math.min(10, Math.max(1, Number(event.target.value) || 1)))} />
          </label>
          <label>
            유사도 임계값
            <input type="number" min={0} max={1} step={0.05} value={threshold} onChange={(event) => setThreshold(Math.min(1, Math.max(0, Number(event.target.value) || 0)))} />
          </label>
        </div>
      </form>
    </div>
  )
}
