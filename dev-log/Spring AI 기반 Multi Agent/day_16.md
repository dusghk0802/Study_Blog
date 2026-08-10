# 16일차

## HTML 기본 태그, 목록, 표, 이미지

📌 학습일 : 2026.08.10

📌 학습 내용 : HTML 문서 구조, 제목, 문단, 텍스트 강조, 목록, 설명 목록, 표, 셀 병합, 이미지, figure

---

#### 1. HTML 기본 문서 구조

HTML 문서는 `<!DOCTYPE html>` 선언으로 시작하며, `<html>`, `<head>`, `<body>` 태그를 기본 구조로 사용한다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>페이지 제목</title>
</head>
<body>
  <h1>페이지 제목</h1>
  <p>본문 내용입니다.</p>
</body>
</html>
```

* `<!DOCTYPE html>` : HTML5 문서임을 선언
* `<html>` : HTML 문서 전체 영역
* `<head>` : 문서 정보 설정
* `<meta charset="UTF-8">` : 문자 인코딩 설정
* `<meta name="viewport">` : 모바일 화면 크기에 맞도록 설정
* `<title>` : 브라우저 탭에 표시되는 제목
* `<body>` : 실제 웹페이지에 표시되는 내용

#### 2. 제목 태그

HTML에서는 `<h1>`부터 `<h6>`까지 제목 태그를 사용할 수 있다.

숫자가 작을수록 중요도가 높고 기본 글자 크기도 크다.

```html
<h1>대제목</h1>
<h2>중제목</h2>
<h3>소제목</h3>
<h4>하위 제목</h4>
<h5>하위 제목</h5>
<h6>가장 작은 제목</h6>
```

`<h1>`은 일반적으로 페이지의 대표 제목에 사용하며, 이후 내용 구조에 따라 `<h2>`, `<h3>` 순서로 사용하는 것이 좋다.

#### 3. 문단과 줄바꿈

문단은 `<p>` 태그를 사용하고, 문장 중간에서 줄을 바꾸고 싶을 때는 `<br>` 태그를 사용한다.

```html
<p>첫 번째 문단입니다.</p>

<p>
  첫 번째 문장입니다.<br>
  두 번째 문장입니다.
</p>
```

* `<p>` : 하나의 문단 생성
* `<br>` : 줄바꿈

#### 4. 텍스트 강조 태그

특정 텍스트를 강조하거나 기울여 표현할 수 있다.

```html
<p>
  일반 텍스트와 <b>굵은 텍스트</b>입니다.
</p>

<p>
  일반 내용 중 <strong>중요한 내용</strong>을 강조합니다.
</p>

<p>
  <em>강조가 필요한 문장</em>을 표현합니다.
</p>

<p>
  <i>기울임 텍스트</i>를 표현합니다.
</p>
```

* `<b>` : 글자를 굵게 표시
* `<strong>` : 중요한 내용임을 의미하면서 굵게 표시
* `<i>` : 글자를 기울여 표시
* `<em>` : 강조 의미를 포함하면서 기울여 표시

화면상으로는 `<b>`와 `<strong>`, `<i>`와 `<em>`이 비슷하게 보일 수 있지만 의미상 차이가 있다.

#### 5. 인용문과 구분선

긴 인용문은 `<blockquote>` 태그를 사용하며, 내용의 영역을 시각적으로 구분할 때는 `<hr>` 태그를 사용할 수 있다.

```html
<blockquote>
  다른 문서나 자료에서 인용한 내용입니다.<br>
  중요한 내용을 함께 표시할 수 있습니다.
</blockquote>

<hr>
```

* `<blockquote>` : 블록 형태의 인용문
* `<hr>` : 주제가 바뀌거나 내용 영역을 구분하는 수평선

#### 6. 순서 있는 목록

순서가 중요한 목록은 `<ol>` 태그를 사용하고 각 항목은 `<li>` 태그로 작성한다.

```html
<ol>
  <li>첫 번째 단계</li>
  <li>두 번째 단계</li>
  <li>세 번째 단계</li>
</ol>
```

기본값은 숫자로 표시된다.

```text
1. 첫 번째 단계
2. 두 번째 단계
3. 세 번째 단계
```

#### 7. 목록 표시 방식 변경

`<ol>`의 `type` 속성을 사용하면 번호 표현 방식을 변경할 수 있다.

```html
<ol type="A">
  <li>첫 번째 항목</li>
  <li>두 번째 항목</li>
  <li>세 번째 항목</li>
</ol>
```

```html
<ol type="I">
  <li>첫 번째 항목</li>
  <li>두 번째 항목</li>
  <li>세 번째 항목</li>
</ol>
```

주요 `type` 값은 다음과 같다.

* `1` : 숫자
* `A` : 대문자 알파벳
* `a` : 소문자 알파벳
* `I` : 대문자 로마 숫자
* `i` : 소문자 로마 숫자

#### 8. 순서 없는 목록

순서가 중요하지 않은 목록은 `<ul>` 태그를 사용한다.

```html
<ul>
  <li>항목 하나</li>
  <li>항목 둘</li>
  <li>항목 셋</li>
</ul>
```

`<ul>`은 항목 앞에 기본적으로 불릿 기호가 표시된다.

#### 9. 설명 목록

용어와 설명을 함께 표현할 때는 `<dl>`, `<dt>`, `<dd>` 태그를 사용한다.

```html
<dl>
  <dt>상품 A</dt>
  <dd>상품 A의 첫 번째 설명</dd>
  <dd>상품 A의 두 번째 설명</dd>
</dl>

<dl>
  <dt>상품 B</dt>
  <dd>상품 B의 설명</dd>
</dl>
```

* `<dl>` : 설명 목록 전체 영역
* `<dt>` : 설명할 용어 또는 제목
* `<dd>` : 해당 용어에 대한 설명

#### 10. 표 기본 구조

표는 `<table>` 태그를 사용하며 행은 `<tr>`, 제목 셀은 `<th>`, 일반 데이터 셀은 `<td>`로 작성한다.

```html
<table>
  <caption>상품 정보</caption>

  <tr>
    <th>구분</th>
    <th>수량</th>
    <th>가격</th>
  </tr>

  <tr>
    <td>상품 A</td>
    <td>1개</td>
    <td>10,000원</td>
  </tr>

  <tr>
    <td>상품 B</td>
    <td>2개</td>
    <td>20,000원</td>
  </tr>
</table>
```

* `<table>` : 표 생성
* `<caption>` : 표 제목
* `<tr>` : 하나의 행
* `<th>` : 제목 셀
* `<td>` : 일반 데이터 셀

#### 11. 표 영역 구분

표의 구조를 명확하게 구분하기 위해 `<thead>`와 `<tbody>`를 사용할 수 있다.

```html
<table>
  <caption>상품 정보</caption>

  <thead>
    <tr>
      <th>구분</th>
      <th>수량</th>
      <th>가격</th>
    </tr>
  </thead>

  <tbody>
    <tr>
      <td>상품 A</td>
      <td>1개</td>
      <td>10,000원</td>
    </tr>

    <tr>
      <td>상품 B</td>
      <td>2개</td>
      <td>20,000원</td>
    </tr>
  </tbody>
</table>
```

* `<thead>` : 표의 제목 영역
* `<tbody>` : 표의 실제 데이터 영역

표의 구조가 복잡해질수록 영역을 구분해 작성하는 것이 좋다.

#### 12. 표 행 병합

`rowspan` 속성을 사용하면 세로 방향으로 여러 셀을 하나로 합칠 수 있다.

```html
<table>
  <tr>
    <th>분류</th>
    <th>상품</th>
    <th>가격</th>
  </tr>

  <tr>
    <td rowspan="2">분류 A</td>
    <td>상품 A</td>
    <td>10,000원</td>
  </tr>

  <tr>
    <td>상품 B</td>
    <td>20,000원</td>
  </tr>
</table>
```

```html
<td rowspan="2">분류 A</td>
```

현재 셀부터 세로 방향으로 2개의 행을 병합한다는 의미이다.

병합된 다음 행에서는 해당 위치의 `<td>`를 작성하지 않는다.

#### 13. 표 기본 스타일 적용

CSS를 사용하면 표의 테두리와 셀 내부 여백 등을 설정할 수 있다.

```html
<style>
  table, th, td {
    border: 1px solid #ccc;
    border-collapse: collapse;
  }

  th, td {
    padding: 10px 20px;
  }
</style>
```

* `border` : 테두리 설정
* `1px` : 테두리 두께
* `solid` : 실선
* `#ccc` : 테두리 색상
* `border-collapse: collapse` : 셀 사이의 겹치는 테두리를 하나로 표시
* `padding` : 셀 내부 여백

```css
padding: 10px 20px;
```

첫 번째 값은 위·아래 여백이고 두 번째 값은 왼쪽·오른쪽 여백이다.

#### 14. 이미지 삽입

웹페이지에 이미지를 표시할 때는 `<img>` 태그를 사용한다.

```html
<img src="images/sample.jpg" alt="예시 이미지">
```

* `src` : 이미지 파일의 경로
* `alt` : 이미지를 표시하지 못했을 때 대신 표시되는 설명

이미지를 사용할 때는 파일 경로를 정확하게 지정해야 한다.

```html
<img src="images/sample.jpg" alt="예시 이미지">
```

현재 HTML 파일과 같은 위치에 `images` 폴더가 있을 때 사용할 수 있다.

#### 15. 이미지와 설명 함께 표시

이미지와 해당 이미지의 설명을 하나의 콘텐츠로 묶을 때는 `<figure>`와 `<figcaption>`을 사용할 수 있다.

```html
<figure>
  <img src="images/sample.jpg" alt="예시 이미지">
  <figcaption>이미지에 대한 설명입니다.</figcaption>
</figure>
```

* `<figure>` : 이미지나 그림 등의 독립적인 콘텐츠 영역
* `<figcaption>` : 해당 콘텐츠에 대한 설명

#### 16. 여러 이미지 구성

같은 형식으로 여러 개의 이미지를 표시할 때는 각각 `<figure>`로 구성할 수 있다.

```html
<figure>
  <img src="images/image1.jpg" alt="첫 번째 이미지">
  <figcaption>첫 번째 이미지 설명</figcaption>
</figure>

<figure>
  <img src="images/image2.jpg" alt="두 번째 이미지">
  <figcaption>두 번째 이미지 설명</figcaption>
</figure>

<figure>
  <img src="images/image3.jpg" alt="세 번째 이미지">
  <figcaption>세 번째 이미지 설명</figcaption>
</figure>
```

이처럼 이미지마다 `<figure>` 영역을 분리하면 이미지와 설명의 관계를 명확하게 표현할 수 있다.

---

#### 핵심 정리

* HTML 문서는 `<!DOCTYPE html>`, `<html>`, `<head>`, `<body>`를 기본 구조로 사용한다.
* `<h1>`~`<h6>`은 제목의 중요도와 단계에 따라 사용한다.
* `<p>`는 문단, `<br>`은 줄바꿈에 사용한다.
* `<strong>`, `<em>`은 단순한 디자인뿐 아니라 의미적인 강조를 표현할 수 있다.
* `<blockquote>`는 긴 인용문, `<hr>`은 내용 영역 구분에 사용한다.
* `<ol>`은 순서가 있는 목록, `<ul>`은 순서가 없는 목록이다.
* `<li>`는 목록의 각 항목을 표현한다.
* `<ol type="">`을 이용하면 숫자, 알파벳, 로마 숫자 등 목록 형식을 변경할 수 있다.
* `<dl>`, `<dt>`, `<dd>`는 용어와 설명으로 구성된 목록을 만들 때 사용한다.
* `<table>`은 표 전체, `<tr>`은 행, `<th>`와 `<td>`는 각각 제목 셀과 데이터 셀을 의미한다.
* `<thead>`와 `<tbody>`를 사용하면 표 구조를 명확하게 구분할 수 있다.
* `rowspan`은 세로 방향의 셀을 병합할 때 사용한다.
* `border-collapse: collapse`를 사용하면 표의 겹치는 테두리를 하나로 표시할 수 있다.
* `<img>`의 `src`는 이미지 경로, `alt`는 이미지에 대한 대체 설명을 지정한다.
* `<figure>`와 `<figcaption>`을 사용하면 이미지와 설명을 하나의 의미 있는 콘텐츠로 구성할 수 있다.

---

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>수습 국원 모집</title>
</head>
<body>
  <h1>수습 국원 모집</h1>
  <h2>방송에 관심 있는 새내기 여러분 환영합니다</h2>

  <p>
    교내 방송국에서 신입생을 대상으로 수습 국원을 모집하고 있습니다.
    학부나 전공에 상관없습니다.<br>
    평소 방송에 관심있었던 여러 학우들의 지원바랍니다.
  </p>

  <ul>
    <li><b>모집 기간 :</b> 3월 2일 ~ 3월 11일</li>
    <li><b>모집 분야 :</b> 아나운서, PD, 엔지니어</li>
    <li><b>지원 방법 :</b> 양식 작성 후 이메일 접수</li>
  </ul>

  <em>지원서 양식은 교내 방송국 홈페이지 공지 게시판에 있습니다.</em>

  <h2>혜택</h2>

  <ol type="a">
    <li>수습기자 활동 중 소정의 활동비 지급</li>
    <li>정기자로 진급하면 장학금 지급</li>
  </ol>

  <figure>
    <img src="../../training/Web/2026.08.10/images/mic.jpg" alt="카메라 이미지">
  </figure>
</body>
</html>
```
