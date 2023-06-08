package be.vdab.dance.domain;

import be.vdab.dance.exceptions.OnvoldoendeBudgetException;
import be.vdab.dance.exceptions.OnvoldoendeTicketsException;

import java.math.BigDecimal;

public class Festival {
    private final long id;
    private final String naam;
    private long ticketsBeschikbaar;
    private BigDecimal reclameBudget;

    public Festival(long id, String naam, long ticketsBeschikbaar, BigDecimal reclameBudget) {
        this.id = id;
        this.naam = naam;
        this.ticketsBeschikbaar = ticketsBeschikbaar;
        this.reclameBudget = reclameBudget;
    }

    public long getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }

    public long getTicketsBeschikbaar() {
        return ticketsBeschikbaar;
    }

    public BigDecimal getReclameBudget() {
        return reclameBudget;
    }

    public void addReclameBudget(BigDecimal budget){
        if(reclameBudget.compareTo(budget) <= 0){
            throw new OnvoldoendeBudgetException("Onvoldoende budget beschikbaar");
        }
        reclameBudget= reclameBudget.add(budget);
    }

    public void boek(int tickets){
        if(tickets<=0){
            throw new IllegalArgumentException();
        }
        if(tickets > ticketsBeschikbaar){
            throw new OnvoldoendeTicketsException("Onvoldoende tickets beschikbaar");
        }
        ticketsBeschikbaar = ticketsBeschikbaar - tickets;
    }
}
