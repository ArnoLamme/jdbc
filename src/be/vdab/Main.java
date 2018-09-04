package be.vdab;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    private static final String URL = "jdbc:mysql://localhost/tuincentrum?useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist";
    private static final String INSERT = "insert into soorten(naam) values(?)";
    
    public static void main(String[] args) {
        System.out.println("Naam:");
        String naam = new Scanner(System.in).nextLine();
        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)){
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            statement.setString(1, naam);
            connection.setAutoCommit(false);
            statement.executeUpdate();
            try(ResultSet resultSet = statement.getGeneratedKeys()){
                resultSet.next();
                System.out.println(resultSet.getLong(1));
                connection.commit();
            }
        }
        catch(SQLException ex){
            ex.printStackTrace(System.err);
        }
    }
}