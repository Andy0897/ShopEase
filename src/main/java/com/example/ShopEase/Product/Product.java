package com.example.ShopEase.Product;

import com.example.ShopEase.Category.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
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

    @Min(value = 0)
    private int quantity;

    private int views;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<byte[]> getImages() {
        return images;
    }

    public void setImages(List<byte[]> images) {
        this.images = images;
    }

    public Integer getMainImageIndex() {
        return mainImageIndex;
    }

    public void setMainImageIndex(Integer mainImageIndex) {
        this.mainImageIndex = mainImageIndex;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }
}