package be.vdab.fietsen.repositories;

import be.vdab.fietsen.domain.*;
import be.vdab.fietsen.projections.AantalDocentenPerWedde;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@Sql({"/insertCampus.sql", "/insertVerantwoordelijkheid.sql",
        "/insertDocent.sql", "/insertDocentVerantwoordelijkheid.sql"})
@Import(DocentRepository.class)
class DocentRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
    private final DocentRepository repository;
    private final EntityManager manager;
    private Campus campus;
    private static final String DOCENTEN = "docenten";
    private Docent docent;

    private static final String DOCENTEN_VERANTWOORDELIJKHEDEN = "docentenverantwoordelijkheden";

    public DocentRepositoryTest(DocentRepository repository, EntityManager manager){
        this.repository = repository;
        this.manager = manager;
    }

    @BeforeEach
    void beforeEach() {
        campus = new Campus("test", new Adres("test", "test", "test", "test"));
        docent = new Docent(
                "test", "test", BigDecimal.TEN, "test@test.be", Geslacht.MAN, campus);//
    }

    @Test
    void campusLazyLoaded() {
        assertThat(repository.findById(idVanTestMan())).hasValueSatisfying(
                docent -> assertThat(docent.getCampus().getNaam()).isEqualTo("test"));
    }

    private long idVanTestMan() {
        return jdbcTemplate.queryForObject(
                "select id from docenten where voornaam = 'testM'", Long.class);
    }

    private long idVanTestVrouw() {
        return jdbcTemplate.queryForObject(
                "select id from docenten where voornaam = 'testV'", Long.class);
    }

    private static final String DOCENTEN_BIJNAMEN = "docentenbijnamen";
    @Test void bijnamenLezen() {
        assertThat(repository.findById(idVanTestMan()))
                .hasValueSatisfying(docent ->
                        assertThat(docent.getBijnamen()).containsOnly("test"));
    }
    @Test void bijnaamToevoegen() {
        manager.persist(campus);
        repository.create(docent);
        docent.addBijnaam("test");
        manager.flush();
        assertThat(countRowsInTableWhere(DOCENTEN_BIJNAMEN,
                "bijnaam = 'test' and docentId = " + docent.getId())).isOne();
    }

    @Test
    void findById() {
        assertThat(repository.findById(idVanTestMan()))
                .hasValueSatisfying(
                        docent -> assertThat(docent.getVoornaam()).isEqualTo("testM"));
    }
    @Test
    void findByOnbestaandeId() {
        assertThat(repository.findById(-1)).isEmpty();
    }


    @Test void man() {
        assertThat(repository.findById(idVanTestMan()))
                .hasValueSatisfying(
                        docent -> assertThat(docent.getGeslacht()).isEqualTo(Geslacht.MAN));
    }
    @Test void vrouw() {
        assertThat(repository.findById(idVanTestVrouw()))
                .hasValueSatisfying(docent ->
                        assertThat(docent.getGeslacht()).isEqualTo(Geslacht.VROUW));
    }

    @Test
    void create() {
        manager.persist(campus);
        repository.create(docent);
        manager.flush();
        assertThat(docent.getId()).isPositive();
        assertThat(countRowsInTableWhere(DOCENTEN, "id = " + docent.getId() + " and campusId = " + campus.getId()));
        assertThat(campus.getDocenten().contains(docent)).isTrue();
    }

    @Test
    void delete() {
        var id = idVanTestMan();
        repository.delete(id);
        manager.flush();
        assertThat(countRowsInTableWhere(DOCENTEN, "id = " + id)).isZero();
    }

    @Test
    void findAll() {
        assertThat(repository.findAll())
                .hasSize(countRowsInTable(DOCENTEN))
                .extracting(Docent::getWedde)
                .isSorted();
    }

    @Test void findByWeddeBetween() {
        var duizend = BigDecimal.valueOf(1_000);
        var tweeduizend = BigDecimal.valueOf(2_000);
        var docenten = repository.findByWeddeBetween(duizend, tweeduizend);
        assertThat(docenten)
                .hasSize(countRowsInTableWhere(DOCENTEN, "wedde between 1000 and 2000"))
                .allSatisfy(
                        docent -> assertThat(docent.getWedde()).isBetween(duizend, tweeduizend));
    }

    @Test void findEmailAdressen() {
        assertThat(repository.findEmailAdressen())
                .hasSize(countRowsInTable(DOCENTEN))
                .allSatisfy(emailAdres -> assertThat(emailAdres).contains("@"));
    }

    @Test void findIdsEnEmailAdressen() {
        assertThat(repository.findIdsEnEmailAdressen())
                .hasSize(countRowsInTable(DOCENTEN));
    }

    @Test void findGrootsteWedde() {
        assertThat(repository.findGrootsteWedde()).isEqualByComparingTo(
                jdbcTemplate.queryForObject("select max(wedde) from docenten",
                        BigDecimal.class));
    }

    @Test
    void findAantalDocentenPerWedde() {
        var duizend = BigDecimal.valueOf(1_000);
        assertThat(repository.findAantalDocentenPerWedde())
                .hasSize(jdbcTemplate.queryForObject(
                        "select count(distinct wedde) from docenten", Integer.class))
                .filteredOn(
                                aantalPerWedde -> aantalPerWedde.wedde().compareTo(duizend) == 0)
                .singleElement()
                .extracting(AantalDocentenPerWedde::aantal)
                .isEqualTo((long) super.countRowsInTableWhere(DOCENTEN, "wedde = 1000"));
    }

    @Test
    void algemeneOpslag() {
        assertThat(repository.algemeneOpslag(BigDecimal.TEN))
                .isEqualTo(countRowsInTable(DOCENTEN));
        assertThat(countRowsInTableWhere(DOCENTEN,
                "wedde = 1100 and id = " + idVanTestMan())).isOne();
    }

    @Test
    void verantwoordelijkhedenLezen() {
        assertThat(repository.findById(idVanTestMan()))
                .hasValueSatisfying(
                        docent -> assertThat(docent.getVerantwoordelijkheden())
                                .containsOnly(new Verantwoordelijkheid("test")));
    }
    @Test
    void verantwoordelijkheidToevoegen() {
        var verantwoordelijkheid = new Verantwoordelijkheid("test2");
        manager.persist(verantwoordelijkheid);
        manager.persist(campus);
        repository.create(docent);
        docent.add(verantwoordelijkheid);
        manager.flush();
        assertThat(countRowsInTableWhere(DOCENTEN_VERANTWOORDELIJKHEDEN,
                "docentId = " + docent.getId() +
                        " and verantwoordelijkheidId = " + verantwoordelijkheid.getId())).isOne();
    }
}