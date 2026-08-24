package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BoardUpdateExample {
    public static void main(String[] args) {
        Connection conn = null;

        //1.JDBC Driver를 메모리로 로딩, DriverManger에 등록
        try {
            Class.forName("oracle.jdbc.OracleDriver");

            //연결하기
            conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/xe",
                    "java", "oracle");
            System.out.println("연결 성공");

            //DB작업
            String sql = new StringBuilder()
                    .append("UPDATE boards SET ")
                    .append("btitle =?, ")
                    .append("bcontent =? ")
                    .append("WHERE bno =?")
                    .toString();

            //PreparedStatement 열기 및 값 지정
/*            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "winter2");
            pstmt.setString(2, "한겨울");
            pstmt.setString(3, "12345");
            pstmt.setInt(4, 25);
            pstmt.setString(5, "winter2@company.com");*/

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "눈사람");
            pstmt.setString(2, "눈으로 만든 사람");
            pstmt.setInt(3, 3);

            //spl문 실행
            int rows =pstmt.executeUpdate();
            System.out.println("수정된 행 수 " + rows);
            pstmt.close();

        } catch (ClassNotFoundException e){
            e.printStackTrace();
        } catch (SQLException e ){
            e.printStackTrace();
        } finally {
            if (conn != null){
                try {
                    //연결 끊기
                    conn.close();
                    System.out.println("연결 끊김");
                } catch (SQLException e){}
            }
        }
    }
}
