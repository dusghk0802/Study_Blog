import { useState } from 'react'
import type { AssistantId } from './api'
import ChatPanel from './ChatPanel'
import DocumentsPanel from './DocumentsPanel'
import EvaluationPanel from './EvaluationPanel'

export const ASSISTANTS: Record<AssistantId, { title: string; description: string; example: string }> = {
  A: { title: '사내 규정 AI 챗봇', description: '취업규칙, 연차휴가 규정, 복리후생 문서', example: '경조사 휴가는 며칠인가요?' },
  B: { title: '교육자료 AI 학습 도우미', description: 'Java, Spring Boot, 데이터베이스 수업자료', example: 'Spring MVC와 WebFlux의 차이는?' },
  C: { title: '제품 매뉴얼 AI 상담사', description: '제품 사용설명서, 오류 해결 매뉴얼', example: '제품 초기화 방법은 무엇인가요?' },
}

const IDS: AssistantId[] = ['A', 'B', 'C']

export default function App() {
  const [active, setActive] = useState<AssistantId>('A')
  const [view, setView] = useState<'chat' | 'eval'>('chat')
  const assistant = ASSISTANTS[active]

  return (
    <div className="app">
      <header className="topbar">
        <span className="brand">문서 AI 어시스턴트</span>
        <nav className="tabs" aria-label="과제 선택">
          {IDS.map((id) => (
            <button
              key={id}
              type="button"
              className={`tab${id === active ? ' is-active' : ''}`}
              aria-current={id === active ? 'page' : undefined}
              onClick={() => setActive(id)}
            >
              {ASSISTANTS[id].title}
            </button>
          ))}
        </nav>
      </header>

      <main className="layout">
        <aside className="side">
          <div className="side-head">
            <h1>{assistant.title}</h1>
            <p>{assistant.description}</p>
          </div>
          <DocumentsPanel key={active} assistant={active} />
        </aside>

        <section className="main">
          <div className="segment" role="group" aria-label="화면 선택">
            <button type="button" className={view === 'chat' ? 'is-active' : ''} onClick={() => setView('chat')}>질문하기</button>
            <button type="button" className={view === 'eval' ? 'is-active' : ''} onClick={() => setView('eval')}>검색 품질 평가</button>
          </div>
          {/* 탭을 옮겨도 과제별 대화와 평가 결과가 남도록 숨기기만 한다 */}
          {IDS.map((id) => (
            <div key={id} className="pane" hidden={id !== active || view !== 'chat'}>
              <ChatPanel assistant={id} example={ASSISTANTS[id].example} />
            </div>
          ))}
          {IDS.map((id) => (
            <div key={id} className="pane" hidden={id !== active || view !== 'eval'}>
              <EvaluationPanel assistant={id} />
            </div>
          ))}
        </section>
      </main>
    </div>
  )
}
