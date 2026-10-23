
public class AirPlane {
    private int airNum;
    private String airName;
    private static int airTotalCount;

    public AirPlane(int airNum, String airName) {
        this.airNum = airNum;
        this.airName = airName;
        AirPlane.airTotalCount++;
        this.airDisplay();
    }

    public void airDisplay() {
        System.out.println("비행기 이름: " + airName + ", 번호: " + airNum);
    }

    public static void printTotal() {
        System.out.println("총 비행기 대수: " + airTotalCount + "대");
    }
}

class AirPlaneTest {
    public static void main(String[] args) {
        new AirPlane(101, "대한항공");
        new AirPlane(202, "아시아나");

        AirPlane.printTotal();  // 마지막에 누적 대수 출력
    }
}