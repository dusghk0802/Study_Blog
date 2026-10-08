package jdbc.select;

import java.sql.*;

public class UserinsertExample {
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
            String sql = ""+
                   "SELECT userid, username, userpassword, userage, useremail "+
                    "FROM users "+
                    "WHERE userid =?";


            //PreparedStatement 열기 및 값 지정
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "winter2");

            //spl문 실행
            ResultSet rs =pstmt.executeQuery();
            if (rs.next()){
                User user = new User();
                user.setUserId(rs.getString("userid"));
                user.setUserName(rs.getString("userName"));
                user.setUserPassword(rs.getString("userpassword"));
                user.setUserAge(rs.getInt(4));
                user.setUserEmail(rs.getString(5));
                System.out.println(user);
            } else {
                System.out.println("사용자 아이디가 존재하지 않습니다.");
            }
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
