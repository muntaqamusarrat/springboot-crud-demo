package com.muntaqa.springcruddemo.controllers;

import com.muntaqa.springcruddemo.models.Category;
import com.muntaqa.springcruddemo.models.CategoryDto;
import com.muntaqa.springcruddemo.services.CategoryRepository;
import com.muntaqa.springcruddemo.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {
//    @Autowired
//    private CategoryRepository categoryRepository;

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/add")
    public String showAddCategoryForm(Model model) {
        Category category = new Category();
        model.addAttribute("category", category);
        return "products/AddCategory";
    }

    @PostMapping("/add")
    public String addCategory(@ModelAttribute("category") CategoryDto categoryDto) {
        //categoryRepository.save(category);
        Category category = new Category();
        category.setName(categoryDto.getName());

        categoryService.addCategory(category);
        return "redirect:/categories/add";
    }

    @GetMapping("/dropdown")
    public String showCategoryDropdown(Model model) {
//        List<Category> categories = categoryRepository.findAll();
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "category-dropdown";
    }

    @GetMapping("/edit/{id}")
    public String showEditCategoryForm(@PathVariable("id") Long id, Model model) {
        Category category = categoryService.getCategoryById(id);
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());

        model.addAttribute("category", category);
        return "products/EditCategory";
    }

    @PutMapping("/update/{id}")
    public String updateCategory(@PathVariable("id") Long id, @ModelAttribute("category") CategoryDto updatedCategoryDto) {
        Category category = categoryService.getCategoryById(id);
        if (category != null) {
            category.setName(updatedCategoryDto.getName());
            categoryService.updateCategory(id, category);
        }
        categoryService.updateCategory(id, category);

        return "redirect:/categories/dropdown";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/categories/dropdown";
    }

}

