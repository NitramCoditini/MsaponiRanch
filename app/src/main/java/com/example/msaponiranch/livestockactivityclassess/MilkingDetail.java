package com.example.msaponiranch.livestockactivityclassess;

public class MilkingDetail {
    public String ranchHandName;
    public String cowMiName;
    public String cowMiBreed;
    public String milkSize;
    public Long currentTime;

    public MilkingDetail() {
    }

    public MilkingDetail(String ranchHandName, String cowMiName, String cowMiBreed, String milkSize, Long currentTime) {
        this.ranchHandName = ranchHandName;
        this.cowMiName = cowMiName;
        this.cowMiBreed = cowMiBreed;
        this.milkSize = milkSize;
        this.currentTime = currentTime;
    }

    public String getRanchHandName() {
        return ranchHandName;
    }

    public void setRanchHandName(String ranchHandName) {
        this.ranchHandName = ranchHandName;
    }

    public String getCowMiName() {
        return cowMiName;
    }

    public void setCowMiName(String cowMiName) {
        this.cowMiName = cowMiName;
    }

    public String getCowMiBreed() {
        return cowMiBreed;
    }

    public void setCowMiBreed(String cowMiBreed) {
        this.cowMiBreed = cowMiBreed;
    }

    public String getMilkSize() {
        return milkSize;
    }

    public void setMilkSize(String milkSize) {
        this.milkSize = milkSize;
    }

    public Long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Long currentTime) {
        this.currentTime = currentTime;
    }
}
