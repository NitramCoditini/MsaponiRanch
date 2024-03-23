package com.example.msaponiranch.ManagerStuff;

public class Worker {
    public String name;
    public String email;
    public String imageId;
    public String userId;

    public Worker() {
    }

    public Worker(String name, String email, String imageId, String userId) {
        this.name = name;
        this.email = email;
        this.imageId = imageId;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
