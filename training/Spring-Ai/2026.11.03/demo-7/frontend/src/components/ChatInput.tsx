import { useState, type KeyboardEvent } from 'react'

type Props = {
  onSend: (question: string) => void
  disabled: boolean
}

const MAX = 1000

function ChatInput({ onSend, disabled }: Props) {
  const [text, setText] = useState('')

  const submit = () => {
    const q = text.trim()
    if (!q || disabled) return
    onSend(q)
    setText('')
  }

  // Enter 전송, Shift+Enter 줄바꿈 (한글 조합 중에는 무시)
  const onKeyDown = (e: KeyboardEvent<HTMLTextAreaElement>) => {
    if (e.key === 'Enter' && !e.shiftKey && !e.nativeEvent.isComposing) {
      e.preventDefault()
      submit()
    }
  }

  return (
    <footer className="composer">
      <div className="composer-box">
        <textarea
          rows={1}
          value={text}
          maxLength={MAX}
          placeholder="질문을 입력하세요"
          onChange={e => setText(e.target.value)}
          onKeyDown={onKeyDown}
        />
        <button type="button" className="btn-send" onClick={submit} disabled={disabled || !text.trim()}>
          {disabled ? <span className="spinner" /> : '전송'}
        </button>
      </div>
      <div className="composer-hint">
        <span>Enter 전송 · Shift + Enter 줄바꿈</span>
        <span>{text.length} / {MAX}</span>
      </div>
    </footer>
  )
}

export default ChatInput
