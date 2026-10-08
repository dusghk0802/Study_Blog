package chap08.witharraylist;

import java.util.ArrayList;

public class CoustomerTset {
    public static void main(String[] args) {
        ArrayList<Customer> customeList = new ArrayList<>();
        Customer customerLee = new Customer(1001, "이순신");
        Customer customershin = new Customer(1002, "심사임당");
        Customer customerYul = new GoldCustomer(1003, "이율곡");
        Customer customerHong = new GoldCustomer(1014, "홍길동");
        Customer customerKim = new VIPCustomer(1015, "김유신", 12345);
        //상위 클래스 = new 하위 클래스도 가능

        customeList.add(customerLee);
        customeList.add(customershin);
        customeList.add(customerYul);
        customeList.add(customerHong);
        customeList.add(customerKim);

        System.out.println("==============고객정보 출력==============");
        for (Customer customer : customeList) {
            System.out.println(customer.showCustomerInfo());
        }

        System.out.println("========할인울과 보너스 포인트 결과========");
        int price = 10000;
        for (Customer customer : customeList) {
            int cost = customer.calcPrice(price);
            System.out.println(customer.getCustomerName() + "님이" + cost + "을 지불하겠습니다.");
            System.out.println(customer.showCustomerInfo());
        }

    }
}
