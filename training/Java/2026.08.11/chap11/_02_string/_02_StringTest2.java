package chap11._02_string;

public class _02_StringTest2 {
    public static void main(String[] args) {
        String javaStr = new String("java");
        String androidStr = new String("android");
        System.out.println(javaStr);
        System.out.println(androidStr);
        System.out.println("처음 문자열의 주소 값: " + System.identityHashCode(javaStr));

        javaStr = javaStr.concat(androidStr);
        System.out.println(javaStr);
        System.out.println("연결된 문자열의 주소 값: " + System.identityHashCode(javaStr));
        //실행 할때마다 주소값 변경되서 출력
    }
}
