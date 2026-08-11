package chap12._02_collection._02_linkedlist;

import java.util.LinkedList;

public class LinkListTest {
    public static void main(String[] args) {

        LinkedList<String> myList = new LinkedList<String>();

        myList.add("A");
        myList.add("B");
        myList.add("C");

        System.out.println(myList);
        myList.add(1, "0");

        myList.addFirst("0");
        System.out.println(myList);
        System.out.println(myList.remove());

        System.out.println(myList);
    }
}
