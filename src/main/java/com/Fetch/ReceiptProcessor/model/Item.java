package com.Fetch.ReceiptProcessor.model;

public class Item {
    private String shortDescription;
    private String price;

    public Item() {

    }

    public Item(String shortDescription, String price) {
        this.shortDescription = shortDescription;
        this.price = price;
    }

    // Getters and Setters
    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDiscription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
