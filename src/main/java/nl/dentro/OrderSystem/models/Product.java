package nl.dentro.OrderSystem.models;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;
    private String category;
    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany
    private Collection<OrderProduct> orderProduct;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collection<OrderProduct> getOrderProduct() {
        return orderProduct;
    }

    public void setOrderProduct(Collection<OrderProduct> orderProduct) {
        this.orderProduct = orderProduct;
    }
    }
