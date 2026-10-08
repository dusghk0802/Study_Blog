package chap08.inheritance;

public class VIPCustomer extends Customer {
    private int agentID;
    double salerRatio;

    //디폴트 생성자
    //디폴트 생성자
    public VIPCustomer(int customerID, String customerName, int agentID){
        super(customerID, customerName);
        customerGrade = "VIP";
        salerRatio = 0.05;
        bonusRatio = 0.1;
        this.agentID = agentID;
        System.out.println("VIPCustomer() 생성자 호출");
    }

    //보너스 포인트 계산
    public  int calcPrice(int price) {
        bonusPoint += price * bonusRatio;
        return price - (int)(price * salerRatio);
    }

    public int getAgentID() {
        return agentID;
    }
}
