package chap11._02_string;

public class _03_StringBuilderTest {
    public static void main(String[] args) {

        String str1 = new String("java");
        System.out.println("문자열 주소 " + System.identityHashCode(str1));

        StringBuffer buffer = new StringBuffer(str1);
        System.out.println("연산전 buffer 메모리 주소 : " + System.identityHashCode(buffer));

        buffer.append(" adn");
        buffer.append(" android");
        buffer.append(" programing is full!!!");
        System.out.println("연산 후 buffer 메모리 주소 : " + System.identityHashCode(buffer));
        //같은 주소 출력됨

        String str2 = buffer.toString();
        System.out.println(str2);
        System.out.println("새로 만들어진 str2 문자열 주소 : " + System.identityHashCode(str2));
    }
}
