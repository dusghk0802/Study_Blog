package chap08.Quiz;

public class GoldCustomer extends Customer {
    //[문제 1] GOLD 고객 클래스 만들기
    //
    //다음 조건에 맞게 Customer 클래스를 상속받는 GoldCustomer 클래스를 작성하세요.
    //
    //[조건]
    //- Customer 클래스를 상속받습니다.
    //- 고객 등급은 "GOLD"입니다.
    //- 보너스 적립률은 2%입니다.
    //- 제품 가격을 5% 할인받습니다.
    //- calcPrice() 메서드를 오버라이딩합니다.
    //- 고객 번호는 10030, 이름은 "강감찬"으로 생성합니다.
    //- 제품 가격은 20000원입니다.
    //- 할인된 실제 지불 금액을 출력하세요.
    //[실행 결과]
    //강감찬 님이 지불해야 하는 금액은 19000원입니다.
    double saleRatio;

    public GoldCustomer(int customerID, String customerName) {
        super(customerID, customerName);

        customerGrade = "GOLD";
        bonusRatio = 0.02;
        saleRatio = 0.05;
    }
    public  int calcPrice(int price) {
        bonusPoint += price * bonusRatio;
        return price - (int)(price * saleRatio);
    }
}
