package softuni.exam.models.entities;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "pictures")
public class Picture extends BaseEntity{
    //•	id – integer number, primary identification field.
    //•	name – a char sequence (between 2 to 20 exclusive). The name of a picture is unique.
    //•	dateAndTime – The date and time of a picture.

    private String name;
    private LocalDateTime dateAndTime;
    private Car car;
    private Set<Offer> offers;

    public Picture() {
    }

    @NotNull
    @Length(min = 2, max = 20)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @NotNull
    public LocalDateTime getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(LocalDateTime dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    @ManyToOne( cascade =CascadeType.ALL, fetch = FetchType.EAGER)
    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    @ManyToMany(mappedBy = "pictures", cascade = CascadeType.ALL)

    public Set<Offer> getOffers() {
        return offers;
    }

    public void setOffers(Set<Offer> offers) {
        this.offers = offers;
    }
}
