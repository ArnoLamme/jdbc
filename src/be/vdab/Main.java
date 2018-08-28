package be.vdab;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    private static final String URL = "jdbc:mysql://localhost/tuincentrum?useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist";
    private static final String SELECT = "select id, voornaam, indienst from werknemers";
    
    public static void main(String[] args) {
        try(Connection connection  = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(SELECT)){
            ResultSetMetaData metaData = resultSet.getMetaData();
            for(int index = 1; index <= metaData.getColumnCount(); index++){
                System.out.println(metaData.getColumnName(index) + " " + metaData.getColumnTypeName(index));
            }
        }
        catch(SQLException ex){
            ex.printStackTrace(System.err);
        }
    }
}