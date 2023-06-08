package be.vdab.dance.console;

import be.vdab.dance.domain.Boeking;
import be.vdab.dance.exceptions.FestivalNietGevondenException;
import be.vdab.dance.exceptions.OnvoldoendeTicketsException;
import be.vdab.dance.services.BoekingService;
import be.vdab.dance.services.FestivalService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class MyRunner implements CommandLineRunner {

    private final FestivalService festivalService;
    private final BoekingService boekingService;

    public MyRunner(FestivalService festivalService, BoekingService boekingService) {
        this.festivalService = festivalService;
        this.boekingService = boekingService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Uitverkochte festivals:");
        festivalService.findUitverkocht()
                .forEach(festival -> System.out.println(festival.getNaam()));

        var scanner= new Scanner(System.in);
        /*System.out.print("Het id van het Festival dat je wilt annuleren:");
        var cancelFestival = scanner.nextLong();

        try {
            festivalService.cancel(cancelFestival);
            System.out.println("Festival geannuleerd");
        }catch (IllegalArgumentException e){
            System.err.println(e.getMessage());
        }catch (FestivalNietGevondenException e){
            System.err.println("Festival niet gevonden. Id:" + e.getId());
        }*/

        System.out.print("Geef je naam in: ");
        var naam = scanner.nextLine();
        System.out.print("Hoeveel tickets wil je? ");
        var amount = scanner.nextInt();
        System.out.print("Geef de ID van het festival dat je wilt bezoeken: ");
        var fesId = scanner.nextLong();

        try {
            var boeking = new Boeking(naam, amount, fesId);
            boekingService.create(boeking);
            System.out.println("Boeking aangemaakt");

        }catch (IllegalArgumentException e){
            System.err.println(e.getMessage());
        }catch (OnvoldoendeTicketsException e){
            System.err.println("Onvoldoende tickets beschikbaar voor dit festival");

        }catch (FestivalNietGevondenException e){
            System.err.println("Festival met ID: " + e.getId() + " niet gevonden");
        }
    }
}
