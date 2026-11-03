import { useEffect, useRef, useState } from 'react'
import type { ChatMessage } from '../types'

type Props = {
  messages: ChatMessage[]
  loading: boolean
  model: string
  onSuggest: (question: string) => void
}

const SUGGESTIONS = [
  '거북선을 만든 한국의 유명한 장군을 알려줘',
  'Spring AI의 ChatClient를 쉽게 설명해줘',
  'REST API와 SSE의 차이를 표로 정리해줘',
  '오늘 점심 메뉴 3가지만 추천해줘'
]

const timeText = (iso: string) =>
  new Date(iso).toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit' })

function MessageList({ messages, loading, model, onSuggest }: Props) {
  const bottomRef = useRef<HTMLDivElement>(null)
  const [copied, setCopied] = useState<number | null>(null)

  // 새 메시지가 오면 맨 아래로 스크롤
  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: 'smooth' })
  }, [messages, loading])

  const copy = async (i: number, text: string) => {
    await navigator.clipboard.writeText(text)
    setCopied(i)
    setTimeout(() => setCopied(null), 1500)
  }

  if (messages.length === 0 && !loading) {
    return (
      <div className="empty">
        <div className="empty-logo">AI</div>
        <h1>무엇을 도와드릴까요?</h1>
        <p>{model ? `${model} 모델이 답변합니다.` : '모델을 불러오는 중입니다.'} 아래 예시를 눌러 바로 시작해 보세요.</p>
        <div className="suggestions">
          {SUGGESTIONS.map(s => (
            <button key={s} type="button" className="suggestion" onClick={() => onSuggest(s)} disabled={!model}>
              {s}
            </button>
          ))}
        </div>
      </div>
    )
  }

  return (
    <div className="messages">
      {messages.map((m, i) => (
        <div key={i} className={`msg msg-${m.role}`}>
          {m.role !== 'user' && <div className="avatar">{m.role === 'error' ? '!' : 'AI'}</div>}
          <div className="bubble-wrap">
            <div className="bubble">{m.text}</div>
            <div className="meta">
              {m.role === 'assistant' && m.model && <span className="model-tag">{m.model}</span>}
              <span>{timeText(m.time)}</span>
              {m.role === 'assistant' && (
                <button type="button" className="link-btn" onClick={() => copy(i, m.text)}>
                  {copied === i ? '복사됨 ✓' : '복사'}
                </button>
              )}
            </div>
          </div>
        </div>
      ))}

      {loading && (
        <div className="msg msg-assistant">
          <div className="avatar">AI</div>
          <div className="bubble typing">
            <span /><span /><span />
          </div>
        </div>
      )}
      <div ref={bottomRef} />
    </div>
  )
}

export default MessageList
