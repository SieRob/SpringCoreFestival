package be.vdab.dance.repositories;

import be.vdab.dance.domain.Festival;
import be.vdab.dance.exceptions.FestivalNietGevondenException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class FestivalRepository {
    private final JdbcTemplate template;
    public FestivalRepository(JdbcTemplate template) {
        this.template = template;
    }

    private final RowMapper<Festival> festivalMapper =
            (rs, rowNum) -> new Festival(
                    rs.getLong("id"),
                    rs.getString("naam"),
                    rs.getInt("ticketsBeschikbaar"),
                    rs.getBigDecimal("reclameBudget")
            );

    public List<Festival> findAll(){
        var sql = """
                SELECT id, naam, ticketsBeschikbaar, reclameBudget
                FROM festivals
                ORDER BY naam
                """;
        return template.query(sql, festivalMapper);
    }

    public List<Festival> findUitverkocht(){
        var sql = """
                SELECT id, naam, ticketsBeschikbaar, reclameBudget
                FROM festivals
                WHERE ticketsBeschikbaar=0
                ORDER BY naam
                """;

        return template.query(sql, festivalMapper);
    }

    public void delete (long id){
        var sql = """
                DELETE FROM festivals
                WHERE id = ?
                """;

        template.update(sql, id);
    }

    public long create(Festival festival){
        var sql = """
                INSERT INTO festivals(naam, ticketsBeschikbaar, reclameBudget)
                VALUES(?,?,?)
                """;
        var keyHolder = new GeneratedKeyHolder();
        template.update(con -> {
            var stmt = con.prepareStatement(sql,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, festival.getNaam());
            stmt.setLong(2, festival.getTicketsBeschikbaar());
            stmt.setBigDecimal(3, festival.getReclameBudget());
            return stmt;
        },keyHolder);
        return keyHolder.getKey().longValue();
    }

    public Optional<Festival> findAndLockById (long id){
        try {
            var sql = """
                    SELECT id, naam, ticketsBeschikbaar, reclameBudget
                    FROM festivals
                    WHERE id = ?
                    FOR UPDATE
                    """;
            return Optional.of(template.queryForObject(sql, festivalMapper, id));
        }catch (IncorrectResultSizeDataAccessException e){
            return Optional.empty();
        }
    }

    public long findAantal(){
        var sql = """
                SELECT COUNT(*)
                FROM festivals
                """;

        return template.queryForObject(sql, Long.class);
    }

    public void verhoogBudget (BigDecimal bedrag){
        var sql = """
                UPDATE festivals
                SET reclameBudget = reclameBudget + ?
                """;
        template.update(sql, bedrag);
    }

    public void update(Festival festival){
        var sql = """
                UPDATE festivals
                SET naam = ?, ticketsBeschikbaar = ?
                WHERE id =?
                """;
        if(template.update(sql, festival.getNaam(), festival.getTicketsBeschikbaar(), festival.getId()) == 0){
            throw new FestivalNietGevondenException(festival.getId());
        }
    }
}
