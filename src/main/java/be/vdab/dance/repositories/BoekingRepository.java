package be.vdab.dance.repositories;

import be.vdab.dance.domain.Boeking;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BoekingRepository {
    private final JdbcTemplate template;
    public BoekingRepository(JdbcTemplate template) {
        this.template = template;
    }

    public void create(Boeking boeking){
        var sql = """
                INSERT INTO boekingen (naam, aantalTickets, festivalId)
                VALUES (?,?,?)
                """;
        template.update(sql, boeking.getNaam(), boeking.getAantalTickets(), boeking.getFestivalId());
    }
}
