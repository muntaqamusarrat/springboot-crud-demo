package com.muntaqa.springcruddemo.controllers;

import com.muntaqa.springcruddemo.models.Product;
import com.muntaqa.springcruddemo.models.ProductDto;
import com.muntaqa.springcruddemo.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = "http://localhost:4200/")
public class ProductRestController {

    private final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping({"", "/"})
    public List<Product> showProductList() {
        List<Product> products = productService.getAllProducts();
        return products;
    }

    @GetMapping("/create")
    public ProductDto showCreatePage(Model model) {
        ProductDto productDto = new ProductDto();
        model.addAttribute("productDto", productDto);
        // model.addAttribute("categories", categoryService.getAllCategories());
        return productDto;
    }

//    @PostMapping("/create")
//    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductDto productDto) {
//        Product createdProduct = productService.createProduct(productDto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
//    }
//
@GetMapping("/edit/{id}")
public ResponseEntity<ProductDto> getProductForEdit(@PathVariable int id) {
    try {
        Product product = productService.getProductById(id);
        ProductDto productDto = new ProductDto();
        productDto.setName(product.getName());
        productDto.setBrand(product.getBrand());
        productDto.setCategory(product.getCategory());
        productDto.setPrice(product.getPrice());
        productDto.setDescription(product.getDescription());
        return ResponseEntity.ok(productDto);
    } catch (Exception ex) {
        System.out.println("Exception: " + ex.getMessage());
        return ResponseEntity.notFound().build();
    }
}
    @PutMapping("/edit/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable("id") String id,
            @Valid @RequestBody ProductDto productDto
    ) {
        Product updatedProduct = productService.updateProduct(Integer.valueOf(id), productDto);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") int id) {
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }
}
