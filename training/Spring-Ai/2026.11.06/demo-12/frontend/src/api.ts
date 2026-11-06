/** 호텔 직원 답변을 글자가 만들어지는 대로 받는다. */
export async function streamAnswer(question: string, onChunk: (text: string) => void): Promise<void> {
  let response: Response
  try {
    response = await fetch(`/api/hotel/chat?question=${encodeURIComponent(question)}`)
  } catch {
    throw new Error('서버에 연결하지 못했습니다. 잠시 후 다시 시도해 주세요.')
  }

  if (!response.ok || !response.body) {
    throw new Error('답변을 받지 못했습니다. 잠시 후 다시 시도해 주세요.')
  }

  const reader = response.body.getReader()
  const decoder = new TextDecoder('utf-8')
  while (true) {
    const { done, value } = await reader.read()
    if (done) break
    onChunk(decoder.decode(value, { stream: true }))
  }
  const rest = decoder.decode()
  if (rest) onChunk(rest)
}
