package be.vdab;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    private static final String URL = "jdbc:mysql://localhost/tuincentrum?useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist";
    private static final String SELECT = "select indienst, voornaam, familienaam "
            + "from werknemers "
            + "where indienst >= ? "
            + "order by indienst";
    
    public static void main(String[] args) {
        System.out.println("Datum vanaf (dd/mm/yyyy):");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/y");
        LocalDate datum = LocalDate.parse(new Scanner(System.in).nextLine(), formatter);
        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(SELECT)){
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            statement.setDate(1, java.sql.Date.valueOf(datum));
            connection.setAutoCommit(false);
            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    System.out.println(resultSet.getDate("indienst") + " " + resultSet.getString("voornaam") + " " + resultSet.getString("familienaam"));
                }
            }
            connection.commit();
        }
        catch(SQLException ex){
            ex.printStackTrace(System.err);
        }
    }
}