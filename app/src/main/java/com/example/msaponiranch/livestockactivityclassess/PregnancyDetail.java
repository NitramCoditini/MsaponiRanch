package com.example.msaponiranch.livestockactivityclassess;

public class PregnancyDetail {
    public String ranchHandName;
    public String femalePregnant;
    public String malePartner;
    public String datetoDeliver;

    public PregnancyDetail() {
    }

    public PregnancyDetail(String ranchHandName, String femalePregnant, String malePartner, String datetoDeliver) {
        this.ranchHandName = ranchHandName;
        this.femalePregnant = femalePregnant;
        this.malePartner = malePartner;
        this.datetoDeliver = datetoDeliver;
    }

    public String getRanchHandName() {
        return ranchHandName;
    }

    public void setRanchHandName(String ranchHandName) {
        this.ranchHandName = ranchHandName;
    }

    public String getFemalePregnant() {
        return femalePregnant;
    }

    public void setFemalePregnant(String femalePregnant) {
        this.femalePregnant = femalePregnant;
    }

    public String getMalePartner() {
        return malePartner;
    }

    public void setMalePartner(String malePartner) {
        this.malePartner = malePartner;
    }

    public String getDatetoDeliver() {
        return datetoDeliver;
    }

    public void setDatetoDeliver(String datetoDeliver) {
        this.datetoDeliver = datetoDeliver;
    }
}
