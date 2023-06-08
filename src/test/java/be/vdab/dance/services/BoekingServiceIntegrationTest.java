package be.vdab.dance.services;

import be.vdab.dance.domain.Boeking;
import be.vdab.dance.exceptions.FestivalNietGevondenException;
import be.vdab.dance.exceptions.OnvoldoendeTicketsException;
import be.vdab.dance.repositories.BoekingRepository;
import be.vdab.dance.repositories.FestivalRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@JdbcTest
@Sql("/festivals.sql")
@Import({BoekingService.class, BoekingRepository.class, FestivalRepository.class})
public class BoekingServiceIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {

    private static final String BOEKINGEN = "boekingen";
    private static final String FESTIVALS = "festivals";
    private final BoekingService boekingService;
    BoekingServiceIntegrationTest(BoekingService boekingService) {
        this.boekingService = boekingService;
    }

    private long idVanTestFes1(){
        return jdbcTemplate.queryForObject("SELECT id FROM festivals WHERE naam = 'festival1'", Long.class);
    }

    @Test
    void create() {
        var fesId = idVanTestFes1();
        boekingService.create(new Boeking("test", 1, fesId));

        assertThat(countRowsInTableWhere(BOEKINGEN, "naam = 'test' AND aantalTickets = 1 AND festivalId = " +  fesId)).isOne();
        assertThat(countRowsInTableWhere(FESTIVALS, "id = " + fesId + " AND ticketsBeschikbaar = 9")).isOne();
    }

    @Test
    void boekingMetTeVeelTicketsFaalt() {
        var fesId = idVanTestFes1();
        assertThatExceptionOfType(OnvoldoendeTicketsException.class).isThrownBy(
                () -> boekingService.create(new Boeking("test", Integer.MAX_VALUE, fesId)));
    }

    @Test
    void BoekingMisluktVoorOnbestaandeFestival() {
        assertThatExceptionOfType(FestivalNietGevondenException.class).isThrownBy(
                () -> boekingService.create(new Boeking("test", 1, Integer.MAX_VALUE)));
    }

    @Test
    void BoekingZonderNaamMislukt() {
        var fesId = idVanTestFes1();
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                () -> boekingService.create(new Boeking("", 1, fesId)));
    }

    @Test
    void BoekingZonderTicketsMislukt() {
        var fesId = idVanTestFes1();
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                () -> boekingService.create(new Boeking("", 0, fesId)));
    }

    @Test
    void BoekingNegatieveTicketsMislukt() {
        var fesId = idVanTestFes1();
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                () -> boekingService.create(new Boeking("test", -2, fesId)));
    }
}
