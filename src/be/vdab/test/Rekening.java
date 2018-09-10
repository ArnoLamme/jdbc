package be.vdab.test;

import java.math.BigDecimal;

public class Rekening {
    private final long rekeningNr;
    private BigDecimal saldo;
    
    public Rekening(long rekeningNr){
        this.rekeningNr = rekeningNr;
        this.saldo = BigDecimal.ZERO;
    }
    
    public Rekening(long rekeningNr, BigDecimal saldo){
        this.rekeningNr = rekeningNr;
        this.saldo = saldo;
    }

    public long getRekeningNr() {
        return rekeningNr;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }
    
    public void setSaldo(BigDecimal saldo){
        this.saldo = saldo;
    }
    
    public static boolean valideerRekening(long reknr){
        if(reknr > 0 && (int) Math.log10(reknr) + 1 == 12){ //controleer lengte
            long eerste10 = reknr / 100;
            long laatste2 = reknr % 100;
            return eerste10 % 97 == laatste2;
        }
        return false;
    }
    
    public void overschrijving(Rekening rekening, BigDecimal bedrag) throws Exception{
        if(this.saldo.compareTo(bedrag) >= 0){
            rekening.setSaldo(rekening.getSaldo().add(bedrag));
            this.setSaldo(this.saldo.subtract(bedrag));
        }
        else{
            throw new Exception("Ongeldige overschrijving - bedrag te groot");
        }
    }
}
