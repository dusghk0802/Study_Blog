/*
람다 표현식
람다식은 함수를 하나의 식으로 표현한것

자바에서 람다식을 사용하기 위해서는 함수형 인터페이스 생성
>> 자바에서는 함수하나만을 사용할 수 없다
>> class Emp { public void run(){}}  > Emp emp = new Emp();  emp.run();

>>javascript 처럼 함수만 사용 .... 

함수형 인터페이스 - 인터페이스가 [ 단 하나의 추상 메서드 ] 만 선언된 인터페이스 

interface MyFunc {
   int max(int a, int b);
}

익명 클래스 ... 익명 객체 선언을 통해서

MyFunc f = new MyFunc(){
	int max(int a, int b) {return a > b ? a : b; }  //람다식을 통해서 표현
}

//사실은 >> OOP >>  class Func implements MyFunc { public int max(){ ...} }

int value =f.max(3.5);

람다식 장점
1. 코드 간결
2. 가독성이 높다 
3. 병렬프로그래밍 가능

람다식 단점
람다를 사용하면 무명함수는 재사용 불가능
디버깅 어렵다
재귀적 구현 어렵다



자바에서 자바스크립트처럼 함수를 사용하기 위한 방법
자에서 함수적 인터페이스  @FunctionalInterface 근거
*/

//javascript 처럼 함수 하나만 만들어서 바로 사용할거야 >> 자바도 함수형 프로그래밍 (인터페이스 통해서)

import java.util.ArrayList;
import java.util.List;

@FunctionalInterface
interface MyLamdaFunction{
	int max(int a, int b);
}

public class EX02_Lamda {

	public static void main(String[] args) {
		
		//익명객체, 익명함수
		System.out.println(
				new MyLamdaFunction() {
					@Override
					public int max(int a, int b) {
						return a>b ? a:b;
					}
				}.max(3, 5));
		
		//자바스크립트 처럼 자바 흉내를...
		//람다식
		/*@FunctionalInterface
		interface MyLAmdaFunction{
			int max(int a, int b)
		}*/
		MyLamdaFunction lamdaFunction = (int a, int b) -> a>b ? a:b;
		System.out.println(lamdaFunction.max(10, 20));
		//객체지향 프로그래밍도 javascript 함수형 프로그래밍을 지원한다...
		
		List<String> list = new ArrayList<>();
		list.add("java");
		list.add("c");
		list.add("react");
		
		for(String str:list) {
			System.out.println(str);
		}
		
		System.out.println();
		list.stream().forEach((String str) -> {System.out.println(str);});
		list.stream().forEach(str -> {System.out.println(str);});
		list.stream().forEach(System.out::println); // ::메서드 참조 : 이미 있는 메서드에 데이터 간단하게 전달 (하나짜리)
	}

}
