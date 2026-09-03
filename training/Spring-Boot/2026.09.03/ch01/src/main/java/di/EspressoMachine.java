package di;

public class EspressoMachine implements CoffeeMachine{
    @Override
    public String brew() {
        return "Brewing coffee with Expresso Machine";
    }
}
