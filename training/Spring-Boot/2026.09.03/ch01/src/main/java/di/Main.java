package di;

public class Main {
    public static void main(String[] args) {
        CoffeeMaker coffeeMaker = new CoffeeMaker();
//        coffeeMaker.setCoffeeMachine(new DripCoffeeMachine());
        coffeeMaker.setCoffeeMachine(new MochaCoffeeMachine());
        coffeeMaker.makeCoffee();
    }
}
