package be.vdab.fietsen.services;

import be.vdab.fietsen.domain.Adres;
import be.vdab.fietsen.domain.Campus;
import be.vdab.fietsen.domain.Docent;
import be.vdab.fietsen.domain.Geslacht;
import be.vdab.fietsen.exceptions.DocentNietGevondenException;
import be.vdab.fietsen.repositories.DocentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DocentServiceTest {
    private DocentService service;
    @Mock
    private DocentRepository repository;
    private Docent docent;

    @BeforeEach
    void beforeEach() {
        service = new DocentService(repository);
        var campus = new Campus("test", new Adres("test", "test", "test", "test"));
        docent = new Docent("test", "test", BigDecimal.valueOf(100), "test@test.be",
                Geslacht.MAN, campus);
    }

    @Test
    void opslag() {
        when(repository.findById(1)).thenReturn(Optional.of(docent));
        service.opslag(1, BigDecimal.TEN);
        assertThat(docent.getWedde()).isEqualByComparingTo("110");
        verify(repository).findById(1);
    }
    @Test
    void opslagVoorOnbestaandeDocent() {
        assertThatExceptionOfType(DocentNietGevondenException.class).isThrownBy(
                () -> service.opslag(-1, BigDecimal.TEN));
        verify(repository).findById(-1);
    }
}