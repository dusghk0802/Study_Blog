import { useEffect, useRef, useState } from 'react'
import './App.css'

function App() {

  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState("")

  //화면 스크롤을 위한 useRef 작성
  const charContainerRef = useRef(null);
  const userId = "jeon";

  //응답전 상태 표기
  const [isLoading, setIsLoading] = useState(false);

  useEffect(()=>{
    if(charContainerRef.current){
      charContainerRef.current.scrollTop = charContainerRef.current.scrollHeight;
    }
  },[messages]);

  const handleSendMessage = async () =>{
    if(newMessage.trim()){
      const userMessage = { userId, message: newMessage};
      setNewMessage('');
      setIsLoading(true);

      try {
        const response = await fetch('http://localhost:8080/chat',{
          method: 'POST',
          headers: { 'Content-Type': 'application/json'},
          body: JSON.stringify(userMessage)
        })

        if(response.ok){
          const data = await response.json();
          setMessages(data.messages);
        }else{
          setMessages(prev => [...prev, {text:'챗봇 응답에 실패 했습니다,', sender:'bot'}])
        }

      } catch {
        setMessages(prev => [...prev, {text:'챗봇 응답에 실패 했습니다,', sender:'bot'}])
      }finally{
        setIsLoading(false)
      }

    }
  }


  return (
    <div className='container m-0 p-0'>
      <div className='card shawdow'>
        <div className='card-header bg-primary text-white'>
          <h5>챗봇</h5>
        </div>

        {/* 챗봇 처리 내용 영역 */}
        <div ref={charContainerRef} className='card-body chat-history overflow-auto'
          style={{height:'500px', overflow: 'auto'}}>
            {
              messages.map((msg, index)=>(
                <div key={index} className={`alert ${msg.sender === 'user' ? 'alert-primary text-end' : 'alert-secondart text-start'}`}>
                  {msg.text}
                </div>
              ))
            }

        {
          isLoading && (
            <div
              style={{
                position: 'fixed', top : 0 ,left: 0 , 
                width:'100vw', height:'100vh', display: 'flex',
                flexDirection:'column', alignItems:'center',
                justifyContent: 'center', zIndex: 9999
              }}
            >
              <img src='https://i.gifer.com/XDZT.gif' alt='로딩 중....'
              style={{width:'120px', height:'120px'}}/>
              <div>응답을 기다리는 중....</div>
            </div>
          )
        }



        </div>

        {/* 메시지 작성 및 전송을 위한 영역 */}
        <div className='card-footer d-flex'>
          <input type='text' className='form-control me-2' placeholder='질문을 입력하세요' 
          value={newMessage} onChange={e => setNewMessage(e.target.value)}/>
          <button onClick={handleSendMessage} className='btn btn-success'>문의하기</button>
        </div>
      </div>

    </div>
  )
}

export default App
