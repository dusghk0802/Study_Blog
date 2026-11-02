import type { Product } from '../types'

type Props = {
  product: Product
  setProduct: (product: Product) => void
  loading: boolean
  error: string
  onSubmit: () => void
}

// marketing_request.html → React
function MarketingRequest({ product, setProduct, loading, error, onSubmit }: Props) {
  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    setProduct({ ...product, [e.target.name]: e.target.value })
  }

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    onSubmit()
  }

  return (
    <section className="card">
      <div className="card-header">
        <h2>마케팅 문구 작성하기</h2>
        <p>제품 정보를 입력하면 AI가 홍보용 문구를 만들어 드립니다.</p>
      </div>

      <form onSubmit={handleSubmit} className="form">
        <div className="field">
          <label htmlFor="name">제품이름</label>
          <input id="name" name="name" value={product.name} onChange={handleChange} placeholder="예) 스테인리스 보온 텀블러" required />
        </div>

        <div className="row">
          <div className="field">
            <label htmlFor="price">제품가격</label>
            <div className="input-suffix">
              <input id="price" name="price" type="number" min="0" value={product.price} onChange={handleChange} placeholder="15000" required />
              <span>원</span>
            </div>
          </div>
          <div className="field">
            <label htmlFor="link">구매링크</label>
            <input id="link" name="link" type="url" value={product.link} onChange={handleChange} placeholder="https://" required />
          </div>
        </div>

        <div className="field">
          <label htmlFor="features">제품특징</label>
          <textarea id="features" name="features" rows={8} value={product.features} onChange={handleChange} placeholder="제품의 특징과 장점을 자유롭게 입력하세요" required />
          <span className="hint">{product.features.length}자</span>
        </div>

        {error && <div className="alert">{error}</div>}

        <div className="actions">
          <button type="submit" className="btn-primary" disabled={loading}>
            {loading ? <><span className="spinner" /> 생성 중...</> : '생성'}
          </button>
        </div>
      </form>
    </section>
  )
}

export default MarketingRequest
