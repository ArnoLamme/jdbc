package be.vdab.oef;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
    private static final String URL = "jdbc:mysql://localhost/bieren?useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist";
    private static final String UPDATE = "update brouwers "
            + "set omzet = NULL "
            + "where id in (";
    private static final String QUERY = "select id "
            + "from brouwers";
    
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
//
//        System.out.println("Maand?");
//        int maand = new Scanner(System.in).nextInt();
//        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
//                PreparedStatement statement = connection.prepareStatement(QUERY)){
//            statement.setInt(1, maand);
//            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
//            connection.setAutoCommit(false);
//            try(ResultSet resultSet = statement.executeQuery()){
//                while(resultSet.next()){
//                    System.out.println(resultSet.getDate("verkochtsinds") + " " + resultSet.getString("naam"));
//                }
//            }
//            connection.commit();
//        }
//        catch(SQLException ex){
//            ex.printStackTrace(System.err);
//        }
//
//
//        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
//                Statement statement = connection.createStatement()){
//            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
//            connection.setAutoCommit(false);
//            try(ResultSet resultSet = statement.executeQuery(QUERY)){
//                while(resultSet.next()){
//                    System.out.println(resultSet.getString("brouwernaam") + " " + resultSet.getInt("aantalbieren") + " bier(en)");
//                }
//            }
//            connection.commit();
//        }
//        catch(SQLException ex){
//            ex.printStackTrace(System.err);
//        }
//
//        System.out.println("Biersoort?");
//        String soort = new Scanner(System.in).nextLine();
//        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
//                PreparedStatement statement = connection.prepareStatement(QUERY);
//                PreparedStatement controle = connection.prepareStatement(QUERY2)){
//            statement.setString(1, soort);
//            controle.setString(1, soort);
//            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
//            connection.setAutoCommit(false);
//            try(ResultSet resultSet = statement.executeQuery()){
//                if(resultSet.next()){
//                    System.out.println(resultSet.getString("bier"));
//                    while(resultSet.next()){
//                        System.out.println(resultSet.getString("bier"));
//                    }
//                }
//                else{
//                    try(ResultSet controleSet = controle.executeQuery()){
//                        if(!controleSet.next()){
//                            System.out.println("FOUT: soort bestaat niet");
//                        }
//                    }
//                }
//            }
//            connection.commit();
//        }
//        catch(SQLException ex){
//            ex.printStackTrace(System.err);
//        }

        System.out.println("Typ brouwernummers, 0 om te eindigen");
        Scanner sc =  new Scanner(System.in);
        int nummer = sc.nextInt();
        Set<Integer> nummers = new LinkedHashSet<>();
        while(nummer != 0){
            if(nummer < 0){
                System.out.println("FOUT: negatief getal");
            }
            else if(!nummers.add(nummer)){
                System.out.println("FOUT: getal reeds ingevoerd");
            }
            nummer = sc.nextInt();
        }
        StringBuilder queryBuilder = new StringBuilder(UPDATE);
        if(nummers.size() > 0){
            nummers.stream().forEach(input -> {queryBuilder.append(input);
                    queryBuilder.append(", ");
            });
            queryBuilder.delete(queryBuilder.length() - 2, queryBuilder.length());
        }
        queryBuilder.append(")");
        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement statement = connection.createStatement()){
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            statement.executeUpdate(queryBuilder.toString());
        }
        catch(SQLException ex){
            ex.printStackTrace(System.err);
        }
    }
}