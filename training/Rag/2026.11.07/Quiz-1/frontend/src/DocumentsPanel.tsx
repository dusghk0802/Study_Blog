import { useCallback, useEffect, useState } from 'react'
import type { ChangeEvent, DragEvent } from 'react'
import { deleteDocument, listDocuments, uploadDocument } from './api'
import type { AssistantId, DocumentInfo } from './api'

interface Notice { type: 'ok' | 'error'; text: string }

export default function DocumentsPanel({ assistant }: { assistant: AssistantId }) {
  const [documents, setDocuments] = useState<DocumentInfo[]>([])
  const [loading, setLoading] = useState(true)
  const [working, setWorking] = useState<string | null>(null)
  const [dragging, setDragging] = useState(false)
  const [notice, setNotice] = useState<Notice | null>(null)

  const refresh = useCallback(async () => {
    try {
      setDocuments(await listDocuments(assistant))
    } catch (error) {
      setNotice({ type: 'error', text: (error as Error).message })
    } finally {
      setLoading(false)
    }
  }, [assistant])

  useEffect(() => {
    void refresh()
  }, [refresh])

  const upload = async (files: File[]) => {
    setNotice(null)
    let last: Notice | null = null
    for (const file of files) {
      setWorking(`${file.name} 처리 중`)
      try {
        const result = await uploadDocument(assistant, file)
        last = { type: 'ok', text: `${result.fileName}: ${result.pages}쪽, 청크 ${result.chunks}개 저장` }
      } catch (error) {
        last = { type: 'error', text: `${file.name}: ${(error as Error).message}` }
        break
      }
    }
    setWorking(null)
    setNotice(last)
    await refresh()
  }

  const handleInput = (event: ChangeEvent<HTMLInputElement>) => {
    const files = Array.from(event.target.files ?? [])
    event.target.value = ''
    if (files.length > 0) void upload(files)
  }

  const handleDrop = (event: DragEvent<HTMLLabelElement>) => {
    event.preventDefault()
    setDragging(false)
    if (working) return
    const files = Array.from(event.dataTransfer.files)
    if (files.length > 0) void upload(files)
  }

  const remove = async (fileName: string) => {
    if (!window.confirm(`${fileName}을(를) 삭제할까요?`)) return
    setWorking(`${fileName} 삭제 중`)
    try {
      await deleteDocument(assistant, fileName)
      setNotice({ type: 'ok', text: `${fileName}을(를) 삭제했습니다.` })
    } catch (error) {
      setNotice({ type: 'error', text: (error as Error).message })
    }
    setWorking(null)
    await refresh()
  }

  return (
    <section className="panel" aria-labelledby={`docs-${assistant}`}>
      <h2 id={`docs-${assistant}`}>문서</h2>

      <label
        className={`dropzone${dragging ? ' is-dragging' : ''}${working ? ' is-disabled' : ''}`}
        onDragOver={(event) => { event.preventDefault(); setDragging(true) }}
        onDragLeave={() => setDragging(false)}
        onDrop={handleDrop}
      >
        <input type="file" accept="application/pdf,.pdf" multiple disabled={!!working} onChange={handleInput} />
        <strong>{working ?? 'PDF 업로드'}</strong>
        <span>{working ? '텍스트 추출과 임베딩을 하고 있습니다' : '끌어다 놓거나 눌러서 선택 · 20MB 이하'}</span>
      </label>

      {notice && <p className={`notice notice-${notice.type}`} role="status">{notice.text}</p>}

      {loading ? (
        <p className="empty">불러오는 중</p>
      ) : documents.length === 0 ? (
        <p className="empty">아직 올린 문서가 없습니다.</p>
      ) : (
        <ul className="doc-list">
          {documents.map((doc) => (
            <li key={doc.fileName}>
              <div className="doc-info">
                <span className="doc-name" title={doc.fileName}>{doc.fileName}</span>
                <small>청크 {doc.chunks}개 · {doc.uploadedAt}</small>
              </div>
              <button type="button" className="btn-text" disabled={!!working} onClick={() => void remove(doc.fileName)}>삭제</button>
            </li>
          ))}
        </ul>
      )}
    </section>
  )
}
