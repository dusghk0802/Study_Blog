package chap10.Quiz;

public class HeapSort implements Sort{
    @Override
    public void ascedning(int[] arr) {
        System.out.println("BubbleSort ascedning");
    }

    @Override
    public void descending(int[] arr) {
        System.out.println("HeapSort descending");
    }

    @Override
    public void description() {
        System.out.println("숫자를 정렬하는 알고리즘입니다.");
    }
}
