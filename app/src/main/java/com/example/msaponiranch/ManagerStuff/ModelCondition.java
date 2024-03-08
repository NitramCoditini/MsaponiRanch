package com.example.msaponiranch.ManagerStuff;

public class ModelCondition {
    public String ranchHandName;
    public String cowname1;
    public String cowtemperature;
    public String cowappetite;
    public String cowappearance;

    public ModelCondition() {
    }

    public ModelCondition(String ranchHandName, String cowname1, String cowtemperature, String cowappetite, String cowappearance) {
        this.ranchHandName = ranchHandName;
        this.cowname1 = cowname1;
        this.cowtemperature = cowtemperature;
        this.cowappetite = cowappetite;
        this.cowappearance = cowappearance;
    }

    public String getRanchHandName() {
        return ranchHandName;
    }

    public void setRanchHandName(String ranchHandName) {
        this.ranchHandName = ranchHandName;
    }

    public String getCowname1() {
        return cowname1;
    }

    public void setCowname1(String cowname1) {
        this.cowname1 = cowname1;
    }

    public String getCowtemperature() {
        return cowtemperature;
    }

    public void setCowtemperature(String cowtemperature) {
        this.cowtemperature = cowtemperature;
    }

    public String getCowappetite() {
        return cowappetite;
    }

    public void setCowappetite(String cowappetite) {
        this.cowappetite = cowappetite;
    }

    public String getCowappearance() {
        return cowappearance;
    }

    public void setCowappearance(String cowappearance) {
        this.cowappearance = cowappearance;
    }
}
