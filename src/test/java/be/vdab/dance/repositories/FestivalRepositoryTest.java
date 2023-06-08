package be.vdab.dance.repositories;

import be.vdab.dance.domain.Festival;
import be.vdab.dance.exceptions.FestivalNietGevondenException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@JdbcTest
@Import(FestivalRepository.class)
@Sql("/festivals.sql")
class FestivalRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static final String FESTIVAL = "festivals";
    private final FestivalRepository festivalRepo;

    public FestivalRepositoryTest(FestivalRepository festivalRepo) {
        this.festivalRepo = festivalRepo;
    }

    @Test
    void findAll() {
        assertThat(festivalRepo.findAll()).hasSize(countRowsInTable(FESTIVAL))
                .extracting(Festival::getNaam)
                .isSortedAccordingTo(String::compareToIgnoreCase);
    }

    @Test
    void findUitverkocht() {
        assertThat(festivalRepo.findUitverkocht()).hasSize(countRowsInTableWhere(FESTIVAL, "ticketsBeschikbaar = 0"))
                .extracting(Festival::getNaam)
                .isSortedAccordingTo(String::compareToIgnoreCase);
    }

    private long idVanTestFestival1(){
        return jdbcTemplate.queryForObject("SELECT id FROM festivals WHERE naam = 'festival1'", Long.class);
    }
    @Test
    void delete() {
        var id = idVanTestFestival1();
        festivalRepo.delete(id);
        assertThat(countRowsInTableWhere(FESTIVAL, "id = " + id)).isZero();
    }

    @Test
    void create() {
        var id = idVanTestFestival1();
        festivalRepo.create(
                new Festival(0, "festival4", 4 , BigDecimal.TEN)
        );

        assertThat(id).isPositive();
        assertThat(countRowsInTableWhere(FESTIVAL, "id = " + id)).isOne();
    }

    @Test
    void findAndLockByid(){
        assertThat(festivalRepo.findAndLockById(idVanTestFestival1()))
                .hasValueSatisfying(festival -> assertThat(festival.getNaam()).isEqualTo("festival1"));
    }

    @Test
    void findAndLockByOnbestaandeIdVindtGeenFestival(){
        assertThat(festivalRepo.findAndLockById(Long.MAX_VALUE)).isEmpty();
    }

    @Test
    void findAantal(){
        assertThat(festivalRepo.findAantal()).isEqualTo(countRowsInTable(FESTIVAL));
    }

    @Test
    void verhoogBudget(){
        festivalRepo.verhoogBudget(BigDecimal.TEN);
        var id = idVanTestFestival1();
        assertThat(countRowsInTableWhere(FESTIVAL, "reclameBudget = 110 AND id = " + id)).isOne();
    }

    @Test
    void updateTickets() {
        var id = idVanTestFestival1();
        var festival = new Festival(id, "abcdef", 3, BigDecimal.TEN);

        festivalRepo.update(festival);
        assertThat(countRowsInTableWhere(FESTIVAL, "ticketsBeschikbaar = 3 AND id = "+ id)).isOne();
    }

    @Test
    void updateTicketsOnbestaandFestivalGeeftEenFout() {
        assertThatExceptionOfType(FestivalNietGevondenException.class).isThrownBy(
                () -> festivalRepo.update(new Festival(Long.MAX_VALUE, "test", 1, BigDecimal.TEN)));
    }
}




















