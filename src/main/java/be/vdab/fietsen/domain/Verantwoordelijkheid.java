package be.vdab.fietsen.domain;

import javax.persistence.*;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

// enkele imports
@Entity
@Table(name = "verantwoordelijkheden")
public class Verantwoordelijkheid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String naam;

    @ManyToMany(mappedBy = "verantwoordelijkheden")
    private Set<Docent> docenten = new LinkedHashSet<>();
    // constructor met parameter (enkel voor naam)
// protected default constructor

    public Verantwoordelijkheid(String naam) {
        this.naam = naam;
    }
    protected Verantwoordelijkheid(){}


    public boolean add(Docent docent) {
        var toegevoegd = docenten.add(docent);
        if ( ! docent.getVerantwoordelijkheden().contains(this)) {
            docent.add(this);
        }
        return toegevoegd;
    }
    public boolean remove(Docent docent) {
        var verwijderd = docenten.remove(docent);
        if (docent.getVerantwoordelijkheden().contains(this)) {
            docent.remove(this);
        }
        return verwijderd;
    }

    public long getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }

    public Set<Docent> getDocenten() {
        return Collections.unmodifiableSet(docenten);
    }
}
