package chap09.template;

public class AICar extends Car{

    @Override
    public void drive() {
        System.out.println("자율 주행합니다.");
        System.out.println("자동차가 알아서 ai로 움직입니다.");
    }

    @Override
    public void stop() {
        System.out.println("스스로 움직입니다.");
    }
}
