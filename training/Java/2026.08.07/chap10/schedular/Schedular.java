package chap10.schedular;

public interface Schedular {
    //콜 대기 상담원 - 정책에 따라 구현
    public void getNextCall();
    public void sendCallToAgent();
}
