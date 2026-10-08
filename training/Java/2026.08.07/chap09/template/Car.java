package chap09.template;

public abstract class Car {
    //이곳에 다 구현할 수 없고, 자동차 마다 기능이 다름.
    public abstract void drive();
    public abstract void stop();

    public void starCar(){
        System.out.println("시동을 겁니다.");
    }

    public void trunOff(){
        System.out.println("시동을 끕니다.");
    }

    //템플릿 메서드 프로그램이 돌아갈 때 시나리오 일련의 순서
    //템플릿 메서드 하위 클래스에서 변경 할 수 없으므로 final로
    final void run()
    {
        starCar();
        drive();
        stop();
        trunOff();
    }
}
