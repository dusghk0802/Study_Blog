import {Outlet} from 'react-router-dom';

const CommonLayout = () => {
  return (
      <div>
        <header style={{backgroundColor: 'lightgray', padding: '10px'}}>
          outlet 컴포넌트 알아보기
        </header>
        <article>
          <Outlet />
        </article>
        <footer style={{backgroundColor: 'lightgray', padding: '10px'}}>
          공통 레이아웃
        </footer>
      </div>
  )
}

export default CommonLayout;