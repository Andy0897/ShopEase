package com.example.ShopEase;

import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class ImageEncoder {
    public String encodeToBase64(byte[] image) {
        return Base64.getEncoder().encodeToString(image);
    }
}
