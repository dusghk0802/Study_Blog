package chap08.Quiz;

public class PlatinumCustomerTest {
    public static void main(String[] args) {
        PlatinumCustomer customeryou = new PlatinumCustomer(10040,"유관순",3000);

        int price = 40000;
        System.out.println(customeryou.customerName + "님이 지불해야 하는 금액은" +
                customeryou.calcPrice(price) + "원입니다.");
    }
}
