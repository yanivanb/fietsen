package be.vdab.fietsen.repositories;

import be.vdab.fietsen.domain.Campus;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
public class CampusRepository {
    private final EntityManager manager;

    public CampusRepository(EntityManager manager) {
        this.manager = manager;
    }

    public void create(Campus campus) {
        manager.persist(campus);
    }
    public Optional<Campus> findById(long id) {
        return Optional.ofNullable(manager.find(Campus.class, id));
    }
}
