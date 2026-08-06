package chap08.Quiz;

public class GoldCustomerTest {
    public static void main(String[] args) {
        GoldCustomer customerKong = new GoldCustomer(10030,"강김찬");

        int price = 20000;
        System.out.println(customerKong.getCustomerName() + "님이 지불해야 하는 금액은 "
                + customerKong.calcPrice(price) + "원입니다.");
    }
}
