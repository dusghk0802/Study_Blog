//문제1.
//회원 5명을 ArrayList에 저장하고, Stream을 이용하여 회원 이름을
//가나다순으로 정렬하여 출력하세요.
//
//또한 프로그램 실행 중 발생할 수 있는 예외를 try-catch-finally를 이용하여
//처리하세요.
//
//[조건]
//
//1. ArrayList를 사용합니다.
//2. 회원은 다음 5명으로 합니다.
//- 김민준
//- 이서연
//- 박지훈
//- 최유진
//- 정현우
//3. map()을 이용하여 Member 객체에서 이름만 가져옵니다.
//4. sorted()를 이용하여 이름을 정렬합니다.
//5. forEach()와 람다식을 이용하여 출력합니다.
//6. try 블록 안에서 Stream 연산을 수행합니다.
//7. catch 블록에서 예외가 발생한 경우 “회원 정보를 처리하는 중 오류가
//발생했습니다.”를 출력합니다.
//8. finally 블록에서 “회원 정보 처리를 종료합니다.”를 출력합니다.
//
//[실행 결과 예]
//
//회원 이름 목록 김민준 박지훈 이서연 정현우 최유진 회원 정보 처리를
//종료합니다.
package chap16;

import java.util.ArrayList;
import java.util.Scanner;

class Member{
    private String name;
    private int memberId;

    public Member(int memberId, String name) {
        this.memberId = memberId;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getMemberId() {
        return memberId;
    }
}
public class Quiz {
    public static void main(String[] args) {
        ArrayList<Member> memberList = new ArrayList<>();

        memberList.add(new Member(2026001,"김민준"));
        memberList.add(new Member(2026002,"이서연"));
        memberList.add(new Member(2026003,"박지훈"));
        memberList.add(new Member(2026004, "최유진"));
        memberList.add(new Member(2026005, "정현우"));

        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("학번 입력 : ");
            int memberId = scanner.nextInt();

            System.out.print("이름 입력 : ");
            String name = scanner.next();

            boolean isMember = memberList.stream()
                    .anyMatch(member -> member.getMemberId() == memberId && member.getName().equals(name));

            if (isMember){
                System.out.println();
                System.out.println("등록된 회원입니다.");
                System.out.println("회원 이름 목록");

                memberList.stream()
                        .map(Member::getName)
                        .sorted()
                        .forEach(Membername -> System.out.println(Membername));
            } else {
                System.out.println("등록되지 않은 학생입니다.");
            }
        } catch (Exception e) {
            System.out.println("회원 정보를 처리하는 중 오류가\n" + "발생했습니다.");
        } finally {
            System.out.println("회원 정보 처리를 종료합니다.");
            scanner.close();
        }
    }
}

//작성하신 프로그램을 아래와 같이 수정해 보세요.
//
//[실행 결과 예 1]
//
//학번 입력 : 2026002 이름 입력 : 이서연
//
//등록된 회원입니다. 회원 이름 목록 김민준 박지훈 이서연 정현우 최유진
//회원 확인을 종료합니다.
//
//[실행 결과 예 2]
//
//학번 입력 : 2026002 이름 입력 : 김민준
//
//등록되지 않은 회원입니다. 회원 확인을 종료합니다.