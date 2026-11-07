export type AssistantId = 'A' | 'B' | 'C'

export interface DocumentInfo { fileName: string; chunks: number; uploadedAt: string }
export interface UploadResult { fileName: string; pages: number; chunks: number }
export interface Source { fileName: string; page: number | null; score: number; snippet: string }
export interface EvalConfig { topK: number; threshold: number; hits: number; inScope: number; rejected: number; outOfScope: number; accuracy: number }
export interface EvalRow { question: string; inScope: boolean; topFile: string | null; topPage: number | null; topScore: number | null; firstHitRank: number | null }
export interface EvalReport { questions: number; configs: EvalConfig[]; rows: EvalRow[] }

async function request<T>(url: string, init?: RequestInit): Promise<T> {
  let response: Response
  try {
    response = await fetch(url, init)
  } catch {
    throw new Error('서버에 연결하지 못했습니다.')
  }
  const text = await response.text()
  if (!response.ok) {
    let message = '요청을 처리하지 못했습니다.'
    try { message = JSON.parse(text).message ?? message } catch { /* JSON이 아니면 기본 문구를 쓴다 */ }
    throw new Error(message)
  }
  return (text ? JSON.parse(text) : undefined) as T
}

export const listDocuments = (id: AssistantId) => request<DocumentInfo[]>(`/api/${id}/documents`)

export const deleteDocument = (id: AssistantId, fileName: string) =>
  request<void>(`/api/${id}/documents?fileName=${encodeURIComponent(fileName)}`, { method: 'DELETE' })

export function uploadDocument(id: AssistantId, file: File) {
  const form = new FormData()
  form.append('file', file)
  return request<UploadResult>(`/api/${id}/documents`, { method: 'POST', body: form })
}

export const evaluate = (id: AssistantId) => request<EvalReport>(`/api/${id}/evaluate`, { method: 'POST' })

interface ChatHandlers {
  onSources: (sources: Source[], insufficient: boolean) => void
  onToken: (text: string) => void
  onDone: () => void
  onError: (message: string) => void
}

/** 답변을 SSE로 받는다. 돌려준 함수를 호출하면 연결을 끊는다. */
export function streamChat(id: AssistantId, question: string, topK: number, threshold: number, handlers: ChatHandlers) {
  const params = new URLSearchParams({ question, topK: String(topK), threshold: String(threshold) })
  const source = new EventSource(`/api/${id}/chat?${params}`)
  let finished = false

  const finish = () => {
    finished = true
    source.close()
  }

  source.addEventListener('sources', (event) => {
    const data = JSON.parse((event as MessageEvent).data)
    handlers.onSources(data.sources, data.insufficient)
  })
  source.addEventListener('token', (event) => handlers.onToken(JSON.parse((event as MessageEvent).data).text))
  source.addEventListener('done', () => {
    finish()
    handlers.onDone()
  })
  source.addEventListener('failure', (event) => {
    finish()
    handlers.onError(JSON.parse((event as MessageEvent).data).message)
  })
  source.onerror = () => {
    if (finished) return
    finish()
    handlers.onError('서버에 연결하지 못했습니다.')
  }
  return finish
}
