# 12일차

## Java 메서드, 클래스, 생성자, 캡슐화

📌 학습일 : 2026.08.04

📌 학습 내용 : 메서드(Method), 클래스(Class), 객체(Object), 생성자(Constructor), 접근 제어자, Getter/Setter, 참조 자료형

---

#### 1. 메서드(Method)

메서드는 특정 기능을 수행하는 코드의 묶음으로, 필요할 때 여러 번 호출하여 사용할 수 있다.

```java
public static int addNum(int n1, int n2) {
    return n1 + n2;
}
```

실습 내용

- 덧셈 메서드
- 뺄셈 메서드
- 곱셈 메서드
- 나눗셈 메서드
- 메서드 호출 및 반환값 출력

#### 2. 클래스(Class)

클래스는 객체를 만들기 위한 설계도이며, 변수(속성)와 메서드(기능)를 포함한다.

```java
public class Student {

    int studentID;
    String studentName;
    String address;

}
```

실습 내용

- Student 클래스 작성
- Person 클래스 작성
- Coffee 클래스 작성

#### 3. 객체(Object)

객체는 클래스를 이용하여 생성한 실제 데이터이다.

```java
Student studentAhn = new Student();
```

객체마다 서로 다른 데이터를 저장할 수 있다.

실습 내용

- Student 객체 생성
- Coffee 객체 생성
- Person 객체 생성

#### 4. 메서드 호출

객체가 가지고 있는 기능은 메서드를 호출하여 사용할 수 있다.

```java
studentAhn.getStudentName();
coffee1.showCoffeeInfo();
```

실습 내용

- 학생 정보 출력
- 커피 정보 출력

#### 5. 생성자(Constructor)

생성자는 객체가 생성될 때 자동으로 호출되는 특별한 메서드이다.

```java
public Person() {

}

public Person(String name, float height, float weight) {

}
```

실습 내용

- 기본 생성자(Default Constructor)
- 매개변수가 있는 생성자
- 생성자를 이용한 객체 초기화

#### 6. 접근 제어자(Access Modifier)

데이터를 보호하기 위해 접근 범위를 제한하는 기능이다.

```java
private int day;
```

실습 내용

- public
- private

#### 7. Getter와 Setter

`private` 변수는 직접 접근할 수 없기 때문에 Getter와 Setter를 사용한다.

```java
public int getDay() {
    return day;
}

public void setDay(int day) {
    this.day = day;
}
```

실습 내용

- 날짜 정보 저장
- Getter를 이용한 값 조회
- Setter를 이용한 값 변경

#### 8. 캡슐화(Encapsulation)

객체의 데이터를 외부에서 직접 수정하지 못하도록 보호하고, Getter와 Setter를 통해 접근하도록 하는 객체지향 개념이다.

실습 내용

- MyDate 클래스
- private 변수 사용
- Getter / Setter 활용

#### 9. 참조 자료형(Reference Type)

객체를 참조하는 자료형으로, 하나의 객체 안에 다른 객체를 포함할 수 있다.

```java
Subject2 korean;
Subject2 math;
```

실습 내용

- Student와 Subject 클래스 관계
- 학생 객체 안에 과목 객체 생성
- 과목별 점수 관리

#### 10. 객체 간 관계

객체는 다른 객체를 참조하여 보다 복잡한 구조를 만들 수 있다.

실습 내용

- Student 객체
- Subject 객체
- 학생별 과목 및 점수 출력

---

#### 핵심 정리

- 메서드는 특정 기능을 수행하는 코드의 묶음이며, 필요할 때 호출하여 사용할 수 있다.
- 클래스는 객체를 생성하기 위한 설계도이며, 객체는 클래스에서 생성된 실제 데이터이다.
- 생성자는 객체 생성과 동시에 필요한 값을 초기화할 수 있다.
- `private` 변수는 외부에서 직접 접근할 수 없으며, Getter와 Setter를 통해 접근한다.
- 캡슐화는 객체의 데이터를 보호하기 위한 객체지향 프로그래밍의 핵심 개념이다.
- 참조 자료형을 사용하면 객체 안에서 다른 객체를 사용할 수 있어 프로그램을 구조적으로 설계할 수 있다.

---

