package chap16;

//2.Runnable 인터페이스 구현하여 Thread 생성
class MyRunnable implements Runnable{

    @Override
    public void run() {
        int i;
        for (i = 0; i<=200; i++) {
            System.out.println(Thread.currentThread().getName() + ":" + i + "\t");
        }
    }
}

public class MyRunnableTest {
    public static void main(String[] args) {
        System.out.println(Thread.currentThread());

        MyThread thread1 = new MyThread();
        thread1.start();
        MyThread thread2 = new MyThread();
        thread2.start();

        System.out.println("end");
    }

}
