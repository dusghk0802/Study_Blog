import { useCallback, useEffect, useState } from 'react'
import ChatPanel from './ChatPanel'
import PlansPanel from './PlansPanel'
import { fetchPlans, type TravelPlan } from './api'

export default function App() {
  const [plans, setPlans] = useState<TravelPlan[]>([])
  const [plansError, setPlansError] = useState<string | null>(null)
  const [loadingPlans, setLoadingPlans] = useState(false)

  const loadPlans = useCallback(async () => {
    setLoadingPlans(true)
    try {
      setPlans(await fetchPlans())
      setPlansError(null)
    } catch (e) {
      setPlansError(e instanceof Error ? e.message : '일정을 불러오지 못했어요.')
    } finally {
      setLoadingPlans(false)
    }
  }, [])

  useEffect(() => {
    loadPlans()
  }, [loadPlans])

  return (
    <div className="app">
      <header className="topbar">
        <div className="logo">K</div>
        <h1>여행·맛집 추천 에이전트</h1>
        <span className="subtitle">카카오맵 MCP + Oracle 일정 저장</span>
      </header>

      <main className="layout">
        {/* 에이전트가 일정을 저장/삭제할 수 있으므로 답변을 받을 때마다 목록을 다시 불러온다 */}
        <ChatPanel onAnswered={loadPlans} />
        <PlansPanel
          plans={plans}
          error={plansError}
          loading={loadingPlans}
          onRefresh={loadPlans}
          onDeleted={loadPlans}
        />
      </main>
    </div>
  )
}
