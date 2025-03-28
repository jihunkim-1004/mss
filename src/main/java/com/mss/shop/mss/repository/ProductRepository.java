package com.mss.shop.mss.repository;

import com.mss.shop.mss.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  List<Product> findByBrand(String brand);

  @Query("SELECT p FROM Product p WHERE p.category = :category ORDER BY p.price ASC")
  List<Product> findLowestPriceByCategory(@Param("category") String category);

  List<Product> findByCategory(String category);

  List<Product> findByBrandAndCategory(String brand, String category);
} 