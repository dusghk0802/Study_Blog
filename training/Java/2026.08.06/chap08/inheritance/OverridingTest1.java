package chap08.inheritance;

public class OverridingTest1 {
    public static void main(String[] args) {
        Customer customerLee = new Customer(1010,"이순신");
        customerLee.bonusPoint = 1000;

        VIPCustomer customerKim = new VIPCustomer(1020, "김유신",1000);
        customerKim.bonusPoint = 1000;

        int price = 10000;
        System.out.println(customerLee.getCustomerName() + "님이 지불해야 하는 금액은" +
                customerLee.calcPrice(price) + "원 입니다.");

        System.out.println(customerKim.getCustomerName() + "님이 지불해야 하는 금액은" +
                customerKim.calcPrice(price) + "원 입니다.");
    }
}
