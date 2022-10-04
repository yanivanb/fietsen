package be.vdab.fietsen.domain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
// enkele andere imports
class CampusTest {
    private Docent docent1;
    private Campus campus1;
    private Campus campus2;
    @BeforeEach
    void beforeEach() {
        campus1 = new Campus("test", new Adres("test", "test", "test", "test"));
        campus2 = new Campus("test2", new Adres("test2","test2","test2","test2"));
        docent1 = new Docent(
                "test", "test", BigDecimal.TEN, "test@test.be", Geslacht.MAN, campus1);
    }
    @Test
    void campus1IsDeCampusVanDocent1() {
        assertThat(docent1.getCampus()).isEqualTo(campus1);
        assertThat(campus1.getDocenten()).containsOnly(docent1);
    }
    @Test
    void docent1VerhuistVanCampus1NaarCampus2() {
        assertThat(campus2.add(docent1)).isTrue();
        assertThat(campus1.getDocenten()).doesNotContain(docent1);
        assertThat(campus2.getDocenten()).containsOnly(docent1);
        assertThat(docent1.getCampus()).isEqualTo(campus2);
    }
    @Test
    void eenNullDocentToevoegenMislukt() {
        assertThatNullPointerException().isThrownBy(() -> campus1.add(null));
    }
}