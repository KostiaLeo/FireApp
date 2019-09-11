package com.example.fireapp;

//класс продукта
public class Product {
    private String name, desc, category, collection;
    private int price;

    public Product() {
    }

    public Product(String name, String desc, String category, String collection, int price) {
        this.name = name;
        this.desc = desc;
        this.category = category;
        this.collection = collection;
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}