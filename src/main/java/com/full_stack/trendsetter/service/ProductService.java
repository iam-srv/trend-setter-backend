package com.full_stack.trendsetter.service;

import com.full_stack.trendsetter.exception.ProductException;
import com.full_stack.trendsetter.model.Product;
import com.full_stack.trendsetter.request.CreateProductRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    public Product createProduct( CreateProductRequest productRequest);

    public String deleteProduct(Long productId) throws ProductException;

    public Product updateProduct(Long ProductId , Product productReq) throws ProductException;

    public Product findProductById(Long id) throws ProductException;

    public List<Product> findProductByCategory(String category);

    public Page<Product> getAllProduct(String category , List<String> colors, List<String> sizes , Integer minPrice
            , Integer maxPrice , Integer minDiscount , String sort, String stock ,  Integer pageNumber , Integer pageSize);

    public List<Product> findAllProducts();
    // sort -> sort by low-high or high-low  // stock -> how much stock // pageNumber & pageSize -> this is for pagination so that we can limit the number of products that need to be put in a single page

}
