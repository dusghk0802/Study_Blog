# 23일차

## HTML 고급 선택자, 동적 효과와 JavaScript 기초

📌 학습일 : 2026.08.19

📌 학습 내용 : 고급 선택자, 속성 선택자, 가상 클래스, filter, transform, transition, animation, JavaScript 기본 문법, 변수, 자료형, 형 변환, 배열, alert, confirm, prompt, console.log, DOM

---

#### 1. 하위 선택자

하위 선택자는 특정 요소 안에 포함된 모든 하위 요소를 선택할 때 사용한다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>하위 선택자</title>
  <style>
    section p {
      color: blue;
    }
  </style>
</head>
<body>
  <section>
    <p>첫 번째 내용</p>

    <div>
      <p>두 번째 내용</p>
    </div>
  </section>
</body>
</html>
```

`section p`는 `<section>` 안에 있는 모든 `<p>` 요소를 선택한다.

#### 2. 자식 선택자

자식 선택자는 특정 요소의 바로 아래에 있는 자식 요소만 선택한다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>자식 선택자</title>
  <style>
    section > p {
      color: blue;
    }
  </style>
</head>
<body>
  <section>
    <p>직접 자식 요소</p>

    <div>
      <p>하위 요소</p>
    </div>
  </section>
</body>
</html>
```

`section > p`는 `<section>`의 바로 아래에 있는 `<p>`에만 적용된다.

#### 3. 인접 형제 선택자

`+`는 기준 요소 바로 다음에 있는 형제 요소 하나를 선택한다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>인접 형제 선택자</title>
  <style>
    h1 + p {
      color: blue;
    }
  </style>
</head>
<body>
  <h1>제목</h1>
  <p>첫 번째 문단</p>
  <p>두 번째 문단</p>
</body>
</html>
```

위 코드에서는 `<h1>` 바로 다음의 첫 번째 `<p>`만 선택된다.

#### 4. 형제 선택자

`~`는 기준 요소 뒤에 있는 같은 부모의 형제 요소를 모두 선택한다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>형제 선택자</title>
  <style>
    h1 ~ p {
      background-color: #222;
      color: #fff;
    }
  </style>
</head>
<body>
  <h1>제목</h1>
  <p>첫 번째 문단</p>
  <p>두 번째 문단</p>
</body>
</html>
```

#### 5. 속성 존재 선택자

속성 선택자를 이용하면 특정 속성을 가지고 있는 요소만 선택할 수 있다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>속성 선택자</title>
  <style>
    a[href] {
      background-color: yellow;
    }
  </style>
</head>
<body>
  <a>링크 없음</a>
  <a href="#">링크 1</a>
  <a href="#">링크 2</a>
</body>
</html>
```

`a[href]`는 `href` 속성을 가진 `<a>` 요소만 선택한다.

#### 6. 특정 속성값 선택

특정 속성이 지정한 값과 정확히 같은 요소만 선택할 수도 있다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>속성값 선택</title>
  <style>
    a[target="_blank"] {
      padding-right: 30px;
      background: url('images/icon.png') no-repeat right center;
    }
  </style>
</head>
<body>
  <a href="#" target="_blank">새 창 링크</a>
  <a href="#">일반 링크</a>
</body>
</html>
```

`[속성="값"]`은 속성값이 정확히 일치할 때 사용한다.

#### 7. 특정 단어가 포함된 속성 선택

`~=`를 사용하면 여러 속성값 중 특정 단어가 포함되어 있는 요소를 선택할 수 있다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>속성 단어 선택</title>
  <style>
    a[class~="button"] {
      border: 1px solid #222;
      border-radius: 5px;
      box-shadow: 4px 4px rgba(0, 0, 0, 0.4);
    }
  </style>
</head>
<body>
  <a href="#" class="menu">메뉴 1</a>
  <a href="#" class="menu button">메뉴 2</a>
</body>
</html>
```

#### 8. 특정 값으로 시작하는 속성 선택

`|=`를 사용하면 특정 값 또는 `값-`으로 시작하는 속성을 선택할 수 있다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>속성 시작값 선택</title>
  <style>
    a[title|="en"] {
      background-color: #eee;
    }
  </style>
</head>
<body>
  <a href="#" title="en">영어</a>
  <a href="#" title="en-us">영어 서비스</a>
</body>
</html>
```

#### 9. 링크 가상 클래스

링크의 상태에 따라 서로 다른 스타일을 지정할 수 있다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>링크 상태</title>
  <style>
    a:link,
    a:visited {
      color: #000;
      text-decoration: none;
    }

    a:hover,
    a:focus {
      background-color: #222;
      color: #fff;
    }

    a:active {
      background-color: red;
    }
  </style>
</head>
<body>
  <a href="#">메뉴</a>
</body>
</html>
```

* `:link` : 방문하지 않은 링크
* `:visited` : 방문한 링크
* `:hover` : 마우스를 올린 상태
* `:focus` : 요소에 포커스가 있는 상태
* `:active` : 클릭하고 있는 상태

#### 10. checked 가상 클래스

라디오 버튼이나 체크박스가 선택되었을 때 스타일을 변경할 수 있다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>선택 상태</title>
  <style>
    input:checked + label {
      color: red;
      font-weight: bold;
    }
  </style>
</head>
<body>
  <input type="radio" id="option1" name="option">
  <label for="option1">선택 항목 1</label>

  <input type="radio" id="option2" name="option">
  <label for="option2">선택 항목 2</label>
</body>
</html>
```

#### 11. 구조 가상 클래스

요소가 위치한 순서를 기준으로 선택할 수 있다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>구조 가상 클래스</title>
  <style>
    .container p:first-child {
      background-color: #fc2;
    }

    .container :last-child {
      background-color: #2cf;
    }

    .container div:only-child {
      background-color: #f0c8fc;
    }
  </style>
</head>
<body>
  <div class="container">
    <p>첫 번째</p>
    <div>두 번째</div>
    <p>마지막</p>
  </div>
</body>
</html>
```

* `:first-child` : 첫 번째 자식
* `:last-child` : 마지막 자식
* `:only-child` : 유일한 자식

#### 12. nth-child

`:nth-child()`를 사용하면 순서나 규칙을 이용해 원하는 자식 요소를 선택할 수 있다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>순서 선택</title>
  <style>
    .container :nth-child(2n) {
      background-color: #fc2;
    }
  </style>
</head>
<body>
  <div class="container">
    <div>1</div>
    <div>2</div>
    <div>3</div>
    <div>4</div>
  </div>
</body>
</html>
```

예시는 다음과 같다.

* `2n` : 짝수 번째
* `2n+1` : 홀수 번째
* `3n` : 3의 배수 번째
* `n+4` : 4번째부터
* `-n+4` : 4번째까지

#### 13. not 선택자

`:not()`은 특정 조건을 제외한 요소를 선택할 때 사용한다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>제외 선택자</title>
  <style>
    input:not([type="radio"]) {
      width: 200px;
      padding: 5px;
      border: 1px solid #ccc;
    }
  </style>
</head>
<body>
  <input type="text">
  <input type="email">
  <input type="radio">
</body>
</html>
```

위 코드에서는 `radio`를 제외한 `<input>` 요소에 스타일이 적용된다.

#### 14. 입력 상태를 이용한 속성 선택

`required`, `readonly` 등의 속성을 가진 입력 요소를 선택하여 다른 스타일을 적용할 수 있다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>입력 상태 선택</title>
  <style>
    input[required] {
      border: 1px solid red;
    }

    input[readonly] {
      border: none;
    }
  </style>
</head>
<body>
  <input type="text" required>
  <input type="text" readonly value="읽기 전용">
</body>
</html>
```

#### 15. filter 함수

`filter`를 사용하면 이미지의 밝기, 색상, 흐림 등의 시각 효과를 적용할 수 있다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>이미지 필터</title>
  <style>
    .blur {
      filter: blur(5px);
    }

    .brightness {
      filter: brightness(2);
    }

    .grayscale {
      filter: grayscale(100%);
    }

    .sepia {
      filter: sepia(100%);
    }
  </style>
</head>
<body>
  <img src="images/image.jpg" class="blur" alt="이미지">
  <img src="images/image.jpg" class="brightness" alt="이미지">
  <img src="images/image.jpg" class="grayscale" alt="이미지">
  <img src="images/image.jpg" class="sepia" alt="이미지">
</body>
</html>
```

실습한 주요 필터는 다음과 같다.

* `blur()` : 흐림
* `brightness()` : 밝기
* `contrast()` : 대비
* `drop-shadow()` : 그림자
* `grayscale()` : 흑백
* `invert()` : 색상 반전
* `sepia()` : 세피아 효과
* `opacity()` : 투명도
* `hue-rotate()` : 색상 회전
* `saturate()` : 채도

#### 16. transform 이동

`translate()`를 사용하면 요소의 위치를 이동할 수 있다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>요소 이동</title>
  <style>
    .box {
      width: 100px;
      height: 100px;
      background-color: orange;
    }

    .box:hover {
      transform: translate(50px, 20px);
    }
  </style>
</head>
<body>
  <div class="box"></div>
</body>
</html>
```

* `translateX()` : 가로 이동
* `translateY()` : 세로 이동
* `translate(x, y)` : 가로·세로 이동

#### 17. transform 확대와 축소

`scale()`을 이용하여 요소의 크기를 확대하거나 축소할 수 있다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>확대와 축소</title>
  <style>
    .box {
      width: 100px;
      height: 100px;
      background-color: orange;
    }

    .box:hover {
      transform: scale(1.5);
    }
  </style>
</head>
<body>
  <div class="box"></div>
</body>
</html>
```

* `scaleX()` : 가로 크기
* `scaleY()` : 세로 크기
* `scale()` : 전체 크기

#### 18. transform 회전

`rotate()`를 사용하면 요소를 지정한 각도만큼 회전시킬 수 있다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>회전</title>
  <style>
    img:hover {
      transform: rotate(40deg);
    }
  </style>
</head>
<body>
  <img src="images/image.png" alt="이미지">
</body>
</html>
```

양수는 시계 방향, 음수는 반시계 방향으로 회전한다.

#### 19. 3차원 회전

X, Y, Z축을 기준으로 요소를 회전시킬 수 있다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>3차원 회전</title>
  <style>
    .box {
      width: 100px;
      height: 100px;
      background-color: orange;
      transition: all 1s;
    }

    .box:hover {
      transform: rotateY(55deg);
    }
  </style>
</head>
<body>
  <div class="box"></div>
</body>
</html>
```

* `rotateX()` : X축 회전
* `rotateY()` : Y축 회전
* `rotateZ()` : Z축 회전
* `rotate3d()` : 3차원 방향 지정

#### 20. transform 비틀기

`skew()`를 사용하면 요소를 기울이거나 비틀 수 있다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>요소 비틀기</title>
  <style>
    .box {
      width: 100px;
      height: 100px;
      background-color: orange;
    }

    .box:hover {
      transform: skewX(30deg);
    }
  </style>
</head>
<body>
  <div class="box"></div>
</body>
</html>
```

#### 21. Transition

`transition`은 요소의 스타일이 변경될 때 즉시 바뀌지 않고 일정 시간 동안 자연스럽게 변화하도록 한다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>Transition</title>
  <style>
    .box {
      width: 100px;
      height: 100px;
      background-color: orange;
      transition: all 2s ease-in;
    }

    .box:hover {
      width: 200px;
      height: 200px;
      background-color: red;
      transform: rotate(270deg);
    }
  </style>
</head>
<body>
  <div class="box"></div>
</body>
</html>
```

#### 22. 가상 요소를 이용한 버튼 효과

`::before`와 `:hover`, `transition`, `transform`을 조합하여 동적인 버튼 효과를 만들 수 있다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>버튼 효과</title>
  <style>
    .button {
      position: relative;
      overflow: hidden;
      padding: 20px 60px;
    }

    .button::before {
      content: "";
      position: absolute;
      width: 0;
      height: 100%;
      background-color: #fff;
      transform: skewX(35deg);
      transition: width 1s;
    }

    .button:hover::before {
      width: 150%;
    }
  </style>
</head>
<body>
  <a href="#" class="button">버튼</a>
</body>
</html>
```

#### 23. Animation 기본 구조

`animation`과 `@keyframes`를 사용하면 요소에 반복되거나 자동 실행되는 움직임을 만들 수 있다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>Animation</title>
  <style>
    .box {
      width: 100px;
      height: 100px;
      background-color: orange;

      animation-name: move;
      animation-duration: 2s;
    }

    @keyframes move {
      from {
        transform: translateX(0);
      }

      to {
        transform: translateX(500px);
      }
    }
  </style>
</head>
<body>
  <div class="box"></div>
</body>
</html>
```

* `animation-name` : 사용할 애니메이션 이름
* `animation-duration` : 실행 시간
* `@keyframes` : 애니메이션 변화 과정 정의

#### 24. 애니메이션 반복과 방향

애니메이션을 계속 반복하거나 진행 방향을 변경할 수 있다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>Animation 반복</title>
  <style>
    .box {
      width: 100px;
      height: 100px;
      background-color: blue;

      animation-name: move;
      animation-duration: 2s;
      animation-iteration-count: infinite;
      animation-direction: alternate;
    }

    @keyframes move {
      from {
        transform: translateX(0);
      }

      to {
        transform: translateX(500px);
      }
    }
  </style>
</head>
<body>
  <div class="box"></div>
</body>
</html>
```

* `infinite` : 무한 반복
* `alternate` : 정방향과 역방향을 번갈아 실행

#### 25. JavaScript alert()

`alert()`는 사용자에게 메시지를 표시하는 알림 창을 띄운다.

```html
<script>
  alert("안녕하세요");
</script>
```

사용자가 확인 버튼을 누르면 창이 닫힌다.

#### 26. JavaScript confirm()

`confirm()`은 확인과 취소 버튼이 있는 확인 창을 표시한다.

```html
<script>
  let result = confirm("계속 진행하시겠습니까?");

  console.log(result);
</script>
```

결과는 다음과 같다.

* 확인 : `true`
* 취소 : `false`

#### 27. JavaScript prompt()

`prompt()`는 사용자로부터 값을 입력받을 때 사용한다.

```html
<script>
  let name = prompt("이름을 입력하세요");

  console.log(name);
</script>
```

입력받은 값은 기본적으로 **문자열(String)** 형태로 반환된다.

#### 28. console.log()

`console.log()`는 변수나 실행 결과를 브라우저 개발자 도구의 콘솔에서 확인할 때 사용한다.

```html
<script>
  let name = "사용자";

  console.log(name);
  console.log("안녕하세요");
</script>
```

#### 29. 변수 선언

JavaScript에서는 `let`과 `const`를 이용하여 값을 저장한다.

```html
<script>
  let age = 20;
  const pi = 3.14;

  console.log(age);
  console.log(pi);
</script>
```

* `let` : 값을 변경할 수 있는 변수
* `const` : 한 번 지정한 값을 다시 대입할 수 없는 상수

#### 30. 문자열 연결

`+`를 이용하면 문자열을 연결할 수 있다.

```html
<script>
  let name = "사용자";
  let room = 2026;

  console.log(
    name + "님은 " + room + "호로 이동하세요."
  );
</script>
```

템플릿 리터럴을 사용할 수도 있다.

```html
<script>
  let name = "사용자";
  let room = 2026;

  console.log(
    `${name}님은 ${room}호로 이동하세요.`
  );
</script>
```

템플릿 리터럴은 백틱 `` ` ``을 사용하고 변수는 `${변수명}` 형식으로 작성한다.

#### 31. 자료형 확인

`typeof` 연산자를 사용하면 값의 자료형을 확인할 수 있다.

```html
<script>
  console.log(typeof 100);
  console.log(typeof "안녕하세요");
</script>
```

결과는 각각 다음과 같다.

```text
number
string
```

#### 32. 문자열과 숫자의 연산

JavaScript에서 문자열과 숫자를 `+` 연산하면 문자열 연결이 발생할 수 있다.

```html
<script>
  let number = "10";

  console.log(number * 50);
  console.log(number + 50);
</script>
```

결과는 다음과 같다.

```text
500
1050
```

`"10" * 50`은 숫자로 변환되어 계산되지만 `"10" + 50`은 문자열 연결로 처리된다.

#### 33. 자료형 변환

JavaScript에서는 값을 다른 자료형으로 변환할 수 있다.

```html
<script>
  let number = parseInt("123");
  let text = String(123);
  let value1 = Boolean(1);
  let value2 = Boolean(0);

  console.log(number);
  console.log(text);
  console.log(value1);
  console.log(value2);
</script>
```

* `parseInt()` : 문자열을 정수로 변환
* `String()` : 문자열로 변환
* `Boolean()` : 논리값으로 변환

#### 34. 배열

배열은 여러 값을 하나의 변수에 순서대로 저장할 때 사용한다.

```html
<script>
  let items = [
    "항목 1",
    "항목 2",
    "항목 3",
    "항목 4"
  ];

  console.log(items);
  console.log(items[0]);
  console.log(items[3]);
</script>
```

배열의 위치를 나타내는 인덱스는 `0`부터 시작한다.

따라서 다음과 같다.

* `items[0]` : 첫 번째 값
* `items[1]` : 두 번째 값
* `items[2]` : 세 번째 값

#### 35. DOM 요소 선택

JavaScript에서 HTML 요소를 가져올 때 `document.getElementById()`를 사용할 수 있다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>DOM 선택</title>
</head>
<body>
  <h1 id="heading">제목</h1>

  <script>
    let heading =
      document.getElementById("heading");

    console.log(heading);
  </script>
</body>
</html>
```

HTML 요소의 `id` 값을 기준으로 JavaScript에서 해당 요소를 가져온다.

#### 36. 클릭 이벤트와 스타일 변경

가져온 HTML 요소에 클릭 이벤트를 지정하여 사용자의 동작에 따라 스타일을 변경할 수 있다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>클릭 이벤트</title>
  <style>
    h1 {
      color: blue;
    }
  </style>
</head>
<body>
  <h1 id="heading">제목</h1>
  <p>위 제목을 클릭해 보세요.</p>

  <script>
    let heading =
      document.getElementById("heading");

    heading.onclick = function() {
      heading.style.color = "red";
    };
  </script>
</body>
</html>
```

`onclick`을 사용하면 해당 요소를 클릭했을 때 지정한 함수를 실행할 수 있다.

#### 핵심 정리

* 하위·자식·형제·속성 선택자를 이용하여 조건에 맞는 HTML 요소를 세밀하게 선택할 수 있다.
* 가상 클래스와 구조 선택자를 이용하여 **링크 상태, 선택 상태, 요소의 순서**에 따라 스타일을 적용할 수 있다.
* `filter`를 이용하여 이미지의 밝기, 흐림, 색상, 채도 등의 효과를 적용할 수 있다.
* `transform`을 이용하여 요소를 **이동, 확대·축소, 회전, 비틀기** 할 수 있다.
* `transition`은 스타일 변화를 자연스럽게 연결하고 `animation`은 `@keyframes`를 이용해 자동 움직임을 만든다.
* JavaScript의 `alert()`, `confirm()`, `prompt()`를 이용하여 사용자와 간단하게 상호작용할 수 있다.
* `let`, `const`, `typeof`를 이용하여 변수와 자료형을 다룰 수 있다.
* 문자열과 숫자는 연산 방식이 다를 수 있으므로 자료형을 확인하고 필요하면 형 변환을 해야 한다.
* 배열은 여러 데이터를 순서대로 저장하며 인덱스는 `0`부터 시작한다.
* DOM과 이벤트를 이용하면 JavaScript에서 HTML 요소를 선택하고 사용자의 동작에 따라 내용을 변경할 수 있다.

---

| <img src="../../training/Web/2026.08.19/day_22_1.JPG" width="500"> | <img src="../../training/Web/2026.08.19/day_22_2.JPG" width="500"> |
| :---: | :---: |

<p align="center"><b>4개 프로필 적용 시</b></p>
