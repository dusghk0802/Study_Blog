import type { ConversationSummary } from '../types'

type Props = {
  open: boolean
  conversations: ConversationSummary[]
  currentId: string | null
  disabled: boolean
  onNew: () => void
  onOpen: (id: string) => void
  onDelete: (id: string) => void
  onClose: () => void
}

// 오늘 / 어제 / 이전 7일 / 이전 으로 묶기
function groupLabel(iso: string) {
  const d = new Date(iso)
  const today = new Date()
  today.setHours(0, 0, 0, 0)
  const diff = Math.floor((today.getTime() - new Date(d.getFullYear(), d.getMonth(), d.getDate()).getTime()) / 86400000)
  if (diff <= 0) return '오늘'
  if (diff === 1) return '어제'
  if (diff < 7) return '이전 7일'
  return '이전'
}

function Sidebar({ open, conversations, currentId, disabled, onNew, onOpen, onDelete, onClose }: Props) {
  const groups: { label: string; items: ConversationSummary[] }[] = []
  for (const c of conversations) {
    const label = groupLabel(c.updatedAt)
    const g = groups.find(x => x.label === label)
    if (g) g.items.push(c)
    else groups.push({ label, items: [c] })
  }

  return (
    <>
      <div className={`backdrop ${open ? 'show' : ''}`} onClick={onClose} />
      <aside className={`sidebar ${open ? 'open' : ''}`}>
        <div className="sidebar-top">
          <div className="brand">
            <div className="brand-logo">AI</div>
            <div className="brand-name">Ollama AI</div>
          </div>
          <button type="button" className="btn-new" onClick={onNew} disabled={disabled}>
            + 새 대화
          </button>
        </div>

        <nav className="history">
          {conversations.length === 0 && <p className="history-empty">아직 대화 기록이 없습니다.</p>}
          {groups.map(g => (
            <div key={g.label} className="history-group">
              <div className="history-label">{g.label}</div>
              {g.items.map(c => (
                <div
                  key={c.id}
                  className={`history-item ${c.id === currentId ? 'active' : ''}`}
                  onClick={() => onOpen(c.id)}
                >
                  <div className="history-text">
                    <div className="history-title">{c.title}</div>
                    <div className="history-meta">{c.model} · {c.messageCount / 2}회</div>
                  </div>
                  <button
                    type="button"
                    className="history-delete"
                    title="삭제"
                    onClick={e => { e.stopPropagation(); onDelete(c.id) }}
                  >
                    ✕
                  </button>
                </div>
              ))}
            </div>
          ))}
        </nav>
      </aside>
    </>
  )
}

export default Sidebar
