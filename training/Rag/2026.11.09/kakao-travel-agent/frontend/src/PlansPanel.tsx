import { useState } from 'react'
import { deletePlan, type TravelPlan } from './api'

interface Props {
  plans: TravelPlan[]
  error: string | null
  loading: boolean
  onRefresh: () => void
  onDeleted: () => void
}

// 여행 이름 + 날짜별로 묶기
function groupPlans(plans: TravelPlan[]) {
  const groups = new Map<string, TravelPlan[]>()
  for (const p of plans) {
    const key = `${p.tripTitle} · ${p.tripDate}`
    groups.set(key, [...(groups.get(key) ?? []), p])
  }
  return [...groups.entries()]
}

export default function PlansPanel({ plans, error, loading, onRefresh, onDeleted }: Props) {
  const [deletingId, setDeletingId] = useState<number | null>(null)

  async function handleDelete(plan: TravelPlan) {
    if (!confirm(`'${plan.placeName}' 일정을 삭제할까요?`)) return
    setDeletingId(plan.planId)
    try {
      await deletePlan(plan.planId)
      onDeleted()
    } catch (e) {
      alert(e instanceof Error ? e.message : '삭제에 실패했어요.')
    } finally {
      setDeletingId(null)
    }
  }

  return (
    <section className="card">
      <div className="card-title">
        저장된 여행 일정
        <button type="button" className="ghost-btn" onClick={onRefresh} disabled={loading}>
          {loading ? '불러오는 중' : '새로고침'}
        </button>
      </div>

      <div className="plans">
        {error && <div className="empty error-text">{error}</div>}
        {!error && plans.length === 0 && <div className="empty">아직 저장된 일정이 없어요.</div>}

        {groupPlans(plans).map(([title, list]) => (
          <div className="trip" key={title}>
            <h3>{title}</h3>
            {list.map((p) => (
              <div className="plan" key={p.planId}>
                <div className="no">{p.visitOrder ?? ''}</div>
                <div className="plan-body">
                  <div className="plan-name">
                    {/* 카카오 검색으로 확인된 장소 상세 페이지 */}
                    {p.placeUrl ? (
                      <a href={p.placeUrl} target="_blank" rel="noopener noreferrer" title="카카오맵 상세 페이지">
                        {p.placeName}
                      </a>
                    ) : (
                      p.placeName
                    )}
                    {p.category && <span className="tag">{p.category}</span>}
                  </div>
                  {p.address && <div className="meta">{p.address}</div>}
                  {p.memo && <div className="meta">{p.memo}</div>}
                </div>
                <button
                  type="button"
                  className="del-btn"
                  onClick={() => handleDelete(p)}
                  disabled={deletingId === p.planId}
                >
                  삭제
                </button>
              </div>
            ))}
          </div>
        ))}
      </div>
    </section>
  )
}
