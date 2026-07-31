# 10일차

## Java 변수, 자료형, 형변환, 연산자

📌 학습일 : 2026.07.31

📌 학습 내용 : 변수 명명 규칙, 자료형(Data Type), 상수(Constant), 형변환(Type Casting), 연산자(Operation), 삼항 연산자

---

### 변수(Variable)

#### 1. 변수 명명 규칙

- 저장할 값을 쉽게 알 수 있는 이름을 사용한다.
- 영문자, 숫자, 밑줄(`_`)을 사용할 수 있다.
- 숫자로 시작할 수 없다.
- 공백은 사용할 수 없다.
- 예약어(`public`, `class`, `int` 등)는 사용할 수 없다.
- 여러 단어를 사용할 경우 **Camel Case**를 사용한다.
  - 예) `studentName`, `courseName`

#### 2. 변수 선언

```java
String studentName = "김자바";
int studentCount = 30;
```

데이터를 저장하기 위해 변수와 자료형을 함께 선언한다.

---

### 자료형(Data Type)

#### 정수형

```java
int age = 20;
long money = 1000000000000L;
```

- `int` : 일반적인 정수
- `long` : 큰 범위의 정수 (`L` 사용 권장)

### 실수형

```java
double score = 95.5;
float pi = 3.14F;
```

- `double` : 실수(기본형, 정밀도 높음)
- `float` : 실수(`F` 사용)

### 문자 및 문자열

```java
char grade = 'A';
String name = "김자바";
```

- `char` : 한 글자
- `String` : 문자열

### 논리형

```java
boolean pass = true;
```

- `true`, `false` 두 가지 값만 저장한다.



### 상수(Constant)

```java
final int MAX_NUM = 100;
```

- `final` 키워드를 사용한다.
- 한 번 값을 저장하면 변경할 수 없다.
- 일반적으로 모두 대문자로 작성한다.



### 형변환(Type Casting)

#### 자동 형변환

작은 자료형에서 큰 자료형으로 변환된다.

```java
int num = 100;
double result = num;
```

```
int → long → float → double
```

#### 명시적 형변환

큰 자료형에서 작은 자료형으로 변환할 때는 직접 형변환해야 한다.

```java
double score = 76.5;
int result = (int) score;
```

실행 결과

```text
76
```

소수점 이하가 제거된다.

#### 문자열 ↔ 숫자 변환

숫자를 문자열로 변환

```java
String.valueOf(85);
Integer.toString(85);
```

문자열을 숫자로 변환

```java
Integer.parseInt("85");
Double.parseDouble("76.5");
```

---

### 연산자(Operation)

#### 산술 연산자

```java
+
-
*
/
%
```

- `%` : 나머지 연산

#### 복합 대입 연산자

```java
+=
-=
*=
/=
%=
```

기존 변수의 값을 계산한 후 다시 저장한다.

#### 증감 연산자

```java
++
--
```

- `++num` : 먼저 증가 후 사용
- `num++` : 먼저 사용 후 증가
- `--num` : 먼저 감소 후 사용
- `num--` : 먼저 사용 후 감소

#### 관계 연산자

```java
>
<
>=
<=
==
!=
```

비교 결과는 `true` 또는 `false`를 반환한다.

#### 논리 연산자

```java
&&
||
!
```

- `&&` : AND (모두 참)
- `||` : OR (하나라도 참)
- `!` : NOT (반대)

#### 삼항 연산자

```java
조건 ? 참 : 거짓
```

예시

```java
int age = 17;

System.out.println(
    age >= 15
        ? "관람 가능합니다."
        : "관람 불가능합니다."
);
```

간단한 조건문을 한 줄로 표현할 때 사용한다.

---

#### 실습 내용

- 변수 명명 규칙 및 Camel Case 작성 방법
- 다양한 자료형(`int`, `long`, `double`, `float`, `char`, `String`, `boolean`) 사용
- `final`을 이용한 상수 선언
- 자동 형변환과 명시적 형변환 실습
- 문자열과 숫자 자료형 변환
- 산술, 관계, 논리, 복합 대입, 증감 연산자 실습
- 삼항 연산자를 이용한 조건 처리
- 학생 정보, 수업 정보, 택배 정보 출력 프로그램 작성
- 나이와 구매 금액에 따른 조건 출력 프로그램 작성



##### 핵심 정리

- 변수는 의미 있는 이름과 Camel Case를 사용한다.
- 자료형은 저장할 데이터의 종류에 맞게 선택한다.
- `final`은 값을 변경할 수 없는 상수를 선언할 때 사용한다.
- 작은 자료형에서 큰 자료형으로는 자동 형변환이 가능하며, 반대의 경우에는 명시적 형변환이 필요하다.
- 연산자는 데이터 계산과 조건 비교를 수행하며, 삼항 연산자는 간단한 조건문을 한 줄로 표현할 수 있다.
