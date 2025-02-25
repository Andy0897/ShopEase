package com.example.ShopEase.Category;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category {
    @Column(name = "category_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
