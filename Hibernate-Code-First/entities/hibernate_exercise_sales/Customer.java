package entities.hibernate_exercise_sales;

import entities.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "customers")
public class Customer extends BaseEntity {
    private String name;
    private String email;
    private String creditCardNumber;

    public Customer() {
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "credit_card_number")
    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }


}
