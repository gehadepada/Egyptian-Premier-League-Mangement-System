package Utils;
import java.sql.*;

public class SqliteConnection {
    
    public static Connection Connector(){
        
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(Consts.url);
            return conn;
            
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
            return null;
            
        }
        
    }
    
    
}
