package com.example.courseproject.domain.entity;

public class Discount {
    private String type;
    private String amount;
    private String afterPrice;
    private String barcode;

    public Discount (String type, String amount, String afterPrice, String barcode){
        this.barcode = barcode;
        this.setType(type);
        this.setAmount(amount);
        this.setAfterPrice(afterPrice);
    }

    public Discount(){

    }
    public void setType(String type){
        this.type=type;
    }

    public void setAmount(String amount){
        this.amount=amount;
    }

    public void setAfterPrice(String afterPrice){
        this.afterPrice=afterPrice;
    }

    public String getBarcode() {
        return this.barcode;
    }

    public String getType() {
        return this.type;
    }

    public String getAmount() {
        return this.amount;
    }

    public String getAfterPrice() {
        return this.afterPrice;
    }
}
