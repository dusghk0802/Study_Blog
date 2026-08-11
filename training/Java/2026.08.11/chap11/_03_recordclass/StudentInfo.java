package chap11._03_recordclass;

public record StudentInfo(int id, String name) {
    public static void main(String[] args) {

        StudentInfo studentInfo = new StudentInfo(12345, "이순신");
        StudentInfo studentInfo2 = new StudentInfo(12345, "이순신");

        System.out.println(studentInfo.equals(studentInfo2));
        System.out.println(studentInfo.name());
        System.out.println(studentInfo);
    }
}
