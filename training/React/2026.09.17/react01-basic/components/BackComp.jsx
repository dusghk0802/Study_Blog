const BackComp = ({setMode}) => {
  return (<>
    <li><a href="/" onClick={(event)=>{      
      event.preventDefault();
      setMode('back'); //상태 변경 함수를 직접 호출
    }}>백엔드</a></li>
    <ul>
      <li>Java</li>
      <li>Oracle</li>
      <li>JSP</li>
      <li>Spring Boot</li>
    </ul>
  </>)
}

export default BackComp;
