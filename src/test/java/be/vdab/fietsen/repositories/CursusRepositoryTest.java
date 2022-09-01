package be.vdab.fietsen.repositories;

import be.vdab.fietsen.domain.GroepsCursus;
import be.vdab.fietsen.domain.IndividueleCursus;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
// enkele andere imports
@DataJpaTest(showSql = false)
@Import(CursusRepository.class)
@Sql("/insertCursus.sql")
class CursusRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

    private final EntityManager manager;
    private static final LocalDate EEN_DATUM = LocalDate.of(2019, 1, 1);
    private final CursusRepository repository;

    private static final String GROEPS_CURSUSSEN = "groepscursussen";
    private static final String INDIVIDUELE_CURSUSSEN = "individuelecursussen";


    CursusRepositoryTest(CursusRepository repository,EntityManager manager){
        this.repository = repository;
        this.manager = manager;
    }

    private UUID idVanTestGroepsCursus() {
        return jdbcTemplate.queryForObject(
                "select bin_to_uuid(id) from groepscursussen where naam = 'testGroep'",
                UUID.class);
    }
    private UUID idVanTestIndividueleCursus() {
        return jdbcTemplate.queryForObject(
                "select bin_to_uuid(id) from individuelecursussen where naam='testIndividueel'",
                UUID.class);
    }
    @Test void findByOnbestaandeId() {
        assertThat(repository.findById(UUID.randomUUID())).isEmpty();
    }
    @Test void createGroepsCursus() {
        var cursus = new GroepsCursus("testGroep2", EEN_DATUM, EEN_DATUM);
        repository.create(cursus);
        manager.flush();
        assertThat(countRowsInTableWhere(GROEPS_CURSUSSEN,
                "id = uuid_to_bin('" + cursus.getId() + "')")).isOne();
    }
    @Test void createIndividueleCursus() {
        var cursus = new IndividueleCursus("testIndividueel2", 7);
        repository.create(cursus);
        manager.flush();
        assertThat(countRowsInTableWhere(INDIVIDUELE_CURSUSSEN,
                "id = uuid_to_bin('" + cursus.getId() + "')")).isOne();
    }
}