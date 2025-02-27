package com.example.ShopEase.Product;

import com.example.ShopEase.Category.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class ProductService {
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public String submitProduct(MultipartFile[] images, Integer mainImageIndex, Product product, BindingResult bindingResult, Model model) {
        List<byte[]> imageList = new ArrayList<>();
        boolean nullImages = true;
        try {
            for (MultipartFile file : images) {
                if (!file.isEmpty()) {
                    imageList.add(file.getBytes());
                    nullImages = false;
                }
            }
            product.setImages(imageList);
            product.setMainImageIndex(mainImageIndex);;
        } catch (IOException e) {
            model.addAttribute("product", product);
            model.addAttribute("hasUploadError", true);
            return "product/add";
        }

        if(bindingResult.hasFieldErrors("title") || bindingResult.hasFieldErrors("description") ||
                bindingResult.hasFieldErrors("category") ||
                bindingResult.hasFieldErrors("price") ||
                bindingResult.hasFieldErrors("quantity") || nullImages) {
            model.addAttribute("product", product);
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("areImagesSelected", !nullImages);
            return "product/add";
        }
        product.setViews(0);

        productRepository.save(product);
        return "redirect:/products";
    }

    public String submitIncreaseViews(Long productId) {
        Product product = productRepository.findById(productId).get();
        product.setViews(product.getViews() + 1);
        productRepository.save(product);
        return "redirect:/products/" + productId;
    }

    public String submitAddQuantity(Long productId, Integer quantity) {
        if(quantity == null || quantity > 0) {
            Product product = productRepository.findById(productId).get();
            product.setQuantity(product.getQuantity() + quantity);
            productRepository.save(product);
            return "redirect:/products/" + productId;
        }
        return "redirect:/products/" + productId + "?quantityError";
    }

    public List<Product> generateSearch(String search) {
        List<Product> products = (List<Product>) productRepository.findAll();
        products = products.stream().filter(product -> product.getTitle().toLowerCase().contains(search.toLowerCase())).toList();
        return products;
    }
}
