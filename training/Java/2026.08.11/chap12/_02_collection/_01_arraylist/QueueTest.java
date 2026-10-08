package chap12._02_collection._01_arraylist;

import java.util.ArrayList;

class MyQueue{
    private ArrayList<String> arrayQueue = new ArrayList<String>();

    public void enQueue(String data) {
        arrayQueue.add(data);
    }
    public String deQueue(){
        int len = arrayQueue.size();
        if (len == 0){
            System.out.println("큐 비었습니다.");
            return null;
        }
        return (arrayQueue.remove(0));
    }
}

public class QueueTest {
    public static void main(String[] args) {

        MyQueue queue = new MyQueue();
        queue.enQueue("A");
        queue.enQueue("B");
        queue.enQueue("C");
        queue.enQueue("D");

        System.out.println(queue.deQueue());
        System.out.println(queue.deQueue());
        System.out.println(queue.deQueue());
        System.out.println(queue.deQueue());
    }
}
