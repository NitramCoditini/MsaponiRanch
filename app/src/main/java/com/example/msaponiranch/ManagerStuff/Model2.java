package com.example.msaponiranch.ManagerStuff;

public class Model2 {

    public String totalPriceStr;
    public String feedName;
    public String feedWeight;
    public String date;

    public Model2() {
    }

    public Model2(String totalPriceStr, String feedName, String feedWeight, String date) {
        this.totalPriceStr = totalPriceStr;
        this.feedName = feedName;
        this.feedWeight = feedWeight;
        this.date = date;
    }

    public String getTotalPriceStr() {
        return totalPriceStr;
    }

    public void setTotalPriceStr(String totalPriceStr) {
        this.totalPriceStr = totalPriceStr;
    }

    public String getFeedName() {
        return feedName;
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }

    public String getFeedWeight() {
        return feedWeight;
    }

    public void setFeedWeight(String feedWeight) {
        this.feedWeight = feedWeight;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
