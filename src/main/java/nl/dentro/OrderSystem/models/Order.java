package nl.dentro.OrderSystem.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double totalPrice;
    private boolean paid;

    @OneToOne
    UserData userData;

    @OneToMany(mappedBy = "order")
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonIgnore
    Collection<OrderProduct> orderProduct;

    public Order( Double totalPrice, boolean paid, UserData userData) {
        this.totalPrice = totalPrice;
        this.paid = paid;
        this.userData = userData;
    }

    public Order() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public Collection<OrderProduct> getOrderProduct() {
        return orderProduct;
    }

    public void setOrderProduct(Collection<OrderProduct> orderProduct) {
        this.orderProduct = orderProduct;
    }
}
