import { useEffect, useState } from 'react'
import type { ChatMessage, ConversationSummary, ModelInfo } from './types'
import { api } from './api'
import Sidebar from './components/Sidebar'
import ChatHeader from './components/ChatHeader'
import MessageList from './components/MessageList'
import ChatInput from './components/ChatInput'
import './App.css'

const MODEL_KEY = 'demo7.model'

// 서버(LocalDateTime)와 같은 로컬 시간 형식
const now = () => {
  const d = new Date()
  return new Date(d.getTime() - d.getTimezoneOffset() * 60000).toISOString().slice(0, 19)
}

function App() {
  const [models, setModels] = useState<ModelInfo[]>([])
  const [model, setModel] = useState('')
  const [conversations, setConversations] = useState<ConversationSummary[]>([])
  const [currentId, setCurrentId] = useState<string | null>(null)
  const [messages, setMessages] = useState<ChatMessage[]>([])
  const [loading, setLoading] = useState(false)
  const [sidebarOpen, setSidebarOpen] = useState(false)

  const refreshList = () => api.conversations().then(setConversations).catch(() => {})

  // 처음 화면: 모델 목록 + 대화 기록 불러오기
  useEffect(() => {
    api.models()
      .then(list => {
        setModels(list)
        let saved: string | null = null
        try { saved = localStorage.getItem(MODEL_KEY) } catch { /* 저장소 사용 불가 */ }
        const first = list.find(m => m.name === saved) ?? list[0]
        if (first) setModel(first.name)
      })
      .catch(() => setMessages([{ role: 'error', text: '모델 목록을 불러오지 못했습니다. Ollama 실행 여부를 확인하세요.', time: now() }]))
    refreshList()
  }, [])

  const changeModel = (name: string) => {
    setModel(name)
    try { localStorage.setItem(MODEL_KEY, name) } catch { /* 저장소 사용 불가 */ }
  }

  // 대화 기록 하나 열기
  const openConversation = async (id: string) => {
    if (loading) return
    setSidebarOpen(false)
    try {
      const c = await api.conversation(id)
      setCurrentId(c.id)
      setMessages(c.messages)
      if (models.some(m => m.name === c.model)) changeModel(c.model)
    } catch (e) {
      setMessages([{ role: 'error', text: e instanceof Error ? e.message : '불러오기 실패', time: now() }])
    }
  }

  const newChat = () => {
    if (loading) return
    setCurrentId(null)
    setMessages([])
    setSidebarOpen(false)
  }

  const deleteConversation = async (id: string) => {
    if (!confirm('이 대화 기록을 삭제할까요?')) return
    await api.deleteConversation(id).catch(() => {})
    if (id === currentId) newChat()
    refreshList()
  }

  const sendQuestion = async (question: string) => {
    if (!model) return
    // 서버 응답 전까지 질문을 먼저 화면에 표시
    setMessages(prev => [...prev.filter(m => m.role !== 'error'), { role: 'user', text: question, time: now() }])
    setLoading(true)
    try {
      const c = await api.chat(currentId, question, model)
      setCurrentId(c.id)
      setMessages(c.messages)
      refreshList()
    } catch (e) {
      setMessages(prev => [...prev, { role: 'error', text: e instanceof Error ? e.message : '요청에 실패했습니다.', time: now() }])
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="app">
      <Sidebar
        open={sidebarOpen}
        conversations={conversations}
        currentId={currentId}
        disabled={loading}
        onNew={newChat}
        onOpen={openConversation}
        onDelete={deleteConversation}
        onClose={() => setSidebarOpen(false)}
      />
      <div className="main">
        <ChatHeader
          models={models}
          model={model}
          onModelChange={changeModel}
          disabled={loading}
          onMenu={() => setSidebarOpen(true)}
        />
        <main className="chat">
          <MessageList messages={messages} loading={loading} model={model} onSuggest={sendQuestion} />
        </main>
        <ChatInput onSend={sendQuestion} disabled={loading || !model} />
      </div>
    </div>
  )
}

export default App
