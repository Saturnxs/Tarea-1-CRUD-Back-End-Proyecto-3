package com.project.demo.rest.product;

import com.project.demo.logic.entity.product.Product;
import com.project.demo.logic.entity.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository ProductRepository;

    @PostMapping("/create")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Product createProduct(@RequestBody Product newProduct) {
        return ProductRepository.save(newProduct);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product Product) {
        return ProductRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(Product.getName());
                    existingProduct.setDescription(Product.getDescription());
                    existingProduct.setPrice(Product.getPrice());
                    existingProduct.setStock(Product.getStock());
                    existingProduct.setCategory(Product.getCategory());
                    return ProductRepository.save(existingProduct);
                })
                .orElseGet(() -> {
                    Product.setId(id);
                    return ProductRepository.save(Product);
                });
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public void deleteProduct(@PathVariable Long id) {
        ProductRepository.deleteById(id);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public Product getProductById(@PathVariable Long id) {
        return ProductRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public List<Product> getAllCategories() {
        return ProductRepository.findAll();
    }

}
