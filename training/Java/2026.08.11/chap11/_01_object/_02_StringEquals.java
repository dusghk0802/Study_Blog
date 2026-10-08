package chap11._01_object;

public class _02_StringEquals {
    public static void main(String[] args) {
        String str1 = new String("abc");
        String str2 = new String("abc");

        System.out.println(str1 == str2); //힙 메보리 주소가 다르므로 false
        System.out.println(str1.equals(str2)); //문자열이 같은지 = 논리적인 동일성

        //이미 재정의가 되어 있음.
        System.out.println(str1.hashCode());
        System.out.println(str2.hashCode());

        //실제 주소값을 출력
        System.out.println(System.identityHashCode(str1));
        System.out.println(System.identityHashCode(str2));
    }
}
