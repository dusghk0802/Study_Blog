import { useEffect, useRef, useState } from 'react'
import type { FormEvent, KeyboardEvent } from 'react'
import { streamAnswer } from './api'

type Role = 'staff' | 'guest'

interface Message {
  id: string
  role: Role
  text: string
  time: string
  failed?: boolean
  question?: string
}

const QUICK_QUESTIONS = [
  '체크인 시간이 언제인가요?',
  '취소 수수료가 어떻게 되나요?',
  '반려동물과 함께 묵을 수 있나요?',
  '룸 서비스는 몇 시까지 되나요?',
]

function now(): string {
  return new Date().toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit' })
}

// 모델이 쓰는 마크다운 굵은 글씨 기호는 채팅 말풍선에서 보이지 않게 한다
function clean(text: string): string {
  return text.replace(/\*\*/g, '')
}

export default function App() {
  const [messages, setMessages] = useState<Message[]>(() => [
    {
      id: 'welcome',
      role: 'staff',
      text: '안녕하세요, LuxeStay Hotel입니다. 무엇을 도와드릴까요?',
      time: now(),
    },
  ])
  const [input, setInput] = useState('')
  const [loading, setLoading] = useState(false)
  const [waiting, setWaiting] = useState(false)
  const listRef = useRef<HTMLDivElement>(null)
  const inputRef = useRef<HTMLTextAreaElement>(null)

  useEffect(() => {
    const list = listRef.current
    if (list) list.scrollTop = list.scrollHeight
  }, [messages, waiting])

  useEffect(() => {
    const box = inputRef.current
    if (!box) return
    box.style.height = 'auto'
    // scrollHeight에는 위아래 테두리(1px씩)가 포함되지 않는다
    box.style.height = `${Math.min(box.scrollHeight + 2, 120)}px`
  }, [input])

  const answer = async (question: string) => {
    const staffId = crypto.randomUUID()
    setLoading(true)
    setWaiting(true)
    let started = false

    try {
      await streamAnswer(question, (chunk) => {
        if (!started) {
          started = true
          setWaiting(false)
          setMessages((prev) => [...prev, { id: staffId, role: 'staff', text: chunk, time: now() }])
        } else {
          setMessages((prev) =>
            prev.map((m) => (m.id === staffId ? { ...m, text: m.text + chunk } : m)),
          )
        }
      })
      if (!started) throw new Error('답변을 받지 못했습니다. 잠시 후 다시 시도해 주세요.')
    } catch (error) {
      const text = error instanceof Error ? error.message : '답변을 받지 못했습니다.'
      setMessages((prev) => [
        ...prev.filter((m) => m.id !== staffId),
        { id: staffId, role: 'staff', text, time: now(), failed: true, question },
      ])
    } finally {
      setWaiting(false)
      setLoading(false)
      inputRef.current?.focus()
    }
  }

  const send = (text: string) => {
    const question = text.trim()
    if (!question || loading) return
    setMessages((prev) => [...prev, { id: crypto.randomUUID(), role: 'guest', text: question, time: now() }])
    setInput('')
    void answer(question)
  }

  const retry = (message: Message) => {
    if (loading || !message.question) return
    setMessages((prev) => prev.filter((m) => m.id !== message.id))
    void answer(message.question)
  }

  const handleSubmit = (event: FormEvent) => {
    event.preventDefault()
    send(input)
  }

  const handleKeyDown = (event: KeyboardEvent<HTMLTextAreaElement>) => {
    // 한글 조합 중 Enter는 글자 확정용이라 전송하지 않는다
    if (event.key === 'Enter' && !event.shiftKey && !event.nativeEvent.isComposing) {
      event.preventDefault()
      send(input)
    }
  }

  const showQuick = messages.length === 1

  return (
    <div className="page">
      <main className="chat" aria-label="호텔 고객 상담">
        <header className="chat-header">
          <span className="avatar avatar-lg" aria-hidden="true">L</span>
          <div className="chat-title">
            <h1>LuxeStay Hotel</h1>
            <p>고객 상담 · 호텔 안내 자료를 바탕으로 AI가 답변합니다</p>
          </div>
        </header>

        <div className="messages" ref={listRef} role="log" aria-live="polite">
          {messages.map((message) => (
            <div key={message.id} className={`row row-${message.role}`}>
              {message.role === 'staff' && <span className="avatar" aria-hidden="true">L</span>}
              <div className="content">
                {message.role === 'staff' && <span className="sender">호텔 직원</span>}
                <div className={`bubble${message.failed ? ' bubble-error' : ''}`}>{clean(message.text)}</div>
                <div className="meta">
                  <time>{message.time}</time>
                  {message.failed && (
                    <button type="button" className="link" disabled={loading} onClick={() => retry(message)}>
                      다시 시도
                    </button>
                  )}
                </div>
              </div>
            </div>
          ))}

          {waiting && (
            <div className="row row-staff">
              <span className="avatar" aria-hidden="true">L</span>
              <div className="content">
                <span className="sender">호텔 직원</span>
                <div className="bubble typing" role="status" aria-label="답변을 작성하고 있습니다">
                  <i /><i /><i />
                </div>
              </div>
            </div>
          )}

          {showQuick && (
            <div className="quick" aria-label="자주 묻는 질문">
              {QUICK_QUESTIONS.map((question) => (
                <button key={question} type="button" className="chip" onClick={() => send(question)}>
                  {question}
                </button>
              ))}
            </div>
          )}
        </div>

        <form className="composer" onSubmit={handleSubmit}>
          <label htmlFor="message" className="sr-only">메시지</label>
          <textarea
            id="message"
            ref={inputRef}
            rows={1}
            value={input}
            onChange={(event) => setInput(event.target.value)}
            onKeyDown={handleKeyDown}
            placeholder="메시지를 입력하세요"
            autoFocus
          />
          <button type="submit" className="send" disabled={loading || !input.trim()}>전송</button>
        </form>
      </main>
    </div>
  )
}
