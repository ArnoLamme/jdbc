package be.vdab;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    private static final String URL = "jdbc:mysql://localhost/tuincentrum?useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist";
//    private static final String SELECT = "select id, voornaam, indienst from werknemers";
    
    public static void main(String[] args) {
        try(Connection connection  = DriverManager.getConnection(URL, USER, PASSWORD)){
            //System.out.println("Connectie geopend");
            //System.out.println(statement.executeUpdate(UPDATE_PRIJS));
            DatabaseMetaData metaData = connection.getMetaData();
            System.out.println(metaData.getDriverName() + " " + metaData.getDriverMajorVersion() + " " + metaData.getDriverMinorVersion());
        }
        catch(SQLException ex){
            ex.printStackTrace(System.err);
        }
    }
}