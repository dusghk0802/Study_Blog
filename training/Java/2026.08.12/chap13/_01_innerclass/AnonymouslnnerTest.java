package chap13._01_innerclass;

class Outer2 {
    Runnable getRunnable(int i){
        int num = 100;
        return new Runnable() {
            @Override
            public void run() {
                System.out.println(i);
                System.out.println(num);
            }
        };
    }
    /*Runnable runner = new Runnable() {
        @Override
        public void run() {
            System.out.println("Runnable이 구현한 익명 클래스 변수");
        }
    };*/

    Runnable runner = () -> {
        System.out.println("Runnable이 구현한 익명 클래스 변수");
    };
    //람다식

}

public class AnonymouslnnerTest {
    public static void main(String[] args) {
        Outer2 out = new Outer2();
        Runnable runnable = out.getRunnable(10);
        runnable.run();
        out.runner.run();
    }
}
