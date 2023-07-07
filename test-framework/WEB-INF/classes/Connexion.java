package emp;
import java.sql.Connection;

import java.sql.DriverManager;

public class Connexion {
    public Connection connexionPostgres(){
        Connection c = null;
        String host = "localhost:5432";
        String dbname = "framework";
        String url = "";
        try {
            url = "jdbc:postgresql://"+host+"/"+dbname+"";
            c = DriverManager.getConnection(url,"postgres","Grace1764");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return c;
    }
}
