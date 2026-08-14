# 20일차

## HTML 링크, 멀티미디어 및 폼 입력 요소

📌 학습일 : 2026.08.14

📌 학습 내용 : 링크, 앵커, 이미지 링크, 오디오, 비디오, form, fieldset, legend, label, input, textarea, checkbox, radio, 날짜·시간 입력, 파일 첨부, 입력 속성

#### 1. 링크 연결

`<a>` 태그는 다른 페이지나 외부 사이트로 이동할 수 있는 링크를 만들 때 사용한다.

```html
<a href="페이지주소">링크 내용</a>
```

`href` 속성에는 이동할 페이지의 주소를 지정한다.

```html
<a href="order.html">주문하기</a>
<a href="https://www.naver.com">네이버 방문하기</a>
```

`target="_blank"`를 사용하면 링크를 새로운 탭에서 열 수 있다.

```html
<a href="order.html" target="_blank">주문서 작성하기</a>
```

#### 2. 이미지를 이용한 링크

`<a>` 태그 안에 `<img>` 태그를 넣으면 이미지를 클릭했을 때 다른 페이지로 이동하도록 만들 수 있다.

```html
<a href="order.html">
  <img src="images/product.jpg" alt="상품 이미지">
</a>
```

이미지 자체가 링크 역할을 하기 때문에 상품 이미지나 배너 등을 클릭하여 페이지를 이동할 때 사용할 수 있다.

#### 3. 앵커를 이용한 페이지 내부 이동

페이지의 특정 위치로 바로 이동하려면 이동할 요소에 `id`를 지정하고 `<a>` 태그의 `href`에 `#아이디`를 작성한다.

```html
<ul>
  <li><a href="#info">상품 정보</a></li>
  <li><a href="#recipe">상품 레시피</a></li>
  <li><a href="#product">상품 구성</a></li>
</ul>

<h2 id="info">상품 정보</h2>
<h2 id="recipe">상품 레시피</h2>
<h2 id="product">상품 구성</h2>
```

같은 페이지의 내용이 많을 때 원하는 위치로 빠르게 이동할 수 있다.

#### 4. 오디오 삽입

웹 페이지에 오디오 파일을 삽입할 때 `<audio>` 태그를 사용한다.

```html
<audio src="medias/music.mp3" controls></audio>
```

주요 속성은 다음과 같다.

* `controls` : 재생 및 볼륨 등의 컨트롤 표시
* `autoplay` : 페이지가 열리면 자동 재생
* `loop` : 반복 재생
* `muted` : 음소거 상태로 재생

```html
<audio src="medias/music.mp3" autoplay loop></audio>
```

#### 5. 비디오 삽입

웹 페이지에 동영상을 삽입할 때 `<video>` 태그를 사용한다.

```html
<video src="medias/video.mp4" controls width="700"></video>
```

동영상이 재생되기 전에 표시할 이미지는 `poster` 속성으로 지정할 수 있다.

```html
<video
  src="medias/video.mp4"
  controls
  width="700"
  poster="images/thumbnail.jpg"
  muted
  loop>
</video>
```

주요 속성은 다음과 같다.

* `controls` : 동영상 컨트롤 표시
* `width` : 동영상 너비 지정
* `poster` : 재생 전 표시할 이미지 지정
* `muted` : 음소거
* `loop` : 반복 재생
* `autoplay` : 자동 재생

#### 6. embed 태그

`<embed>` 태그를 사용하면 오디오, 비디오 등의 외부 콘텐츠를 웹 페이지에 삽입할 수 있다.

```html
<embed src="medias/music.mp3">
```

#### 7. form 태그

사용자로부터 정보를 입력받는 영역을 만들 때 `<form>` 태그를 사용한다.

```html
<form>
  입력 요소
</form>
```

폼 내부에는 `<input>`, `<textarea>`, `<button>` 등의 입력 요소를 사용할 수 있다.

#### 8. fieldset과 legend

`<fieldset>`은 폼에서 관련된 입력 요소들을 하나의 그룹으로 묶을 때 사용한다.

`<legend>`는 해당 그룹의 제목을 표시한다.

```html
<form>
  <fieldset>
    <legend>배송 정보</legend>

    <label>
      이름
      <input type="text">
    </label>
  </fieldset>
</form>
```

폼의 내용이 많을 때 상품 정보, 개인정보, 배송 정보 등의 영역을 구분하기 좋다.

#### 9. label 태그

`<label>`은 입력 요소가 어떤 정보를 입력하는 곳인지 표시할 때 사용한다.

```html
<label for="user-name">이름</label>
<input type="text" id="user-name">
```

`label`의 `for` 값과 `input`의 `id` 값을 동일하게 지정하면 서로 연결된다.

```html
<label for="phone">연락처</label>
<input type="tel" id="phone">
```

#### 10. 텍스트와 비밀번호 입력

일반적인 한 줄 텍스트는 `type="text"`를 사용한다.

```html
<input type="text">
```

비밀번호처럼 입력 내용을 화면에서 숨기려면 `type="password"`를 사용한다.

```html
<input type="password">
```

입력 칸의 크기는 `size` 속성으로 지정할 수 있다.

```html
<input type="text" size="10">
```

#### 11. 체크박스

여러 항목 중 하나 이상을 선택할 수 있도록 만들 때 `checkbox`를 사용한다.

```html
<label>
  <input type="checkbox" value="product1">
  상품 1
</label>

<label>
  <input type="checkbox" value="product2">
  상품 2
</label>
```

체크박스는 여러 항목을 동시에 선택할 수 있다.

#### 12. 라디오 버튼

여러 항목 중 하나만 선택하도록 만들 때 `radio`를 사용한다.

```html
<label>
  <input type="radio" name="gift" value="yes">
  포장함
</label>

<label>
  <input type="radio" name="gift" value="no">
  포장 안함
</label>
```

같은 `name` 값을 지정하면 해당 그룹에서는 하나의 항목만 선택할 수 있다.

#### 13. 숫자와 범위 입력

숫자를 입력할 때는 `type="number"`를 사용한다.

```html
<input type="number" min="0" max="5">
```

`min`과 `max`를 사용하여 입력 가능한 최소값과 최대값을 지정할 수 있다.

범위에서 값을 선택하도록 만들 때는 `type="range"`를 사용할 수 있다.

```html
<input type="range" min="0" max="5">
```

#### 14. 이메일과 전화번호 입력

이메일 주소를 입력할 때는 `type="email"`을 사용한다.

```html
<input type="email">
```

전화번호를 입력할 때는 `type="tel"`을 사용한다.

```html
<input type="tel">
```

입력 방법을 안내하고 싶다면 `placeholder`를 사용할 수 있다.

```html
<input
  type="tel"
  placeholder="하이픈을 빼고 입력해 주세요.">
```

#### 15. textarea 태그

여러 줄의 내용을 입력받을 때 `<textarea>`를 사용한다.

```html
<textarea rows="5" cols="30"></textarea>
```

* `rows` : 입력 영역의 세로 크기
* `cols` : 입력 영역의 가로 크기

`placeholder`를 사용하여 입력 안내 문구를 표시할 수도 있다.

```html
<textarea
  rows="5"
  cols="60"
  placeholder="내용을 입력해 주세요.">
</textarea>
```

#### 16. 날짜와 시간 입력

HTML의 `input` 타입을 이용하면 날짜와 시간을 선택할 수 있는 입력 요소를 만들 수 있다.

```html
<input type="date">
<input type="time">
<input type="week">
<input type="datetime-local">
```

각 타입의 역할은 다음과 같다.

* `date` : 날짜 선택
* `time` : 시간 선택
* `week` : 연도와 주 선택
* `datetime-local` : 날짜와 시간 선택

날짜의 선택 범위는 `min`과 `max` 속성으로 제한할 수 있다.

```html
<input
  type="date"
  min="2026-01-01"
  max="2026-12-31">
```

#### 17. 파일 첨부

사용자가 파일을 선택하여 첨부하도록 만들 때 `type="file"`을 사용한다.

```html
<input type="file">
```

사진이나 문서 등을 첨부받아야 하는 폼에서 사용할 수 있다.

#### 18. 입력 필드 주요 속성

폼의 입력 요소에는 사용자의 입력을 제어하기 위한 여러 속성을 사용할 수 있다.

```html
<input
  type="text"
  autofocus
  required>
```

주요 속성은 다음과 같다.

* `autofocus` : 페이지가 열렸을 때 해당 입력 요소에 자동으로 커서를 위치
* `required` : 반드시 입력해야 하는 필수 항목으로 지정
* `placeholder` : 입력할 내용에 대한 안내 문구 표시
* `min` : 입력 가능한 최소값 지정
* `max` : 입력 가능한 최대값 지정
* `size` : 입력 영역의 표시 크기 지정

#### 19. 전송과 초기화 버튼

폼에 입력한 정보를 전송할 때 `type="submit"`을 사용한다.

```html
<input type="submit" value="접수하기">
```

입력한 내용을 초기화할 때는 `type="reset"`을 사용한다.

```html
<input type="reset" value="다시쓰기">
```

두 버튼을 함께 사용할 수도 있다.

```html
<div>
  <input type="submit" value="주문하기">
  <input type="reset" value="취소하기">
</div>
```

#### 20. 일반 버튼

`type="button"`을 사용하면 기본 동작이 없는 일반 버튼을 만들 수 있다.

```html
<input type="button" value="공지 창 열기">
```

JavaScript와 함께 사용하면 버튼을 클릭했을 때 특정 기능을 실행할 수 있다.

```html
<input
  type="button"
  value="공지 창 열기"
  onclick="window.open('notice.html')">
```

#### 21. 이미지 버튼

이미지를 버튼처럼 사용하려면 `type="image"`를 사용할 수 있다.

```html
<input
  type="image"
  src="images/login.png"
  alt="로그인">
```

일반적인 텍스트 버튼 대신 이미지를 이용하여 폼을 전송할 수 있다.

#### 22. HTML 폼 기본 구조

폼에서는 입력 항목의 종류와 목적에 따라 여러 입력 요소를 조합하여 사용할 수 있다.

```html
<form>
  <fieldset>
    <legend>개인 정보</legend>

    <label for="user-name">이름</label>
    <input
      type="text"
      id="user-name"
      required>

    <label for="phone">연락처</label>
    <input
      type="tel"
      id="phone"
      placeholder="01012345678">

    <label for="memo">메모</label>
    <textarea
      id="memo"
      rows="5"
      cols="30">
    </textarea>
  </fieldset>

  <input type="submit" value="제출하기">
  <input type="reset" value="취소하기">
</form>
```

#### 핵심 정리

* `<a>` 태그를 사용하여 다른 HTML 페이지나 외부 사이트로 이동할 수 있다.
* `target="_blank"`를 사용하면 링크를 새로운 탭에서 열 수 있다.
* `href="#아이디"`와 `id`를 이용하면 현재 페이지의 특정 위치로 이동할 수 있다.
* `<a>` 태그 안에 `<img>`를 넣으면 이미지를 링크로 사용할 수 있다.
* `<audio>`와 `<video>`를 사용하여 웹 페이지에 오디오와 동영상을 삽입할 수 있다.
* `<form>`은 사용자로부터 정보를 입력받기 위한 영역을 만든다.
* `<fieldset>`과 `<legend>`를 이용하면 관련된 입력 항목을 하나의 그룹으로 구분할 수 있다.
* `<label>`은 입력 요소의 설명을 표시하며 `for`와 `id`를 이용하여 입력 요소와 연결할 수 있다.
* `checkbox`는 여러 항목을 선택할 수 있고 `radio`는 같은 그룹에서 하나의 항목만 선택할 수 있다.
* `text`, `password`, `number`, `range`, `email`, `tel` 등 다양한 `input` 타입을 목적에 맞게 사용할 수 있다.
* `date`, `time`, `week`, `datetime-local`을 이용하여 날짜와 시간을 입력받을 수 있다.
* `<textarea>`는 여러 줄의 텍스트를 입력받을 때 사용한다.
* `file` 타입을 이용하면 사용자가 파일을 첨부할 수 있다.
* `required`, `autofocus`, `placeholder`, `min`, `max` 등의 속성을 이용하여 입력 조건을 설정할 수 있다.
* `submit`은 폼을 전송하고 `reset`은 입력 내용을 초기화한다.
* `button`은 JavaScript 등의 기능을 실행하는 일반 버튼으로 사용할 수 있다.
* `image` 타입을 사용하면 이미지를 폼 전송 버튼으로 사용할 수 있다.
