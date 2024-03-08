package com.example.msaponiranch.ManagerStuff;

public class AdministrationProcess {
    public String processName;
    public String dateAdministered;
    public String dosageInstruction;
    public String dosageAmount;

    public AdministrationProcess() {
    }

    public AdministrationProcess(String processName, String dateAdministered, String dosageInstruction, String dosageAmount) {
        this.processName = processName;
        this.dateAdministered = dateAdministered;
        this.dosageInstruction = dosageInstruction;
        this.dosageAmount = dosageAmount;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getDateAdministered() {
        return dateAdministered;
    }

    public void setDateAdministered(String dateAdministered) {
        this.dateAdministered = dateAdministered;
    }

    public String getDosageInstruction() {
        return dosageInstruction;
    }

    public void setDosageInstruction(String dosageInstruction) {
        this.dosageInstruction = dosageInstruction;
    }

    public String getDosageAmount() {
        return dosageAmount;
    }

    public void setDosageAmount(String dosageAmount) {
        this.dosageAmount = dosageAmount;
    }
}
