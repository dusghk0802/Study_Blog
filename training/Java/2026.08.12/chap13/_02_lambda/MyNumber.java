package chap13._02_lambda;

@FunctionalInterface
public interface MyNumber {
    int getMaxNumber(int num1, int num2);
//    int add(int num1, int num2);
    //두 개 이상의 메서드인 경우 오류 발생 -> 하나만 가능
}
