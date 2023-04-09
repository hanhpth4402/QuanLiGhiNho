package Model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reminder {
    private String title;
    private LocalDate beginDay;
    private LocalTime beginTime;
    private LocalDate endDay;
    private LocalTime endTime;
    private String content;
    private Boolean done;

    private static int MASO;

    private int MaR;

    public Reminder(){
        MASO += 1;
        setMaR(MASO);
    }

    public Reminder(int maR, String title, LocalDate beginDay, LocalTime beginTime, LocalDate endDay, LocalTime endTime, String content, Boolean done) {
        this.title = title;
        this.beginDay = beginDay;
        this.beginTime = beginTime;
        this.endDay = endDay;
        this.endTime = endTime;
        this.content = content;
        this.done = done;
        MaR = maR;
    }

    public static void setMASO(int MASO) {
        Reminder.MASO = MASO;
    }

    public static int getMASO() {
        return MASO;
    }

    public int getMaR() {
        return MaR;
    }

    public void setMaR(int maR) {
        MaR = maR;
    }

    public String getTitle() {
        return title;
    }

    public Boolean setTitle(String title) {
        if (title == "") return false;
        this.title = title;
        return true;
    }

    public LocalDate getBeginDay() {
        return beginDay;
    }

    public Boolean setBeginDay(LocalDate beginDay) {
        if (beginDay == null) return false;
        this.beginDay = beginDay;
        return true;
    }

    public LocalTime getBeginTime() { return beginTime; }

    public Boolean setBeginTime(LocalTime beginTime) {
        if (beginTime == null) return false;
        this.beginTime = beginTime;
        return true;
    }

    public LocalDate getEndDay() { return endDay; }

    public Boolean setEndDay(LocalDate endDay) {
        if (endDay == null || endDay.isBefore(getBeginDay())) return false;
        this.endDay = endDay;
        return true;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public Boolean setEndTime(LocalTime endTime) {
        if (endTime == null) return false;
        if (getEndDay().isEqual(getBeginDay()) && endTime.isBefore(getBeginTime())) return false;
        this.endTime = endTime;
        return true;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return "Reminder{" +
                "title='" + title + '\'' +
                ", beginDay=" + beginDay +
                ", beginTime=" + beginTime +
                ", endDay=" + endDay +
                ", endTime=" + endTime +
                ", content='" + content + '\'' +
                ", done=" + done +
                '}';
    }
}
