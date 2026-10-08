package chap07.arraylist;

public class StudentTest {
    public static void main(String[] args) {

        Student studentLee = new Student(100,"이코사");
        studentLee.addSubject("국어", 100);
        studentLee.addSubject("수학",99);

        Student studentKim = new Student(101,"김코사");
        studentKim.addSubject("국어", 100);
        studentKim.addSubject("수학",99);
        studentKim.addSubject("영어",99);

        studentLee.showStudentInfo();
        System.out.println("------------------------------------");
        studentKim.showStudentInfo();
    }
}
