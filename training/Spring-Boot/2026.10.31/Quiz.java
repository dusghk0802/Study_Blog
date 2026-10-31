/*
10.30 복습 퀴즈 - 람다식, 함수형 인터페이스, 메서드 참조, Stream API

Q1. 함수형 인터페이스 4가지 형태를 람다식으로 구현하기
    A : 입력 x, 리턴 x  -> "Hello Lamda" 출력
    B : 입력 o, 리턴 x  -> 입력받은 숫자의 제곱 출력
    C : 입력 x, 리턴 o  -> 100 리턴
    D : 입력 o, 리턴 o  -> 두 수 중 작은 값 리턴

Q2. 익명 클래스로 만든 코드를 람다식으로 바꾸기

Q3. 메서드 참조(::)로 바꾸기
    s -> s.toUpperCase()          ->  String::toUpperCase
    str -> System.out.println(str) ->  System.out::println

Q4. 1 ~ 10 중 홀수의 합을 명령형(for, if)과 선언형(stream) 두 가지로 구하기

Q5. 과일 목록에서
    1) 중복 제거  2) 길이 5 이하만  3) 대문자로 변환  4) 정렬  5) 앞에서 3개만 출력

Q6. 점수 목록에서 80점 이상인 점수의 개수와 평균 구하기
*/

import java.util.Arrays;
import java.util.List;

public class Quiz {

	public static void main(String[] args) {

		System.out.println("===== Q1. 함수형 인터페이스 4가지 =====");
		QuizA a = () -> System.out.println("Hello Lamda");
		a.print();

		QuizB b = x -> System.out.println("제곱: " + x * x);
		b.square(7);

		QuizC c = () -> 100;   // 리턴문 하나면 {}와 return 생략 가능
		System.out.println("get(): " + c.get());

		QuizD d = (x, y) -> x < y ? x : y;
		System.out.println("min(3, 8): " + d.min(3, 8));

		System.out.println("\n===== Q2. 익명 클래스 -> 람다식 =====");
		//익명 클래스
		QuizD anonymous = new QuizD() {
			@Override
			public int min(int x, int y) {
				return x < y ? x : y;
			}
		};
		//람다식 (인터페이스 이름, 메서드 이름, 타입, return 모두 생략)
		QuizD lamda = (x, y) -> x < y ? x : y;
		System.out.println("익명 클래스: " + anonymous.min(10, 4));
		System.out.println("람다식     : " + lamda.min(10, 4));

		System.out.println("\n===== Q3. 메서드 참조 =====");
		List<String> langs = Arrays.asList("java", "spring", "react");
		langs.stream()
				.map(String::toUpperCase)       // s -> s.toUpperCase()
				.forEach(System.out::println);  // str -> System.out.println(str)

		System.out.println("\n===== Q4. 명령형 vs 선언형 =====");
		List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		//명령형 : 어떻게(How)
		int sum1 = 0;
		for (int n : nums) {
			if (n % 2 == 1) {
				sum1 += n;
			}
		}

		//선언형 : 무엇을(What)
		int sum2 = nums.stream()
				.filter(n -> n % 2 == 1)
				.mapToInt(Integer::intValue)
				.sum();

		System.out.println("명령형 홀수 합: " + sum1);
		System.out.println("선언형 홀수 합: " + sum2);

		System.out.println("\n===== Q5. 중간연산 n번 -> 최종연산 1번 =====");
		List<String> fruits = Arrays.asList("apple", "kiwi", "banana", "apple", "grape", "melon", "kiwi", "fig");

		fruits.stream()                    //생성하기
				.distinct()                //중복 제거
				.filter(f -> f.length() <= 5)
				.map(String::toUpperCase)
				.sorted()
				.limit(3)
				.forEach(System.out::println); //최종연산 (스트림 닫힘)

		System.out.println("원본은 그대로: " + fruits); //스트림은 원본을 변경하지 않음

		System.out.println("\n===== Q6. 80점 이상 개수와 평균 =====");
		List<Integer> scores = Arrays.asList(95, 72, 88, 64, 80, 91, 55);

		long count = scores.stream()
				.filter(s -> s >= 80)
				.count();

		double avg = scores.stream()        //스트림은 일회용이라 다시 생성
				.filter(s -> s >= 80)
				.mapToInt(Integer::intValue)
				.average()
				.orElse(0);

		System.out.println("80점 이상 인원: " + count + "명");
		System.out.println("80점 이상 평균: " + avg);
	}

}

@FunctionalInterface
interface QuizA {
	void print();
}

@FunctionalInterface
interface QuizB {
	void square(int x);
}

@FunctionalInterface
interface QuizC {
	int get();
}

@FunctionalInterface
interface QuizD {
	int min(int a, int b);
}
