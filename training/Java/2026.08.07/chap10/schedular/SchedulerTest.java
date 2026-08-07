package chap10.schedular;

import java.io.IOException;

public class SchedulerTest {
    public static void main(String[] args) throws IOException {
        System.out.println("전화 상담 할당 방식 선택");
        System.out.println("R: 한명씩 파례로 할당");
        System.out.println("L: 쉬고 있거나 대기 적은 상담원 할당");
        System.out.println("P: 우선순위가 높은 고객 할당");

        int ch = System.in.read();
        Schedular schedular = null;
        if (ch == 'R' || ch =='r'){
            schedular = new RoundRobin();
        }
        else if (ch == 'L' || ch =='l'){
            schedular = new Lastjob();
        }
        else if (ch == 'P' || ch =='p') {
            schedular = new PriorityAllocation();
        }
        else {
            System.out.println("지원되지 않는 기능입니다.");
            return;
        }
        schedular.getNextCall();
        schedular.sendCallToAgent();
    }

}
