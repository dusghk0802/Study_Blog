package chap08.inheritance;

public class OverridingTest3 {
    public static void main(String[] args) {
        int price = 10000;
        Customer customerLee = new Customer(1010,"이순신");
        System.out.println(customerLee.getCustomerName() + "님이 지불해야 하는 금액은" + customerLee.calcPrice(price)
                + "원 입니다.");

        VIPCustomer customerKim = new VIPCustomer(1020,"김유신",1001);
        System.out.println(customerKim.getCustomerName() + "님이 지불해야 하는 금액은" + customerKim.calcPrice(price)
                + "원 입니다.");

        Customer vc = new Customer(1010,"이순신");
        System.out.println(vc.getCustomerName() + "님이 지불해야 하는 금액은" + vc.calcPrice(price)
                + "원 입니다.");
    }
}
