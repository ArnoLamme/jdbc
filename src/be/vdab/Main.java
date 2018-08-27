package be.vdab;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    private static final String URL = "jdbc:mysql://localhost/tuincentrum?useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist";
    private static final String SELECT = "select id, naam from leveranciers order by id";
    
    public static void main(String[] args) {
        try(Connection connection  = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT)){
            //System.out.println("Connectie geopend");
            //System.out.println(statement.executeUpdate(UPDATE_PRIJS));
            while(resultSet.next()){
                System.out.println(resultSet.getInt("id") + " " + resultSet.getString("naam"));
            }
        }
        catch(SQLException ex){
            ex.printStackTrace(System.err);
        }
    }
}