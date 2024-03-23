package com.example.msaponiranch.ManagerStuff;

public class TaskDetail {
    public String workerUserId;
    public String title;
    public String description;

    public String cowName;
    public String enteredDate;
    public String startTime;
    public String endTime;
    public int progressText; // Stored as an integer for easy manipulation

    public TaskDetail() {
    }

    public TaskDetail(String workerUserId, String title, String description, String cowName, String enteredDate, String startTime, String endTime, int progressText) {
        this.workerUserId = workerUserId;
        this.title = title;
        this.description = description;
        this.cowName = cowName;
        this.enteredDate = enteredDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.progressText = progressText;
    }

    public String getWorkerUserId() {
        return workerUserId;
    }

    public void setWorkerUserId(String workerUserId) {
        this.workerUserId = workerUserId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCowName() {
        return cowName;
    }

    public void setCowName(String cowName) {
        this.cowName = cowName;
    }

    public String getEnteredDate() {
        return enteredDate;
    }

    public void setEnteredDate(String enteredDate) {
        this.enteredDate = enteredDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getProgressText() {
        return progressText;
    }

    public void setProgressText(int progressText) {
        this.progressText = progressText;
    }
}
