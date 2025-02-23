package com.example.ShopEase.Product;

import com.example.ShopEase.Brand.Brand;
import com.example.ShopEase.Category.Category;
import com.example.ShopEase.Size.ProductSize;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "products")
public class Product {
    @Column(name = "product_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 6, message = "Заглавието трябва да съдържа поне 6 символа")
    @Size(max = 50, message = "Заглавието не трябва да надвишава 50 символа.")
    private String title;

    @Size(min = 6, message = "Описанието трябва да съдържа поне 6 символа")
    @Size(max = 500, message = "Описание не трябва да надвишава 500 символа.")
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @NotNull(message = "Полето е задължително")
    private Category category;

    @ElementCollection
    @Lob
    private List<byte[]> images = new ArrayList<>();

    private Integer mainImageIndex;

    @Min(value = 1, message = "Цената трябва да бъде поне 1 лв.")
    private double price;

    public Product(Long id, String title, String description, Category category, List<byte[]> images, Integer mainImageIndex, double price) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.images = images;
        this.mainImageIndex = mainImageIndex;
        this.price = price;
    }
}
