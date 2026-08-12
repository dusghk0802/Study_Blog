package chap12._01_generics;

public class Powder <T extends  Material> extends Material {

    public void doPrinting(){
        System.out.println("Powder 재료로 출력합니다.");
    }

    public String toString(){
        return "재료는 Powder입니다.";
    }
}
