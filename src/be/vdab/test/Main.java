package be.vdab.test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    private static final String URL = "jdbc:mysql://localhost/bank?useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist";
    
    public static void main(String[] args) {
        System.out.println("Kies een optie.");
        System.out.println("1 - Nieuwe rekening");
        System.out.println("2 - Saldo consulteren");
        System.out.println("3 - Overschrijven");
        Scanner sc = new Scanner(System.in);
        int optie = sc.nextInt();
        switch(optie){
            case 1: 
                System.out.println("Geef een nieuw rekeningnummer");
                long nieuwnr = sc.nextLong();
                if(valideerRekening(nieuwnr)){
                    System.out.println("geldig rekeningnummer");
                    maakNieuweRekening(nieuwnr);
                }
                else{
                    System.out.println("ongeldig rekeningnummer");
                }
                break;
                
            case 2:
                System.out.println("Geef een rekeningnummer op:");
                long reknr = sc.nextLong();
                vraagSaldo(reknr);
                break;
                
            case 3:
                System.out.println("Overschrijven van?");
                long rekVan = sc.nextLong();
                System.out.println("naar?");
                long rekNaar = sc.nextLong();
                System.out.println("bedrag?");
                BigDecimal bedrag = sc.nextBigDecimal();
                overschrijving(rekVan, rekNaar, bedrag);
                break;
                
            default:
                System.out.println("Ongeldige optie");
                break;
        }
    }
    
    public static boolean valideerRekening(long reknr){
        if(reknr > 0 && (int) Math.log10(reknr) + 1 == 12){ //controleer lengte
            long eerste10 = reknr / 100;
            long laatste2 = reknr % 100;
            return eerste10 % 97 == laatste2;
        }
        return false;
    }
    
    private static void maakNieuweRekening(long reknr){
        String query = "insert into rekeningen (rekeningnr) "
                + "values (?)";
        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(query)){
            statement.setLong(1, reknr);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            connection.setAutoCommit(false);
            statement.executeUpdate();
            connection.commit();
        }
        catch(SQLException ex){
            ex.printStackTrace(System.err);
        }
    }
    
    private static void vraagSaldo(long reknr){
        String query = "select saldo "
                + "from rekeningen "
                + "where rekeningnr = ?";
        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(query)){
            statement.setLong(1, reknr);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    System.out.println("Saldo: " + resultSet.getBigDecimal("saldo"));
                }
                else{
                    System.out.println("Rekeningnummer niet in database");
                }
            }
        }
        catch(SQLException ex){
            ex.printStackTrace(System.err);
        }
    }
    
    private static void overschrijving(long rekVan, long rekNaar, BigDecimal bedrag){
        if(rekVan != rekNaar && bedrag.compareTo(BigDecimal.ZERO) > 0){
            String query = "select saldo "
                    + "from rekeningen "
                    + "where rekeningnr = ?";
            String update = "update rekeningen "
                    + "set saldo = saldo + ? "
                    + "where rekeningnr = ?";
            
            try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                    PreparedStatement queryStatement = connection.prepareStatement(query);
                    PreparedStatement updateStatement = connection.prepareStatement(update)){
                connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                connection.setAutoCommit(false);
                queryStatement.setLong(1, rekVan);
                try(ResultSet resultSet = queryStatement.executeQuery()){
                    if(resultSet.next()){
                        if(resultSet.getBigDecimal("saldo").compareTo(bedrag) <= 0){
                            throw new Exception("Ongeldige overschrijving - te groot bedrag");
                        }
                    }
                    else{
                        throw new Exception("Ongeldige overschrijving - rekeningnummer bestaat niet");
                    }
                }
                queryStatement.setLong(1, rekNaar);
                try(ResultSet resultSet = queryStatement.executeQuery()){
                    if(!resultSet.next()){
                        throw new Exception("Ongeldige overschrijving - rekeningnummer bestaat niet");
                    }
                }
                updateStatement.setBigDecimal(1, bedrag.negate());
                updateStatement.setLong(2, rekVan);
                updateStatement.execute();
                updateStatement.setBigDecimal(1, (bedrag));
                updateStatement.setLong(2, rekNaar);
                updateStatement.execute();
                connection.commit();
            }
            catch(SQLException ex){
                ex.printStackTrace(System.err);
            }
            catch(Exception ex){
                System.out.println(ex.getMessage());
            }
        }
        else{
            System.out.println("Ongeldige overschrijving");
        }
    }
}