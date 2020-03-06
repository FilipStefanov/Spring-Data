package entities.hibernate_exercise_sales;

import entities.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;


@Entity
@Table(name = "products")
public class Product extends BaseEntity {
    private String name;
    private Double quantity;
    private BigDecimal price;

    public Product() {
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "quantity")
    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    @Column(name = "price")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }


}

