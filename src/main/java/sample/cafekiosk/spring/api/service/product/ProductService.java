package sample.cafekiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;


import java.util.List;
import java.util.stream.Collectors;

import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.*;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    //동시성 이슈가 있다.  --> UUID나 시컨스테이블로 처리해야 한다.
    public ProductResponse createProduct(ProductCreateRequest request) {
        //productNumber
        //DB에서 마지막 저장된 Product의 상품번호를 가져와서 +1
        // 001 -> 002
        String nextProductNumber = createNextProductNumber();
        Product savedProduct = request.toEntity(nextProductNumber);

        productRepository.save(savedProduct);
        return ProductResponse.of(savedProduct);

    }
    
    private String createNextProductNumber() {
        String lastestProductNumber = productRepository.findLatestProductNumber();
        if (lastestProductNumber == null) return "001";
        int lastestProductNumberInt = Integer.valueOf(lastestProductNumber);
        int nextProductNumberInt = lastestProductNumberInt + 1;
        //9 -> 009, 10 -> 010
        return String.format("%03d", nextProductNumberInt);
    }

    public List<ProductResponse> getSellingProducts() {
        List<Product> products = productRepository.findAllBySellingStatusIn(forDisplay());
        return products.stream().map(ProductResponse::of).collect(Collectors.toList());
    }

}
