const UPLOAD_URL = '/api/documents/upload'
const RAG_URL = '/api/documents/rag'
const SIMPLE_URL = '/api/documents/simple'

export type AskMode = 'rag' | 'simple'

/** 업로드 진행률을 보여 주기 위해 fetch 대신 XMLHttpRequest를 쓴다. */
export function uploadPdf(file: File, onProgress: (percent: number) => void): Promise<string> {
  return new Promise((resolve, reject) => {
    const form = new FormData()
    form.append('file', file)

    const xhr = new XMLHttpRequest()
    xhr.open('POST', UPLOAD_URL)

    xhr.upload.onprogress = (event) => {
      if (event.lengthComputable) {
        onProgress(Math.round((event.loaded / event.total) * 100))
      }
    }

    xhr.onload = () => {
      if (xhr.status >= 200 && xhr.status < 300) {
        resolve(xhr.responseText || '처리가 완료되었습니다.')
      } else {
        reject(new Error(errorMessage(xhr.status, xhr.responseText)))
      }
    }

    xhr.onerror = () => reject(new Error('서버에 연결하지 못했습니다. 백엔드가 실행 중인지 확인해 주세요.'))
    xhr.send(form)
  })
}

export async function askQuestion(question: string, mode: AskMode): Promise<string> {
  // /rag는 폼 파라미터(@RequestParam), /simple은 JSON(@RequestBody)으로 받는다
  const request: RequestInit = mode === 'rag'
    ? {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' },
        body: new URLSearchParams({ question }),
      }
    : {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ question }),
      }
  const url = mode === 'rag' ? RAG_URL : SIMPLE_URL

  let response: Response
  try {
    response = await fetch(url, request)
  } catch {
    throw new Error('서버에 연결하지 못했습니다. 백엔드가 실행 중인지 확인해 주세요.')
  }

  const text = await response.text()
  if (!response.ok) {
    if (response.status === 404) throw new Error(`질문 API를 찾지 못했습니다. (POST ${url})`)
    throw new Error(errorMessage(response.status, text))
  }
  return text
}

function errorMessage(status: number, body: string): string {
  if (status === 404) return '업로드 API를 찾지 못했습니다. (POST /api/documents/upload)'
  if (status === 413) return '파일이 서버의 업로드 용량 제한보다 큽니다.'

  // 스프링 기본 에러는 JSON으로 오기 때문에 그대로 보여 주지 않는다
  const text = body?.trim()
  if (text && !text.startsWith('{') && !text.startsWith('<')) return text
  if (status >= 500 && !text) return '서버에 연결하지 못했습니다. 백엔드가 실행 중인지 확인해 주세요.'
  return `요청을 처리하지 못했습니다. (${status})`
}
