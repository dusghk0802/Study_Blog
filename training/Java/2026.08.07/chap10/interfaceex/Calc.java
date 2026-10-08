package chap10.interfaceex;

public interface Calc {

    //public static final double PI = 3.14;
    double PI = 3.14;
    int ERROR = -999999999;

    int add(int num1, int num2);
    int subtract(int num1, int num2);
    int times(int num1, int num2);
    int divide(int num1, int num2);
}
