package be.vdab.oef;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    private static final String URL = "jdbc:mysql://localhost/bieren?useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist";
    private static final String QUERY = "update bieren "
            + "set brouwerid = 2 "
            + "where brouwerid = 1 and alcohol >= 8.5";
    private static final String QUERY2 = "update bieren "
            + "set brouwerid = 3 "
            + "where brouwerid = 1";
    private static final String QUERY3 = "delete from brouwers "
            + "where id = 1";
    
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

        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement statement = connection.createStatement()){
            statement.addBatch(QUERY);
            statement.addBatch(QUERY2);
            statement.addBatch(QUERY3);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            statement.executeBatch();
            connection.commit();
        }
        catch(SQLException ex){
            ex.printStackTrace(System.err);
        }
    }
}