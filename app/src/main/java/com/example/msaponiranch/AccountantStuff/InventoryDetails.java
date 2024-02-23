package com.example.msaponiranch.AccountantStuff;

public class InventoryDetails {
    public String imageId;
    public String feedName;
    public String feedWeight;
    public String date;

    public InventoryDetails() {
    }

    public InventoryDetails(String imageId, String feedName, String feedWeight, String date) {
        this.imageId = imageId;
        this.feedName = feedName;
        this.feedWeight = feedWeight;
        this.date = date;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
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
