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
    private static final String SELECT = "select id from soorten where naam = ?";
    private static final String INSERT = "insert into soorten(naam) values (?)";
    
    public static void main(String[] args) {
        System.out.println("Soortnaam:");
        String soortNaam = new Scanner(System.in).nextLine();
        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statementSelect = connection.prepareStatement(SELECT);
                PreparedStatement statementInsert = connection.prepareStatement(INSERT)){
            statementSelect.setString(1, soortNaam);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            connection.setAutoCommit(false);
            try(ResultSet resultSet = statementSelect.executeQuery()){
                if(resultSet.next()){
                    System.out.println("Soort met deze naam bestaat al");
                }
                else{
                    statementInsert.setString(1, soortNaam);
                    statementInsert.executeUpdate();
                    connection.commit();
                }
            }
        }
        catch(SQLException ex){
            ex.printStackTrace(System.err);
        }
    }
}