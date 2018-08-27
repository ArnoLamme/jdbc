package be.vdab;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    private static final String URL = "jdbc:mysql://localhost/tuincentrum/useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "vdab";
    
    public static void main(String[] args) {
        try(Connection connection  = DriverManager.getConnection(URL, USER, PASSWORD)){
            System.out.println("Connectie geopend");
        }
        catch(SQLException ex){
            ex.printStackTrace(System.err);
        }
    }
}