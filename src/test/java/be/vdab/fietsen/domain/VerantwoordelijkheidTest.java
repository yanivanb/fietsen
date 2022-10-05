package be.vdab.fietsen.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;
// enkele imports
class VerantwoordelijkheidTest {
    private Verantwoordelijkheid verantwoordelijkheid1;
    private Docent docent1;
    private Campus campus1;
    @BeforeEach
    void beforeEach() {
        verantwoordelijkheid1 = new Verantwoordelijkheid("EHBO");
        campus1 = new Campus("test", new Adres("test", "test", "test", "test"));
        docent1 = new Docent(
                "test", "test", BigDecimal.TEN, "test@test.be", Geslacht.MAN, campus1);
    }
    @Test
    void docentToevoegen() {
        assertThat(verantwoordelijkheid1.getDocenten()).isEmpty();
        assertThat(verantwoordelijkheid1.add(docent1)).isTrue();
        assertThat(verantwoordelijkheid1.getDocenten()).containsOnly(docent1);
        assertThat(docent1.getVerantwoordelijkheden())
                .containsOnly(verantwoordelijkheid1);
    }
    @Test
    void docentVerwijderen() {
        assertThat(verantwoordelijkheid1.add(docent1)).isTrue();
        assertThat(verantwoordelijkheid1.remove(docent1)).isTrue();
        assertThat(verantwoordelijkheid1.getDocenten()).isEmpty();
        assertThat(docent1.getVerantwoordelijkheden()).isEmpty();
    }
}