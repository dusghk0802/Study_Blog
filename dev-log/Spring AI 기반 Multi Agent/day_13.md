# 13일차

## Java static, Singleton, this, 배열(Array), 객체 협력

📌 학습일 : 2026.08.05

📌 학습 내용 : static, Singleton Pattern, this, 객체 간 협력, 캡슐화, 배열(Array), 2차원 배열

---

#### 1. 캡슐화(Encapsulation)

객체의 내부 데이터를 외부에서 직접 접근하지 못하도록 보호하고, 필요한 기능만 외부에 제공하는 객체지향 개념이다.

```java
private 자료형 변수명;

public 자료형 get변수명() {
    return 변수명;
}

public void set변수명(자료형 변수명) {
    this.변수명 = 변수명;
}
```


#### 2. 객체 간 협력(Cooperation)

객체는 서로 메서드를 호출하고 데이터를 전달하면서 하나의 기능을 수행한다.

```java
객체명1.메서드명(객체명2);
```


#### 3. static 변수와 static 메서드

`static`은 객체를 생성하지 않아도 사용할 수 있는 클래스 구성 요소이며, 모든 객체가 하나의 값을 공유한다.

```java
public class 클래스명 {

    static int 공유변수 = 0;

    public static int get공유변수() {
        return 공유변수;
    }
}
```

클래스 이름으로 직접 접근할 수 있다.

```java
클래스명.공유변수;
클래스명.get공유변수();
```


#### 4. 싱글톤 패턴(Singleton Pattern)

프로그램에서 객체를 하나만 생성하고 공유하기 위한 디자인 패턴이다.

```java
public class 클래스명 {

    private static 클래스명 인스턴스 = new 클래스명();

    private 클래스명() {
    }

    public static 클래스명 getInstance() {
        return 인스턴스;
    }
}
```

생성자를 `private`으로 선언하여 외부에서 객체를 생성하지 못하도록 한다.

#### 5. this 키워드

`this`는 현재 객체 자기 자신을 가리키는 참조 변수이다.

멤버 변수와 매개변수의 이름이 같을 때 구분하기 위해 사용한다.

```java
public void set변수명(int 변수명) {
    this.변수명 = 변수명;
}
```

같은 클래스의 다른 생성자를 호출할 수도 있다.

```java
public 클래스명() {
    this("기본값", 0);
}

public 클래스명(String 변수명1, int 변수명2) {
    this.변수명1 = 변수명1;
    this.변수명2 = 변수명2;
}
```


#### 6. 배열(Array)

배열은 같은 자료형의 여러 데이터를 하나의 변수로 관리하는 자료구조이다.

```java
int[] 배열명 = {1, 2, 3, 4, 5};
```

배열의 크기를 먼저 지정하여 생성할 수도 있다.

```java
int[] 배열명 = new int[5];

배열명[0] = 10;
배열명[1] = 20;
```

#### 7. 배열과 반복문

배열은 반복문과 함께 사용하여 모든 데이터를 순차적으로 처리할 수 있다.

```java
for (int i = 0; i < 배열명.length; i++) {
    System.out.println(배열명[i]);
}
```

`length`는 배열의 전체 길이를 반환한다.


#### 8. 배열 복사(Array Copy)

`System.arraycopy()`를 사용하면 배열의 데이터를 다른 배열로 복사할 수 있다.

```java
System.arraycopy(배열명1, 0, 배열명2, 0, 배열명1.length);
```

또는 원하는 범위만 복사할 수도 있다.

```java
System.arraycopy(배열명1, 0, 배열명2, 1, 4);
```

#### 9. 2차원 배열(Two-Dimensional Array)

2차원 배열은 행과 열 구조의 데이터를 저장하는 배열이다.

```java
int[][] 배열명 = {
    {1, 2, 3},
    {4, 5, 6}
};
```

중첩 반복문을 사용하여 모든 데이터를 출력할 수 있다.

```java
for (int i = 0; i < 배열명.length; i++) {
    for (int j = 0; j < 배열명[i].length; j++) {
        System.out.println(배열명[i][j]);
    }
}
```

#### 10. 클래스와 static 활용

인스턴스 변수는 객체마다 서로 다른 값을 저장하고, static 변수는 모든 객체가 공유하는 값을 저장한다.

```java
public class 클래스명 {

    private String 변수명1;
    private int 변수명2;

    static int 객체수 = 0;

    public 클래스명(String 변수명1, int 변수명2) {
        this.변수명1 = 변수명1;
        this.변수명2 = 변수명2;
        객체수++;
    }
}
```
---

#### 핵심 정리

* 캡슐화는 객체의 데이터를 보호하고 필요한 기능만 외부에 제공하는 객체지향 개념이다.
* 객체는 서로 메서드를 호출하고 데이터를 전달하면서 하나의 기능을 수행한다.
* `static` 변수와 메서드는 클래스에 소속되며, 모든 객체가 하나의 값을 공유한다.
* 싱글톤 패턴은 프로그램에서 하나의 객체만 생성하여 공유하는 디자인 패턴이다.
* `this`는 현재 객체를 참조하며, 멤버 변수와 매개변수를 구분하거나 다른 생성자를 호출할 때 사용한다.
* 배열은 같은 자료형의 여러 데이터를 저장하는 자료구조이며, 반복문과 함께 자주 사용된다.
* `System.arraycopy()`를 이용하면 배열을 효율적으로 복사할 수 있다.
* 2차원 배열은 행과 열 구조의 데이터를 저장하며, 중첩 반복문으로 처리한다.
* 객체마다 개별적으로 관리해야 하는 값은 인스턴스 변수로, 모든 객체가 공유해야 하는 값은 static 변수로 선언한다.

---
