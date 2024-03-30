package com.example.msaponiranch.AccountantStuff;

public class SoldItemDetail {
    public String sdItemName;
    public String sdItemQuantity;
    public String sdItemDate;
    public String sdItemTotal;

    public SoldItemDetail() {
    }

    public SoldItemDetail(String sdItemName, String sdItemQuantity, String sdItemDate, String sdItemTotal) {
        this.sdItemName = sdItemName;
        this.sdItemQuantity = sdItemQuantity;
        this.sdItemDate = sdItemDate;
        this.sdItemTotal = sdItemTotal;
    }

    public String getSdItemName() {
        return sdItemName;
    }

    public void setSdItemName(String sdItemName) {
        this.sdItemName = sdItemName;
    }

    public String getSdItemQuantity() {
        return sdItemQuantity;
    }

    public void setSdItemQuantity(String sdItemQuantity) {
        this.sdItemQuantity = sdItemQuantity;
    }

    public String getSdItemDate() {
        return sdItemDate;
    }

    public void setSdItemDate(String sdItemDate) {
        this.sdItemDate = sdItemDate;
    }

    public String getSdItemTotal() {
        return sdItemTotal;
    }

    public void setSdItemTotal(String sdItemTotal) {
        this.sdItemTotal = sdItemTotal;
    }
}
