package softuni.exam.models.entities;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "sellers")
public class Seller extends BaseEntity{
    //•	id – integer number, primary identification field.
    //•	firstName – a char sequence (between 2 to 20 exclusive).
    //•	lastName – a char sequence (between 2 to 20 exclusive).
    //•	email – an email – (must contains ‘@’ and ‘.’ – dot). The email of a seller is unique.
    //•	rating – enumerated value must be one of these GOOD, BAD or UNKNOWN. Cannot be null.
    //•	town – a char sequence – the name of a town. Cannot be null.

    private String firstName;
    private String lastName;
    private String email;
    private Rating rating;
    private String town;

    public Seller() {
    }
    @Column(name = "first_name", nullable = false)
    @Length(min = 2, max = 20)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    @Column(name = "last_name", nullable = false)
    @Length(min = 2, max = 20)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    @Column(name = "email", nullable = false, unique = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @Enumerated(EnumType.STRING)
    @NotNull
    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }
    @NotNull
    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
}
