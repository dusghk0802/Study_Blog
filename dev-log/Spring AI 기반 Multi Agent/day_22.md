# 22일차

## HTML 배경 스타일, 반응형 웹과 레이아웃

📌 학습일 : 2026.08.18

📌 학습 내용 : background-clip, background-image, background-repeat, background-position, background-attachment, background-origin, background-size, linear-gradient, radial-gradient, 반응형 이미지, object-fit, 미디어 쿼리, Flexbox, Grid

---

#### 1. 배경색 적용 범위 지정

`background-clip`은 요소의 배경색이 어느 영역까지 적용될지 지정한다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>배경색 적용 범위</title>
  <style>
    .box {
      width: 300px;
      padding: 20px;
      border: 10px dotted #222;
      background-color: #ffd9a0;
    }

    .border {
      background-clip: border-box;
    }

    .padding {
      background-clip: padding-box;
    }

    .content {
      background-clip: content-box;
    }
  </style>
</head>
<body>
  <div class="box border">영역 1</div>
  <div class="box padding">영역 2</div>
  <div class="box content">영역 3</div>
</body>
</html>
```

* `border-box` : 테두리 영역까지 배경 적용
* `padding-box` : 패딩 영역까지 배경 적용
* `content-box` : 콘텐츠 영역에만 배경 적용

#### 2. 배경 이미지 삽입

`background-image`를 사용하면 HTML 요소의 배경에 이미지를 지정할 수 있다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>배경 이미지</title>
  <style>
    body {
      background-image: url('이미지 경로');
    }
  </style>
</head>
<body>
</body>
</html>
```

#### 3. 배경 이미지 반복

`background-repeat`을 사용하여 배경 이미지의 반복 방식을 지정한다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>배경 이미지 반복</title>
  <style>
    body {
      background-image: url('이미지 경로');
      background-repeat: no-repeat;
    }
  </style>
</head>
<body>
</body>
</html>
```

* `repeat` : 가로와 세로 반복
* `repeat-x` : 가로 반복
* `repeat-y` : 세로 반복
* `no-repeat` : 반복하지 않음

#### 4. 배경 이미지 위치 지정

`background-position`은 배경 이미지가 표시될 위치를 지정한다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>배경 이미지 위치</title>
  <style>
    body {
      background-image: url('이미지 경로');
      background-repeat: no-repeat;
      background-position: right top;
    }
  </style>
</head>
<body>
</body>
</html>
```

#### 5. 배경 이미지 고정

`background-attachment`를 사용하면 스크롤할 때 배경 이미지의 움직임을 지정할 수 있다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>배경 이미지 고정</title>
  <style>
    body {
      background-image: url('이미지 경로');
      background-repeat: no-repeat;
      background-position: right top;
      background-attachment: fixed;
    }
  </style>
</head>
<body>
</body>
</html>
```

`fixed`는 스크롤해도 배경 이미지를 같은 위치에 고정한다.

#### 6. 배경 이미지 시작 위치

`background-origin`은 배경 이미지가 시작되는 기준 영역을 지정한다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>배경 이미지 시작 위치</title>
  <style>
    .box {
      width: 300px;
      height: 200px;
      padding: 20px;
      border: 20px solid #ccc;
      background-image: url('이미지 경로');
      background-repeat: no-repeat;
    }

    .padding {
      background-origin: padding-box;
    }

    .border {
      background-origin: border-box;
    }

    .content {
      background-origin: content-box;
    }
  </style>
</head>
<body>
  <div class="box padding"></div>
  <div class="box border"></div>
  <div class="box content"></div>
</body>
</html>
```

#### 7. 배경 이미지 크기

`background-size`를 이용하여 배경 이미지의 크기를 조절할 수 있다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>배경 이미지 크기</title>
  <style>
    .box {
      width: 300px;
      height: 300px;
      background: url('이미지 경로') no-repeat;
    }

    .size1 {
      background-size: auto;
    }

    .size2 {
      background-size: 50%;
    }

    .size3 {
      background-size: contain;
    }

    .size4 {
      background-size: cover;
    }
  </style>
</head>
<body>
  <div class="box size1"></div>
  <div class="box size2"></div>
  <div class="box size3"></div>
  <div class="box size4"></div>
</body>
</html>
```

* `auto` : 원래 크기 유지
* `%` : 요소 크기를 기준으로 지정
* `contain` : 이미지 전체가 보이도록 조절
* `cover` : 영역 전체를 채우도록 조절

#### 8. 목록 불릿에 배경 이미지 사용

기본 불릿을 제거하고 배경 이미지를 이용하여 목록 앞에 이미지를 표시할 수 있다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>목록 이미지</title>
  <style>
    ul {
      list-style: none;
    }

    li {
      padding-left: 40px;
      line-height: 40px;
      background-image: url('이미지 아이콘 경로');
      background-repeat: no-repeat;
      background-position: left center;
    }
  </style>
</head>
<body>
  <ul>
    <li>메뉴 1</li>
    <li>메뉴 2</li>
    <li>메뉴 3</li>
  </ul>
</body>
</html>
```

#### 9. 선형 그라데이션

`linear-gradient()`를 이용하면 직선 방향으로 색상이 자연스럽게 변하는 배경을 만들 수 있다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>선형 그라데이션</title>
  <style>
    .gradient {
      width: 500px;
      height: 300px;
      background: linear-gradient(
        to right bottom,
        blue,
        white
      );
    }
  </style>
</head>
<body>
  <div class="gradient"></div>
</body>
</html>
```

각도를 직접 지정할 수도 있다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>각도 그라데이션</title>
  <style>
    .gradient {
      width: 500px;
      height: 300px;
      background: linear-gradient(45deg, red, white);
    }
  </style>
</head>
<body>
  <div class="gradient"></div>
</body>
</html>
```

#### 10. 원형 그라데이션

`radial-gradient()`를 이용하면 중심에서 바깥쪽으로 퍼지는 형태의 그라데이션을 만들 수 있다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>원형 그라데이션</title>
  <style>
    .gradient {
      width: 300px;
      height: 300px;
      background: radial-gradient(
        circle,
        white,
        yellow,
        red
      );
    }
  </style>
</head>
<body>
  <div class="gradient"></div>
</body>
</html>
```

시작 위치도 지정할 수 있다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>원형 그라데이션 위치</title>
  <style>
    .gradient {
      width: 300px;
      height: 300px;
      border-radius: 50%;
      background: radial-gradient(
        circle at 20% 20%,
        white,
        blue
      );
    }
  </style>
</head>
<body>
  <div class="gradient"></div>
</body>
</html>
```

#### 11. 반응형 이미지

이미지가 부모 영역보다 커지지 않도록 설정하면 브라우저 크기에 따라 자동으로 크기가 조절된다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, initial-scale=1.0">
  <title>반응형 이미지</title>
  <style>
    .responsive-image {
      max-width: 100%;
      height: auto;
    }
  </style>
</head>
<body>
  <img
    src="images/image.jpg"
    alt="이미지"
    class="responsive-image">
</body>
</html>
```

#### 12. object-fit

`object-fit`은 지정된 영역 안에서 이미지가 표시되는 방식을 설정한다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>이미지 크기 조절</title>
  <style>
    .container {
      width: 200px;
      height: 300px;
    }

    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
  </style>
</head>
<body>
  <div class="container">
    <img src="이미지 경로" alt="이미지">
  </div>
</body>
</html>
```

주요 값은 다음과 같다.

* `fill` : 영역에 맞게 이미지 크기 변경
* `contain` : 이미지 전체 표시
* `cover` : 비율을 유지하며 영역 전체 채움
* `none` : 원래 크기 유지
* `scale-down` : 더 작은 크기로 표시

#### 13. 미디어 쿼리

미디어 쿼리를 사용하면 화면 크기에 따라 서로 다른 스타일을 적용할 수 있다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, initial-scale=1.0">
  <title>미디어 쿼리</title>
  <style>
    body {
      background: url('이미지 경로') no-repeat fixed;
      background-size: cover;
    }

    @media screen and (max-width: 767px) {
      body {
        background: url('이미지 경로') no-repeat fixed;
        background-size: cover;
      }
    }
  </style>
</head>
<body>
  <h1>반응형 페이지</h1>
</body>
</html>
```

#### 14. Flexbox 기본 구조

`display: flex`를 사용하면 내부 요소를 플렉스 항목으로 배치할 수 있다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>Flexbox</title>
  <style>
    .container {
      display: flex;
    }

    .item {
      width: 100px;
      padding: 20px;
      border: 1px solid #222;
    }
  </style>
</head>
<body>
  <div class="container">
    <div class="item">1</div>
    <div class="item">2</div>
    <div class="item">3</div>
  </div>
</body>
</html>
```

#### 15. flex-direction

`flex-direction`은 플렉스 항목의 배치 방향을 지정한다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>Flex 방향</title>
  <style>
    .container {
      display: flex;
      flex-direction: row;
    }
  </style>
</head>
<body>
  <div class="container">
    <div>1</div>
    <div>2</div>
    <div>3</div>
  </div>
</body>
</html>
```

* `row` : 왼쪽 → 오른쪽
* `row-reverse` : 오른쪽 → 왼쪽
* `column` : 위 → 아래
* `column-reverse` : 아래 → 위

#### 16. flex-wrap과 flex-flow

`flex-wrap`은 공간이 부족할 경우 플렉스 항목의 줄바꿈 여부를 지정한다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>Flex 줄바꿈</title>
  <style>
    .container {
      display: flex;
      flex-wrap: wrap;
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

`flex-flow`를 사용하면 방향과 줄바꿈을 한 번에 지정할 수 있다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>flex-flow</title>
  <style>
    .container {
      display: flex;
      flex-flow: row wrap;
    }
  </style>
</head>
<body>
  <div class="container">
    <div>1</div>
    <div>2</div>
    <div>3</div>
  </div>
</body>
</html>
```

#### 17. justify-content

`justify-content`는 주축을 기준으로 플렉스 항목의 정렬 방법을 지정한다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>주축 정렬</title>
  <style>
    .container {
      display: flex;
      justify-content: space-between;
    }
  </style>
</head>
<body>
  <div class="container">
    <div>1</div>
    <div>2</div>
    <div>3</div>
  </div>
</body>
</html>
```

* `flex-start` : 시작점 정렬
* `flex-end` : 끝점 정렬
* `center` : 가운데 정렬
* `space-between` : 양 끝을 기준으로 일정 간격
* `space-around` : 각 항목 주변에 일정 간격

#### 18. align-items와 화면 중앙 배치

`align-items`는 교차축을 기준으로 플렉스 항목을 정렬한다.

`justify-content`와 함께 사용하면 요소를 화면 중앙에 배치할 수 있다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>화면 중앙 배치</title>
  <style>
    body {
      min-height: 100vh;
      display: flex;
      justify-content: center;
      align-items: center;
    }
  </style>
</head>
<body>
  <button>버튼</button>
</body>
</html>
```

#### 19. align-content

`align-content`는 여러 줄로 배치된 플렉스 항목을 교차축 방향으로 정렬한다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>여러 줄 정렬</title>
  <style>
    .container {
      width: 300px;
      height: 300px;
      display: flex;
      flex-flow: row wrap;
      align-content: center;
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

#### 20. flex-basis와 flex-grow

`flex-basis`는 플렉스 항목의 기본 크기를 지정하고 `flex-grow`는 남는 공간을 차지할 비율을 지정한다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>Flex 항목 크기</title>
  <style>
    .container {
      display: flex;
    }

    .item {
      flex-basis: 100px;
    }

    .item1 {
      flex-grow: 1;
    }

    .item2 {
      flex-grow: 2;
    }
  </style>
</head>
<body>
  <div class="container">
    <div class="item item1">1</div>
    <div class="item item2">2</div>
  </div>
</body>
</html>
```

#### 21. gap

`gap`은 Flex 또는 Grid 항목 사이의 간격을 지정한다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>항목 간격</title>
  <style>
    .container {
      display: flex;
      gap: 30px;
    }
  </style>
</head>
<body>
  <div class="container">
    <div>1</div>
    <div>2</div>
    <div>3</div>
  </div>
</body>
</html>
```

#### 22. Grid 기본 구조

`display: grid`를 사용하면 행과 열을 기준으로 요소를 배치할 수 있다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>Grid</title>
  <style>
    .container {
      display: grid;
      grid-template-columns: 100px 200px 300px;
      grid-template-rows: 50px 100px;
    }

    .item {
      border: 1px solid #222;
    }
  </style>
</head>
<body>
  <div class="container">
    <div class="item">1</div>
    <div class="item">2</div>
    <div class="item">3</div>
    <div class="item">4</div>
    <div class="item">5</div>
    <div class="item">6</div>
  </div>
</body>
</html>
```

#### 23. repeat()와 fr 단위

`repeat()`를 사용하면 반복되는 값을 간단하게 작성할 수 있고 `fr`은 공간을 비율로 나누는 단위이다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>Grid 열</title>
  <style>
    .container {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
    }
  </style>
</head>
<body>
  <div class="container">
    <div>1</div>
    <div>2</div>
    <div>3</div>
  </div>
</body>
</html>
```

`repeat(3, 1fr)`은 같은 너비의 열 3개를 만든다.

#### 24. minmax()

`minmax()`를 이용하여 Grid 행이나 열의 최소 크기와 최대 크기를 지정할 수 있다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>Grid 행 높이</title>
  <style>
    .container {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      grid-auto-rows: minmax(100px, auto);
    }
  </style>
</head>
<body>
  <div class="container">
    <div>내용 1</div>
    <div>내용 2</div>
    <div>내용 3</div>
  </div>
</body>
</html>
```

#### 25. Grid 항목 간격

Grid에서도 `gap`을 이용하여 행과 열 사이의 간격을 지정한다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>Grid 간격</title>
  <style>
    .container {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 20px 30px;
    }
  </style>
</head>
<body>
  <div class="container">
    <div>1</div>
    <div>2</div>
    <div>3</div>
    <div>4</div>
    <div>5</div>
    <div>6</div>
  </div>
</body>
</html>
```

#### 26. Grid 라인을 이용한 배치

`grid-column`과 `grid-row`를 이용하여 Grid 항목이 차지하는 영역을 지정한다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>Grid 영역</title>
  <style>
    .container {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      grid-template-rows: repeat(3, 100px);
    }

    .item1 {
      grid-column: 1 / -1;
      grid-row: 1;
    }

    .item2 {
      grid-column: 1;
      grid-row: 2 / -1;
    }
  </style>
</head>
<body>
  <div class="container">
    <div class="item1">영역 1</div>
    <div class="item2">영역 2</div>
  </div>
</body>
</html>
```

#### 27. grid-template-areas

Grid의 각 영역에 이름을 지정하여 레이아웃을 만들 수 있다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>Grid 영역 이름</title>
  <style>
    .container {
      display: grid;

      grid-template-areas:
        "item1 item1 item2"
        "item1 item1 item3"
        "item4 item5 item6";
    }

    .item1 {
      grid-area: item1;
    }

    .item2 {
      grid-area: item2;
    }

    .item3 {
      grid-area: item3;
    }
  </style>
</head>
<body>
  <div class="container">
    <div class="item1">1</div>
    <div class="item2">2</div>
    <div class="item3">3</div>
  </div>
</body>
</html>
```

#### 28. 외부 스타일 파일 연결

`<link>` 태그를 사용하여 HTML 문서와 외부 스타일 파일을 연결할 수 있다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>외부 스타일 연결</title>
  <link rel="stylesheet" href="css/style.css">
</head>
<body>
  <section>
    <h1>제목</h1>
  </section>
</body>
</html>
```

HTML 구조와 스타일을 서로 다른 파일로 관리할 수 있다.

#### 29. 프로필 페이지 구조

`section`, `nav`, `article`, `ul` 등을 조합하여 프로필 형태의 페이지를 구성할 수 있다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, initial-scale=1.0">

  <title>프로필 페이지</title>
  <link rel="stylesheet" href="css/style.css">
</head>
<body class="member">
  <section>
    <nav class="menu">
      <a href="#">메뉴</a>
    </nav>

    <article class="profile">
      <img
        src="이미지 경로"
        alt="프로필 이미지">

      <h1>사용자 이름</h1>
      <h2>사용자 소개</h2>

      <a href="#" class="btnView">
        자세히 보기
      </a>
    </article>

    <ul class="contact">
      <li>연락처 1</li>
      <li>연락처 2</li>
    </ul>
  </section>
</body>
</html>
```

#### 핵심 정리

* 배경 관련 속성을 이용하여 이미지의 **범위, 위치, 반복, 크기**를 조절할 수 있다.
* `linear-gradient()`와 `radial-gradient()`로 다양한 그라데이션 배경을 만들 수 있다.
* 반응형 이미지와 미디어 쿼리를 이용하여 **화면 크기에 대응하는 웹 페이지**를 만들 수 있다.
* Flexbox는 요소를 **한 방향으로 배치하고 정렬**할 때 사용한다.
* `justify-content`, `align-items`, `flex-wrap` 등으로 플렉스 항목의 위치와 배치를 조절할 수 있다.
* Grid는 요소를 **행과 열을 기준으로 배치**할 때 사용한다.
* `grid-template`, `grid-column`, `grid-row`, `grid-area` 등을 이용하여 다양한 Grid 레이아웃을 구성할 수 있다.
* `gap`을 이용하면 Flex와 Grid 항목 사이의 간격을 쉽게 지정할 수 있다.
* 외부 스타일 파일을 연결하여 HTML 구조와 스타일을 분리해서 관리할 수 있다.

---


| <img src="../../training/Web/2026.08.18/day_22_2.JPG" width="500"> | <img src="../../training/Web/2026.08.18/day_22_3.JPG" width="500"> | <img src="../../training/Web/2026.08.18/day_22_4.JPG" width="500"> | <img src="../../training/Web/2026.08.18/day_22_5.JPG" width="500"> |
| :---: | :---: | :---: | :---: |

<p align="center"><b>4개 프로필 적용 시</b></p>
