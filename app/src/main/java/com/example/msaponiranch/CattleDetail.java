package com.example.msaponiranch;

public class CattleDetail {
    public String imageId;
    public String cowname;
    public String gender;
    public String cowbreed;
    public String ageWithUnit;
    public String cowweight;

    public CattleDetail() {
    }

    public CattleDetail(String imageId, String cowname, String gender, String cowbreed, String ageWithUnit, String cowweight) {
        this.imageId = imageId;
        this.cowname = cowname;
        this.gender = gender;
        this.cowbreed = cowbreed;
        this.ageWithUnit = ageWithUnit;
        this.cowweight = cowweight;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getCowname() {
        return cowname;
    }

    public void setCowname(String cowname) {
        this.cowname = cowname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCowbreed() {
        return cowbreed;
    }

    public void setCowbreed(String cowbreed) {
        this.cowbreed = cowbreed;
    }

    public String getAgeWithUnit() {
        return ageWithUnit;
    }

    public void setAgeWithUnit(String ageWithUnit) {
        this.ageWithUnit = ageWithUnit;
    }

    public String getCowweight() {
        return cowweight;
    }

    public void setCowweight(String cowweight) {
        this.cowweight = cowweight;
    }
}
