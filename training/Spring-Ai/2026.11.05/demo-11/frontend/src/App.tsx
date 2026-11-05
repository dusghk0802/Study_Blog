import { useEffect, useRef, useState } from 'react'
import type { ChangeEvent, DragEvent } from 'react'
import { uploadPdf } from './api'
import AskPanel from './AskPanel'

type Status = 'waiting' | 'uploading' | 'processing' | 'done' | 'failed'

interface UploadItem {
  id: string
  file: File
  status: Status
  percent: number
  message?: string
  finishedAt?: Date
}

const STATUS_LABEL: Record<Status, string> = {
  waiting: '대기',
  uploading: '업로드 중',
  processing: '임베딩 중',
  done: '완료',
  failed: '실패',
}

const PIPELINE = [
  { title: 'PDF 읽기', detail: '페이지 단위로 텍스트 추출' },
  { title: '청크 분할', detail: '청크당 최대 1,000 토큰' },
  { title: '임베딩', detail: '1,536차원 벡터로 변환' },
  { title: '벡터 저장', detail: 'PostgreSQL pgvector' },
]

function formatSize(bytes: number): string {
  if (bytes < 1024 * 1024) return `${Math.max(1, Math.round(bytes / 1024))} KB`
  return `${(bytes / (1024 * 1024)).toFixed(1)} MB`
}

function isPdf(file: File): boolean {
  return file.type === 'application/pdf' || file.name.toLowerCase().endsWith('.pdf')
}

export default function App() {
  const [items, setItems] = useState<UploadItem[]>([])
  const [dragging, setDragging] = useState(false)
  const [notice, setNotice] = useState('')
  const runningRef = useRef(false)

  const update = (id: string, patch: Partial<UploadItem>) =>
    setItems((prev) => prev.map((item) => (item.id === id ? { ...item, ...patch } : item)))

  const addFiles = (files: File[]) => {
    const pdfs = files.filter(isPdf)
    const skipped = files.length - pdfs.length
    setNotice(skipped > 0 ? `PDF가 아닌 파일 ${skipped}개는 제외했습니다.` : '')
    if (pdfs.length === 0) return

    const added = pdfs.map((file): UploadItem => ({
      id: crypto.randomUUID(),
      file,
      status: 'waiting',
      percent: 0,
    }))
    setItems((prev) => [...prev, ...added])
  }

  // 임베딩은 시간이 걸리므로 한 번에 한 파일씩 순서대로 처리한다
  useEffect(() => {
    if (runningRef.current) return
    const next = items.find((item) => item.status === 'waiting')
    if (!next) return

    runningRef.current = true
    update(next.id, { status: 'uploading', message: undefined, percent: 0 })

    uploadPdf(next.file, (percent) => {
      update(next.id, percent >= 100 ? { percent, status: 'processing' } : { percent })
    })
      .then((message) => update(next.id, { status: 'done', percent: 100, message, finishedAt: new Date() }))
      .catch((error: Error) => update(next.id, { status: 'failed', message: error.message, finishedAt: new Date() }))
      .finally(() => {
        runningRef.current = false
        setItems((prev) => [...prev])
      })
  }, [items])

  const handleInput = (event: ChangeEvent<HTMLInputElement>) => {
    addFiles(Array.from(event.target.files ?? []))
    event.target.value = ''
  }

  const handleDrop = (event: DragEvent<HTMLLabelElement>) => {
    event.preventDefault()
    setDragging(false)
    addFiles(Array.from(event.dataTransfer.files))
  }

  const retry = (id: string) => update(id, { status: 'waiting', percent: 0, message: undefined })
  const clearFinished = () =>
    setItems((prev) => prev.filter((item) => item.status !== 'done' && item.status !== 'failed'))

  const doneCount = items.filter((item) => item.status === 'done').length
  const failedCount = items.filter((item) => item.status === 'failed').length
  const activeCount = items.length - doneCount - failedCount

  return (
    <div className="app">
      <header className="topbar">
        <div className="brand">
          <span className="brand-mark" aria-hidden="true">
            <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <path d="M14 3H7a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h10a2 2 0 0 0 2-2V8z" />
              <path d="M14 3v5h5" />
            </svg>
          </span>
          <span className="brand-name">RAG 문서 관리</span>
        </div>
      </header>

      <main className="content">
        <div className="page-head">
          <h1>문서 검색</h1>
          <p>PDF를 올려서 벡터로 저장하고, 문서 내용을 AI에게 질문할 수 있습니다.</p>
        </div>

        <AskPanel />

        <section className="panel" aria-labelledby="upload-title">
          <h2 id="upload-title" className="sr-only">PDF 업로드</h2>
          <label
            className={`dropzone${dragging ? ' is-dragging' : ''}`}
            onDragOver={(event) => { event.preventDefault(); setDragging(true) }}
            onDragLeave={() => setDragging(false)}
            onDrop={handleDrop}
          >
            <input type="file" accept="application/pdf,.pdf" multiple onChange={handleInput} />
            <span className="dropzone-icon" aria-hidden="true">
              <svg viewBox="0 0 24 24" width="22" height="22" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">
                <path d="M12 16V5" /><path d="m7 10 5-5 5 5" /><path d="M5 19h14" />
              </svg>
            </span>
            <span className="dropzone-title">PDF 파일을 끌어다 놓거나 <u>파일을 선택</u>하세요</span>
            <span className="dropzone-hint">여러 파일을 올리면 순서대로 처리합니다</span>
          </label>
          {notice && <p className="notice" role="status">{notice}</p>}

          <ol className="pipeline" aria-label="처리 과정">
            {PIPELINE.map((step, index) => (
              <li key={step.title}>
                <span className="pipeline-no">{index + 1}</span>
                <span>
                  <strong>{step.title}</strong>
                  <small>{step.detail}</small>
                </span>
              </li>
            ))}
          </ol>
        </section>

        <section className="panel" aria-labelledby="list-title">
          <div className="panel-head">
            <div>
              <h2 id="list-title">업로드 내역</h2>
              {items.length > 0 && (
                <p className="summary">
                  완료 {doneCount} · 실패 {failedCount} · 진행 {activeCount}
                </p>
              )}
            </div>
            {doneCount + failedCount > 0 && (
              <button type="button" className="btn-text" onClick={clearFinished}>끝난 항목 지우기</button>
            )}
          </div>

          {items.length === 0 ? (
            <p className="empty">이 화면에서 업로드한 파일이 여기에 표시됩니다.</p>
          ) : (
            <ul className="file-list" aria-live="polite">
              {items.map((item) => (
                <li key={item.id} className="file-row">
                  <div className="file-main">
                    <span className="file-name" title={item.file.name}>{item.file.name}</span>
                    <span className="file-meta">
                      {formatSize(item.file.size)}
                      {item.finishedAt && ` · ${item.finishedAt.toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit' })}`}
                    </span>
                    {item.message && (
                      <span className={`file-message${item.status === 'failed' ? ' is-error' : ''}`}>{item.message}</span>
                    )}
                    {item.status === 'uploading' && (
                      <span className="progress" role="progressbar" aria-valuenow={item.percent} aria-valuemin={0} aria-valuemax={100} aria-label={`${item.file.name} 업로드`}>
                        <span style={{ width: `${item.percent}%` }} />
                      </span>
                    )}
                    {item.status === 'processing' && (
                      <span className="progress is-indeterminate" role="progressbar" aria-label={`${item.file.name} 임베딩 처리 중`}>
                        <span />
                      </span>
                    )}
                  </div>
                  <div className="file-side">
                    <span className={`chip chip-${item.status}`}>{STATUS_LABEL[item.status]}</span>
                    {item.status === 'failed' && (
                      <button type="button" className="btn-text" onClick={() => retry(item.id)}>다시 시도</button>
                    )}
                  </div>
                </li>
              ))}
            </ul>
          )}
        </section>
      </main>
    </div>
  )
}
