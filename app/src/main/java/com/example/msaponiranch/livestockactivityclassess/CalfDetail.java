package com.example.msaponiranch.livestockactivityclassess;

public class CalfDetail {


    public String imageId;
    public String ranchHandName;

    public String femaleParent;
    public String maleParent;
    public String calfName;
    public String calfGender;
    public String calfBreed;
    public String calfAge;
    public String calfWeight;

    public CalfDetail() {
    }

    public CalfDetail(String imageId, String ranchHandName, String femaleParent, String maleParent, String calfName, String calfGender, String calfBreed, String calfAge, String calfWeight) {
        this.imageId = imageId;
        this.ranchHandName = ranchHandName;
        this.femaleParent = femaleParent;
        this.maleParent = maleParent;
        this.calfName = calfName;
        this.calfGender = calfGender;
        this.calfBreed = calfBreed;
        this.calfAge = calfAge;
        this.calfWeight = calfWeight;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getRanchHandName() {
        return ranchHandName;
    }

    public void setRanchHandName(String ranchHandName) {
        this.ranchHandName = ranchHandName;
    }

    public String getFemaleParent() {
        return femaleParent;
    }

    public void setFemaleParent(String femaleParent) {
        this.femaleParent = femaleParent;
    }

    public String getMaleParent() {
        return maleParent;
    }

    public void setMaleParent(String maleParent) {
        this.maleParent = maleParent;
    }

    public String getCalfName() {
        return calfName;
    }

    public void setCalfName(String calfName) {
        this.calfName = calfName;
    }

    public String getCalfGender() {
        return calfGender;
    }

    public void setCalfGender(String calfGender) {
        this.calfGender = calfGender;
    }

    public String getCalfBreed() {
        return calfBreed;
    }

    public void setCalfBreed(String calfBreed) {
        this.calfBreed = calfBreed;
    }

    public String getCalfAge() {
        return calfAge;
    }

    public void setCalfAge(String calfAge) {
        this.calfAge = calfAge;
    }

    public String getCalfWeight() {
        return calfWeight;
    }

    public void setCalfWeight(String calfWeight) {
        this.calfWeight = calfWeight;
    }
}
