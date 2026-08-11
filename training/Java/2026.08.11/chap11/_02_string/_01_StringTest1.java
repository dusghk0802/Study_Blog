package chap11._02_string;

public class _01_StringTest1 {
    public static void main(String[] args) {
        String str1 = new String("abc");
        String str2 = new String("abc");

        System.out.println(str1 == str2); //힙 메보리 주소가 다르므로 false
        System.out.println(str1.equals(str2)); //문자열이 같은지 = 논리적인 동일성

        String str3 = "abc";
        String str4 = "abc";
        System.out.println(str3 == str4); //상수풀에 저장되므로 주소값이 같음
        System.out.println(str3.equals((str4))); //문자열이 같으므로 true

    }
}
