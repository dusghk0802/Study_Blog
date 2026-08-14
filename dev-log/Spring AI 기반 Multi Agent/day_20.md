# 20일차

## HTML 링크, 멀티미디어 및 폼 입력 요소

📌 학습일 : 2026.08.14

📌 학습 내용 : 링크, 앵커, 이미지 링크, 오디오, 비디오, form, fieldset, legend, label, input, textarea, checkbox, radio, 날짜·시간 입력, 파일 첨부, 입력 속성

---

#### 1. 링크 연결

`<a>` 태그는 다른 페이지나 외부 사이트로 이동할 수 있는 링크를 만들 때 사용한다.

```html
<a href="페이지주소">링크 내용</a>
```

`href` 속성에는 이동할 페이지의 주소를 지정한다.

```html
<a href="page.html">페이지 이동</a>
<a href="https://www.example.com">외부 사이트 이동</a>
```

`target="_blank"`를 사용하면 링크를 새로운 탭에서 열 수 있다.

```html
<a href="page.html" target="_blank">새 탭에서 페이지 열기</a>
```

#### 2. 이미지를 이용한 링크

`<a>` 태그 안에 `<img>` 태그를 넣으면 이미지를 클릭했을 때 다른 페이지로 이동하도록 만들 수 있다.

```html
<a href="page.html">
  <img src="images/image.jpg" alt="이미지">
</a>
```

이미지 자체가 링크 역할을 하기 때문에 이미지나 배너 등을 클릭하여 다른 페이지로 이동할 때 사용할 수 있다.

#### 3. 앵커를 이용한 페이지 내부 이동

페이지의 특정 위치로 이동하려면 이동할 요소에 `id`를 지정하고 `<a>` 태그의 `href`에 `#아이디`를 작성한다.

```html
<ul>
  <li><a href="#section1">첫 번째 영역</a></li>
  <li><a href="#section2">두 번째 영역</a></li>
  <li><a href="#section3">세 번째 영역</a></li>
</ul>

<h2 id="section1">첫 번째 영역</h2>
<h2 id="section2">두 번째 영역</h2>
<h2 id="section3">세 번째 영역</h2>
```

페이지의 내용이 많을 때 원하는 위치로 빠르게 이동할 수 있다.

#### 4. 오디오 삽입

웹 페이지에 오디오 파일을 삽입할 때 `<audio>` 태그를 사용한다.

```html
<audio src="medias/audio.mp3" controls></audio>
```

주요 속성은 다음과 같다.

* `controls` : 재생, 일시정지, 볼륨 등의 컨트롤 표시
* `autoplay` : 페이지가 열리면 자동 재생
* `loop` : 반복 재생
* `muted` : 음소거 상태로 재생

```html
<audio src="medias/audio.mp3" autoplay loop></audio>
```

#### 5. 비디오 삽입

웹 페이지에 동영상을 삽입할 때 `<video>` 태그를 사용한다.

```html
<video src="medias/video.mp4" controls width="700"></video>
```

`poster` 속성을 사용하면 동영상이 재생되기 전에 표시할 이미지를 지정할 수 있다.

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

`<embed>` 태그는 외부 콘텐츠를 웹 페이지에 삽입할 때 사용한다.

```html
<embed src="medias/audio.mp3">
```

오디오, 비디오 등의 외부 파일을 웹 페이지에 포함할 수 있다.

#### 7. form 태그

사용자로부터 정보를 입력받기 위한 영역을 만들 때 `<form>` 태그를 사용한다.

```html
<form>
  입력 요소
</form>
```

폼 내부에는 `<input>`, `<textarea>`, `<button>` 등의 입력 요소를 사용할 수 있다.

#### 8. fieldset과 legend

`<fieldset>`은 폼에서 관련된 입력 요소를 하나의 그룹으로 묶을 때 사용한다.

`<legend>`는 그룹의 제목을 표시한다.

```html
<form>
  <fieldset>
    <legend>사용자 정보</legend>

    <label for="user-name">이름</label>
    <input type="text" id="user-name">
  </fieldset>
</form>
```

입력 항목이 많을 경우 사용자 정보, 선택 정보, 추가 정보 등으로 영역을 구분할 수 있다.

#### 9. label 태그

`<label>`은 입력 요소가 어떤 정보를 입력하는 곳인지 표시할 때 사용한다.

```html
<label for="user-name">이름</label>
<input type="text" id="user-name">
```

`label`의 `for` 값과 `input`의 `id` 값을 동일하게 지정하면 두 요소가 연결된다.

```html
<label for="user-phone">연락처</label>
<input type="tel" id="user-phone">
```

#### 10. 텍스트 입력

일반적인 한 줄의 텍스트를 입력받을 때 `type="text"`를 사용한다.

```html
<input type="text">
```

입력 칸의 크기는 `size` 속성을 이용하여 지정할 수 있다.

```html
<input type="text" size="20">
```

#### 11. 비밀번호 입력

입력한 내용을 화면에서 숨겨야 할 경우 `type="password"`를 사용한다.

```html
<label for="user-password">비밀번호</label>
<input type="password" id="user-password">
```

사용자가 입력한 문자가 화면에서 그대로 표시되지 않는다.

#### 12. 체크박스

여러 항목 중 하나 이상을 선택할 수 있도록 만들 때 `checkbox`를 사용한다.

```html
<label>
  <input type="checkbox" value="option1">
  선택 항목 1
</label>

<label>
  <input type="checkbox" value="option2">
  선택 항목 2
</label>
```

체크박스는 여러 항목을 동시에 선택할 수 있다.

#### 13. 라디오 버튼

여러 항목 중 하나만 선택하도록 만들 때 `radio`를 사용한다.

```html
<label>
  <input type="radio" name="option" value="yes">
  선택 1
</label>

<label>
  <input type="radio" name="option" value="no">
  선택 2
</label>
```

같은 `name` 값을 가진 라디오 버튼은 하나의 그룹으로 묶이며 해당 그룹에서는 하나의 항목만 선택할 수 있다.

#### 14. 숫자 입력

숫자를 입력할 때 `type="number"`를 사용한다.

```html
<input type="number" min="0" max="10">
```

* `min` : 입력 가능한 최소값
* `max` : 입력 가능한 최대값

숫자의 입력 범위를 제한해야 할 때 사용할 수 있다.

#### 15. 범위 입력

일정한 범위 안에서 값을 선택하도록 만들 때 `type="range"`를 사용한다.

```html
<input type="range" min="0" max="100">
```

슬라이더 형태로 값을 선택할 수 있다.

#### 16. 이메일 입력

이메일 주소를 입력받을 때 `type="email"`을 사용한다.

```html
<label for="user-email">이메일</label>
<input type="email" id="user-email">
```

이메일 형식의 데이터를 입력받는 폼에서 사용할 수 있다.

#### 17. 전화번호 입력

전화번호를 입력받을 때 `type="tel"`을 사용한다.

```html
<label for="user-phone">연락처</label>
<input type="tel" id="user-phone">
```

`placeholder`를 사용하면 입력 방법을 안내할 수 있다.

```html
<input
  type="tel"
  id="user-phone"
  placeholder="숫자만 입력하세요">
```

#### 18. 여러 줄의 텍스트 입력

여러 줄의 내용을 입력받을 때 `<textarea>`를 사용한다.

```html
<textarea rows="5" cols="30"></textarea>
```

* `rows` : 입력 영역의 세로 크기
* `cols` : 입력 영역의 가로 크기

`placeholder`를 사용하여 입력할 내용에 대한 안내 문구를 표시할 수 있다.

```html
<textarea
  rows="5"
  cols="50"
  placeholder="내용을 입력하세요">
</textarea>
```

#### 19. 날짜 입력

날짜를 선택할 수 있는 입력 요소를 만들 때 `type="date"`를 사용한다.

```html
<input type="date">
```

`min`과 `max`를 사용하여 선택할 수 있는 날짜 범위를 제한할 수 있다.

```html
<input
  type="date"
  min="2026-01-01"
  max="2026-12-31">
```

#### 20. 시간 입력

시간을 선택할 때 `type="time"`을 사용한다.

```html
<input type="time">
```

날짜와 시간을 함께 선택할 때는 `datetime-local`을 사용할 수 있다.

```html
<input type="datetime-local">
```

특정 주를 선택할 때는 `week`를 사용한다.

```html
<input type="week">
```

주요 날짜 및 시간 입력 타입은 다음과 같다.

* `date` : 날짜 선택
* `time` : 시간 선택
* `week` : 연도와 주 선택
* `datetime-local` : 날짜와 시간 선택

#### 21. 파일 첨부

사용자가 파일을 선택하여 첨부하도록 만들 때 `type="file"`을 사용한다.

```html
<label for="user-file">파일 첨부</label>
<input type="file" id="user-file">
```

이미지, 문서 등의 파일을 선택할 수 있는 입력 요소를 생성한다.

#### 22. 입력 필드 주요 속성

입력 요소에는 사용자의 입력을 제어하기 위한 다양한 속성을 사용할 수 있다.

```html
<input
  type="text"
  placeholder="내용을 입력하세요"
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

#### 23. 전송 버튼

폼에 입력한 정보를 전송할 때 `type="submit"`을 사용한다.

```html
<input type="submit" value="제출">
```

`submit` 버튼을 클릭하면 `<form>`에 입력된 데이터를 전송한다.

#### 24. 초기화 버튼

폼에 입력한 내용을 초기 상태로 되돌릴 때 `type="reset"`을 사용한다.

```html
<input type="reset" value="초기화">
```

전송 버튼과 초기화 버튼을 함께 사용할 수 있다.

```html
<div>
  <input type="submit" value="제출">
  <input type="reset" value="초기화">
</div>
```

#### 25. 일반 버튼

기본적인 동작이 없는 버튼을 만들 때 `type="button"`을 사용한다.

```html
<input type="button" value="버튼">
```

JavaScript와 함께 사용하면 버튼을 클릭했을 때 특정 기능을 실행하도록 만들 수 있다.

```html
<input
  type="button"
  value="새 창 열기"
  onclick="window.open('page.html')">
```

`window.open()`은 지정한 페이지를 새로운 창이나 탭으로 열 때 사용할 수 있다.

#### 26. 이미지 버튼

이미지를 버튼으로 사용하려면 `type="image"`를 사용한다.

```html
<input
  type="image"
  src="images/button.png"
  alt="전송">
```

이미지를 클릭하면 `submit` 버튼과 같이 폼을 전송하는 기능을 수행한다.

#### 27. HTML 폼 기본 구조

폼에서는 입력받을 정보의 종류에 따라 여러 입력 요소를 조합하여 사용할 수 있다.

```html
<form>
  <fieldset>
    <legend>사용자 정보</legend>

    <label for="user-name">이름</label>
    <input
      type="text"
      id="user-name"
      placeholder="이름을 입력하세요"
      required>

    <label for="user-password">비밀번호</label>
    <input
      type="password"
      id="user-password"
      required>

    <label for="user-email">이메일</label>
    <input
      type="email"
      id="user-email">

    <label for="user-phone">연락처</label>
    <input
      type="tel"
      id="user-phone"
      placeholder="숫자만 입력하세요">

    <label for="user-memo">내용</label>
    <textarea
      id="user-memo"
      rows="5"
      cols="30"
      placeholder="내용을 입력하세요">
    </textarea>
  </fieldset>

  <input type="submit" value="제출">
  <input type="reset" value="초기화">
</form>
```

#### 핵심 정리

* `<a>` 태그를 사용하여 다른 페이지나 외부 사이트로 이동할 수 있다.
* `target="_blank"`를 사용하면 링크를 새로운 탭에서 열 수 있다.
* `href="#아이디"`와 `id`를 이용하면 현재 페이지의 특정 위치로 이동할 수 있다.
* `<a>` 태그 안에 `<img>`를 넣으면 이미지를 링크로 사용할 수 있다.
* `<audio>`와 `<video>`를 사용하여 오디오와 동영상을 웹 페이지에 삽입할 수 있다.
* `<embed>`를 사용하여 외부 콘텐츠를 웹 페이지에 삽입할 수 있다.
* `<form>`은 사용자로부터 정보를 입력받기 위한 영역이다.
* `<fieldset>`과 `<legend>`를 이용하면 관련된 입력 요소를 하나의 그룹으로 구분할 수 있다.
* `<label>`의 `for`와 입력 요소의 `id`를 연결하여 입력 항목을 명확하게 지정할 수 있다.
* `text`는 일반 텍스트, `password`는 비밀번호 입력에 사용한다.
* `checkbox`는 여러 항목을 선택할 수 있고 `radio`는 같은 그룹에서 하나의 항목만 선택할 수 있다.
* `number`는 숫자 입력, `range`는 일정 범위의 값을 선택할 때 사용한다.
* `email`은 이메일 주소, `tel`은 전화번호를 입력받을 때 사용한다.
* `<textarea>`는 여러 줄의 내용을 입력받을 때 사용한다.
* `date`, `time`, `week`, `datetime-local`을 이용하여 날짜와 시간을 입력받을 수 있다.
* `file` 타입을 사용하면 사용자가 파일을 첨부할 수 있다.
* `required`, `autofocus`, `placeholder`, `min`, `max`, `size` 등의 속성을 이용하여 입력 조건을 설정할 수 있다.
* `submit`은 폼 데이터를 전송하고 `reset`은 입력 내용을 초기화한다.
* `button`은 JavaScript 등과 연결하여 특정 기능을 실행할 때 사용할 수 있다.
* `image` 타입을 사용하면 이미지를 폼 전송 버튼으로 사용할 수 있다.

---

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>좋아하는 음식</title>
</head>
<body>
  <ol>
    <h1>좋아하는 음식</h1>
    <li>샐러드 채소를 씻고 물기를 제거한 후 준비 합니다.</li>
    <li>샐러드 채소를 씻고 물기를 제거한 후 준비 합니다.</li>
    <li>샐러드 채소를 씻고 물기를 제거한 후 준비 합니다.</li>
  </ol>
<img src="images/salad.jpg" alt="샐러드 이미지">
<!-- <object width="900" height="800" data="product.pdf" type=""></object> -->
<!-- cttl + ? : 주석처리 -->
 <hr>
 <a href="order.html" target="_blank">주문서 하기</a><br>
 <a href="https://www.naver.com" target="_blank">네이버 사이트 방문</a><br>

 <a href="oreder.html"><img src="images/tangerines.jpg" alt="레드향"></a>

 <br><br>
 <embed src="medias/spring.mp3" type="">
<audio src="medias/spring.mp3" autoplay loop></audio>
<video src="medias/salad.mp4" controls width="700" poster="images/salad.jpg" muted loop></video>
<br><br>


</body>
</html>
```
