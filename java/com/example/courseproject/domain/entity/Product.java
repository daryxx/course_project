package com.example.courseproject.domain.entity;

public class Product {
    private String name;
    private String count;
    private String price;
    private String barcode;

    public Product(){

    }

    public Product (String name, String price, String count, String barcode){
        this.barcode = barcode;
        this.setCount(count);
        this.setName(name);
        this.setPrice(price);
    }

    public void setCount(String count){
        this.count=count;
    }

    public void setName(String name){
        this.name=name;
    }

    public void setPrice(String price){
        this.price=price;
    }

    public String getBarcode() {
        return this.barcode;
    }


    public String getName() {
        return this.name;
    }

    public String getCount() {
        return this.count;
    }

    public String getPrice() {
        return this.price;
    }
}
