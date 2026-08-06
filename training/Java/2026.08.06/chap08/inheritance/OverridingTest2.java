package chap08.inheritance;

public class OverridingTest2 {
    public static void main(String[] args) {
        Customer vc = new VIPCustomer(1010,"나몰라",2000);
        vc.bonusPoint = 1000;

        System.out.println(vc.getCustomerName() + "님이 지불해야 하는 금액은" +
                vc.calcPrice(1000) + "원 입니다.");
    }
}
