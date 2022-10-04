package be.vdab.fietsen.repositories;

import be.vdab.fietsen.domain.Adres;
import be.vdab.fietsen.domain.Campus;
import be.vdab.fietsen.domain.Docent;
import be.vdab.fietsen.domain.TelefoonNr;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;
// enkele andere imports
@DataJpaTest(showSql = false)
@Import(CampusRepository.class)
@Sql({"/insertCampus.sql", "/insertDocent.sql"})
class CampusRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static final String CAMPUSSEN = "campussen";
    private CampusRepository repository;

    public CampusRepositoryTest(CampusRepository repository) {
        this.repository = repository;
    }

    private long idVanTestCampus() {
        return jdbcTemplate.queryForObject(
                "select id from campussen where naam = 'test'", Long.class);
    }

    @Test
    void docentenLazyLoaded() {
        assertThat(repository.findById(idVanTestCampus()))
.hasValueSatisfying(campus ->
                assertThat(campus.getDocenten())
                .hasSize(2)
                .first()
                .extracting(Docent::getVoornaam).isEqualTo("testM"));
    }
    @Test
    void findById() {
        assertThat(repository.findById(idVanTestCampus()))
                .hasValueSatisfying(campus -> {
                    assertThat(campus.getNaam()).isEqualTo("test");
                    assertThat(campus.getAdres().getGemeente()).isEqualTo("test");
                });
    }
    @Test
    void findByOnbestaandeId() {
        assertThat(repository.findById(-1)).isEmpty();
    }
    @Test
    void create() {
        var campus = new Campus("test", new Adres("test", "test", "test", "test"));
        repository.create(campus);
        assertThat(countRowsInTableWhere(CAMPUSSEN,
                "id =" + campus.getId())).isOne();
    }

    @Test
    void telefoonNrsLezen() {
        assertThat(repository.findById(idVanTestCampus()))
                .hasValueSatisfying(
                        campus -> assertThat(campus.getTelefoonNrs())
                                .containsOnly(new TelefoonNr("1", false, "test")));
    }
}