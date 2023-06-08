package be.vdab.dance.services;

import be.vdab.dance.domain.Boeking;
import be.vdab.dance.exceptions.FestivalNietGevondenException;
import be.vdab.dance.repositories.BoekingRepository;
import be.vdab.dance.repositories.FestivalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BoekingService {
    private final BoekingRepository boekingRepo;
    private final FestivalRepository festivalRepo;
    public BoekingService(BoekingRepository boekingRepo, FestivalRepository festivalRepo) {
        this.boekingRepo = boekingRepo;
        this.festivalRepo = festivalRepo;
    }

    @Transactional
    public void create(Boeking boeking){
        var festival = festivalRepo.findAndLockById(boeking.getFestivalId())
                .orElseThrow(() -> new FestivalNietGevondenException(boeking.getFestivalId()));
        festival.boek(boeking.getAantalTickets());
        boekingRepo.create(boeking);
        festivalRepo.update(festival);
    }
}
