package dev.iamfoodie.rxjava.models;

public class Task {

    private String description;
    private int priority;
    private boolean isComplete;

    public Task(String description, int priority, boolean isComplete) {
        this.description = description;
        this.priority = priority;
        this.isComplete = isComplete;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}
