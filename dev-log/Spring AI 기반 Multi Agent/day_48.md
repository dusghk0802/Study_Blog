# 48일차

## React useEffect·내부/외부 서버 통신

📌 학습일 : 2026.09.19\
📌 학습 내용 : useEffect, 컴포넌트 생명주기, fetch, JSON 데이터 통신, 내부 서버 통신, 외부 API 통신, React Router, NavLink, async/await, try-catch-finally

---

#### 1. useEffect와 컴포넌트 생명주기

React 함수형 컴포넌트에서 `useEffect`를 이용하여 컴포넌트의 생명주기를 확인하였다.

```jsx
useEffect(function() {
  console.log('컴포넌트 마운트')

  return () => {
    console.log('컴포넌트 언마운트')
  }
}, [])
```

`useEffect()`는 컴포넌트가 렌더링된 이후 실행되며, 빈 의존성 배열 `[]`을 지정하면 컴포넌트가 처음 마운트될 때 실행된다.

`return`으로 반환하는 함수는 컴포넌트가 화면에서 제거될 때 실행되는 정리 함수로 사용할 수 있다.

#### 2. useEffect 의존성 배열

```jsx
useEffect(() => {
  // 실행할 코드
})
```

의존성 배열을 생략하면 컴포넌트가 렌더링될 때마다 `useEffect`가 실행된다.

```jsx
useEffect(() => {
  // 실행할 코드
}, [])
```

빈 배열을 지정하면 컴포넌트가 처음 마운트될 때 실행된다.

```jsx
useEffect(() => {
  // 실행할 코드
}, [상태변수])
```

의존성 배열에 State 변수를 지정하면 해당 값이 변경될 때 `useEffect`가 다시 실행된다.

#### 3. State를 이용한 요소 위치 변경

```jsx
const [위치, set위치] = useState(초기위치)
const [횟수, set횟수] = useState(1)

const 왼쪽이동 = () => {
  set위치(() => 위치 - 20)
  set횟수(() => 횟수 + 1)
}

const 오른쪽이동 = () => {
  set위치(() => 위치 + 20)
}
```

`useState`로 요소의 위치와 이동 횟수를 관리하고 버튼을 클릭할 때 State 값을 변경하여 화면에 반영하는 방법을 실습하였다.

State가 변경되면 컴포넌트가 다시 렌더링되는 과정도 확인하였다.

#### 4. fetch를 이용한 내부 JSON 데이터 통신

```jsx
useEffect(() => {
  fetch('./json/데이터.json')
    .then((response) => {
      return response.json()
    })
    .then((json) => {
      set목록(json)
    })
}, [])
```

`fetch()`를 이용하여 프로젝트 내부의 JSON 파일을 가져오고 `response.json()`으로 JSON 데이터를 JavaScript 객체로 변환하는 방법을 실습하였다.

가져온 데이터는 State에 저장하여 컴포넌트에서 사용할 수 있다.

#### 5. map을 이용한 JSON 데이터 출력

```jsx
const 목록태그 = 목록.map((data) => {
  return (
    <li key={data.id}>
      {data.id}
    </li>
  )
})
```

JSON 배열 데이터를 `map()`으로 반복 처리하여 여러 개의 JSX 요소로 출력하는 방법을 확인하였다.

반복되는 요소에는 각각을 구분할 수 있도록 `key` 속성을 지정하였다.

#### 6. 클릭 이벤트를 이용한 JSON 데이터 조회

```jsx
<a
  href={data.id}
  data-id={data.num}
  onClick={(e) => {
    e.preventDefault()
    props.링크클릭(e.target.dataset.id)
  }}
>
  {data.id}
</a>
```

링크를 클릭했을 때 `e.preventDefault()`를 이용하여 기본 이동 동작을 막고 `data-*` 속성에 저장된 값을 가져오는 방법을 실습하였다.

가져온 값을 부모 컴포넌트의 함수로 전달하여 선택한 데이터에 맞는 JSON 파일을 조회하였다.

#### 7. Props를 이용한 컴포넌트 간 데이터 전달

```jsx
<목록컴포넌트
  링크클릭={(번호) => {
    fetch('./json/데이터' + 번호 + '.json')
      .then(response => response.json())
      .then(json => {
        set결과(json)
      })
  }}
/>
```

부모 컴포넌트에서 함수를 Props로 전달하고 자식 컴포넌트에서 해당 함수를 호출하여 데이터를 부모 컴포넌트로 전달하는 방법을 실습하였다.

선택된 데이터는 State에 저장한 뒤 다른 컴포넌트에서 출력하였다.

#### 8. 외부 API 데이터 통신

```jsx
useEffect(() => {
  fetch('외부 API 주소')
    .then(response => response.json())
    .then(json => {
      set데이터(json)
    })
}, [])
```

`fetch()`를 이용하면 프로젝트 내부 파일뿐만 아니라 외부 서버의 API에서도 데이터를 받아올 수 있다는 것을 확인하였다.

외부 API에서 전달받은 JSON 데이터를 State에 저장하여 화면에 출력하였다.

#### 9. 외부 API 데이터 목록 출력

```jsx
const 목록 = 데이터.results.map((data) => {
  return (
    <tr key={data.id}>
      <td>
        <img src={data.picture} alt={data.name} />
      </td>
      <td>{data.name}</td>
      <td>{data.email}</td>
    </tr>
  )
})
```

외부 API에서 받은 배열 데이터를 `map()`으로 반복 처리하여 표 형태로 출력하는 방법을 실습하였다.

JSON 객체의 속성에 접근하여 이미지, 이름, 국가, 이메일 등의 데이터를 화면에 표시할 수 있다는 것을 확인하였다.

#### 10. React Router를 이용한 페이지 이동

```jsx
import { Route, Routes } from 'react-router-dom'

<Routes>
  <Route path="/" element={<컴포넌트 />} />
  <Route path="/local" element={<내부통신컴포넌트 />} />
  <Route path="/external" element={<외부통신컴포넌트 />} />
</Routes>
```

`Routes`와 `Route`를 이용하여 URL 경로에 따라 서로 다른 컴포넌트를 화면에 출력하는 방법을 실습하였다.

#### 11. NavLink를 이용한 메뉴 구성

```jsx
import { NavLink } from 'react-router-dom'

<NavLink to="/">수명주기</NavLink>
<NavLink to="/local">내부통신</NavLink>
<NavLink to="/external">외부통신</NavLink>
```

`NavLink`를 이용하여 각 Route로 이동할 수 있는 내비게이션 메뉴를 구성하였다.

페이지 전체를 새로 불러오지 않고 React Router를 통해 컴포넌트를 전환할 수 있다.

#### 12. 내부 도서 JSON 데이터 불러오기

```jsx
const [도서목록, set도서목록] = useState([])
const [로딩, set로딩] = useState(true)
```

도서 목록을 저장할 State와 데이터 통신 상태를 관리하기 위한 로딩 State를 생성하였다.

```jsx
const response = await fetch('/json/books.json')
const data = await response.json()
set도서목록(data)
```

내부 `books.json` 파일에서 도서 정보를 가져와 State에 저장하였다.

#### 13. async/await를 이용한 비동기 통신

```jsx
const 데이터가져오기 = async () => {
  try {
    const response = await fetch('/json/books.json')
    const data = await response.json()
    set목록(data)
  } catch (error) {
    console.error('데이터 조회 실패:', error)
  } finally {
    set로딩(false)
  }
}
```

기존의 `.then()` 방식뿐만 아니라 `async/await`를 이용하여 비동기 통신을 처리하는 방법을 실습하였다.

`try`에서 데이터 통신을 수행하고, 오류가 발생하면 `catch`에서 처리하며, 성공 여부와 관계없이 `finally`에서 로딩 상태를 변경하였다.

#### 14. 로딩 상태 처리

```jsx
if (로딩) return <p>Loading...</p>
```

API 또는 JSON 데이터를 가져오는 동안 바로 데이터를 출력하지 않고 `Loading...`을 표시하도록 구현하였다.

데이터 통신이 완료되면 로딩 State를 `false`로 변경하여 실제 데이터를 화면에 출력하였다.

#### 15. 도서 목록 출력

```jsx
<ul>
  {도서목록.map((도서) => (
    <li key={도서.id}>
      <strong>{도서.title}</strong> by {도서.author}
    </li>
  ))}
</ul>
```

JSON 파일에서 가져온 도서 배열을 `map()`으로 반복하여 제목과 저자를 목록 형태로 출력하였다.

#### 16. 날씨 API를 이용한 데이터 조회

```jsx
const [날씨, set날씨] = useState({
  temp: '',
  desc: '',
  icon: ''
})
```

날씨 API에서 받아올 온도, 날씨 상태, 아이콘 정보를 State로 관리하였다.

```jsx
fetch('날씨 API 주소')
  .then(response => response.json())
  .then(result => {
    set날씨({
      temp: result.main.temp,
      desc: result.weather[0].main,
      icon: result.weather[0].icon
    })
  })
```

외부 날씨 API의 JSON 응답에서 필요한 데이터만 선택하여 State에 저장하는 방법을 실습하였다.

#### 17. 날씨 아이콘 출력

```jsx
<img
  src={`https://openweathermap.org/img/wn/${날씨.icon}@2x.png`}
  alt="Weather icon"
/>
```

API에서 받은 아이콘 코드를 템플릿 리터럴 `${}`을 이용해 이미지 URL에 삽입하여 현재 날씨에 해당하는 아이콘을 출력하였다.

```jsx
if (날씨.icon) {
  return (
    <>
      <p>Temperature: {날씨.temp} °C</p>
      <p>Description: {날씨.desc}</p>
    </>
  )
} else {
  return <>Loading...</>
}
```

아이콘 데이터가 존재하는지 확인하여 데이터 로딩이 완료되기 전에는 `Loading...`을 표시하고, 데이터가 준비되면 날씨 정보를 출력하도록 조건부 렌더링하였다.

---

#### 핵심 정리

- `useEffect()`를 이용하여 컴포넌트가 마운트·업데이트·언마운트되는 과정에서 필요한 작업을 처리할 수 있다.
- 의존성 배열을 생략하거나 `[]`, `[State]`를 지정하는 방식에 따라 `useEffect`의 실행 시점을 조절할 수 있다.
- `fetch()`를 이용하여 내부 JSON 파일이나 외부 API의 데이터를 가져올 수 있다.
- `response.json()`을 이용하여 응답받은 JSON 데이터를 JavaScript에서 사용할 수 있는 형태로 변환한다.
- `map()`을 이용하여 배열 형태의 데이터를 반복하여 JSX 요소로 출력할 수 있다.
- Props를 이용하여 부모와 자식 컴포넌트 사이에서 값이나 함수를 전달할 수 있다.
- `Routes`, `Route`, `NavLink`를 이용하여 URL에 따라 서로 다른 컴포넌트를 표시할 수 있다.
- 비동기 통신은 `.then()` 방식뿐만 아니라 `async/await` 방식으로도 처리할 수 있다.
- `try-catch-finally`를 이용하여 비동기 통신의 정상 처리, 오류 처리, 종료 처리를 구분할 수 있다.
- 별도의 State를 이용하여 데이터가 준비되기 전 `Loading...`을 표시할 수 있다.
- 외부 API 응답에서 필요한 데이터만 State에 저장하여 화면에 출력할 수 있다.
- 템플릿 리터럴을 이용하여 API에서 받은 값을 이미지 URL 등에 동적으로 삽입할 수 있다.

---

<p align="center">
  <img src="../../training/Python/2026.09.18/day_48_1.PNG" alt="day_48" width="700">
</p>
