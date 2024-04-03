package com.muntaqa.springcruddemo.controllers;

import com.muntaqa.springcruddemo.models.Category;
import com.muntaqa.springcruddemo.models.Product;
import com.muntaqa.springcruddemo.models.ProductDto;
import com.muntaqa.springcruddemo.services.CategoryService;
import com.muntaqa.springcruddemo.services.ProductService;
import com.muntaqa.springcruddemo.services.ProductsRepository;
import jakarta.validation.Valid;
import org.hibernate.validator.constraints.ModCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

@Controller
@RequestMapping("/products")
@CrossOrigin(origins = "http://localhost:4200/")
// ^^ front-end url ^^
public class ProductsController {
    private final CategoryService categoryService;
    private final ProductService productService;
//    @Autowired
//    private ProductsRepository repo;

    @Autowired
    public ProductsController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping({"", "/"})
    public String showProductList(Model model) {
//      List<Product> products = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "products/index";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        ProductDto productDto = new ProductDto();
        model.addAttribute("productDto", productDto);
        //category now shows dynamic value
        model.addAttribute("categories", categoryService.getAllCategories());
        //Category category = new Category();
        //model.addAttribute("categories", category);
        return "products/CreateProduct";
    }

    @PostMapping("/create")
    public String createProduct(
            @Valid @ModelAttribute ProductDto productDto,
            BindingResult result
    ) {
        if (productDto.getImageFile().isEmpty()) {
            result.addError(new FieldError("productDto","imageFile", "The image file is required"));
        }

        if (result.hasErrors()) {
            return "products/CreateProduct";
        }

        MultipartFile image = productDto.getImageFile();
        Date createdAt = new Date();
        String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

        try{
            String uploadDir = "public/images/";
            Path uploadPath = Paths.get(uploadDir);

            if(!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = image.getInputStream()){
                Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        Product product = new Product();
        product.setName(productDto.getName());
        product.setBrand(productDto.getBrand());
        product.setCategory(productDto.getCategory());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setCreatedAt(createdAt);
        product.setImageFileName(storageFileName);

        //repo.save(product);
        productService.createProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/edit")
    public String showEditPage(
            Model model,
            @RequestParam int id
    ) {
        try{
//          Product product = repo.findById(id).get();
            Product product = productService.getProductById(id);
            model.addAttribute("product", product);

            ProductDto productDto = new ProductDto();
            productDto.setName(product.getName());
            productDto.setBrand(product.getBrand());
            productDto.setCategory(product.getCategory());
            productDto.setPrice(product.getPrice());
            productDto.setDescription(product.getDescription());

            model.addAttribute("productDto", productDto);
        } catch(Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/products";
        }
        return "products/EditProduct";
    }

//    @PostMapping("/edit")
//    public String updateProduct(
//            Model model,
//            @RequestParam int id,
//            @Valid @ModelAttribute ProductDto productDto,
//            BindingResult result
//    ) {
//        try{
////            Product product = repo.findById(id).get();
//            Product product = productService.getProductById(id);
//            model.addAttribute("product", product);
//
//            if(result.hasErrors()) {
//                return "products/EditProduct";
//            }
//
//            if(!productDto.getImageFile().isEmpty()) {
//                String uploadDir = "public/images/";
//                Path oldImagePath = Paths.get(uploadDir + product.getImageFileName());
//
//                try{
//                    Files.delete(oldImagePath);
//                } catch(Exception ex){
//                    System.out.println("Exception: " + ex.getMessage());
//                }
//
//                MultipartFile image = productDto.getImageFile();
//                Date createdAt = new Date();
//                String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();
//
//                try (InputStream inputStream = image.getInputStream()) {
//                    Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
//                            StandardCopyOption.REPLACE_EXISTING);
//                }
//                product.setImageFileName(storageFileName);
//            }
//            product.setName(productDto.getName());
//            product.setBrand(productDto.getBrand());
//            product.setCategory(productDto.getCategory());
//            product.setPrice(productDto.getPrice());
//            product.setDescription(productDto.getDescription());
//
//            productService.updateProduct(product);
//        }
//        catch(Exception ex){
//            System.out.println("Exception: " + ex.getMessage());
//        }
//        return "redirect:/products";
//    }

    @PostMapping("/edit")
    public String updateProduct(
            Model model,
            @RequestParam int id,
            @Valid @ModelAttribute ProductDto productDto,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                return "products/EditProduct";
            }

            Product product = productService.getProductById(id);
            model.addAttribute("product", product);

            if (productDto.getImageFile() != null && !productDto.getImageFile().isEmpty()) {
                String uploadDir = "public/images/";
                Path oldImagePath = Paths.get(uploadDir + product.getImageFileName());

                try {
                    Files.delete(oldImagePath);
                } catch (Exception ex) {
                    System.out.println("Exception: " + ex.getMessage());
                }

                MultipartFile image = productDto.getImageFile();
                Date createdAt = new Date();
                String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

                try {
                    String uploadPath = uploadDir + storageFileName;
                    Files.copy(image.getInputStream(), Paths.get(uploadPath),
                            StandardCopyOption.REPLACE_EXISTING);
                    product.setImageFileName(storageFileName);
                } catch (Exception ex) {
                    System.out.println("Exception: " + ex.getMessage());
                }
            }

            product.setName(productDto.getName());
            product.setBrand(productDto.getBrand());
            product.setCategory(productDto.getCategory());
            product.setPrice(productDto.getPrice());
            product.setDescription(productDto.getDescription());

            productService.updateProduct(product);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        return "redirect:/products";
    }

    @GetMapping("/delete")
    public String deleteProduct (
            @RequestParam int id
    ) {
        try{
//            Product product = repo.findById(id).get();
            Product product = productService.getProductById(id);

            Path imagePath  = Paths.get("public/images/" + product.getImageFileName());
            try{
                Files.delete(imagePath);
            } catch(Exception ex){
                System.out.println("Exception: " + ex.getMessage());
            }
//            repo.delete(product);
            productService.deleteProductById(id);
        }
        catch(Exception ex){
            System.out.println("Exception: " + ex.getMessage());
        }
        return "redirect:/products";
    }
}

