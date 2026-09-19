import {NavLink} from 'react-router-dom'

const TopNavi = () => {
  return (
    <nav>
      <NavLink to="/">수명주기</NavLink>&nbsp;&nbsp;
      <NavLink to="/local">내부통신</NavLink>&nbsp;&nbsp;
      <NavLink to="/external">외부통신</NavLink>&nbsp;&nbsp;
      <NavLink to="/localbook">내부통신1</NavLink>&nbsp;&nbsp;
      <NavLink to="/weather">외부통신1</NavLink>&nbsp;&nbsp;
    </nav>
  )
}
export default TopNavi
