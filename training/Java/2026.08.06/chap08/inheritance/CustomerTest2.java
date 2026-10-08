package chap08.inheritance;

public class CustomerTest2 {
    public static void main(String[] args) {

        Customer customerLee = new Customer(1010,"이순신");
        customerLee.setCustomerID(1000);
        customerLee.setCustomerName("이순신");
        customerLee.bonusPoint = 10000;

        VIPCustomer customerKim = new VIPCustomer(1020,"김유신",1001);
        customerKim.setCustomerID(1001);
        customerKim.setCustomerName("김유신");
        customerKim.bonusPoint = 10000;

        Customer vc = new VIPCustomer(1001, "상속 테스트",10000);
        vc.setCustomerID(1001);
        vc.setCustomerName("상속 테스트");
        vc.bonusPoint = 10000;

        System.out.println(customerLee.showCustomerInfo());
        System.out.println(customerKim.showCustomerInfo());
        System.out.println(vc.showCustomerInfo());
    }
}
