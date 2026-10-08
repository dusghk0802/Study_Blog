package chap10.interfaceex;

public class CalculatorTest {
    public static void main(String[] args) {

        int num1 = 10;
        int num2 = 5;

        Calc calc = new CompleteCalc();
        System.out.println(calc.add(num1, num2));
        System.out.println(calc.subtract(num1, num2));

    }
}
