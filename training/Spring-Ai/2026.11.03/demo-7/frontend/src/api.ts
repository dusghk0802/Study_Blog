import type { Conversation, ConversationSummary, ModelInfo } from './types'

// 스프링부트 ChatController 호출
async function request<T>(url: string, init?: RequestInit): Promise<T> {
  const res = await fetch(url, init)
  if (!res.ok) throw new Error(`서버 오류 (${res.status})`)
  const text = await res.text()
  return (text ? JSON.parse(text) : undefined) as T
}

export const api = {
  models: () => request<ModelInfo[]>('/api/models'),

  conversations: () => request<ConversationSummary[]>('/api/conversations'),

  conversation: (id: string) => request<Conversation>(`/api/conversations/${id}`),

  deleteConversation: (id: string) => request<void>(`/api/conversations/${id}`, { method: 'DELETE' }),

  chat: (conversationId: string | null, question: string, model: string) =>
    request<Conversation>('/api/chat', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ conversationId, question, model })
    })
}
