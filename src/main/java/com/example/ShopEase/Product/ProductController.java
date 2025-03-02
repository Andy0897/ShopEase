package com.example.ShopEase.Product;

import com.example.ShopEase.Category.*;
import com.example.ShopEase.ImageEncoder;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Controller
@RequestMapping("/products")
public class ProductController {
    private ProductRepository productRepository;
    private ProductService productService;
    private CategoryRepository categoryRepository;

    public ProductController(ProductRepository productRepository, ProductService productService, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.productService = productService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("areImagesSelected", true);
        return "product/add";
    }

    @PostMapping("/submit")
    public String saveProduct(@RequestParam("images") MultipartFile[] images, @RequestParam(value = "mainImageIndex", required = false) Integer mainImageIndex, @ModelAttribute @Valid Product product, BindingResult bindingResult, Model model) {
        if (mainImageIndex == null || mainImageIndex < 0 || mainImageIndex >= images.length) {
            mainImageIndex = 0;
        }
        return productService.submitProduct(images, mainImageIndex, product, bindingResult, model);
    }

    @GetMapping
    public String getShowAllProducts(@RequestParam(value = "search", required = false) String search, Model model) {
        List<Product> products;
        if (search != null) {
            products = productService.generateSearch(search);
        } else {
            products = (List<Product>) productRepository.findAll();
        }
        model.addAttribute("products", products);
        model.addAttribute("encoder", new ImageEncoder());
        model.addAttribute("categories", categoryRepository.findAll());
        return "product/show-all";
    }

    @GetMapping("/{productId}")
    public String getShowSingleProduct(@PathVariable("productId") Long productId, Model model) {
        Product product = productRepository.findById(productId).get();
        model.addAttribute("product", product);
        model.addAttribute("encoder", new ImageEncoder());
        return "product/show-single";
    }

    @PostMapping("/submit-add-quantity")
    public String getSubmitAddQuantity(@RequestParam("productId") Long productId, @RequestParam("quantity") Integer quantity) {
        return productService.submitAddQuantity(productId, quantity);
    }
}