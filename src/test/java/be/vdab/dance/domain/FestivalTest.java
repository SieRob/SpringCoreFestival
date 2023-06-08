package be.vdab.dance.domain;

import be.vdab.dance.exceptions.OnvoldoendeTicketsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class FestivalTest {

    private Festival fes;

    @BeforeEach
    void beforeEach(){
        fes = new Festival(1,"Test", 100, BigDecimal.TEN);
    }

    @Test
    void boek() {
        fes.boek(100);
        assertThat(fes.getTicketsBeschikbaar()).isZero();
    }

    @Test
    void nulTicketsBoekenMislukt(){
        assertThatIllegalArgumentException().isThrownBy(() -> fes.boek(0));
    }

    @Test
    void negatiefAantalTicketsBoekenMislukt() {
        assertThatIllegalArgumentException().isThrownBy(() -> fes.boek(-5));
    }

    @Test
    void boekenMisluktBijonvoldoendeTicketsBeschikbaar() {
        assertThatExceptionOfType(OnvoldoendeTicketsException.class).isThrownBy(() -> fes.boek(110));
    }
}