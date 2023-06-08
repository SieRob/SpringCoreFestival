package be.vdab.dance.services;

import be.vdab.dance.domain.Boeking;
import be.vdab.dance.domain.Festival;
import be.vdab.dance.exceptions.FestivalNietGevondenException;
import be.vdab.dance.exceptions.OnvoldoendeTicketsException;
import be.vdab.dance.repositories.BoekingRepository;
import be.vdab.dance.repositories.FestivalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BoekingServiceTest {
    private BoekingService boekingService;
    private FestivalService festivalService;
    @Mock
    private BoekingRepository boekingRepo;
    @Mock
    private FestivalRepository festivalRepo;

    private Festival fes1;

    @BeforeEach
    void beforeEach(){
        boekingService= new BoekingService(boekingRepo, festivalRepo);
        fes1 = new Festival(1 , "HardRockFes" , 10, BigDecimal.valueOf(100));
    }

    @Test
    void boekingZonderFesIdMislukt() {
        assertThatExceptionOfType(FestivalNietGevondenException.class).isThrownBy(
                () -> boekingService.create(new Boeking("test", 1,1)));
    }

    @Test
    void boekingZonderNaamMislukt() {
        when(festivalRepo.findAndLockById(1)).thenReturn(Optional.of(fes1));
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                () -> boekingService.create(new Boeking("", 1,1)));
    }

    @Test
    void boekingZonderTicketsMislukt() {
        when(festivalRepo.findAndLockById(1)).thenReturn(Optional.of(fes1));
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                () -> boekingService.create(new Boeking("Test", 0,1)));
    }

    @Test
    void boekingNegatieveTicketsMislukt() {
        when(festivalRepo.findAndLockById(1)).thenReturn(Optional.of(fes1));
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                () -> boekingService.create(new Boeking("Test", -2,1)));
    }
    @Test
    void boekingTeVeelTicketsMislukt() {
        when(festivalRepo.findAndLockById(1)).thenReturn(Optional.of(fes1));
        assertThatExceptionOfType(OnvoldoendeTicketsException.class).isThrownBy(
                () -> boekingService.create(new Boeking("Test", 100_000,1)));
    }
    @Test
    void create() {
        when(festivalRepo.findAndLockById(1)).thenReturn(Optional.of(fes1));
        var boeking = new Boeking("Test", 2, 1);
        boekingService.create(boeking);

        assertThat(fes1.getTicketsBeschikbaar()).isEqualTo(8);

        verify(festivalRepo).findAndLockById(1);
        verify(boekingRepo).create(boeking);

    }


}