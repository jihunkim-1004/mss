package com.mss.shop.mss.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.mss.shop.mss.dto.ApiResponseDto;
import com.mss.shop.mss.dto.CategoryPriceDto;
import com.mss.shop.mss.dto.CategoryPriceRangeDto;
import com.mss.shop.mss.dto.CategoryPriceResponseDto;
import com.mss.shop.mss.dto.LowestPriceResponseDto;
import com.mss.shop.mss.dto.ProductDto;
import com.mss.shop.mss.entity.Product;
import com.mss.shop.mss.repository.ProductRepository;
import com.mss.shop.mss.service.ProductService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductIntegrationTest {

  @Autowired
  private ProductService productService;

  @Autowired
  private ProductRepository productRepository;

  @BeforeEach
  void setUp() {
    productRepository.deleteAll();

    // 테스트 데이터 생성
    List<Product> products = List.of(
        createProduct("A", "상의", 11200),
        createProduct("A", "아우터", 5500),
        createProduct("A", "바지", 4200),
        createProduct("A", "스니커즈", 9000),
        createProduct("A", "가방", 2000),
        createProduct("A", "모자", 1700),
        createProduct("A", "양말", 1800),
        createProduct("A", "액세서리", 2300),

        createProduct("B", "상의", 10500),
        createProduct("B", "아우터", 5900),
        createProduct("B", "바지", 3800),
        createProduct("B", "스니커즈", 9100),
        createProduct("B", "가방", 2100),
        createProduct("B", "모자", 2000),
        createProduct("B", "양말", 2000),
        createProduct("B", "액세서리", 2200),

        createProduct("C", "상의", 10000),
        createProduct("C", "아우터", 6200),
        createProduct("C", "바지", 3300),
        createProduct("C", "스니커즈", 9200),
        createProduct("C", "가방", 2200),
        createProduct("C", "모자", 1900),
        createProduct("C", "양말", 2200),
        createProduct("C", "액세서리", 2100)
    );

    productRepository.saveAll(products);
  }

  @Test
  @DisplayName("카테고리별 최저가격 조회 통합 테스트")
  void getLowestPriceByCategory_Integration() {
    // when
    CategoryPriceResponseDto response = productService.getLowestPriceByCategory();

    // then
    assertThat(response).isNotNull();
    assertThat(response.getCategoryPrices()).hasSize(8);

    // 상의 카테고리의 최저가격 확인
    CategoryPriceDto topCategory = response.getCategoryPrices().stream()
        .filter(dto -> dto.getCategory().equals("상의"))
        .findFirst()
        .orElseThrow();
    assertThat(topCategory.getBrand()).isEqualTo("C");
    assertThat(topCategory.getPrice()).isEqualTo(10000);
  }

  @Test
  @DisplayName("단일 브랜드 최저가격 조회 통합 테스트")
  void getLowestPriceBySingleBrand_Integration() {
    // when
    LowestPriceResponseDto response = productService.getLowestPriceBySingleBrand();

    // then
    assertThat(response).isNotNull();
    assertThat(response.getLowestPrice().getBrand()).isEqualTo("C");
    assertThat(response.getLowestPrice().getCategories()).hasSize(8);
    assertThat(response.getLowestPrice().getTotalPrice()).isEqualTo("37,100");
  }

  @Test
  @DisplayName("카테고리별 최저/최고가격 조회 통합 테스트")
  void getPriceRangeByCategory_Integration() {
    // when
    CategoryPriceRangeDto response = productService.getPriceRangeByCategory("상의");

    // then
    assertThat(response).isNotNull();
    assertThat(response.getCategory()).isEqualTo("상의");
    assertThat(response.getLowestPrices()).hasSize(1);
    assertThat(response.getHighestPrices()).hasSize(1);
    assertThat(response.getLowestPrices().get(0).getPrice()).isEqualTo("10,000");
    assertThat(response.getHighestPrices().get(0).getPrice()).isEqualTo("11,200");
  }

  @Test
  @DisplayName("상품 등록 통합 테스트")
  void createProduct_Integration() {
    // given
    ProductDto productDto = new ProductDto();
    productDto.setBrand("D");
    productDto.setCategory("상의");
    productDto.setPrice(11000);

    // when
    ApiResponseDto response = productService.createProduct(productDto);

    // then
    assertThat(response.isSuccess()).isTrue();
    assertThat(response.getMessage()).isEqualTo("상품이 성공적으로 등록되었습니다.");

    // DB에서 실제로 저장되었는지 확인
    List<Product> products = productRepository.findByBrand("D");
    assertThat(products).hasSize(1);
    assertThat(products.get(0).getCategory()).isEqualTo("상의");
    assertThat(products.get(0).getPrice()).isEqualTo(11000);
  }

  @Test
  @DisplayName("상품 수정 통합 테스트")
  void updateProduct_Integration() {
    // given
    Product product = productRepository.findByBrand("A").get(0);
    ProductDto productDto = new ProductDto();
    productDto.setBrand("A");
    productDto.setCategory("상의");
    productDto.setPrice(11500);

    // when
    ApiResponseDto response = productService.updateProduct(product.getId(), productDto);

    // then
    assertThat(response.isSuccess()).isTrue();
    assertThat(response.getMessage()).isEqualTo("상품이 성공적으로 수정되었습니다.");

    // DB에서 실제로 수정되었는지 확인
    Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
    assertThat(updatedProduct.getPrice()).isEqualTo(11500);
  }

  @Test
  @DisplayName("상품 삭제 통합 테스트")
  void deleteProduct_Integration() {
    // given
    Product product = productRepository.findByBrand("A").get(0);

    // when
    ApiResponseDto response = productService.deleteProduct(product.getId());

    // then
    assertThat(response.isSuccess()).isTrue();
    assertThat(response.getMessage()).isEqualTo("상품이 성공적으로 삭제되었습니다.");

    // DB에서 실제로 삭제되었는지 확인
    assertThat(productRepository.findById(product.getId())).isEmpty();
  }

  @Test
  @DisplayName("상품 등록 실패 통합 테스트 - 중복 브랜드/카테고리")
  void createProduct_Failure_Duplicate_Integration() {
    // given
    ProductDto productDto = new ProductDto();
    productDto.setBrand("A");
    productDto.setCategory("상의");
    productDto.setPrice(11000);

    // when
    ApiResponseDto response = productService.createProduct(productDto);

    // then
    assertThat(response.isSuccess()).isFalse();
    assertThat(response.getMessage()).isEqualTo("이미 존재하는 브랜드와 카테고리 조합입니다.");
  }

  private Product createProduct(String brand, String category, int price) {
    Product product = new Product();
    product.setBrand(brand);
    product.setCategory(category);
    product.setPrice(price);
    return product;
  }
} 