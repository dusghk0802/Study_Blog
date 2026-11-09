export interface TravelPlan {
  planId: number
  tripTitle: string
  tripDate: string
  visitOrder: number | null
  placeName: string
  category: string | null
  address: string | null
  placeUrl: string | null
  memo: string | null
  createdAt: string
}

// 에이전트와 대화 (카카오맵 검색 + Oracle 일정 저장 Tool 호출)
export async function chat(conversationId: string, message: string): Promise<string> {
  const res = await fetch('/api/travel/chat', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ conversationId, message }),
  })
  if (!res.ok) throw new Error(`서버 오류 (${res.status})`)
  const data: { answer: string } = await res.json()
  return data.answer
}

export async function fetchPlans(): Promise<TravelPlan[]> {
  const res = await fetch('/api/travel/plans')
  if (!res.ok) throw new Error(`일정 조회 실패 (${res.status})`)
  return res.json()
}

export async function deletePlan(planId: number): Promise<void> {
  const res = await fetch(`/api/travel/plans/${planId}`, { method: 'DELETE' })
  if (!res.ok) throw new Error(`삭제 실패 (${res.status})`)
}
