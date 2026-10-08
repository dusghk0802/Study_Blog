package chap12._01_generics;

public class GenericPrinterTest <T extends Material>{
    public static void main(String[] args) {
        GenericPrinter<Powder> powerPrinter = new GenericPrinter<Powder>();
        powerPrinter.setMaterial(new Powder());
//        Powder powder = (Powder) powerPrinter.getMaterial();//다운캐스팅 (powder)
        Powder powder = powerPrinter.getMaterial();
        System.out.println(powerPrinter);

        GenericPrinter<Plastic> plasticPrinter = new GenericPrinter<Plastic>();
        plasticPrinter.setMaterial(new Plastic());
        Plastic plastic = plasticPrinter.getMaterial();
        System.out.println(plasticPrinter);

/*        GenericPrinter<Water> printerWater = new GenericPrinter<Water>();
        printerWater.setMaterial(new Water());
        System.out.println(printerWater);*/
        //Water를 상속하지 않아 오류 발생
    }
}
