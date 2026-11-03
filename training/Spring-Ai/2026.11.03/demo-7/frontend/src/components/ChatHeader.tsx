import type { ModelInfo } from '../types'

type Props = {
  models: ModelInfo[]
  model: string
  onModelChange: (name: string) => void
  disabled: boolean
  onMenu: () => void
}

const gb = (bytes: number) => `${(bytes / 1024 ** 3).toFixed(1)}GB`

function ChatHeader({ models, model, onModelChange, disabled, onMenu }: Props) {
  return (
    <header className="topbar">
      <div className="topbar-left">
        <button type="button" className="btn-menu" onClick={onMenu} aria-label="대화 기록 열기">☰</button>
        <label className="model-select">
          <span className="status-dot" />
          <select value={model} onChange={e => onModelChange(e.target.value)} disabled={disabled || models.length === 0}>
            {models.length === 0 && <option value="">모델 없음</option>}
            {models.map(m => (
              <option key={m.name} value={m.name}>
                {m.name}{m.parameterSize ? ` · ${m.parameterSize}` : ''} · {gb(m.size)}
              </option>
            ))}
          </select>
        </label>
      </div>
      <span className="topbar-note">로컬 Ollama 모델 · 모델이 클수록 정확도가 높습니다</span>
    </header>
  )
}

export default ChatHeader
