package nl.dentro.OrderSystem.models;

import javax.persistence.*;

@Entity
@Table(name = "stock_locations")
public class StockLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String location;

    private boolean available = true;


    @OneToOne(mappedBy = "stockLocation")
    Product product;

    public StockLocation(Long id, String location, boolean available) {
        this.id = id;
        this.location = location;
        this.available = available;
    }

    public StockLocation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StockLocation that = (StockLocation) o;

        if (available != that.available) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return location != null ? location.equals(that.location) : that.location == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (available ? 1 : 0);
        return result;
    }
}
