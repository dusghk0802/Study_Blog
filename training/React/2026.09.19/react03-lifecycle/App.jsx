import {Route, Routes} from 'react-router-dom'
import TopNavi from './components/TopNavi.jsx'
import LifeCycle from './components/Lifecycle.jsx'
import LocalJsonFetcher from './components/LocalJsonFetcher.jsx'
import ExternalJsonFetcher from './components/ExternalApiFetcher.jsx'

function App() {

  return (<>
    <TopNavi></TopNavi>
    <Routes>
      <Route path="/" element={<LifeCycle />} />
       <Route path="/local" element={<LocalJsonFetcher />} />
       <Route path="/external" element={<ExternalJsonFetcher />} />
    </Routes>
    </>)
}

export default App
