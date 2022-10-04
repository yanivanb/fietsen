package be.vdab.fietsen.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class DocentTest {
    private final static BigDecimal WEDDE = BigDecimal.valueOf(200);
    private Docent docent1;
    private Campus campus1;
    private Docent docent2;
    private Campus campus2;
    @BeforeEach
    void beforeEach() {
        campus1 = new Campus("test", new Adres("test", "test", "test", "test"));
        docent1 = new Docent("test", "test", WEDDE, "test@test.be",Geslacht.MAN, campus1);
        docent2 = new Docent("test2", "test2", WEDDE, "test2@test.be", Geslacht.MAN, campus1);
        campus2 = new Campus("test2", new Adres("test2", "test2", "test2", "test2"));
    }

    @Test void docent1KomtVoorInCampus1() {
        assertThat(docent1.getCampus()).isEqualTo(campus1);
        assertThat(campus1.getDocenten()).contains(docent1);
    }
    @Test void docent1VerhuistVanCampus1NaarCampus2() {
        docent1.setCampus(campus2);
        assertThat(docent1.getCampus()).isEqualTo(campus2);
        assertThat(campus1.getDocenten()).doesNotContain(docent1);
        assertThat(campus2.getDocenten()).containsOnly(docent1);
    }
    @Test void eenNullCampusInDeSetterMislukt() {
        assertThatNullPointerException().isThrownBy(() -> docent1.setCampus(null));
    }

    @Test
    void meerdereDocentenKunnenTotDezelfdeCampusBehoren() {
        assertThat(campus1.getDocenten()).containsOnly(docent1, docent2);
    }
    @Test
    void opslag() {
        docent1.opslag(BigDecimal.TEN);
        assertThat(docent1.getWedde()).isEqualByComparingTo("220");
    }
    @Test
    void opslagMetNullMislukt() {
        assertThatNullPointerException().isThrownBy(() -> docent1.opslag(null));
    }
    @Test
    void opslagMet0Mislukt() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> docent1.opslag(BigDecimal.ZERO));
    }
    @Test
    void negatieveOpslagMislukt() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> docent1.opslag(BigDecimal.valueOf(-1)));
    }

    @Test void eenNieuweDocentHeeftGeenBijnamen() {
        assertThat(docent1.getBijnamen()).isEmpty();
    }
    @Test void bijnaamToevoegen() {
        assertThat(docent1.addBijnaam("test")).isTrue();
        assertThat(docent1.getBijnamen()).containsOnly("test");
    }
    @Test void tweeKeerDezelfdeBijnaamMislukt() {
        docent1.addBijnaam("test");
        assertThat(docent1.addBijnaam("test")).isFalse();
        assertThat(docent1.getBijnamen()).containsOnly("test");
    }
    @Test void nullAlsBijnaamMislukt() {
        assertThatNullPointerException().isThrownBy(() -> docent1.addBijnaam(null));
    }
    @Test void eenLegeBijnaamMislukt() {
        assertThatIllegalArgumentException().isThrownBy(() -> docent1.addBijnaam(""));
    }
    @Test void eenBijnaamMetEnkelSpatiesMislukt() {
        assertThatIllegalArgumentException().isThrownBy(()->docent1.addBijnaam(" "));
    }
    @Test void bijnaamVerwijderen() {
        docent1.addBijnaam("test");
        assertThat(docent1.removeBijnaam("test")).isTrue();
        assertThat(docent1.getBijnamen()).isEmpty();
    }
    @Test void eenBijnaamVerwijderenDieJeNietToevoegdeMislukt() {
        docent1.addBijnaam("test");
        assertThat(docent1.removeBijnaam("test2")).isFalse();
        assertThat(docent1.getBijnamen()).containsOnly("test");
    }
}