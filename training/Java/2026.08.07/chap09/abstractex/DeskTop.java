package chap09.abstractex;

public class DeskTop extends Computer{

    @Override
    public void dispaly() {
        System.out.println("DeskTop Display");
    }

    @Override
    public void typing() {
        System.out.println("DeskTop typing");
    }
}
