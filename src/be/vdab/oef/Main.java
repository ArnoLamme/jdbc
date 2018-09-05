package be.vdab.oef;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    private static final String URL = "jdbc:mysql://localhost/bieren?useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist";
    private static final String QUERY = "select verkochtsinds, naam "
            + "from bieren "
            + "where {fn month(verkochtsinds)} = ? "
            + "order by verkochtsinds";
    
    public static void main(String[] args) {
//        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
//                Statement statement = connection.createStatement()){
//            System.out.println(statement.executeUpdate(QUERY));
//        }
//        catch(SQLException ex){
//            ex.printStackTrace(System.err);
//        }
//
//        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
//                Statement statement = connection.createStatement();
//                ResultSet resultSet = statement.executeQuery(QUERY)){
//            while(resultSet.next()){
//                System.out.println(resultSet.getString("brouwernaam") + " " + resultSet.getInt("aantalbieren"));
//            }
//        }
//        catch(SQLException ex){
//            ex.printStackTrace(System.err);
//        }
//
//        System.out.println("Alcohol tussen?");
//        Scanner sc = new Scanner(System.in);
//        int min = sc.nextInt();
//        int max = sc.nextInt();
//        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
//                PreparedStatement statement = connection.prepareStatement(QUERY)){
//            statement.setInt(1, min);
//            statement.setInt(2, max);
//            try(ResultSet resultSet = statement.executeQuery()){
//                while(resultSet.next()){
//                    System.out.println(resultSet.getString("naam") + " " + resultSet.getInt("alcohol"));
//                }
//            }
//        }
//        catch(SQLException ex){
//            ex.printStackTrace(System.err);
//        }
//
//        System.out.println("Alcohol tussen?");
//        Scanner sc = new Scanner(System.in);
//        int min = sc.nextInt();
//        int max = sc.nextInt();
//        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
//                CallableStatement statement = connection.prepareCall(QUERY)){
//            statement.setInt(1, min);
//            statement.setInt(2, max);
//            try(ResultSet resultSet = statement.executeQuery()){
//                while(resultSet.next()){
//                    System.out.println(resultSet.getString("naam") + " " + resultSet.getString("brouwernaam") + " " + resultSet.getInt("alcohol"));
//                }
//            }
//        }
//        catch(SQLException ex){
//            ex.printStackTrace(System.err);
//        }
//
//        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
//                Statement statement = connection.createStatement()){
//            statement.addBatch(QUERY);
//            statement.addBatch(QUERY2);
//            statement.addBatch(QUERY3);
//            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
//            connection.setAutoCommit(false);
//            statement.executeBatch();
//            connection.commit();
//        }
//        catch(SQLException ex){
//            ex.printStackTrace(System.err);
//        }

        System.out.println("Maand?");
        int maand = new Scanner(System.in).nextInt();
        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(QUERY)){
            statement.setInt(1, maand);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    System.out.println(resultSet.getDate("verkochtsinds") + " " + resultSet.getString("naam"));
                }
            }
            connection.commit();
        }
        catch(SQLException ex){
            ex.printStackTrace(System.err);
        }
    }
}