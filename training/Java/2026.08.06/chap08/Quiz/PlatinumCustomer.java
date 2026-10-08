package chap08.Quiz;

public class PlatinumCustomer extends Customer {
    //[문제 2] PLATINUM 고객 클래스 만들기
    //Customer 클래스를 상속받는 PlatinumCustomer 클래스를 작성하세요.
    //[조건]
    //- Customer 클래스를 상속받습니다.
    //- 고객 등급은 "PLATINUM"입니다.
    //- 보너스 적립률은 7%입니다.
    //- 제품 가격을 15% 할인받습니다.
    //- 담당 상담원 번호 agentID를 가집니다.
    //- 생성자에서 고객 번호, 고객 이름, 담당 상담원 번호를 전달받습니다.
    //- calcPrice() 메서드를 오버라이딩합니다.
    //- 고객 번호는 10040, 이름은 "유관순", 담당 상담원 번호는 3000입니다.
    //- 제품 가격은 40000원입니다.
    //- 할인된 실제 지불 금액을 출력하세요.
    //[실행 결과]
    //유관순 님이 지불해야 하는 금액은 34000원입니다.
    private int agentID;
    private double saleRatio;

    public PlatinumCustomer(int customerID, String customerName, int agentID) {
        super(customerID, customerName);

        customerGrade = "PLATINUM";
        bonusRatio = 0.07;
        saleRatio = 0.15;
        this.agentID = agentID;
    }

    public int calcPrice(int price) {
        bonusPoint += (int) (price * bonusRatio);
        return price - (int) (price * saleRatio);
    }

}
