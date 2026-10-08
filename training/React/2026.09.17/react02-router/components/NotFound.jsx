import {Link} from 'react-router-dom'

const NotFound = () => {
  return (<>
    <h2>NotFound</h2>
    <p>페이지를 찾을 수 없습니다.</p>
    <Link to="/"></Link>
  </>)
}

export default NotFound