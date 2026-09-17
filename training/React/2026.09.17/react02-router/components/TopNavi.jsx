import {Link, NavLink} from 'react-router-dom'

const TopNavi = () => {
  return (<>
    <nav> 
      <a href='/'>Home</a>&nbsp;&nbsp; 
      <NavLink to="/intro">인트로</NavLink>&nbsp;&nbsp;
      <NavLink to="/intro/router">Router관련 Hook</NavLink>&nbsp;&nbsp;
      <Link to="/xyz">잘못된 url</Link>&nbsp;&nbsp;
    </nav>
    {/* &nbsp;&nbsp; = 공백 두칸 */}
  </>)
}

export default TopNavi