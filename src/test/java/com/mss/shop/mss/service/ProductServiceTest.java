package com.mss.shop.mss.service;

import com.mss.shop.mss.dto.*;
import com.mss.shop.mss.entity.Product;
import com.mss.shop.mss.exception.ProductException;
import com.mss.shop.mss.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class ProductServiceTest {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private TestEntityManager entityManager;

  private ProductService productService;

  @BeforeEach
  void setUp() {
    productService = new ProductService(productRepository);
    insertTestData();
  }

  @Test
  @DisplayName("카테고리별 최저가격 조회 성공")
  void getLowestPriceByCategory_Success() {
    // when
    CategoryPriceResponseDto response = productService.getLowestPriceByCategory();

    // then
    assertThat(response).isNotNull();
    assertThat(response.getCategoryPrices().get(0).getBrand()).isEqualTo("C");
    assertThat(response.getCategoryPrices().get(0).getPrice()).isEqualTo(10000);
  }

  @Test
  @DisplayName("카테고리별 최저가격 조회 실패 - 데이터 없음")
  void getLowestPriceByCategory_Failure_NoData() {
    // given
    productRepository.deleteAll();

    // when & then
    assertThatThrownBy(() -> productService.getLowestPriceByCategory())
        .isInstanceOf(ProductException.class)
        .hasMessage("상품 정보가 없습니다.");
  }

  @Test
  @DisplayName("상품 등록 성공")
  void createProduct_Success() {
    // given
    ProductDto productDto = new ProductDto();
    productDto.setBrand("B");
    productDto.setCategory("하의");
    productDto.setPrice(15000);

    // when
    ApiResponseDto response = productService.createProduct(productDto);

    // then
    assertThat(response.isSuccess()).isTrue();
    assertThat(response.getMessage()).isEqualTo("상품이 성공적으로 등록되었습니다.");
    assertThat(productRepository.findAll()).hasSize(75);
  }

  @Test
  @DisplayName("상품 등록 실패 - 브랜드 누락")
  void createProduct_Failure_MissingBrand() {
    // given
    ProductDto productDto = new ProductDto();
    productDto.setCategory("상의");
    productDto.setPrice(11200);

    // when
    ApiResponseDto response = productService.createProduct(productDto);

    // then
    assertThat(response.isSuccess()).isFalse();
    assertThat(response.getMessage()).isEqualTo("브랜드, 카테고리, 가격은 필수 입력값입니다.");
  }

  @Test
  @DisplayName("상품 삭제 성공")
  void deleteProduct_Success() {
    // given
    Product product = productRepository.findAll().get(0);

    // when
    ApiResponseDto response = productService.deleteProduct(product.getId());

    // then
    assertThat(response.isSuccess()).isTrue();
    assertThat(response.getMessage()).isEqualTo("상품이 성공적으로 삭제되었습니다.");
    assertThat(productRepository.findAll()).hasSize(73);
  }

  @Test
  @DisplayName("상품 삭제 실패 - 상품 없음")
  void deleteProduct_Failure_ProductNotFound() {
    // when
    ApiResponseDto response = productService.deleteProduct(999L);

    // then
    assertThat(response.isSuccess()).isFalse();
    assertThat(response.getMessage()).isEqualTo("상품을 찾을 수 없습니다.");
  }

  private void insertTestData() {
    Product productA = createProduct("A", "상의", 11200);
    Product productB = createProduct("C", "상의", 10000);
    entityManager.persist(productA);
    entityManager.persist(productB);
    entityManager.flush();
  }

  private Product createProduct(String brand, String category, int price) {
    Product product = new Product();
    product.setBrand(brand);
    product.setCategory(category);
    product.setPrice(price);
    return product;
  }
}
