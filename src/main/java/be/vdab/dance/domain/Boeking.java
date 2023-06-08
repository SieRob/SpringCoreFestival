package be.vdab.dance.domain;

import java.math.BigDecimal;

public class Boeking {
    private final long id;
    private final String naam;
    private final int aantalTickets;
    private final long festivalId;

    public Boeking(String naam, int aantalTickets, long festivalId) {
        //boeking aanmaken door gebruiker
        if(naam.isBlank()){
            throw new IllegalArgumentException("Naam mag niet leeg zijn");
        }
        if(aantalTickets <= 0){
            throw new IllegalArgumentException("Aantal tickets moet een positief getal zijn");
        }
        if(festivalId <= 0){
            throw new IllegalArgumentException("Het festival id moet een positief getal zijn");
        }
        this.id = 0;
        this.naam = naam;
        this.aantalTickets = aantalTickets;
        this.festivalId = festivalId;
    }

    public Boeking(long id, String naam, int aantalTickets, long festivalId) {
        //Boeking lezen uit database
        this.id = id;
        this.naam = naam;
        this.aantalTickets = aantalTickets;
        this.festivalId = festivalId;
    }

    public long getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }

    public int getAantalTickets() {
        return aantalTickets;
    }

    public long getFestivalId() {
        return festivalId;
    }
}
