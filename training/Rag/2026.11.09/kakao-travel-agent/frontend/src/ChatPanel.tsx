import { useEffect, useRef, useState, type FormEvent, type KeyboardEvent } from 'react'
import { marked } from 'marked'
import { chat } from './api'

interface Message {
  role: 'user' | 'bot' | 'error'
  text: string
}

const WELCOME: Message = {
  role: 'bot',
  text: '안녕하세요! 여행지와 날짜를 알려주시면 장소를 찾아 일정을 짜 드릴게요. 마음에 들면 **"저장해줘"** 라고 말씀해 주세요.',
}

const SUGGESTIONS = [
  '이번 주말 강릉 바다 근처 맛집 추천해줘',
  '부산 해운대 1박2일 코스 짜줘',
  '방금 추천한 일정 저장해줘',
  '저장된 여행 일정 보여줘',
]

interface Props {
  onAnswered: () => void
}

export default function ChatPanel({ onAnswered }: Props) {
  const [conversationId, setConversationId] = useState(() => crypto.randomUUID())
  const [messages, setMessages] = useState<Message[]>([WELCOME])
  const [input, setInput] = useState('')
  const [sending, setSending] = useState(false)
  const listRef = useRef<HTMLDivElement>(null)

  useEffect(() => {
    listRef.current?.scrollTo({ top: listRef.current.scrollHeight, behavior: 'smooth' })
  }, [messages, sending])

  async function send(text: string) {
    const message = text.trim()
    if (!message || sending) return
    setMessages((prev) => [...prev, { role: 'user', text: message }])
    setInput('')
    setSending(true)
    try {
      const answer = await chat(conversationId, message)
      setMessages((prev) => [...prev, { role: 'bot', text: answer }])
      onAnswered()
    } catch (e) {
      const reason = e instanceof Error ? e.message : '알 수 없는 오류'
      setMessages((prev) => [...prev, { role: 'error', text: `요청 실패: ${reason}` }])
    } finally {
      setSending(false)
    }
  }

  function handleSubmit(e: FormEvent) {
    e.preventDefault()
    send(input)
  }

  function handleKeyDown(e: KeyboardEvent<HTMLTextAreaElement>) {
    // 한글 조합 중 Enter 는 무시 (마지막 글자 중복 전송 방지)
    if (e.key === 'Enter' && !e.shiftKey && !e.nativeEvent.isComposing) {
      e.preventDefault()
      send(input)
    }
  }

  function resetConversation() {
    setConversationId(crypto.randomUUID())
    setMessages([{ role: 'bot', text: '새 대화를 시작했어요. 어디로 떠나실 건가요?' }])
  }

  return (
    <section className="card">
      <div className="card-title">
        대화
        <button type="button" className="ghost-btn" onClick={resetConversation} disabled={sending}>
          새 대화
        </button>
      </div>

      <div className="messages" ref={listRef}>
        {messages.map((m, i) =>
          m.role === 'bot' ? (
            <div
              key={i}
              className="msg bot"
              dangerouslySetInnerHTML={{ __html: marked.parse(m.text, { async: false }) }}
            />
          ) : (
            <div key={i} className={`msg ${m.role}`}>
              {m.text}
            </div>
          ),
        )}
        {sending && (
          <div className="msg bot typing">
            <span className="dot" />
            <span className="dot" />
            <span className="dot" />
            장소를 찾아보는 중...
          </div>
        )}
      </div>

      <div className="chips">
        {SUGGESTIONS.map((s) => (
          <button key={s} type="button" className="chip" onClick={() => send(s)} disabled={sending}>
            {s}
          </button>
        ))}
      </div>

      <form className="composer" onSubmit={handleSubmit}>
        <textarea
          value={input}
          onChange={(e) => setInput(e.target.value)}
          onKeyDown={handleKeyDown}
          placeholder="예) 다음 주 토요일 전주 한옥마을 근처 맛집이랑 카페 추천해줘"
        />
        <button type="submit" className="send-btn" disabled={sending || !input.trim()}>
          전송
        </button>
      </form>
    </section>
  )
}
