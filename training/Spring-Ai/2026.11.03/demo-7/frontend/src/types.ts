// 대화 한 건 (서버 ChatMessage와 같은 형태, error는 화면에서만 사용)
export type ChatMessage = {
  role: 'user' | 'assistant' | 'error'
  text: string
  model?: string | null
  time: string
}

// 대화방 하나
export type Conversation = {
  id: string
  title: string
  model: string
  createdAt: string
  updatedAt: string
  messages: ChatMessage[]
}

// 왼쪽 대화 목록
export type ConversationSummary = {
  id: string
  title: string
  model: string
  updatedAt: string
  messageCount: number
}

// 선택 가능한 Ollama 모델
export type ModelInfo = {
  name: string
  parameterSize: string | null
  size: number
}
