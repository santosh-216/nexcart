package com.nexcart.repository;

import com.nexcart.Enum.ProductCategory;
import com.nexcart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {

    @Query("SELECT p FROM Product p WHERE p.price > :price AND p.category = :productCategory")
    List<Product> getProdByCategoryAndPriceGreaterThan(@Param("price") int price,
                                                       @Param("productCategory") ProductCategory productCategory);

}
