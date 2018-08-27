package be.vdab.oef;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    private static final String URL = "jdbc:mysql://localhost/bieren?useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist";
//    private static final String QUERY = "delete from bieren where alcohol is null";
    private static final String QUERY = "select brouwernaam, aantalbieren "
            + "from (select brouwers.naam as brouwernaam, count(bieren.id) as aantalbieren "
            + "from brouwers inner join bieren on brouwers.id = bieren.brouwerid "
            + "group by brouwers.id, brouwers.naam) as temp "
            + "where temp.aantalbieren > 0 "
            + "order by brouwernaam";
    
    public static void main(String[] args) {
//        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
//                Statement statement = connection.createStatement()){
//            System.out.println(statement.executeUpdate(QUERY));
//        }
//        catch(SQLException ex){
//            ex.printStackTrace(System.err);
//        }

        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(QUERY)){
            while(resultSet.next()){
                System.out.println(resultSet.getString("brouwernaam") + " " + resultSet.getInt("aantalbieren"));
            }
        }
        catch(SQLException ex){
            ex.printStackTrace(System.err);
        }
    }
}