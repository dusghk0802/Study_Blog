import { useState } from 'react'
import type { FormEvent } from 'react'
import { askQuestion } from './api'
import type { AskMode } from './api'

interface Qa {
  id: string
  question: string
  mode: AskMode
  answer?: string
  error?: string
}

export default function AskPanel() {
  const [question, setQuestion] = useState('')
  const [history, setHistory] = useState<Qa[]>([])
  const [loading, setLoading] = useState(false)

  const run = async (id: string, text: string, mode: AskMode) => {
    setLoading(true)
    try {
      const answer = await askQuestion(text, mode)
      setHistory((prev) => prev.map((qa) => (qa.id === id ? { ...qa, answer, error: undefined } : qa)))
    } catch (error) {
      const message = error instanceof Error ? error.message : '답변을 받지 못했습니다.'
      setHistory((prev) => prev.map((qa) => (qa.id === id ? { ...qa, error: message } : qa)))
    } finally {
      setLoading(false)
    }
  }

  const ask = (mode: AskMode) => {
    const text = question.trim()
    if (!text || loading) return

    const id = crypto.randomUUID()
    setHistory((prev) => [{ id, question: text, mode }, ...prev])
    setQuestion('')
    void run(id, text, mode)
  }

  // Enter를 누르면 PDF 질문으로 보낸다
  const handleSubmit = (event: FormEvent) => {
    event.preventDefault()
    ask('rag')
  }

  const retry = (qa: Qa) => {
    setHistory((prev) => prev.map((item) => (item.id === qa.id ? { ...item, error: undefined } : item)))
    void run(qa.id, qa.question, qa.mode)
  }

  return (
    <section className="panel" aria-labelledby="ask-title">
      <div className="panel-head">
        <div>
          <h2 id="ask-title">AI에게 질문하기</h2>
          <p className="summary">PDF로 질문하면 업로드한 문서에서 찾아 답하고, 일반 질문은 문서 없이 답합니다.</p>
        </div>
      </div>

      <form className="ask-form" onSubmit={handleSubmit}>
        <label htmlFor="ask-input" className="sr-only">질문</label>
        <input
          id="ask-input"
          type="text"
          value={question}
          onChange={(event) => setQuestion(event.target.value)}
          placeholder="예: 구글 딥마인드가 발표한 AI 모델은 무엇인가요?"
          autoComplete="off"
        />
        <div className="ask-actions">
          <button type="submit" className="btn-primary" disabled={loading || !question.trim()}>
            PDF로 질문
          </button>
          <button type="button" className="btn-secondary" disabled={loading || !question.trim()} onClick={() => ask('simple')}>
            일반 질문
          </button>
        </div>
      </form>

      {history.length > 0 && (
        <ul className="qa-list" aria-live="polite">
          {history.map((qa) => (
            <li key={qa.id} className="qa-item">
              <div className="qa-head">
                <span className={`qa-mode qa-mode-${qa.mode}`}>{qa.mode === 'rag' ? 'PDF' : '일반'}</span>
                <p className="qa-question">{qa.question}</p>
              </div>
              {qa.answer && <p className="qa-answer">{qa.answer}</p>}
              {qa.error && (
                <div className="qa-error">
                  <span>{qa.error}</span>
                  <button type="button" className="btn-text" disabled={loading} onClick={() => retry(qa)}>다시 시도</button>
                </div>
              )}
              {!qa.answer && !qa.error && (
                <span className="progress is-indeterminate" role="progressbar" aria-label="답변 생성 중">
                  <span />
                </span>
              )}
            </li>
          ))}
        </ul>
      )}
    </section>
  )
}
