import java.sql.*; 

/**
 * MySQLGen
 */
public class MySQLGen {
    final static String StringDriver="com.mysql.cj.jdbc.Driver";
    final static String StringConnection="jdbc:mysql://localhost:3306/tugas_clustering"; 
    static final String StringConnectionUser = "root";
    static final String StringConnectionPass = "";

    public static void main(String[] args) {
        Connection conn = null;

        try{
            Class.forName(StringDriver);
        } catch (Exception ex){
            System.out.println("SQLException: " + ex.getMessage());
        }
        try {

            conn =  DriverManager.getConnection(StringConnection, StringConnectionUser, StringConnectionPass);

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

        conn = null;
    }

}