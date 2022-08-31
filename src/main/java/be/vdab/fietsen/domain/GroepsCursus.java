package be.vdab.fietsen.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "groepscursussen")
public class GroepsCursus extends Cursus {
    private LocalDate van;
    private LocalDate tot;
// constructor met parameters, protected default constructor, getters

    protected GroepsCursus() {}

    public GroepsCursus(String naam, LocalDate van, LocalDate tot) {
        super(naam);
        this.van = van;
        this.tot = tot;
    }


}
