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
    private static final String SELECT = "select id, voornaam, familienaam, geboorte, indienst "
            + "from werknemers";
    
    public static void main(String[] arrays){
        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement statement = connection.createStatement()){
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            try(ResultSet resultSet = statement.executeQuery(SELECT)){
                while(resultSet.next()){
                    Werknemer werknemer = new Werknemer(resultSet.getLong("id"), resultSet.getString("voornaam"), resultSet.getString("familienaam"), resultSet.getDate("geboorte").toLocalDate(), resultSet.getDate("indienst").toLocalDate());
                    System.out.print(werknemer);
                    if(werknemer.isJarig()){
                        System.out.print(" JARIG");
                    }
                    System.out.println();
                }
            }
            connection.commit();
        }
        catch(SQLException ex){
            ex.printStackTrace(System.err);
        }
    }
}
