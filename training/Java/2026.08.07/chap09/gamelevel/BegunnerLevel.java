package chap09.gamelevel;

public class BegunnerLevel extends PlayerLevel{
    @Override
    public void run() {
        System.out.println("천천히 달립니다.");
    }

    @Override
    public void jump() {
        System.out.println("Jump할줄 모르지롱.");
    }

    @Override
    public void turn() {
        System.out.println("Turn할줄 모르지롱.");
    }

    @Override
    public void showLevelMessage() {
        System.out.println("*****초급자 레벨입니다.*****");
    }
}
