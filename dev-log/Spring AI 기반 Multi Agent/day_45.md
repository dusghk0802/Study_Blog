# 45일차

## React 컴포넌트·Props·이벤트 및 모듈화

📌 학습일 : 2026.09.16 

📌 학습 내용 : JSX, 함수형 컴포넌트, Fragment, Props, 구조분해할당, 배열 출력, key, React 이벤트, 이벤트 함수 전달, preventDefault, 컴포넌트 모듈화, export default, import, children, CSS 스타일링

---

#### 1. JSX를 이용한 기본 화면 구성

React에서는 JSX를 이용하여 JavaScript 안에서 HTML과 유사한 형태로 화면을
작성할 수 있다.

``` jsx
function App() {
  return (
    <>
      <h2>제목</h2>

      <ol>
        <li>첫 번째 항목</li>
        <li>두 번째 항목</li>
      </ol>

      <form>
        <select name="구분">
          <option value="항목1">항목 1</option>
          <option value="항목2">항목 2</option>
        </select>

        <input type="text" name="내용" />
        <input type="submit" value="추가" />
      </form>
    </>
  );
}

export default App;
```

여러 개의 JSX 요소를 반환할 때는 하나의 최상위 요소로 감싸야 하며,
별도의 HTML 태그가 필요하지 않을 경우 Fragment인 `<> </>`를 사용할 수
있다.

#### 2. 함수형 컴포넌트 작성

화면의 반복되는 부분을 각각의 컴포넌트로 분리하여 작성하였다.

``` jsx
function 첫번째컴포넌트() {
  return (
    <>
      <li>첫 번째 영역</li>
      <ul>
        <li>항목 1</li>
        <li>항목 2</li>
      </ul>
    </>
  );
}

const 두번째컴포넌트 = () => {
  return (
    <>
      <li>두 번째 영역</li>
      <ul>
        <li>항목 1</li>
        <li>항목 2</li>
      </ul>
    </>
  );
};
```

일반 함수와 화살표 함수 형태로 함수형 컴포넌트를 만들 수 있다는 것을
확인하였다.

#### 3. 컴포넌트 호출과 재사용

작성한 컴포넌트는 HTML 태그처럼 `App` 컴포넌트에서 호출하여 사용할 수
있다.

``` jsx
function App() {
  return (
    <>
      <h2>React Component</h2>

      <ol>
        <첫번째컴포넌트 />
        <두번째컴포넌트 />
        <두번째컴포넌트 />
      </ol>
    </>
  );
}
```

같은 컴포넌트를 여러 번 호출하여 동일한 화면 구조를 재사용할 수 있다는
것을 실습하였다.

#### 4. Props를 이용한 데이터 전달

Props는 부모 컴포넌트에서 자식 컴포넌트로 데이터를 전달할 때 사용하는
객체이다.

``` jsx
function 자식컴포넌트(props) {
  return (
    <>
      <h2>{props.제목}</h2>
      <p>{props.내용}</p>
    </>
  );
}

function App() {
  return (
    <자식컴포넌트
      제목="제목 데이터"
      내용="내용 데이터"
    />
  );
}
```

Props에는 문자열뿐만 아니라 배열, 객체, 함수 등 다양한 데이터를 전달할
수 있으며, 자식 컴포넌트에서는 전달받은 Props를 읽어서 사용한다.

#### 5. 배열 데이터를 Props로 전달

배열을 부모 컴포넌트에서 선언한 뒤 Props를 이용하여 자식 컴포넌트로
전달하였다.

``` jsx
function 목록컴포넌트(props) {
  const 목록 = [];

  for (let i = 0; i < props.데이터.length; i++) {
    목록.push(
      <li key={i}>{props.데이터[i]}</li>
    );
  }

  return (
    <ul>
      {목록}
    </ul>
  );
}

function App() {
  const 데이터 = ['항목1', '항목2', '항목3'];

  return (
    <목록컴포넌트 데이터={데이터} />
  );
}
```

배열의 데이터를 반복문으로 순회하면서 `<li>` 요소를 생성하고 화면에
출력하는 방법을 실습하였다.

#### 6. 반복 요소의 key

React에서 반복해서 생성되는 요소에는 각 요소를 구분하기 위한 `key`를
지정한다.

``` jsx
for (let i = 0; i < props.데이터.length; i++) {
  목록.push(
    <li key={i}>{props.데이터[i]}</li>
  );
}
```

실습에서는 반복문의 인덱스 값을 `key`로 사용하여 각각의 목록 요소를
구분하였다.

#### 7. Props 객체 사용

여러 개의 값을 Props로 전달하고 `props.속성명` 형태로 사용하는 방법을
실습하였다.

``` jsx
function 자식컴포넌트(props) {
  return (
    <>
      <h2>Props 객체 사용</h2>
      <p>
        {props.값1}, {props.값2}, {props.값3}, {props.값4}
      </p>
    </>
  );
}

function App() {
  return (
    <자식컴포넌트
      값1="데이터1"
      값2="데이터2"
      값3="데이터3"
      값4="데이터4"
    />
  );
}
```

#### 8. Props 구조분해할당

Props 객체를 그대로 사용하지 않고 구조분해할당을 이용하여 필요한 값을
바로 사용할 수 있다.

``` jsx
function 자식컴포넌트({ 값1, 값2, 값3, 값4 }) {
  return (
    <p>
      {값1}, {값2}, {값3}, {값4}
    </p>
  );
}
```

전체 Props 중 필요한 값만 구조분해할당하여 사용할 수도 있다.

``` jsx
function 자식컴포넌트({ 값1, 값3 }) {
  return (
    <p>
      {값1}, {값3}
    </p>
  );
}
```

#### 9. React 이벤트 처리

React에서는 이벤트 핸들러 이름을 `onClick`처럼 카멜 케이스로 작성하고
함수 형태로 전달한다.

``` jsx
function 자식컴포넌트(props) {
  return (
    <a href="/" onClick={() => {
      props.클릭이벤트();
    }}>
      클릭
    </a>
  );
}

function App() {
  return (
    <자식컴포넌트
      클릭이벤트={() => {
        alert('클릭됨');
      }}
    />
  );
}
```

부모 컴포넌트에서 함수를 Props로 전달하고 자식 컴포넌트의 클릭
이벤트에서 해당 함수를 실행하는 방법을 실습하였다.

#### 10. 이벤트를 이용한 자식 데이터 전달

이벤트 함수의 매개변수를 이용하면 자식 컴포넌트에서 부모 컴포넌트로 값을
전달할 수 있다.

``` jsx
function 자식컴포넌트({ 클릭이벤트 }) {
  return (
    <a href="/" onClick={(event) => {
      event.preventDefault();
      클릭이벤트('자식에서 전달한 값');
    }}>
      클릭
    </a>
  );
}

function App() {
  return (
    <자식컴포넌트
      클릭이벤트={(메시지) => {
        alert(메시지);
      }}
    />
  );
}
```

자식 컴포넌트에서 이벤트가 발생했을 때 부모가 전달한 함수를 실행하면서
매개변수에 데이터를 담아 전달하였다.

#### 11. preventDefault를 이용한 기본 이벤트 방지

링크를 클릭하면 기본적으로 지정된 주소로 이동하는 동작이 발생한다.

``` jsx
event.preventDefault();
```

`preventDefault()`를 사용하여 링크의 기본 이동 동작을 막고 React에서
작성한 이벤트 처리만 실행되도록 하였다.

#### 12. 컴포넌트 모듈화

컴포넌트가 많아지면 `App.jsx` 하나에 모두 작성하지 않고 별도의 파일로
분리하여 관리할 수 있다.

``` jsx
function 컴포넌트명() {
  return (
    <div>컴포넌트 내용</div>
  );
}

export default 컴포넌트명;
```

`export default`를 이용하여 작성한 컴포넌트를 다른 파일에서 사용할 수
있도록 내보내는 방법을 실습하였다.

#### 13. import를 이용한 컴포넌트 가져오기

별도의 파일로 분리한 컴포넌트는 `import`를 이용하여 `App.jsx`에서 가져올
수 있다.

``` jsx
import 첫번째컴포넌트 from './components/첫번째컴포넌트';
import 두번째컴포넌트 from './components/두번째컴포넌트';

function App() {
  return (
    <>
      <첫번째컴포넌트 />
      <두번째컴포넌트 />
    </>
  );
}

export default App;
```

컴포넌트를 파일별로 분리하면 코드의 가독성이 좋아지고 필요한 컴포넌트를
재사용하기 쉬워진다.

#### 14. Header·Button·Footer 컴포넌트 구성

화면을 역할별로 나누어 Header, Button, Footer와 같은 컴포넌트를 각각
작성하고 `App`에서 조합하였다.

``` jsx
function 헤더컴포넌트() {
  return (
    <header>
      <h1>제목</h1>
    </header>
  );
}

export default 헤더컴포넌트;
```

``` jsx
function 푸터컴포넌트() {
  return (
    <footer>
      <p>하단 내용</p>
    </footer>
  );
}

export default 푸터컴포넌트;
```

각 컴포넌트를 별도의 파일로 관리한 뒤 필요한 위치에서 불러와 하나의
화면을 구성하는 방법을 실습하였다.

#### 15. children을 이용한 Button 컴포넌트

컴포넌트 태그 사이에 작성된 내용은 `children`을 통해 전달받을 수 있다.

``` jsx
function 버튼컴포넌트({ onClick, children }) {
  return (
    <button onClick={onClick}>
      {children}
    </button>
  );
}

export default 버튼컴포넌트;
```

``` jsx
<버튼컴포넌트 onClick={() => alert('클릭')}>
  버튼 내용
</버튼컴포넌트>
```

버튼의 클릭 동작은 Props로 전달하고 버튼 내부에 표시할 내용은
`children`으로 전달하는 방법을 실습하였다.

#### 16. Props를 이용한 카드 컴포넌트 재사용

같은 형태의 카드 컴포넌트에 서로 다른 제목, 내용, 작성자 정보를 Props로
전달하여 재사용하였다.

``` jsx
function App() {
  return (
    <>
      <카드컴포넌트
        제목="첫 번째 제목"
        내용="첫 번째 내용"
        작성자="작성자1"
      />

      <카드컴포넌트
        제목="두 번째 제목"
        내용="두 번째 내용"
        작성자="작성자2"
      />
    </>
  );
}
```

화면의 구조는 그대로 유지하면서 전달하는 Props 값만 변경하여 여러 개의
카드를 만들 수 있다는 것을 확인하였다.

#### 17. 카드 컴포넌트 CSS 스타일링

카드 컴포넌트에 CSS를 적용하여 크기, 여백, 테두리, 그림자와 글자
스타일을 지정하였다.

``` css
.card {
  width: 224px;
  margin: 12px auto;
  padding: 24px;
  border: 1px solid #ddd;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  text-align: center;
}
```

``` css
.card h2 {
  margin: 0 0 8px;
  font-size: 1.33rem;
  color: #333;
}

.card p {
  margin: 2px 0;
}

.card p:last-child {
  font-size: 0.92rem;
  color: #888;
}
```

컴포넌트의 구조와 스타일을 분리하여 관리하고 동일한 카드 스타일을 여러
컴포넌트에 적용하였다.

------------------------------------------------------------------------

#### 핵심 정리

-   JSX는 JavaScript 안에서 HTML과 유사한 형태로 UI를 작성할 수 있는
    문법이다.
-   여러 JSX 요소를 반환할 때는 하나의 최상위 요소 또는 Fragment로
    감싸야 한다.
-   React에서는 일반 함수와 화살표 함수 형태로 함수형 컴포넌트를 작성할
    수 있다.
-   작성한 컴포넌트는 HTML 태그처럼 호출하고 여러 번 재사용할 수 있다.
-   Props는 부모 컴포넌트에서 자식 컴포넌트로 데이터를 전달할 때
    사용한다.
-   Props에는 문자열, 배열, 객체, 함수 등 다양한 데이터를 전달할 수
    있다.
-   Props는 객체 형태 또는 구조분해할당을 이용하여 사용할 수 있다.
-   배열을 반복하여 JSX 요소를 생성할 때는 `key`를 지정한다.
-   React 이벤트는 `onClick`과 같이 카멜 케이스로 작성한다.
-   함수를 Props로 전달하여 부모와 자식 컴포넌트 사이에서 이벤트를
    처리할 수 있다.
-   이벤트 함수의 매개변수를 이용하면 자식 컴포넌트에서 부모 컴포넌트로
    값을 전달할 수 있다.
-   `event.preventDefault()`는 요소의 기본 이벤트 동작을 막을 때
    사용한다.
-   `export default`와 `import`를 이용하여 컴포넌트를 별도의 파일로
    모듈화할 수 있다.
-   `children`을 이용하면 컴포넌트 태그 사이의 내용을 자식 컴포넌트에서
    사용할 수 있다.
-   같은 컴포넌트에 서로 다른 Props를 전달하여 동일한 UI를 재사용할 수
    있다.
-   CSS를 이용하여 컴포넌트의 구조와 스타일을 분리하여 관리할 수 있다.

---

