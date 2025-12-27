package com.example.emyntra;

public class ProductItem {
    String brand;
    String price;
    String imageName; // This will be "men_1", "women_2", etc.
    boolean isLiked;  // Track if item is in Wishlist (Main pages)
    boolean isAddedToCart; // Track if item is in Cart (Search page)

    public ProductItem(String brand, String price, String imageName) {
        this.brand = brand;
        this.price = price;
        this.imageName = imageName;
        this.isLiked = false;       // Default: not liked
        this.isAddedToCart = false; // Default: not in cart
    }

    // Getters
    public String getBrand() { return brand; }
    public String getPrice() { return price; }
    public String getImageName() { return imageName; }
    public boolean isLiked() { return isLiked; }
    public boolean isAddedToCart() { return isAddedToCart; }

    // Setters
    public void setLiked(boolean liked) {
        isLiked = liked;
    }
    public void setAddedToCart(boolean added) {
        isAddedToCart = added;
    }
}