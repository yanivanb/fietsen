package be.vdab.fietsen.repositories;

import be.vdab.fietsen.domain.Cursus;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CursusRepository {
    private final EntityManager manager;

    public CursusRepository(EntityManager manager) {
        this.manager = manager;
    }

    public Optional<Cursus> findById(UUID id) {
        return Optional.ofNullable(manager.find(Cursus.class, id));
    }
    public void create(Cursus cursus) {
        manager.persist(cursus);
    }
}
