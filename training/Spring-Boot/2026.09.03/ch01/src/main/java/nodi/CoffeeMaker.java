package nodi;

public class CoffeeMaker {
//    private EspressoMachine espressoMachine;
    private DripCoffeeMachine dripCoffeeMachine;

    //CoffeeMaker
    public CoffeeMaker(){
//        this.espressoMachine = new EspressoMachine();
        this.dripCoffeeMachine = new DripCoffeeMachine();
    }
    public  void makeCoffee(){
//        System.out.println(espressoMachine.brew());
        System.out.println(dripCoffeeMachine.brew());
    }
}
