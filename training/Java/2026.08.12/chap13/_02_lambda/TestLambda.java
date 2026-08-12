package chap13._02_lambda;

interface PrintString{
    void showString(String string);
}

public class TestLambda {
    public static void main(String[] args) {
        PrintString lambdaStr = s -> System.out.println(s);
        lambdaStr.showString("hello test1");

        showMyString(lambdaStr);
    }

    public static void showMyString(PrintString p){p.showString("hello test2");}

    public static PrintString returnString(){return s -> System.out.println(s + "world");

    }
}
