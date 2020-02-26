package edu.dartmouth.stayfocus;

public class Entry {

    private String startTime;
    private String endTime;
    private String duration;
    private String success;

    public Entry() {}
    public Entry(String startTime, String endTime, String duration, String success) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.success = success;
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}

