package com.full_stack.trendsetter.service;

import com.full_stack.trendsetter.exception.ProductException;
import com.full_stack.trendsetter.model.Category;
import com.full_stack.trendsetter.model.Product;
import com.full_stack.trendsetter.repository.CategoryRepository;
import com.full_stack.trendsetter.repository.ProductRepository;
import com.full_stack.trendsetter.request.CreateProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private  UserService userService;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Product createProduct(CreateProductRequest productRequest) {
        Category topLevel = categoryRepository.findByName(productRequest.getTopLevelCategory());

        if(topLevel == null){
            Category topLevelCategory = new Category();
            topLevelCategory.setName(productRequest.getTopLevelCategory());
            topLevelCategory.setLevel(1);

            topLevel = categoryRepository.save(topLevelCategory);
        }
        Category secondLevel = categoryRepository.findByNameAndParent(productRequest.getSecondLevelCategory() ,topLevel.getName());
        if(secondLevel == null){
            Category secondLevelCategory = new Category();
            secondLevelCategory.setName(productRequest.getSecondLevelCategory());
            secondLevelCategory.setParentCategory(topLevel);
            secondLevelCategory.setLevel(2);
            secondLevel = categoryRepository.save(secondLevelCategory);
        }
        Category thirdLevel = categoryRepository.findByNameAndParent(productRequest.getThirdLevelCategory() , secondLevel.getName());

        if(thirdLevel == null){
            Category thirdLevelCategory  = new Category();
            thirdLevelCategory.setName(productRequest.getThirdLevelCategory());
            thirdLevelCategory.setParentCategory(secondLevel);
            thirdLevelCategory.setLevel(3);

            thirdLevel = categoryRepository.save(thirdLevelCategory);
        }
        Product product = new Product();

        product.setTitle(productRequest.getTitle());
        product.setColor(productRequest.getColor());
        product.setDescription(productRequest.getDescription());
        product.setDiscountedPrice(productRequest.getDiscountedPrice());
        product.setDiscountPercent(productRequest.getDiscountPercent());
        product.setImageUrl(productRequest.getImageUrl());
        product.setBrand(productRequest.getBrand());
        product.setPrice(productRequest.getPrice());
        product.setSizes(productRequest.getSize());
        product.setQuantity(productRequest.getQuantity());
        product.setCategory(thirdLevel);
        product.setCreatedAt(LocalDateTime.now());

        Product savedProduct = productRepository.save(product);
        return savedProduct;
    }

    @Override
    public String deleteProduct(Long productId) throws ProductException {
        Product product = findProductById(productId);
        product.getSizes().clear();
        productRepository.delete(product);
        return "Product Deleted SuccessFully";
    }

    @Override
    public Product updateProduct(Long productId, Product productReq) throws ProductException {
        Product product = findProductById(productId);
        if(productReq.getQuantity() != 0 ){
            product.setQuantity(productReq.getQuantity());
        }
        // we can update all the parameters of the product but for now quantity is updated

        return productRepository.save(product);
    }

    @Override
    public Product findProductById(Long id) throws ProductException {
        Optional<Product> opt = productRepository.findById(id);

        if(opt.isPresent()){
            return opt.get();
        }
        throw new ProductException("Product not found with given id -"+ id);
    }

    @Override
    public List<Product> findProductByCategory(String category) {
        return null;
    }

    @Override
    public Page<Product> getAllProduct(String category, List<String> colors, List<String> sizes,
                                       Integer minPrice, Integer maxPrice, Integer minDiscount,
                                       String sort, String stock, Integer pageNumber, Integer pageSize) {

        // Get the filtered products based on the specified criteria
        List<Product> products = productRepository.filterProducts(category, minPrice, maxPrice, minDiscount, sort);

        // Apply additional filters based on colors and stock
        if (!colors.isEmpty()) {
            products = products.stream()
                    .filter(p -> colors.stream().anyMatch(c -> c.equalsIgnoreCase(p.getColor())))
                    .collect(Collectors.toList());
        }

        if (stock != null) {
            if (stock.equals("in_stock")) {
                products = products.stream().filter(p -> p.getQuantity() > 0).collect(Collectors.toList());
            } else if (stock.equals("out_of_stock")) {
                products = products.stream().filter(p -> p.getQuantity() < 1).collect(Collectors.toList());
            }
        }

        // Calculate start and end indices for pagination
        int totalProducts = products.size();
        int startIndex = pageNumber * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalProducts);

        // Ensure the indices are within bounds
        if (startIndex > endIndex || startIndex < 0 || endIndex > totalProducts) {
            throw new IllegalArgumentException("Invalid page number or page size");
        }

        // Get the sublist for the current page
        List<Product> pageContent = products.subList(startIndex, endIndex);

        // Create and return a Page object
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return new PageImpl<>(pageContent, pageable, totalProducts);
    }


    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }
}
