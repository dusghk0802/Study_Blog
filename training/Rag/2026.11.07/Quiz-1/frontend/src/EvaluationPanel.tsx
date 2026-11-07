import { useState } from 'react'
import { evaluate } from './api'
import type { AssistantId, EvalReport } from './api'

export default function EvaluationPanel({ assistant }: { assistant: AssistantId }) {
  const [report, setReport] = useState<EvalReport | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')

  const run = async () => {
    setLoading(true)
    setError('')
    try {
      setReport(await evaluate(assistant))
    } catch (e) {
      setError((e as Error).message)
    } finally {
      setLoading(false)
    }
  }

  const best = report ? Math.max(...report.configs.map((config) => config.accuracy)) : 0

  return (
    <div className="panel eval">
      <div className="panel-head">
        <div>
          <h2>검색 품질 평가</h2>
          <p className="summary">질문 10개로 topK와 임계값별 검색 결과를 비교합니다. 답변은 생성하지 않습니다.</p>
        </div>
        <button type="button" className="btn-primary" disabled={loading} onClick={() => void run()}>
          {loading ? '평가 중' : report ? '다시 평가' : '평가 실행'}
        </button>
      </div>

      {error && <p className="notice notice-error" role="alert">{error}</p>}

      {report && (
        <>
          <h3>설정별 결과</h3>
          <div className="table-wrap">
            <table>
              <thead>
                <tr><th>topK</th><th>임계값</th><th>정답 검색</th><th>문서 밖 질문 거절</th><th>정확도</th></tr>
              </thead>
              <tbody>
                {report.configs.map((config) => (
                  <tr key={`${config.topK}-${config.threshold}`} className={config.accuracy === best ? 'is-best' : ''}>
                    <td>{config.topK}</td>
                    <td>{config.threshold.toFixed(1)}</td>
                    <td>{config.hits} / {config.inScope}</td>
                    <td>{config.rejected} / {config.outOfScope}</td>
                    <td>{Math.round(config.accuracy * 100)}%</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          <h3>질문별 검색 결과</h3>
          <div className="table-wrap">
            <table>
              <thead>
                <tr><th>질문</th><th>구분</th><th>1위 문서</th><th>유사도</th><th>정답 순위</th></tr>
              </thead>
              <tbody>
                {report.rows.map((row) => (
                  <tr key={row.question}>
                    <td>{row.question}</td>
                    <td>{row.inScope ? '문서 안' : '문서 밖'}</td>
                    <td>{row.topFile ?? '-'}{row.topPage ? ` · ${row.topPage}쪽` : ''}</td>
                    <td>{row.topScore == null ? '-' : row.topScore.toFixed(3)}</td>
                    <td>{row.inScope ? (row.firstHitRank ?? '없음') : '-'}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </>
      )}
    </div>
  )
}
