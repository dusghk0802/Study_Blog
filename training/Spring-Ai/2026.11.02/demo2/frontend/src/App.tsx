import { useState } from 'react'
import MarketingRequest from './components/MarketingRequest'
import MarketingResponse from './components/MarketingResponse'
import type { Product } from './types'
import './App.css'

const EMPTY: Product = { name: '', price: '', link: '', features: '' }

function App() {
  const [product, setProduct] = useState<Product>(EMPTY)
  const [marketingResult, setMarketingResult] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')

  // POST /api/marketing → { marketingResult }
  const handleSubmit = async () => {
    setLoading(true)
    setError('')
    try {
      const response = await fetch('/api/marketing', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ ...product, price: Number(product.price) || 0 }),
      })
      if (!response.ok) throw new Error(`서버 오류 (${response.status})`)

      const data: { marketingResult: string } = await response.json()
      setMarketingResult(data.marketingResult)
    } catch (err) {
      setError(err instanceof Error ? err.message : '요청에 실패했습니다.')
    } finally {
      setLoading(false)
    }
  }

  // 다시 생성하기 → 입력 화면으로
  const handleReset = () => {
    setMarketingResult('')
  }

  return (
    <div className="layout">
      <header className="topbar">
        <div className="brand">
          <span className="brand-logo">M</span>
          <span className="brand-name">AI 마케팅 문구 생성기</span>
        </div>
        <span className="badge">Spring AI · OpenAI</span>
      </header>

      <main className="container">
        {marketingResult ? (
          <MarketingResponse product={product} marketingResult={marketingResult} onReset={handleReset} />
        ) : (
          <MarketingRequest
            product={product}
            setProduct={setProduct}
            loading={loading}
            error={error}
            onSubmit={handleSubmit}
          />
        )}
      </main>
    </div>
  )
}

export default App
