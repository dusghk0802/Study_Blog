package chap15;

import java.io.IOException;

public class _01_SysteminTest1 {
    public static void main(String[] args) {
        System.out.println("알파벳 하나를 쓰고 엔터를 누르세요");

        int i =0;
        try {
            i = System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(i);
    }
}
