package com.muntaqa.springcruddemo.services;

import com.muntaqa.springcruddemo.models.Category;
import com.muntaqa.springcruddemo.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository repo;

    @Autowired
    public CategoryService(CategoryRepository repo) {
        this.repo = repo;
    }

    public Category addCategory(Category category) {
        return repo.save(category);
    }

    public List<Category> getAllCategories() {
        return repo.findAll();
    }

    public Category getCategoryById(Long id) {
        Optional<Category> optionalCategory = repo.findById(id);
        return optionalCategory.orElse(null);
    }

    public Category updateCategory(Long id, Category updatedCategory) {
        Category existingCategory = getCategoryById(id);
        if (existingCategory != null) {
            updatedCategory.setId(existingCategory.getId());
            return repo.save(updatedCategory);
        }
        return null;
    }

    public boolean deleteCategory(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }
}
