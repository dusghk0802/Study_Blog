# 21일차

## CSS 텍스트 스타일과 박스 모델

📌 학습일 : 2026.08.17

📌 학습 내용 : CSS 선택자, 글꼴, 글자 크기, 글자 색상, 텍스트 정렬, 줄 간격, 텍스트 꾸미기, 목록 스타일, 표 스타일, 블록·인라인 요소, 박스 모델, border, border-radius, margin, padding, box-shadow, display, float

---

#### 1. CSS 기본 스타일 적용

CSS는 HTML 요소의 글자, 색상, 크기, 배치 등의 디자인을 지정할 때 사용한다.

```html
<style>
  p {
    color: blue;
    font-size: 20px;
  }
</style>
```

선택자를 이용하여 스타일을 적용할 HTML 요소를 지정하고 `{ }` 안에 속성과 값을 작성한다.

```css
선택자 {
  속성: 값;
}
```

#### 2. id 선택자

`id` 선택자는 특정 요소 하나에 스타일을 적용할 때 사용한다.

HTML에서는 `id` 속성을 지정하고 CSS에서는 이름 앞에 `#`을 붙인다.

```html
<div id="container">
  내용
</div>
```

```css
#container {
  width: 600px;
  margin: 20px auto;
}
```

`id`는 하나의 HTML 문서에서 고유한 이름으로 사용하는 것이 기본이다.

#### 3. class 선택자

`class` 선택자는 여러 요소에 동일한 스타일을 적용할 때 사용한다.

HTML에서는 `class` 속성을 지정하고 CSS에서는 이름 앞에 `.`을 붙인다.

```html
<p class="accent">강조할 내용</p>
```

```css
.accent {
  font-weight: bold;
  color: red;
}
```

같은 `class`를 여러 요소에서 반복하여 사용할 수 있다.

#### 4. span 태그를 이용한 부분 스타일

`<span>`은 문장 안의 특정 부분에만 스타일을 적용할 때 사용할 수 있는 인라인 요소이다.

```html
<p>
  일반 내용 중
  <span class="accent">강조할 내용</span>
  입니다.
</p>
```

```css
.accent {
  color: red;
  font-weight: bold;
}
```

문장 전체가 아니라 일부 글자에만 색상이나 굵기 등을 적용할 때 사용할 수 있다.

#### 5. 글꼴 지정

`font-family` 속성을 사용하여 글꼴을 지정한다.

```css
h1 {
  font-family: Arial, sans-serif;
}
```

여러 글꼴을 순서대로 지정하면 앞의 글꼴을 사용할 수 없는 경우 다음 글꼴을 사용한다.

웹 글꼴을 불러와 사용할 수도 있다.

```css
@import url('웹폰트주소');

h1 {
  font-family: "웹폰트명", sans-serif;
}
```

#### 6. 글자 크기

`font-size`를 사용하여 글자의 크기를 지정한다.

```css
h1 {
  font-size: 40px;
}

p {
  font-size: 20px;
}
```

`px`을 사용하면 고정된 크기를 지정할 수 있다.

`em`은 부모 요소의 글자 크기를 기준으로 상대적인 크기를 지정한다.

```css
.accent {
  font-size: 1.5em;
}
```

#### 7. 글자 굵기와 기울기

`font-weight`는 글자의 굵기를 지정한다.

```css
.bold {
  font-weight: bold;
}
```

숫자를 이용하여 굵기를 지정할 수도 있다.

```css
.bold {
  font-weight: 900;
}
```

`font-style`은 글자의 기울기를 지정한다.

```css
.italic {
  font-style: italic;
}
```

두 속성을 함께 사용할 수도 있다.

```css
.text {
  font-weight: bold;
  font-style: italic;
}
```

#### 8. 글자 색상

`color` 속성을 사용하여 글자 색상을 지정한다.

```css
p {
  color: red;
}
```

RGB 값을 사용할 수도 있다.

```css
p {
  color: rgb(255, 0, 0);
}
```

RGBA를 사용하면 색상과 함께 투명도를 지정할 수 있다.

```css
h1 {
  color: rgba(255, 0, 0, 0.5);
}
```

마지막 값은 투명도를 나타내며 `0`에 가까울수록 투명하고 `1`에 가까울수록 불투명하다.

#### 9. 배경 이미지

`background` 속성을 이용하여 요소의 배경에 이미지를 지정할 수 있다.

```css
body {
  background: url('images/background.jpg') no-repeat fixed;
  background-size: cover;
}
```

* `url()` : 사용할 이미지 경로 지정
* `no-repeat` : 배경 이미지 반복 방지
* `fixed` : 스크롤해도 배경 위치 고정
* `background-size: cover` : 영역을 채우도록 이미지 크기 조절

#### 10. 텍스트 정렬

`text-align` 속성을 이용하여 텍스트의 가로 정렬 방법을 지정한다.

```css
.center {
  text-align: center;
}

.justify {
  text-align: justify;
}
```

주요 값은 다음과 같다.

* `left` : 왼쪽 정렬
* `right` : 오른쪽 정렬
* `center` : 가운데 정렬
* `justify` : 양쪽 정렬

#### 11. 줄 간격

`line-height`는 텍스트의 줄 높이를 지정한다.

```css
.small-line {
  line-height: 0.7;
}

.big-line {
  line-height: 2.5;
}
```

값이 커질수록 줄 사이의 간격이 넓어진다.

요소의 높이와 `line-height`를 동일하게 지정하면 한 줄의 텍스트를 세로 방향으로 가운데 정렬하는 데 사용할 수도 있다.

```css
.title {
  height: 100px;
  line-height: 100px;
  text-align: center;
}
```

#### 12. 텍스트 꾸미기

`text-decoration`을 사용하여 텍스트에 밑줄, 윗줄, 취소선 등을 적용할 수 있다.

```css
.none {
  text-decoration: none;
}

.underline {
  text-decoration: underline;
}

.overline {
  text-decoration: overline;
}

.cancel {
  text-decoration: line-through;
}
```

주요 값은 다음과 같다.

* `none` : 선 없음
* `underline` : 밑줄
* `overline` : 윗줄
* `line-through` : 취소선

링크의 기본 밑줄을 제거할 때도 많이 사용한다.

```css
a {
  text-decoration: none;
}
```

#### 13. 텍스트 그림자

`text-shadow`를 이용하여 글자에 그림자를 적용할 수 있다.

```css
.title {
  text-shadow: 5px 5px 3px #000;
}
```

기본 구조는 다음과 같다.

```css
text-shadow: 가로거리 세로거리 번짐정도 색상;
```

여러 값을 조절하여 그림자의 위치와 번짐 정도를 변경할 수 있다.

#### 14. 영문 대소문자 변환

`text-transform`은 영문자의 대소문자 표시 방법을 변경한다.

```css
.capitalize {
  text-transform: capitalize;
}

.uppercase {
  text-transform: uppercase;
}

.lowercase {
  text-transform: lowercase;
}
```

* `capitalize` : 각 단어의 첫 글자를 대문자로 표시
* `uppercase` : 모든 문자를 대문자로 표시
* `lowercase` : 모든 문자를 소문자로 표시

#### 15. 목록 스타일

`list-style-type`을 사용하여 목록 앞의 불릿이나 번호 표시 방법을 변경할 수 있다.

```css
.list1 {
  list-style-type: none;
}

.list2 {
  list-style-type: upper-alpha;
}
```

`none`을 지정하면 목록 앞의 불릿을 제거할 수 있다.

```css
ul {
  list-style-type: none;
}
```

`upper-alpha`를 사용하면 순서 목록을 대문자 알파벳 형식으로 표시할 수 있다.

#### 16. 목록에 이미지 사용

`list-style-image`를 사용하면 목록의 기본 불릿 대신 이미지를 사용할 수 있다.

```css
ul {
  list-style-image: url('images/icon.png');
}
```

목록의 각 항목 앞에 지정한 이미지가 표시된다.

#### 17. 표 스타일

CSS를 이용하여 표와 셀에 테두리를 지정할 수 있다.

```css
table {
  border: 1px solid black;
}

th,
td {
  border: 1px dotted black;
  padding: 10px;
  text-align: center;
}
```

* `border` : 테두리 지정
* `padding` : 셀 내부 여백 지정
* `text-align` : 셀 내용 정렬

`caption-side`를 사용하면 표 제목의 위치를 지정할 수 있다.

```css
table {
  caption-side: bottom;
}
```

#### 18. 표 테두리 합치기

표에서는 각 셀의 테두리가 겹쳐 두 줄처럼 표시될 수 있다.

`border-collapse`를 사용하면 테두리를 하나로 합칠 수 있다.

```css
table {
  border-collapse: collapse;
}
```

`collapse`는 서로 인접한 셀의 테두리를 하나의 테두리로 표시한다.

#### 19. 블록 레벨 요소

블록 레벨 요소는 기본적으로 부모 영역의 가로 너비를 차지하며 새로운 줄에서 시작한다.

대표적인 블록 레벨 요소는 다음과 같다.

```html
<div>영역</div>
<p>문단</p>
<h1>제목</h1>
```

블록 레벨 요소는 다른 요소와 같은 줄에 배치되지 않고 기본적으로 위에서 아래로 배치된다.

#### 20. 인라인 레벨 요소

인라인 레벨 요소는 필요한 만큼의 영역만 차지하며 다른 인라인 요소와 같은 줄에 표시될 수 있다.

```html
<p>
  일반 내용
  <span class="accent">강조 내용</span>
  일반 내용
</p>
```

대표적인 인라인 요소에는 `<span>`, `<a>`, `<strong>` 등이 있다.

#### 21. 박스 모델

HTML의 요소는 하나의 사각형 박스로 생각할 수 있으며 이를 박스 모델이라고 한다.

박스 모델은 다음 영역으로 구성된다.

* `content` : 실제 내용이 표시되는 영역
* `padding` : 내용과 테두리 사이의 내부 여백
* `border` : 요소의 테두리
* `margin` : 다른 요소와의 외부 여백

```css
.box {
  width: 200px;
  height: 100px;
  padding: 10px;
  border: 1px solid black;
  margin: 20px;
}
```

#### 22. 너비와 높이

`width`와 `height`를 이용하여 요소의 너비와 높이를 지정한다.

```css
.box {
  width: 400px;
  height: 100px;
}
```

고정된 크기뿐만 아니라 `%`를 이용하여 부모 요소를 기준으로 상대적인 크기를 지정할 수도 있다.

```css
.box {
  width: 50%;
  height: 100px;
}
```

#### 23. box-sizing

기본 박스 모델에서는 `width`와 `height`에 `padding`과 `border` 크기가 추가될 수 있다.

`box-sizing: border-box`를 사용하면 지정한 너비와 높이에 테두리와 패딩을 포함할 수 있다.

```css
* {
  box-sizing: border-box;
}
```

전체 요소에 적용하여 크기 계산을 쉽게 할 때 사용할 수 있다.

#### 24. 테두리 스타일

`border-style`은 요소의 테두리 모양을 지정한다.

```css
.box1 {
  border-style: solid;
}

.box2 {
  border-style: dashed;
}

.box3 {
  border-style: dotted;
}
```

* `solid` : 실선
* `dashed` : 긴 점선
* `dotted` : 점선

테두리의 스타일, 두께, 색상을 한 번에 지정할 수도 있다.

```css
.box {
  border: 2px solid black;
}
```

#### 25. 테두리 두께

`border-width`를 사용하여 테두리의 두께를 지정한다.

```css
.box {
  border-width: 2px;
}
```

여러 값을 지정하여 방향별로 다른 두께를 적용할 수도 있다.

```css
.box {
  border-width: 10px 5px 5px 10px;
}
```

4개의 값을 지정하면 다음 순서로 적용된다.

`위쪽 → 오른쪽 → 아래쪽 → 왼쪽`

#### 26. 테두리 색상

`border-color`를 사용하여 테두리 색상을 지정한다.

```css
.box {
  border-color: red;
}
```

특정 방향에만 다른 색상을 적용할 수도 있다.

```css
.box {
  border-top-color: blue;
  border-left-color: red;
}
```

#### 27. 둥근 테두리

`border-radius`를 사용하면 요소의 모서리를 둥글게 만들 수 있다.

```css
.box {
  border-radius: 20px;
}
```

이미지에도 적용할 수 있다.

```css
img {
  border-radius: 25px;
}
```

`50%`를 지정하면 정사각형 형태의 요소나 이미지를 원형으로 만들 수 있다.

```css
.circle {
  border-radius: 50%;
}
```

#### 28. 특정 모서리만 둥글게 만들기

모서리별 속성을 사용하면 원하는 부분만 둥글게 만들 수 있다.

```css
.box {
  border-top-left-radius: 20px;
  border-top-right-radius: 20px;
}
```

필요한 모서리에만 각각 다른 값을 적용할 수 있다.

#### 29. margin

`margin`은 요소의 테두리 바깥쪽 여백을 지정한다.

```css
.box {
  margin: 50px;
}
```

값의 개수에 따라 적용되는 방향이 달라진다.

```css
.box1 {
  margin: 30px 50px;
}

.box2 {
  margin: 30px 20px 50px;
}

.box3 {
  margin: 30px 50px 30px 50px;
}
```

* 값 1개 : 모든 방향
* 값 2개 : 위·아래 / 왼쪽·오른쪽
* 값 3개 : 위 / 왼쪽·오른쪽 / 아래
* 값 4개 : 위 / 오른쪽 / 아래 / 왼쪽

특정 방향만 지정할 수도 있다.

```css
.box {
  margin-right: 20px;
}
```

#### 30. padding

`padding`은 콘텐츠와 테두리 사이의 내부 여백을 지정한다.

```css
.box {
  padding: 10px;
}
```

버튼이나 텍스트 영역 내부에 여백을 만들 때 사용할 수 있다.

```css
button {
  padding: 15px 30px;
}
```

`margin`은 요소의 **바깥쪽 여백**, `padding`은 요소의 **안쪽 여백**이라는 차이가 있다.

#### 31. 박스 그림자

`box-shadow`를 사용하여 요소에 그림자를 적용할 수 있다.

```css
.box {
  box-shadow: 5px 5px 10px #000;
}
```

기본 구조는 다음과 같다.

```css
box-shadow: 가로거리 세로거리 번짐정도 색상;
```

버튼이나 이미지 등의 요소에 입체적인 효과를 줄 때 사용할 수 있다.

#### 32. display 속성

`display` 속성을 이용하여 요소가 화면에 표시되는 방식을 변경할 수 있다.

```css
.menu li {
  display: inline-block;
}
```

`inline-block`을 사용하면 블록 요소의 크기와 여백을 지정하면서 여러 요소를 한 줄에 배치할 수 있다.

```html
<ul class="menu">
  <li>메뉴 1</li>
  <li>메뉴 2</li>
  <li>메뉴 3</li>
</ul>
```

```css
.menu {
  list-style: none;
}

.menu li {
  display: inline-block;
  padding: 20px;
  border: 1px solid #222;
}
```

메뉴 등의 요소를 가로 방향으로 배치할 때 사용할 수 있다.

#### 33. float 속성

`float` 속성을 사용하면 요소를 왼쪽이나 오른쪽으로 배치하고 주변 콘텐츠가 해당 요소를 감싸도록 만들 수 있다.

```css
img {
  float: right;
  margin-right: 40px;
}
```

주요 값은 다음과 같다.

* `left` : 요소를 왼쪽에 배치
* `right` : 요소를 오른쪽에 배치
* `none` : float 적용하지 않음

이미지를 왼쪽이나 오른쪽에 배치하고 주변에 텍스트가 흐르도록 만들 때 사용할 수 있다.

#### 34. 폼 요소 CSS 적용

속성 선택자를 사용하면 특정 `input` 타입에만 스타일을 적용할 수 있다.

```css
input[type="text"],
input[type="password"],
input[type="email"] {
  width: 300px;
  height: 30px;
}
```

버튼에 크기와 글자 크기를 지정할 수도 있다.

```css
#buttons input {
  width: 150px;
  height: 50px;
  font-size: 20px;
}
```

`hover`를 사용하면 마우스를 요소 위에 올렸을 때 스타일을 변경할 수 있다.

```css
#buttons input:hover {
  background-color: #222;
  color: #fff;
}
```

#### 35. 드롭다운 목록 스타일

`<select>` 요소에도 CSS를 이용하여 크기를 지정할 수 있다.

```css
select {
  width: 100px;
  height: 30px;
}
```

폼의 다른 입력 요소와 크기를 맞추는 데 사용할 수 있다.

#### 핵심 정리

* CSS는 HTML 요소의 글꼴, 색상, 크기, 여백, 테두리, 배치 등을 지정한다.
* `#아이디`는 특정 요소 하나를 선택하고 `.클래스`는 여러 요소에 공통 스타일을 적용할 때 사용한다.
* `<span>`을 사용하면 문장 내부의 특정 부분에만 스타일을 적용할 수 있다.
* `font-family`, `font-size`, `font-weight`, `font-style`을 이용하여 글꼴을 설정할 수 있다.
* `color`를 이용하여 글자 색상을 지정하며 RGB와 RGBA 방식도 사용할 수 있다.
* `text-align`은 텍스트 정렬, `line-height`는 줄 간격을 지정한다.
* `text-decoration`은 밑줄·윗줄·취소선 등을 지정한다.
* `text-shadow`는 글자 그림자, `text-transform`은 영문 대소문자 표시 방식을 지정한다.
* `list-style-type`과 `list-style-image`를 이용하여 목록의 불릿이나 번호를 변경할 수 있다.
* 표에는 `border`, `padding`, `text-align`, `border-collapse` 등의 스타일을 적용할 수 있다.
* 블록 레벨 요소는 한 줄의 영역을 차지하고 인라인 요소는 필요한 영역만 차지한다.
* 박스 모델은 `content`, `padding`, `border`, `margin`으로 구성된다.
* `width`와 `height`는 요소의 크기를 지정한다.
* `box-sizing: border-box`를 사용하면 지정한 크기에 테두리와 패딩을 포함할 수 있다.
* `border`를 이용하여 테두리의 두께, 스타일, 색상을 지정할 수 있다.
* `border-radius`를 이용하여 요소나 이미지의 모서리를 둥글게 만들 수 있다.
* `margin`은 요소 바깥쪽 여백이고 `padding`은 요소 안쪽 여백이다.
* `box-shadow`를 사용하여 요소에 그림자 효과를 적용할 수 있다.
* `display: inline-block`을 사용하면 여러 요소를 가로로 배치하면서 크기와 여백을 지정할 수 있다.
* `float`를 사용하면 요소를 왼쪽이나 오른쪽에 배치하고 주변 콘텐츠가 요소를 감싸도록 만들 수 있다.
* 속성 선택자를 사용하면 특정 `input` 타입에만 CSS를 적용할 수 있다.
* `:hover`를 사용하면 마우스를 요소 위에 올렸을 때 스타일을 변경할 수 있다.
