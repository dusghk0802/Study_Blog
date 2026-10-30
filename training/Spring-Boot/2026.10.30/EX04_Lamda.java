
@FunctionalInterface
interface MyFunc{
	void method(int x);
}

@FunctionalInterface
interface MyFunc2{
	int method(int x, int y);
}

public class EX04_Lamda {

	public static void main(String[] args) {
		
		//1. MyFunc을 익명클래스 구현
		MyFunc my = new MyFunc() {
			
			@Override
			public void method(int x) {
				System.out.println("param x: " + x);
			}
		};	
		my.method(1000);
		
		//2. MyFunc2을 람다식으로 구현
		/*
		 * MyFunc2 my2 = (x,y) -> { int result = x + y; return result; }
		 */

		MyFunc2 my2 = (x,y) -> x + y;
		int result = my2.method(111, 222);
		System.out.println(result);
		
		//MyFunc 람다식으로
		MyFunc my3 = x -> System.out.println("param-x :" + x);
		my3.method(1234);
	}

}
