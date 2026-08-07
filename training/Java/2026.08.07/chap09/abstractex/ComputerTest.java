package chap09.abstractex;

public class ComputerTest {
    public static void main(String[] args) {

//        Computer c1 = new Computer(); //추상 메서드
        Computer c2 = new DeskTop();
//        Computer c3 = new NoteBook();
        Computer c4 = new MyNoteBook();

        c2.dispaly();
        c4.dispaly();
        //상속과 다형성의 원리 가져 오면서 추상 메서드
    }
}
