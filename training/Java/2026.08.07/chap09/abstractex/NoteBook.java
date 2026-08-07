package chap09.abstractex;

//typing을 구현하던지 노트북도 abstract로 만들어라.
public abstract class NoteBook extends Computer{

    @Override
    public void dispaly() {
        System.out.println("NoteBook Display()");
    }
}
