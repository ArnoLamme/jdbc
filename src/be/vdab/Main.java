package be.vdab;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    private static final String URL = "jdbc:mysql://localhost/tuincentrum?useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist";
    private static final String SELECT = "select naam from leveranciers where woonplaats = ?";
    
    public static void main(String[] args) {
        System.out.println("Woonplaats:");
        String woonplaats = new Scanner(System.in).nextLine();
        try(Connection connection  = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement statement = connection.prepareStatement(SELECT)){
            //System.out.println("Connectie geopend");
            //System.out.println(statement.executeUpdate(UPDATE_PRIJS));
            statement.setString(1, woonplaats);
            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    System.out.println(resultSet.getString("naam"));
                }
            }
        }
        catch(SQLException ex){
            ex.printStackTrace(System.err);
        }
    }
}