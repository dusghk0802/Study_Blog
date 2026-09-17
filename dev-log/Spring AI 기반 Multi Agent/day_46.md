# 46일차

## React State·Style·Form·Shallow Comparison·Router

📌 학습일 : 2026.09.17  
📌 학습 내용 : State, useState, 조건부 렌더링, Style, 이미지 삽입, Form, preventDefault, 얕은 비교, 스프레드 연산자, React Router, 중첩 라우팅, Outlet, Link, NavLink, useLocation, useSearchParams

---

#### 1. State와 useState

State는 컴포넌트에서 변경되는 데이터를 관리할 때 사용한다.

```jsx
const [상태값, 상태변경함수] = useState('초기값');
```

`useState`를 이용해 상태값과 상태를 변경하는 함수를 생성하고, 상태가 변경되면 컴포넌트가 다시 렌더링되는 구조를 학습하였다.

#### 2. State를 이용한 화면 변경

```jsx
const [화면상태, 상태변경함수] = useState('전체');

if (화면상태 === '화면1') {
  // 화면1 컴포넌트 출력
}
else if (화면상태 === '화면2') {
  // 화면2 컴포넌트 출력
}
else {
  // 전체 컴포넌트 출력
}
```

상태값에 따라 서로 다른 컴포넌트를 화면에 출력하는 조건부 렌더링을 실습하였다.

자식 컴포넌트에 상태 변경 함수 또는 이벤트 함수를 Props로 전달하여 부모 컴포넌트의 State를 변경하는 방법도 확인하였다.

#### 3. React에서 스타일 적용

React에서는 인라인 스타일, 스타일 객체, CSS 파일 등을 이용해 스타일을 적용할 수 있다.

```jsx
<li style={{color: "red"}}>항목</li>
```

여러 스타일을 사용할 경우 객체로 작성하여 적용할 수 있다.

```jsx
const 스타일객체 = {
  color: "white",
  backgroundColor: "blue",
  padding: "10px"
};
```

JSX에서는 HTML의 `class` 대신 `className`을 사용하여 CSS 클래스를 적용하였다.

#### 4. 이미지 삽입

React에서 이미지를 삽입하는 여러 방법을 실습하였다.

```jsx
<img src="/img/이미지.png" />
```

public 폴더의 이미지는 경로를 직접 지정하여 사용할 수 있다.

```jsx
import 이미지파일 from './assets/이미지.png';

<img src={이미지파일} />
```

assets 폴더의 이미지는 import한 뒤 사용할 수 있으며, 외부 웹 이미지의 URL을 직접 지정하는 방법도 확인하였다.

#### 5. Form 데이터 처리

폼의 submit 이벤트를 이용하여 사용자가 입력한 값을 가져오는 방법을 실습하였다.

```jsx
<form onSubmit={(event) => {
  event.preventDefault();

  let 선택값 = event.target.선택항목.value;
  let 입력값 = event.target.입력항목.value;
}}>
```

`preventDefault()`를 이용해 submit 시 발생하는 기본 페이지 이동을 막고 입력된 폼 데이터를 처리하였다.

#### 6. 폼값 검증과 State 변경

```jsx
if (선택값 !== '' && 입력값 !== '') {
  상태변경함수('검증 완료');
}
else {
  alert('빈값 있음');
}
```

폼에서 전달받은 값이 비어 있는지 확인하고, 정상적으로 입력된 경우 State를 변경하여 화면에 결과를 출력하는 방법을 실습하였다.

#### 7. 얕은 비교

React에서는 객체나 배열 형태의 State가 변경되었는지 확인할 때 참조값을 기준으로 얕은 비교를 사용한다.

```jsx
상태값.배열.push('새로운 값');
상태변경함수(상태값);
```

기존 객체를 직접 수정하면 참조값이 그대로이기 때문에 React가 상태 변화를 인식하지 못할 수 있다는 것을 확인하였다.

#### 8. 스프레드 연산자를 이용한 State 변경

```jsx
const 새로운배열 = [...상태값.배열, '새로운 값'];
const 새로운객체 = {...상태값, 배열: 새로운배열};

상태변경함수(새로운객체);
```

스프레드 연산자를 이용하여 새로운 배열과 객체를 생성하면 새로운 참조값이 만들어지고 React가 상태 변화를 감지하여 화면을 다시 렌더링하는 것을 확인하였다.

#### 9. React Router

React Router는 URL 경로에 따라 화면에 표시할 컴포넌트를 결정할 때 사용한다.

```jsx
<BrowserRouter>
  <App />
</BrowserRouter>
```

라우팅 기능을 사용하기 위해 최상위 컴포넌트를 `BrowserRouter`로 감싸는 구조를 실습하였다.

#### 10. Routes와 Route

```jsx
<Routes>
  <Route path="/" element={<컴포넌트1 />} />
  <Route path="/경로" element={<컴포넌트2 />} />
  <Route path="*" element={<오류컴포넌트 />} />
</Routes>
```

`Route`의 `path`에 URL을 지정하고 `element`에 해당 경로에서 출력할 컴포넌트를 지정하였다.

`*` 경로를 이용해 존재하지 않는 URL로 접근했을 때 NotFound 화면을 출력하는 방법도 실습하였다.

#### 11. 중첩 라우팅과 Outlet

```jsx
<Route path="/상위경로/*" element={<공통레이아웃 />}>
  <Route index element={<기본컴포넌트 />} />
  <Route path="하위경로" element={<하위컴포넌트 />} />
</Route>
```

중첩 라우팅을 이용하여 공통 레이아웃 내부에 여러 하위 페이지를 구성하였다.

```jsx
<Outlet />
```

`Outlet`은 중첩 라우팅에서 자식 컴포넌트가 출력될 위치를 지정할 때 사용한다.

#### 12. Link와 NavLink

```jsx
<Link to="/">메인</Link>
<NavLink to="/경로">메뉴</NavLink>
```

`Link`와 `NavLink`를 이용하여 페이지를 이동하는 방법을 실습하였다.

`NavLink`는 현재 URL과 링크가 일치하면 자동으로 `active` 클래스가 적용되는 특징이 있다.

```css
.active {
  background-color: aqua;
  font-weight: bold;
  color: red;
}
```

이를 이용해 현재 선택된 메뉴를 CSS로 구분하여 표시하였다.

#### 13. useLocation

```jsx
const 위치정보 = useLocation();
```

`useLocation`을 이용하여 현재 페이지의 경로와 쿼리스트링 정보를 가져오는 방법을 실습하였다.

```jsx
위치정보.pathname
위치정보.search
```

`pathname`으로 현재 경로를 확인하고 `search`로 URL의 쿼리스트링을 확인하였다.

#### 14. useSearchParams

```jsx
const [쿼리정보, 쿼리변경함수] = useSearchParams();

const 값1 = 쿼리정보.get('항목1');
const 값2 = 쿼리정보.get('항목2');
```

`useSearchParams`를 이용하여 쿼리스트링의 값을 항목별로 가져오는 방법을 실습하였다.

```jsx
쿼리변경함수({
  항목1: 변경값,
  항목2: 값2
});
```

버튼을 클릭했을 때 쿼리스트링의 값이나 페이지 번호를 변경하는 기능도 구현하였다.

---

#### 핵심 정리

- State는 컴포넌트에서 변경되는 데이터를 관리하며 `useState`로 생성한다.
- State가 변경되면 해당 컴포넌트가 다시 렌더링된다.
- 상태값에 따라 다른 컴포넌트를 출력하는 조건부 렌더링을 구현할 수 있다.
- React에서는 인라인 스타일, 스타일 객체, CSS 파일을 이용해 스타일을 적용할 수 있다.
- 이미지는 public 경로, assets import, 외부 URL 등의 방법으로 삽입할 수 있다.
- Form에서는 `preventDefault()`로 기본 submit 동작을 막고 입력값을 처리할 수 있다.
- 객체와 배열 형태의 State는 기존 데이터를 직접 수정하기보다 새로운 객체나 배열을 만들어 변경해야 한다.
- 스프레드 연산자를 사용하면 새로운 참조값을 생성하여 React가 상태 변화를 인식할 수 있다.
- React Router를 이용하면 URL에 따라 서로 다른 컴포넌트를 렌더링할 수 있다.
- `Outlet`은 중첩 라우팅에서 자식 컴포넌트가 출력되는 위치를 지정한다.
- `NavLink`는 현재 경로와 일치할 경우 `active` 클래스가 자동으로 적용된다.
- `useLocation`으로 현재 URL 정보를 확인하고 `useSearchParams`로 쿼리스트링을 조회하거나 변경할 수 있다.

---

<p align="center">
  <img src="../../training/React/2026.09.17/day_46_1.PNG" alt="day_46" width="700">
</p>
```jsx
import {Link, NavLink} from 'react-router-dom'

const TopNavi = () => {
  return (<>
    <nav> 
      <a href='/'>Home</a>&nbsp;&nbsp; 
      <NavLink to="/intro">인트로</NavLink>&nbsp;&nbsp;
      <NavLink to="/intro/router">Router관련 Hook</NavLink>&nbsp;&nbsp;
      <Link to="/xyz">잘못된 url</Link>&nbsp;&nbsp;
    </nav>
  </>)
}

export default TopNavi
```

Link와 NavLink를 이용해 페이지를 이동하는 방법과 NavLink를 사용하면 현재 선택된 메뉴에 active 클래스가 자동으로 적용된다는 것을 배웠다. 
</br>또한 HTML의 &nbsp;를 사용하면 공백을 넣을 수 있으며, &nbsp;&nbsp;처럼 연속해서 사용해 메뉴 사이의 간격을 조절할 수 있다는 것도 알게 되었다.
</br>기존의 a 태그와 Link, NavLink의 차이가 처음에는 헷갈렸지만 직접 사용해 보면서 각각의 역할을 이해할 수 있었다. 메뉴 사이에 공백을 넣는 간단한 방법도 함께 실습해서 기억에 남았다.
