package be.vdab.oef;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    private static final String URL = "jdbc:mysql://localhost/bieren?useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist";
    private static final String QUERY = "delete from bieren where alcohol is null";
    
    public static void main(String[] args) {
        try(Connection connection  = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement statement = connection.createStatement()){
            System.out.println(statement.executeUpdate(QUERY));
        }
        catch(SQLException ex){
            ex.printStackTrace(System.err);
        }
    }
}