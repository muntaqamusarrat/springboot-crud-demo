package com.muntaqa.springcruddemo.services;

import com.muntaqa.springcruddemo.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository extends JpaRepository<Product, Integer> {
}