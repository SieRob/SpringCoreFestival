package be.vdab.dance.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;

class BoekingTest {

    @Test
    void eenBoekingDielukt(){
        new Boeking("an", 2, 1);
    }

    @Test
    void NaamVerplicht(){
        assertThatIllegalArgumentException().isThrownBy(() -> new Boeking("", 2,1));
    }
    @Test
    void nulTicketsBoekenKanNiet(){
        assertThatIllegalArgumentException().isThrownBy(() -> new Boeking("a", 0 , 1));
    }

    @Test
    void negatiefAantalTicketsBoekenKanNiet(){
        assertThatIllegalArgumentException().isThrownBy(() -> new Boeking("a", -1 , 1));
    }

    @Test
    void FestivalIdMoetPositiefZijn() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Boeking("an", 2, 0));
    }
}