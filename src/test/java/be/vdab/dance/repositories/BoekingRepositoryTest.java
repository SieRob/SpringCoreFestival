package be.vdab.dance.repositories;

import be.vdab.dance.domain.Boeking;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import(BoekingRepository.class)
@Sql("/festivals.sql")
class BoekingRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static final String BOEKINGEN = "boekingen";
    private final BoekingRepository boekingRepo;

    public BoekingRepositoryTest(BoekingRepository boekingRepo) {
        this.boekingRepo = boekingRepo;
    }

    private long idVanTestFestival1(){
        return jdbcTemplate.queryForObject(
                "SELECT id FROM festivals WHERE naam = 'festival1'", Long.class
        );
    }

    @Test
    void create() {
        var fesId = idVanTestFestival1();
        boekingRepo.create(new Boeking(0,"test", 2 , fesId));
        assertThat(countRowsInTableWhere(BOEKINGEN, "naam = 'test' AND aantalTickets = 2 AND festivalId = " + fesId)).isOne();
    }
}