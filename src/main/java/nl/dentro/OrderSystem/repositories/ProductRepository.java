package nl.dentro.OrderSystem.repositories;

import nl.dentro.OrderSystem.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository <Product, Long> {
    List<Product> findAllProductsByCategoryEqualsIgnoreCase(String category);
}
