package chap09.abstractex;

public abstract class Computer {

    //2개의 추상 메서드- 이 메서드들은 구현 부분이 없음.
    public abstract void dispaly();
    public abstract void typing();

    //2개의 구현 메서드
    public void trunOn(){
        System.out.println("전원을 켭니다.");
    }
    public void trunOff(){
        System.out.println("전원을 끕니다.");
    }
}
