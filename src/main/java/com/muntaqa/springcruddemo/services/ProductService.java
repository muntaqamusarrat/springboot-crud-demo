package com.muntaqa.springcruddemo.services;

import com.muntaqa.springcruddemo.models.Product;
import com.muntaqa.springcruddemo.models.ProductDto;
import com.muntaqa.springcruddemo.services.ProductsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductsRepository repo;

    @Autowired
    public ProductService(ProductsRepository repo) {
        this.repo = repo;
    }

    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    public Product getProductById(int id) {
        return repo.findById(id).orElse(null);
    }

    public Product createProduct(Product product) {
        return repo.save(product);
    }

//    public Product updateProduct(Product product) {
//        return repo.save(product);
//    }

    @Transactional
    public Product updateProduct(int id, ProductDto productDto) {

        Product productToUpdate = convertDtoToProduct(id, productDto);

        Product existingProduct = getProductById(id);

        existingProduct.setName(productToUpdate.getName());
        existingProduct.setBrand(productToUpdate.getBrand());
        existingProduct.setCategory(productToUpdate.getCategory());
        existingProduct.setPrice(productToUpdate.getPrice());
        existingProduct.setDescription(productToUpdate.getDescription());



        return repo.save(existingProduct);
    }


    private Product convertDtoToProduct(int id, ProductDto productDto) {
        Product product = new Product();
        product.setId(id); // Set the ID from the path variable
        product.setName(productDto.getName());
        product.setBrand(productDto.getBrand());
        product.setCategory(productDto.getCategory());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        // Handle image file conversion if needed
        return product;
    }


    public void deleteProductById(int id) {
        repo.deleteById(id);
    }

    @Transactional
    public Product updateProduct(Product product) {
        try {

            Product updatedProduct = repo.save(product);


            return updatedProduct;
        } catch (Exception ex) {
            // Handle any exceptions here
            System.out.println("Exception: " + ex.getMessage());
            throw new RuntimeException("Failed to update product: " + ex.getMessage());
        }
    }
}

