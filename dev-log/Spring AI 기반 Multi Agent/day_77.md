# 77일차

## React: JavaScript ES6 문법 (화살표 함수, JSON 객체, 배열 함수, 이벤트, 객체 리터럴, 스프레드 연산자, 모듈, 템플릿 리터럴, 구조분해 할당, 연산자, 기본 매개변수)

📌 학습일 : 2026.10.29

📌 학습 내용 : 화살표 함수(Arrow Function), 익명 함수, let / const, JSON 객체 배열, for...in / for...of, 콜백 함수, Array.isArray, forEach, map, filter, JSON.stringify, 이벤트 객체, preventDefault, querySelector, currentTarget, this, 이벤트 버블링 / 캡처링, stopPropagation, 향상된 객체 리터럴, 속성 축약, 메서드 축약, 스프레드 연산자(...), 객체·배열 복제와 병합, inline / internal / external 스크립트, 상대경로 / 절대경로, ES6 모듈, export / import, export default, type="module", 템플릿 리터럴, 구조분해 할당, 삼항 연산자, ===, 널 병합 연산자(??), 기본 매개변수(Default Parameter)

---

#### 1. 화살표 함수 (Arrow Function)

`function` 키워드 대신 `=>`로 함수를 짧게 표현하는 방식으로, React에서 컴포넌트와 이벤트 처리 함수를 만들 때 가장 많이 쓴다.

| 형태 | 기존 함수 | 화살표 함수 |
| --- | --- | --- |
| 매개변수 없음 | `function(){ console.log("bar"); }` | `() => console.log("bar")` |
| 매개변수 1개 | `function(x){ return x + 1; }` | `x => x + 1` (괄호 생략 가능) |
| 매개변수 2개 이상 | `function(x, y){ return x + y; }` | `(x, y) => x + y` |
| 로직이 있는 경우 | `function(a, b){ let c = 100; return c; }` | `(a, b) => { let c = 100; return c; }` |

```javascript
let foo = function () { console.log("foo"); };   // 익명 함수
let bar = () => console.log("bar 함수");          // 화살표 함수

let hello = val => "hello" + val;
document.getElementById("demo").innerHTML = hello("world");   // helloworld
```

실행 블록 `{}`를 생략하면 결과가 자동으로 return되지만, `{}`를 쓰면 반드시 `return`을 직접 적어야 한다.

#### 2. JSON 객체 배열 다루기

외부 API에서 받아오는 데이터는 대부분 `{ "list": [{}, {}, {}] }`처럼 객체 안에 객체 배열이 들어 있는 형태다.

| 반복문 | 꺼내는 값 | 예 |
| --- | --- | --- |
| `for...in` | 배열의 **인덱스** (객체는 키) | `students[index].이름` |
| `for...of` | 배열의 **값** 자체 | `obj.name` |

```javascript
let myCars = {
  "name": "john", "age": 30,
  "cars": [
    {"name": "Ford", "model": ["Mustang", "Fous"]},
    {"name": "BMW",  "model": ["X5", "X7", "X8"]},
    {"name": "Fiat", "model": ["500", "Panda"]}
  ]
};

// 차량이름과 차량모델만 출력
for (let obj of myCars.cars) {
  document.write("차량이름 : " + obj.name);
  for (let index in obj.model) {
    document.write(" " + obj.model[index]);
  }
  document.write("<hr>");
}
```

#### 3. 콜백 함수와 배열 함수

콜백 함수는 다른 함수의 인자로 전달되어, 그 함수의 논리에 따라 나중에 호출되는 함수다.

| 함수 | 기능 | 반환값 |
| --- | --- | --- |
| `Array.isArray(data)` | 배열인지 확인 | true / false |
| `forEach((value, index, array) => {})` | 배열 요소 개수만큼 콜백 실행 | 없음 (undefined) |
| `map(callback)` | 콜백의 return 값으로 **새로운 배열** 생성 | 새 배열 (길이 동일) |
| `filter(callback)` | 콜백이 true인 요소만 추출 | 새 배열 (길이 줄어듦) |

```javascript
const numbers = [65, 44, 2, 7];
const newArr = numbers.map(num => num * 10);              // [650, 440, 20, 70]

const datalist = [1, 2, 3, 4, 5].map(n => n % 2 === 0 ? "짝수" : "홀수");
// ["홀수", "짝수", "홀수", "짝수", "홀수"]

const nameArray = users.map(user => user.name);            // 객체 배열에서 이름만 추출
const filterData = [2, 5, 6, 9].filter(n => n % 2 === 0);  // [2, 6]
```

**React에서 게시글 삭제 (filter)**

```javascript
const deleteBoardId = (boards, deleteId) => {
  return boards.filter(board => board.id !== deleteId);
};

const newBoardList = deleteBoardId(boards, 2);          // id 2인 글을 뺀 새 배열
console.log("data" + JSON.stringify(newBoardList));     // 객체를 문자열로 바꿔서 전송
```

React에서는 원본 배열을 직접 바꾸지 않고, `map()`으로 목록을 화면에 그리거나 수정하고 `filter()`로 삭제한 **새 배열**을 만들어 상태를 바꾼다.

#### 4. 이벤트 객체와 이벤트 전파

```javascript
document.getElementById("myAnchor").addEventListener("click", function(event){
  event.preventDefault();   // a 태그의 기본 동작(링크 이동) 막기
  alert("hello world");
});

const btn = document.querySelector(".btn");   // . → class, # → id (CSS 선택자)
btn.addEventListener("click", function(e){
  console.log(e.currentTarget);
  console.log(this);
  console.log(this === e.currentTarget);      // true
});
```

| 속성·메서드 | 의미 |
| --- | --- |
| `event.preventDefault()` | 태그의 기본 동작(링크 이동, 폼 제출 등)을 막음 |
| `event.target` | 실제로 클릭한 요소 |
| `event.currentTarget` | 리스너가 등록된 요소 (일반 함수에서는 `this`와 같음) |
| `event.stopPropagation()` | 이벤트가 더 이상 전파되지 않게 막음 |

| 구분 | 캡처링 (Capturing) | 버블링 (Bubbling) |
| --- | --- | --- |
| 방향 | 위 → 아래 (window → 클릭한 요소) | 아래 → 위 (클릭한 요소 → window) |
| 등록 | `addEventListener('click', fn, true)` | `addEventListener('click', fn)` (기본값) |
| 활용 | 부모가 먼저 이벤트를 가로챌 때 | 이벤트 위임 (부모 하나에 리스너를 달고 target으로 자식 구분) |

이벤트는 캡처링 단계 → 타깃 단계 → 버블링 단계 순서로 전달된다.

#### 5. 향상된 객체 리터럴 (Enhanced Object Literal)

| 구분 | 기존 방식 | ES6 축약 |
| --- | --- | --- |
| 속성 | `{ language: language }` | `{ language }` (키와 변수 이름이 같으면 하나만 표기) |
| 메서드 | `coding: function() {}` | `coding() {}` |

```javascript
const language = "java";
const dataObj5 = {
  language,
  coding() {
    console.log(this.language + "...coding");
  }
};
dataObj5.coding();   // java...coding
```

React, Vue에서는 props나 state를 객체로 넘길 때 이 축약 표현을 많이 사용한다.

#### 6. 스프레드 연산자 (Spread Operator)

`...`은 배열, 객체, 문자열의 요소를 펼쳐서 **복제하거나 합칠 때** 사용한다.

```javascript
const obj = {a: 10, b: 20};
let newobj = {...obj};                                   // 복제

const combineArray = [...[1, 2, 3], ...[4, 5, 6]];       // [1, 2, 3, 4, 5, 6]
const combineObj = {...{name: "길동"}, ...{age: 25}};    // {name: "길동", age: 25}

// React, Vue에서 데이터 추가
const person = {name: "김유신", age: 40};
const updatePerson = {...person, city: "seoul"};         // {name: "김유신", age: 40, city: "seoul"}
```

React의 상태는 직접 수정하지 않고, 스프레드로 기존 값을 복제한 뒤 바뀐 값만 덮어쓴 새 객체로 교체한다.

#### 7. 스크립트 적용 방식과 경로

| 방식 | 예 |
| --- | --- |
| inline | `<button onclick="함수()">` |
| internal | 페이지 내부의 `<script> ... </script>` |
| external | `<script src="./a.js"></script>` 별도 js 파일 연결 |

| 경로 | 의미 |
| --- | --- |
| `./` | 상대경로, 현재 파일이 있는 디렉토리 |
| `../` | 상대경로, 상위(부모) 디렉토리 |
| `/` | 웹의 루트 디렉토리 |
| `http://...` | 절대경로 (예: AWS S3에 올린 이미지, React 정적 파일) |

a.js에서 `var total = 200;`, b.js에서 `var total = 500;`을 선언하고 차례로 불러오면, 두 파일이 같은 전역 공간을 써서 나중에 불러온 b.js의 값 500으로 덮어써진다.

#### 8. ES6 모듈 (export / import)

전역 변수가 충돌하는 문제를 막고 파일 단위로 코드를 나눠 쓰기 위해 모듈을 사용한다. React, Vue의 컴포넌트도 모두 이 방식으로 불러온다.

```javascript
// data.js : 공통으로 사용하는 script
export const pi = 3.14;
export function sum(i, j) { return i + j; }

// app.js
import {pi, sum} from './data.js';
console.log(pi);                    // 3.14
console.log("data: " + sum(10, 20));   // data: 30
```

```javascript
// data2.js
const message = () => { ... };
export default message;

// app2.js
import message from './data2.js';   // default는 {} 없이, 이름을 자유롭게 지정
console.log(`message ${message()}`);
```

| 구분 | named export | default export |
| --- | --- | --- |
| 내보내기 | `export const pi` | `export default message` |
| 개수 | 파일당 여러 개 | 파일당 1개 |
| 가져오기 | `import {pi, sum} from` (이름 동일, 중괄호 필수) | `import message from` (중괄호 없음) |

HTML에서 모듈 파일을 불러올 때는 `<script type="module" src="./app.js"></script>`처럼 `type="module"`을 반드시 지정해야 한다.

#### 9. 템플릿 리터럴 (Template Literal)

백틱(`` ` ``)으로 문자열을 감싸고 `${}` 안에 변수나 표현식을 넣어 문자열과 결합한다. Java의 `printf`와 비슷한 역할이다.

```javascript
const lang = "javascript";
const expression  = "i love " + lang + "!!!";                          // 기존 방식
const expression2 = `i love ${lang.split('').reverse().join('')} !!!`; // i love tpircsavaj !!!

const user4 = { name: "순신", age: 30 };
const userData = `사랑하는 ${user4.name} 이제 너도 ${user4.age}가 되었구나`;
```

작은따옴표나 큰따옴표로 감싸면 `${}`가 그대로 글자로 출력되므로 반드시 백틱을 사용해야 한다.

#### 10. 구조분해 할당 (Destructuring Assignment)

배열이나 객체의 값을 꺼내 변수에 한 번에 담는 문법으로, React에서 props와 useState를 받을 때 매번 사용한다.

| 구분 | 기준 | 예 |
| --- | --- | --- |
| 배열 | **순서(위치)** | `const [a2, b2, c2] = ["바나나", "망고", "딸기"];` |
| 배열 건너뛰기 | 쉼표로 자리 비우기 | `const [, , th3, th4] = [1, 2, 3, 4, 5];` → 3, 4 |
| 객체 | **속성 이름** (순서 무관) | `const {lang, position, area} = smith;` |

```javascript
const person = { name: "길동", age: 20 };

const name2 = person.name;          // 기존 방식
const age2  = person.age;

const {name, age} = person;          // ES6 : 속성 이름과 같은 변수명으로 꺼냄
const {name: name3, age: age3} = person;   // 다른 이름으로 받고 싶을 때
```

객체 구조분해는 속성 이름으로 값을 찾기 때문에, `const {name3, age3} = person;`처럼 객체에 없는 이름을 쓰면 undefined가 된다.

#### 11. 연산자와 기본 매개변수

| 연산자 | 의미 | 예 |
| --- | --- | --- |
| 삼항 연산자 | `조건 ? A : B` | `n % 2 === 0 ? "짝수" : "홀수"` |
| `===` | 값과 **타입**까지 같은지 비교 | `1 === "1"` → false |
| `??` (널 병합) | 왼쪽이 **null / undefined**일 때만 오른쪽 값 사용 | `text ?? "hello world"` |
| `\|\|` (OR) | 왼쪽이 **falsy**(0, "", false, null, undefined)면 오른쪽 값 사용 | `h \|\| 100` |

```javascript
function printTitle(text) {
  let title = text ?? "hello world";
  console.log(title);
}
printTitle("AAAA");   // AAAA
printTitle();         // hello world

// 기본 매개변수 (Default Parameter)
function printPersonData2(h = 1111, w = 2222, age = 3333) {
  console.log(h, w, age);
}
printPersonData2();               // 1111 2222 3333
printPersonData2(111, 222, 333);  // 111 222 333
```

`||`는 0이나 빈 문자열도 값이 없다고 보고 기본값으로 바꾸지만, `??`와 기본 매개변수는 undefined(또는 null)일 때만 기본값을 쓰므로 0을 정상 값으로 유지할 수 있다.

#### 핵심 정리

- 화살표 함수는 `=>`로 함수를 짧게 표현하며, `{}`를 생략하면 결과가 자동으로 return된다.
- `for...in`은 인덱스(키)를, `for...of`는 값을 꺼낸다.
- `map()`은 새 배열을 만들고, `filter()`는 조건에 맞는 요소만 남긴 새 배열을 만들며, React에서는 각각 목록 출력·수정과 삭제에 사용한다.
- `preventDefault()`는 기본 동작을, `stopPropagation()`은 이벤트 전파를 막는다.
- 이벤트는 캡처링(위 → 아래) → 타깃 → 버블링(아래 → 위) 순서로 전달되며, 기본 리스너는 버블링 단계에서 실행된다.
- 향상된 객체 리터럴은 키와 변수명이 같을 때 속성을, `function`을 생략해 메서드를 축약한다.
- 스프레드 연산자 `...`로 배열과 객체를 복제하고 합치며, React는 이 방식으로 새 상태를 만든다.
- 여러 js 파일을 `<script src>`로 불러오면 전역 변수가 충돌하므로 `export` / `import` 모듈을 사용한다.
- default export는 파일당 1개이고 중괄호 없이 가져오며, named export는 중괄호와 같은 이름으로 가져온다.
- 템플릿 리터럴은 백틱과 `${}`로 문자열과 표현식을 결합한다.
- 배열 구조분해는 순서로, 객체 구조분해는 속성 이름으로 값을 꺼낸다.
- `??`는 null / undefined일 때만, `||`는 falsy일 때 기본값을 사용한다.

---

React를 시작하기 전에 꼭 알아야 하는 ES6 문법인 화살표 함수, 배열 함수, 스프레드 연산자, 모듈, 템플릿 리터럴, 구조분해 할당, 기본 매개변수를 하루 동안 이어서 정리했다.
</br>게시글 삭제를 filter로 구현해 보면서 React에서는 원본 배열을 직접 바꾸지 않고 새 배열을 만들어 화면을 다시 그린다는 흐름을 이해할 수 있었다.
</br>fillterData와 filterData처럼 변수 이름이 한 글자만 달라도 ReferenceError가 나서 그 아래 코드가 전부 실행되지 않았고, funtion 오타나 객체 속성 사이의 쉼표 누락 같은 문법 에러는 script 전체를 멈추게 한다는 것을 직접 겪었다.
</br>그래서 브라우저 개발자 도구의 콘솔에서 에러 메시지와 줄 번호를 먼저 확인하는 습관이 중요하다고 느꼈다.
</br>a.js와 b.js에서 같은 이름의 전역 변수를 선언하면 나중에 불러온 값으로 덮어써지는 것을 보며, React와 Vue가 export와 import로 파일을 모듈 단위로 나누는 이유를 알게 되었다.
</br>객체 구조분해는 순서가 아니라 속성 이름으로 값을 찾는다는 점과, ||와 ??가 0이나 빈 문자열을 다르게 처리한다는 점은 헷갈리기 쉬워 다시 정리해 두어야겠다.
</br>다음 수업에서 React 컴포넌트를 만들 때 오늘 배운 문법이 props, state, 목록 출력에 어떻게 쓰이는지 연결해서 보고 싶다.
