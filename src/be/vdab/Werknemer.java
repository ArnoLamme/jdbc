package be.vdab;

import java.time.LocalDate;

public class Werknemer {
    private long id;
    private String voornaam;
    private String familienaam;
    private LocalDate geboorte;
    private LocalDate inDienst;
    
    public Werknemer(long id, String voornaam, String familienaam, LocalDate geboorte, LocalDate inDienst){
        this.id = id;
        this.voornaam = voornaam;
        this.familienaam = familienaam;
        this.geboorte = geboorte;
        this.inDienst = inDienst;
    }
    
    @Override
    public String toString(){
        return this.voornaam + " " + this.familienaam;
    }
    
    public boolean isJarig(){
        LocalDate vandaag = LocalDate.now();
        return this.geboorte.getMonth() == vandaag.getMonth() && this.geboorte.getDayOfMonth() == vandaag.getDayOfMonth();
    }
}