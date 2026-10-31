# 79일차

## Spring Boot: 람다식, 함수형 인터페이스, 메서드 참조, Stream API 복습 퀴즈

📌 학습일 : 2026.10.31

📌 학습 내용 : 람다식(Lambda Expression), 함수형 인터페이스, @FunctionalInterface, 입력·리턴에 따른 람다식 형태, 익명 클래스, 메서드 참조(::), 명령형 프로그래밍, 선언형 프로그래밍, Stream API, 중간 연산 / 최종 연산, filter, map, distinct, sorted, limit, forEach, count, mapToInt, sum, average, OptionalDouble, orElse, 스트림의 일회성, 원본 불변, 단일 파일 실행(java 파일명.java)

---

#### 1. 복습 퀴즈 구성


| 문제 | 내용 | 개념 |
| --- | --- | --- |
| Q1 | 입력·리턴 유무가 다른 함수형 인터페이스 4개를 람다식으로 구현 | 함수형 인터페이스, 람다식 생략 규칙 |
| Q2 | 익명 클래스로 작성한 코드를 람다식으로 바꾸기 | 익명 클래스 → 람다식 |
| Q3 | 람다식을 메서드 참조로 바꾸기 | `::` 메서드 참조 |
| Q4 | 1 ~ 10 중 홀수의 합을 두 가지 방법으로 구하기 | 명령형 vs 선언형 |
| Q5 | 과일 목록을 중복 제거 → 필터 → 변환 → 정렬 → 3개만 출력 | 중간 연산 체이닝, 원본 불변 |
| Q6 | 80점 이상인 점수의 개수와 평균 구하기 | `count`, `average`, 스트림의 일회성 |

#### 2. Q1. 함수형 인터페이스 4가지 형태

```java
@FunctionalInterface
interface QuizA { void print(); }              // 입력 X, 리턴 X

@FunctionalInterface
interface QuizB { void square(int x); }        // 입력 O, 리턴 X

@FunctionalInterface
interface QuizC { int get(); }                 // 입력 X, 리턴 O

@FunctionalInterface
interface QuizD { int min(int a, int b); }     // 입력 O, 리턴 O
```

```java
QuizA a = () -> System.out.println("Hello Lamda");
a.print();                                      // Hello Lamda

QuizB b = x -> System.out.println("제곱: " + x * x);
b.square(7);                                    // 제곱: 49

QuizC c = () -> 100;
System.out.println("get(): " + c.get());        // get(): 100

QuizD d = (x, y) -> x < y ? x : y;
System.out.println("min(3, 8): " + d.min(3, 8)); // min(3, 8): 3
```

| 형태 | 람다식 | 생략 규칙 |
| --- | --- | --- |
| 입력 X / 리턴 X | `() -> System.out.println(...)` | 매개변수가 없으면 `()`는 생략 불가 |
| 입력 O / 리턴 X | `x -> System.out.println(...)` | 매개변수가 1개면 `()`와 타입 생략 가능 |
| 입력 X / 리턴 O | `() -> 100` | 실행문이 return 하나면 `{}`와 `return` 생략 |
| 입력 O / 리턴 O | `(x, y) -> x < y ? x : y` | 매개변수가 2개 이상이면 `()` 생략 불가 |

람다식에는 인터페이스 이름도 메서드 이름도 없지만, 대입되는 변수의 타입(`QuizA` ~ `QuizD`)을 보고 컴파일러가 어떤 추상 메서드를 구현하는지 알아낸다. 그래서 추상 메서드가 1개뿐인 함수형 인터페이스에만 람다식을 쓸 수 있다.

#### 3. Q2. 익명 클래스 → 람다식

```java
// 익명 클래스
QuizD anonymous = new QuizD() {
    @Override
    public int min(int x, int y) {
        return x < y ? x : y;
    }
};

// 람다식 (new 인터페이스, 메서드 이름, 타입, return 모두 생략)
QuizD lamda = (x, y) -> x < y ? x : y;

System.out.println("익명 클래스: " + anonymous.min(10, 4));   // 4
System.out.println("람다식     : " + lamda.min(10, 4));       // 4
```

| 익명 클래스에서 지운 부분 | 지울 수 있는 이유 |
| --- | --- |
| `new QuizD() { ... }` | 대입하는 변수 타입이 `QuizD`라 알 수 있음 |
| `@Override public int min` | 추상 메서드가 하나뿐이라 어떤 메서드인지 정해져 있음 |
| `int x, int y` | 추상 메서드 선언에서 타입을 알 수 있음 |
| `{ return ...; }` | 실행문이 return 하나뿐임 |

두 방식의 결과는 같고, 람다식은 익명 클래스에서 꼭 필요한 **매개변수와 실행 내용만 남긴 것**이라는 점을 다시 확인했다.

#### 4. Q3. 메서드 참조 (::)

```java
List<String> langs = Arrays.asList("java", "spring", "react");

langs.stream()
        .map(String::toUpperCase)        // s -> s.toUpperCase()
        .forEach(System.out::println);   // str -> System.out.println(str)
```

```
JAVA
SPRING
REACT
```

| 람다식 | 메서드 참조 | 종류 |
| --- | --- | --- |
| `s -> s.toUpperCase()` | `String::toUpperCase` | 특정 타입의 인스턴스 메서드 참조 |
| `str -> System.out.println(str)` | `System.out::println` | 객체의 인스턴스 메서드 참조 |
| `n -> n.intValue()` | `Integer::intValue` | 특정 타입의 인스턴스 메서드 참조 |

람다식이 받은 값을 **이미 있는 메서드 하나에 그대로 넘기기만 할 때** 메서드 참조로 바꿀 수 있다. `s -> s.toUpperCase() + "!"`처럼 다른 처리가 더 있으면 메서드 참조로 바꿀 수 없다.

#### 5. Q4. 명령형 vs 선언형

```java
List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// 명령형 : 어떻게(How)
int sum1 = 0;
for (int n : nums) {
    if (n % 2 == 1) {
        sum1 += n;
    }
}

// 선언형 : 무엇을(What)
int sum2 = nums.stream()
        .filter(n -> n % 2 == 1)
        .mapToInt(Integer::intValue)
        .sum();
```

```
명령형 홀수 합: 25
선언형 홀수 합: 25
```

| 구분 | 명령형 | 선언형 (Stream) |
| --- | --- | --- |
| 작성 내용 | 반복(for), 조건(if), 누적 변수를 직접 작성 | 홀수만(filter) → 숫자로(mapToInt) → 합(sum) |
| 상태 변경 | `sum1` 값을 계속 바꿈 | 직접 바꾸는 변수 없음 |

`Stream<Integer>`에는 `sum()`이 없어서 `mapToInt(Integer::intValue)`로 `IntStream`으로 바꾼 뒤에 `sum()`을 사용해야 한다.

#### 6. Q5. 중간 연산 n번 → 최종 연산 1번

```java
List<String> fruits = Arrays.asList("apple", "kiwi", "banana", "apple", "grape", "melon", "kiwi", "fig");

fruits.stream()                        // 생성하기
        .distinct()                    // 중복 제거
        .filter(f -> f.length() <= 5)  // 5글자 이하만
        .map(String::toUpperCase)      // 대문자로 변환
        .sorted()                      // 정렬
        .limit(3)                      // 앞에서 3개
        .forEach(System.out::println); // 최종 연산 (스트림 닫힘)

System.out.println("원본은 그대로: " + fruits);
```

| 단계 | 연산 | 결과 |
| --- | --- | --- |
| 생성 | `stream()` | apple, kiwi, banana, apple, grape, melon, kiwi, fig |
| 중간 | `distinct()` | apple, kiwi, banana, grape, melon, fig |
| 중간 | `filter(f -> f.length() <= 5)` | apple, kiwi, grape, melon, fig |
| 중간 | `map(String::toUpperCase)` | APPLE, KIWI, GRAPE, MELON, FIG |
| 중간 | `sorted()` | APPLE, FIG, GRAPE, KIWI, MELON |
| 중간 | `limit(3)` | APPLE, FIG, GRAPE |
| 최종 | `forEach(System.out::println)` | APPLE / FIG / GRAPE 출력 |

```
원본은 그대로: [apple, kiwi, banana, apple, grape, melon, kiwi, fig]
```

중간 연산은 결과가 다시 Stream이라 계속 이어 붙일 수 있고, 최종 연산을 해야 실제로 결과가 만들어진다. 마지막에 원본 `fruits`를 출력해 보니 중복도 그대로이고 소문자도 그대로여서 **스트림은 원본을 바꾸지 않고 읽기만 한다**는 것을 확인했다.

#### 7. Q6. 80점 이상 개수와 평균

```java
List<Integer> scores = Arrays.asList(95, 72, 88, 64, 80, 91, 55);

long count = scores.stream()
        .filter(s -> s >= 80)
        .count();                      // 4

double avg = scores.stream()           // 스트림은 일회용이라 다시 생성
        .filter(s -> s >= 80)
        .mapToInt(Integer::intValue)
        .average()                     // OptionalDouble
        .orElse(0);                    // 88.5
```

```
80점 이상 인원: 4명
80점 이상 평균: 88.5
```

| 최종 연산 | 반환 타입 | 설명 |
| --- | --- | --- |
| `count()` | `long` | 요소 개수 |
| `sum()` | `int` | 합계 (`IntStream`에서 사용) |
| `average()` | `OptionalDouble` | 평균, 요소가 없으면 값이 비어 있음 |
| `orElse(0)` | `double` | 값이 비어 있으면 0을 사용 |

`average()`는 조건에 맞는 점수가 하나도 없으면 평균을 낼 수 없어서 `double`이 아니라 `OptionalDouble`을 반환한다. 그래서 `orElse(0)`으로 값이 없을 때의 기본값을 정해 줘야 한다.

`count()`로 이미 최종 연산을 한 스트림은 닫혔기 때문에, 평균을 구할 때는 `scores.stream()`으로 **스트림을 새로 생성**했다.

#### 8. 단일 파일 실행과 클래스 순서

```
java Quiz.java
Error: can't find main(String[]) method in class: QuizA
```

`javac`로 컴파일하지 않고 `java 파일명.java`로 바로 실행하면, 파일에서 **가장 먼저 선언된 클래스**의 `main`을 실행한다. 처음에는 인터페이스 `QuizA` ~ `QuizD`를 파일 맨 위에 두어서 `QuizA`에서 `main`을 찾다가 에러가 났다.

```java
import java.util.Arrays;
import java.util.List;

public class Quiz {                    // main이 있는 클래스를 맨 위로
    public static void main(String[] args) { ... }
}

@FunctionalInterface
interface QuizA { void print(); }      // 인터페이스는 아래로
...
```

`main`이 있는 `Quiz` 클래스를 맨 위로 옮기고 인터페이스를 아래로 내리니 정상적으로 실행되었다. IDE에서 실행할 때는 `main`이 있는 클래스를 직접 골라 실행해서 순서가 상관없지만, 단일 파일 실행에서는 순서가 중요하다.

#### 핵심 정리

- 람다식은 함수형 인터페이스의 추상 메서드 하나를 구현한 것이며, 형태는 매개변수와 리턴 타입이 정한다.
- 매개변수가 1개면 `()`와 타입을, 실행문이 return 하나면 `{}`와 `return`을 생략할 수 있다.
- 익명 클래스에서 인터페이스 이름, 메서드 이름, 타입, return을 지우고 매개변수와 실행 내용만 남긴 것이 람다식이다.
- 받은 값을 이미 있는 메서드 하나에 그대로 넘기는 람다식은 `::` 메서드 참조로 바꿀 수 있다.
- 명령형은 for, if로 과정을 직접 쓰고, 선언형은 Stream으로 원하는 결과만 쓴다.
- `Stream<Integer>`에서 `sum()`, `average()`를 쓰려면 `mapToInt()`로 `IntStream`으로 바꿔야 한다.
- 중간 연산은 계속 이어 붙일 수 있고, 최종 연산은 한 번만 가능하며 그 뒤 스트림은 닫힌다.
- 스트림은 원본을 바꾸지 않고, 일회용이라 다시 쓰려면 새로 생성해야 한다.
- `average()`는 `OptionalDouble`을 반환하므로 `orElse()`로 값이 없을 때를 처리한다.
- `java 파일명.java`로 실행하면 맨 위에 선언된 클래스의 `main`을 실행한다.

---

```java
// Q1. 함수형 인터페이스 4가지를 람다식으로 구현
QuizA a = () -> System.out.println("Hello Lamda");
QuizB b = x -> System.out.println("제곱: " + x * x);
QuizC c = () -> 100;
QuizD d = (x, y) -> x < y ? x : y;
```

어제 배운 람다식을 혼자 다시 써 보려고 하니, 입력과 리턴이 있고 없고에 따라 괄호와 `return`을 어디까지 생략할 수 있는지가 헷갈렸다.
</br>인터페이스 4개를 직접 만들어 하나씩 람다식으로 바꿔 보면서, 람다식의 모양은 내가 정하는 것이 아니라 인터페이스의 추상 메서드가 정한다는 것을 확실히 이해할 수 있었다.

```java
// Q2. 익명 클래스 → 람다식
QuizD anonymous = new QuizD() {
    @Override
    public int min(int x, int y) { return x < y ? x : y; }
};
QuizD lamda = (x, y) -> x < y ? x : y;
```

익명 클래스와 람다식을 나란히 놓고 같은 결과가 나오는 것을 확인하니, 람다식이 새로운 문법이라기보다 익명 클래스에서 컴파일러가 알 수 있는 부분을 지운 것이라는 점이 와닿았다.

```java
// Q5. 중간 연산 n번 → 최종 연산 1번
fruits.stream()
        .distinct()
        .filter(f -> f.length() <= 5)
        .map(String::toUpperCase)
        .sorted()
        .limit(3)
        .forEach(System.out::println);   // APPLE, FIG, GRAPE
```

Q5는 결과를 바로 예상하기 어려워서 중간 연산을 한 단계씩 표로 따라가 보았는데, 그렇게 하니 `sorted()` 다음에 `limit(3)`이 와야 정렬된 앞 3개가 나온다는 것처럼 **연산 순서에 따라 결과가 달라진다**는 것을 알 수 있었다.
</br>또 마지막에 원본 리스트를 출력해 보니 그대로 남아 있어서, 스트림은 원본을 바꾸지 않는다는 특징을 눈으로 확인했다.

```java
// Q6. 스트림은 일회용이라 다시 생성
long count = scores.stream().filter(s -> s >= 80).count();
double avg = scores.stream().filter(s -> s >= 80)
        .mapToInt(Integer::intValue)
        .average()
        .orElse(0);
```

평균을 구할 때 `average()`가 `double`이 아니라 `OptionalDouble`을 돌려줘서 처음에는 왜 `orElse()`가 필요한지 몰랐는데, 조건에 맞는 점수가 하나도 없으면 평균을 낼 수 없기 때문이라는 것을 알게 되었다.
</br>스트림을 변수에 담아 두고 `count()`와 `average()`를 연달아 쓰면 이미 닫힌 스트림이라 에러가 나기 때문에, 필요할 때마다 `stream()`으로 새로 만들어야 한다는 것도 다시 정리할 수 있었다.

```
java Quiz.java
Error: can't find main(String[]) method in class: QuizA
```

</br>직접 문제를 풀어 보니 어제 수업 때 이해했다고 생각한 부분도 막상 혼자 쓰려면 막히는 곳이 있었고, 복습할 때는 눈으로 읽는 것보다 직접 코드를 작성해 보는 것이 훨씬 효과적이라는 것을 느꼈다.
</br>다음에는 `collect(Collectors.toList())`, `groupingBy()`처럼 어제 다루지 못한 최종 연산도 연습해야겠다.
