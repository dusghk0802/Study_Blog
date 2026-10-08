package chap15;

import java.io.IOException;

public class _02_SysteminTest1 {
    public static void main(String[] args) {
        System.out.println("알파벳 하나를 쓰고 엔터를 누르세요");

        int i =0;
        try {
            while ((i = System.in.read()) != '\n'){
                System.out.print((char)i);
            };
            //println로 하면 문자와 숫자가 나열되서 출력됨
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(i);
    }
}
