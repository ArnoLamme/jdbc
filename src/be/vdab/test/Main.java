package be.vdab.test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
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
        int optie = 0;  //optie initialiseren
        try{
            optie = sc.nextInt();   //input lezen
        }
        catch(InputMismatchException ex){
        }
        System.out.println();
        
        switch(optie){
            case 1: //nieuwe rekening
                System.out.println("Geef een nieuw rekeningnummer");
                try{
                    maakNieuweRekening(sc.nextLong());
                }
                catch(InputMismatchException ex){
                    System.out.println("Ongeldige input");
                }
                break;
                
            case 2: //saldo consulteren
                System.out.println("Geef het rekeningnummer:");
                try{
                    vraagSaldo(sc.nextLong());
                }
                catch(InputMismatchException ex){
                    System.out.println("Ongeldige input");
                }
                break;
                
            case 3: //overschrijving
                try{
                    System.out.println("Overschrijven van?");
                    long rekVan = sc.nextLong();
                    System.out.println("naar?");
                    long rekNaar = sc.nextLong();
                    System.out.println("bedrag?");
                    BigDecimal bedrag = sc.nextBigDecimal();
                    overschrijving(rekVan, rekNaar, bedrag);
                }
                catch(InputMismatchException ex){
                    System.out.println("Ongeldige input");
                }
                break;
                
            default:
                System.out.println("Ongeldige input");
                break;
        }
    }
    
    private static void maakNieuweRekening(long reknr){
        if(Rekening.valideerRekening(reknr)){
            System.out.println("Geldig rekeningnummer");
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
        else{
            System.out.println("Ongeldig rekeningnummer");
        }
    }
    
    private static void vraagSaldo(long reknr){
        if(Rekening.valideerRekening(reknr)){
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
        else{
            System.out.println("Ongeldig rekeningnummer");
        }
    }
    
    private static void overschrijving(long rekVan, long rekNaar, BigDecimal bedrag){
        if(Rekening.valideerRekening(rekVan) && Rekening.valideerRekening(rekNaar) && rekVan != rekNaar && bedrag.compareTo(BigDecimal.ZERO) > 0){ //controle op geldige parameters
            String query = "select saldo "  //queries
                    + "from rekeningen "
                    + "where rekeningnr = ?";
            String update = "update rekeningen "
                    + "set saldo = ? "
                    + "where rekeningnr = ?";
            
            try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                    PreparedStatement queryStatement = connection.prepareStatement(query);
                    PreparedStatement updateStatement = connection.prepareStatement(update)){
                connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                connection.setAutoCommit(false);
                Rekening rekeningVan = new Rekening(rekVan);
                Rekening rekeningNaar = new Rekening(rekNaar);
                
                queryStatement.setLong(1, rekVan);
                try(ResultSet resultSet = queryStatement.executeQuery()){
                    if(resultSet.next()){
                        rekeningVan.setSaldo(resultSet.getBigDecimal("saldo"));
                    }
                    else{
                        throw new Exception("Ongeldige overschrijving - rekeningnummer bestaat niet");
                    }
                }
                
                queryStatement.setLong(1, rekNaar);
                try(ResultSet resultSet = queryStatement.executeQuery()){
                    if(resultSet.next()){
                        rekeningNaar.setSaldo(resultSet.getBigDecimal("saldo"));
                    }
                    else{
                        throw new Exception("Ongeldige overschrijving - rekeningnummer bestaat niet");
                    }
                }
                
                rekeningVan.overschrijving(rekeningNaar, bedrag);
                updateStatement.setBigDecimal(1, rekeningVan.getSaldo());
                updateStatement.setLong(2, rekeningVan.getRekeningNr());
                updateStatement.execute();
                updateStatement.setBigDecimal(1, rekeningNaar.getSaldo());
                updateStatement.setLong(2, rekeningNaar.getRekeningNr());
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