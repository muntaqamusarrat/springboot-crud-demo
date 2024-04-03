package com.muntaqa.springcruddemo.services;

import com.muntaqa.springcruddemo.models.Category;
import com.muntaqa.springcruddemo.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
