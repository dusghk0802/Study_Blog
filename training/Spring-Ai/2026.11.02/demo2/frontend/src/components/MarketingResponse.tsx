import { useState } from 'react'
import type { Product } from '../types'

type Props = {
  product: Product
  marketingResult: string
  onReset: () => void
}

// marketing_response.html → React
function MarketingResponse({ product, marketingResult, onReset }: Props) {
  const [copied, setCopied] = useState(false)

  const handleCopy = async () => {
    await navigator.clipboard.writeText(marketingResult)
    setCopied(true)
    setTimeout(() => setCopied(false), 1500)
  }

  return (
    <section className="card">
      <div className="card-header">
        <h2>AI 생성 마케팅 문구</h2>
        <button type="button" className="btn-ghost" onClick={handleCopy}>
          {copied ? '복사됨 ✓' : '복사'}
        </button>
      </div>

      <dl className="summary">
        <div><dt>제품</dt><dd>{product.name}</dd></div>
        <div><dt>가격</dt><dd>{Number(product.price).toLocaleString('ko-KR')}원</dd></div>
      </dl>

      {/* 컨트롤러에서 전달한 marketingResult 출력 */}
      <pre className="result">{marketingResult}</pre>

      <div className="actions">
        <button type="button" className="btn-primary" onClick={onReset}>다시 생성하기</button>
      </div>
    </section>
  )
}

export default MarketingResponse
