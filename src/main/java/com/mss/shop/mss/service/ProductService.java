package com.mss.shop.mss.service;

import com.mss.shop.mss.dto.ApiResponseDto;
import com.mss.shop.mss.dto.BrandPriceResponseDto;
import com.mss.shop.mss.dto.CategoryPriceDetailDto;
import com.mss.shop.mss.dto.CategoryPriceDto;
import com.mss.shop.mss.dto.CategoryPriceRangeDto;
import com.mss.shop.mss.dto.CategoryPriceResponseDto;
import com.mss.shop.mss.dto.LowestPriceResponseDto;
import com.mss.shop.mss.dto.ProductDto;
import com.mss.shop.mss.entity.Product;
import com.mss.shop.mss.exception.ProductException;
import com.mss.shop.mss.repository.ProductRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;

  // 1. 카테고리별 최저가격 조회
  public CategoryPriceResponseDto getLowestPriceByCategory() {
    try {
      List<String> categories = Arrays.asList("상의", "아우터", "바지", "스니커즈", "가방", "모자", "양말", "액세서리");
      List<CategoryPriceDto> categoryPrices = categories.stream().map(category -> {
        List<Product> products = productRepository.findLowestPriceByCategory(category);
        if (!products.isEmpty()) {
          Product product = products.get(0);
          CategoryPriceDto dto = new CategoryPriceDto();
          dto.setCategory(category);
          dto.setBrand(product.getBrand());
          dto.setPrice(product.getPrice());
          return dto;
        }
        return null;
      }).filter(Objects::nonNull).collect(Collectors.toList());

      if (categoryPrices.isEmpty()) {
        throw new ProductException("상품 정보가 없습니다.");
      }

      return new CategoryPriceResponseDto(categoryPrices);
    } catch (Exception e) {
      throw new ProductException(e.getMessage());
    }
  }

  // 2. 단일 브랜드 최저가격 조회
  public LowestPriceResponseDto getLowestPriceBySingleBrand() {
    try {
      List<String> brands = productRepository.findAll().stream().map(Product::getBrand).distinct().toList();

      if (brands.isEmpty()) {
        throw new ProductException("등록된 브랜드가 없습니다.");
      }

      String lowestBrand = null;
      int lowestTotalPrice = Integer.MAX_VALUE;
      List<Product> lowestProducts = null;

      for (String brand : brands) {
        List<Product> products = productRepository.findByBrand(brand);
        if (products.size() == 8) { // 모든 카테고리가 있는 경우만
          int totalPrice = products.stream().mapToInt(Product::getPrice).sum();
          if (totalPrice < lowestTotalPrice) {
            lowestTotalPrice = totalPrice;
            lowestBrand = brand;
            lowestProducts = products;
          }
        }
      }

      if (lowestProducts == null) {
        throw new ProductException("모든 카테고리의 상품을 보유한 브랜드가 없습니다.");
      }

      List<CategoryPriceDetailDto> categoryPrices = lowestProducts.stream().map(product -> new CategoryPriceDetailDto(product.getCategory(), product.getPrice())).collect(Collectors.toList());

      BrandPriceResponseDto brandPrice = new BrandPriceResponseDto(lowestBrand, categoryPrices, lowestTotalPrice);
      return new LowestPriceResponseDto(brandPrice);
    } catch (Exception e) {
      throw new ProductException(e.getMessage());
    }
  }

  // 3. 카테고리별 최저/최고가격 조회
  public CategoryPriceRangeDto getPriceRangeByCategory(String category) {
    try {
      List<Product> products = productRepository.findByCategory(category);
      if (products.isEmpty()) {
        throw new ProductException("해당 카테고리의 상품이 없습니다.");
      }

      // 최저가와 최고가 찾기
      int minPrice = products.stream()
          .mapToInt(Product::getPrice)
          .min()
          .orElseThrow(() -> new ProductException("최저가를 찾을 수 없습니다."));

      int maxPrice = products.stream()
          .mapToInt(Product::getPrice)
          .max()
          .orElseThrow(() -> new ProductException("최고가를 찾을 수 없습니다."));

      // 최저가와 최고가를 가진 상품들 찾기
      List<CategoryPriceDetailDto> lowestPrices = products.stream()
          .filter(p -> p.getPrice() == minPrice)
          .map(p -> new CategoryPriceDetailDto(p.getBrand(), p.getPrice()))
          .collect(Collectors.toList());

      List<CategoryPriceDetailDto> highestPrices = products.stream()
          .filter(p -> p.getPrice() == maxPrice)
          .map(p -> new CategoryPriceDetailDto(p.getBrand(), p.getPrice()))
          .collect(Collectors.toList());

      return new CategoryPriceRangeDto(category, lowestPrices, highestPrices);
    } catch (Exception e) {
      throw new ProductException(e.getMessage());
    }
  }

  // 4. 상품 관리 (등록, 수정, 삭제)
  public ApiResponseDto createProduct(ProductDto productDto) {
    try {
      if (productDto.getBrand() == null || productDto.getCategory() == null || productDto.getPrice() == null) {
        throw new ProductException("브랜드, 카테고리, 가격은 필수 입력값입니다.");
      }

      // 중복 체크
      List<Product> existingProducts = productRepository.findByBrandAndCategory(productDto.getBrand(), productDto.getCategory());
      if (!existingProducts.isEmpty()) {
        throw new ProductException("이미 존재하는 브랜드와 카테고리 조합입니다.");
      }

      Product product = new Product();
      product.setBrand(productDto.getBrand());
      product.setCategory(productDto.getCategory());
      product.setPrice(productDto.getPrice());

      productRepository.save(product);
      return new ApiResponseDto(true, "상품이 성공적으로 등록되었습니다.");
    } catch (Exception e) {
      return new ApiResponseDto(false, e.getMessage());
    }
  }

  public ApiResponseDto updateProduct(Long id, ProductDto productDto) {
    try {
      if (productDto.getBrand() == null || productDto.getBrand().trim().isEmpty()) {
        throw new ProductException("브랜드명은 필수입니다.");
      }
      if (productDto.getCategory() == null || productDto.getCategory().trim().isEmpty()) {
        throw new ProductException("카테고리는 필수입니다.");
      }
      if (productDto.getPrice() == null || productDto.getPrice() <= 0) {
        throw new ProductException("가격은 0보다 커야 합니다.");
      }

      Product product = productRepository.findById(id).orElseThrow(() -> new ProductException("상품을 찾을 수 없습니다."));
      product.setBrand(productDto.getBrand());
      product.setCategory(productDto.getCategory());
      product.setPrice(productDto.getPrice());
      productRepository.save(product);
      return new ApiResponseDto(true, "상품이 성공적으로 수정되었습니다.");
    } catch (Exception e) {
      return new ApiResponseDto(false, e.getMessage());
    }
  }

  public ApiResponseDto deleteProduct(Long id) {
    try {
      Product product = productRepository.findById(id).orElseThrow(() -> new ProductException("상품을 찾을 수 없습니다."));
      productRepository.delete(product);
      return new ApiResponseDto(true, "상품이 성공적으로 삭제되었습니다.");
    } catch (Exception e) {
      return new ApiResponseDto(false, e.getMessage());
    }
  }
}