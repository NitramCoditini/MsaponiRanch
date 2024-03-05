package com.example.msaponiranch.ManagerStuff;

public class Model4 {
    public String cowName;
    public String feedNameOf;
    public Double quantity;
    public Long currentTime;

    public Model4() {
    }

    public Model4(String cowName, String feedNameOf, Double quantity, Long currentTime) {
        this.cowName = cowName;
        this.feedNameOf = feedNameOf;
        this.quantity = quantity;
        this.currentTime = currentTime;
    }

    public String getCowName() {
        return cowName;
    }

    public void setCowName(String cowName) {
        this.cowName = cowName;
    }

    public String getFeedNameOf() {
        return feedNameOf;
    }

    public void setFeedNameOf(String feedNameOf) {
        this.feedNameOf = feedNameOf;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Long currentTime) {
        this.currentTime = currentTime;
    }
}
