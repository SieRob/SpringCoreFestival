package be.vdab.dance.services;

import be.vdab.dance.domain.Festival;
import be.vdab.dance.exceptions.FestivalNietGevondenException;
import be.vdab.dance.repositories.FestivalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class FestivalService {
    private final FestivalRepository festivalRepo;

    public FestivalService(FestivalRepository festivalRepo) {
        this.festivalRepo = festivalRepo;
    }

    public List<Festival> findAll(){
        return festivalRepo.findAll();
    }

    public List<Festival> findUitverkocht(){
        return festivalRepo.findUitverkocht();
    }

    public void delete(long id){
        festivalRepo.delete(id);
    }

    public long create(Festival festival){
        return festivalRepo.create(festival);
    }
    @Transactional
    public void cancel(long festivalId){
        var festival = festivalRepo.findAndLockById(festivalId)
                .orElseThrow(() -> new FestivalNietGevondenException(festivalId));
        var teVerdelen = festival.getReclameBudget();

        festivalRepo.delete(festivalId);

        var aantalResterendeFestivals = festivalRepo.findAantal();
        var exrtareclamebudgetPerFestival = teVerdelen.divide(BigDecimal.valueOf(aantalResterendeFestivals), 2 , RoundingMode.HALF_UP);
        festivalRepo.verhoogBudget(exrtareclamebudgetPerFestival);
    }
}


















