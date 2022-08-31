package be.vdab.fietsen.domain;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "cursussen")
public abstract class Cursus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String naam;
// constructor met parameters, protected default constructor, getters

    public Cursus(long id, String naam) {
        this.id = id;
        this.naam = naam;
    }
    public Cursus(String naam) {
        this.naam = naam;
    }

    protected Cursus() {
    }

    public long getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }
}
