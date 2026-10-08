package chap13._01_innerclass;

import chap13._02_lambda.StringConCatImpl;
import chap13._02_lambda.StringConcat;

public class TestStringConcat {
    public static void main(String[] args) {
        String s1 = "Hello";
        String s2 = "World";

        StringConcat concat2 = (s, v) -> System.out.println(s + "," + v);
        concat2.makeString(s1, s2);
    }
}
