package be.vdab;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    private static final String URL = "jdbc:mysql://localhost/tuincentrum?useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist";
    private static final String UPDATE_PRIJS = "update planten set verkoopprijs = verkoopprijs * 1.1";
    
    public static void main(String[] args) {
        try(Connection connection  = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement statement = connection.createStatement()){
            //System.out.println("Connectie geopend");
            System.out.println(statement.executeUpdate(UPDATE_PRIJS));
        }
        catch(SQLException ex){
            ex.printStackTrace(System.err);
        }
    }
}