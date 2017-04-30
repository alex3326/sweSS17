package at.sw2017.swess17.application;

import java.util.Date;

/**
 * Created by mRozwold on 30.04.2017.
 */


public class Task {
    public String getText_description() {
        return text_description_;
    }

    public void setText_description(String text_description_) {
        this.text_description_ = text_description_;
    }

    public String getName() {
        return name_;
    }

    public void setName(String name_) {
        this.name_ = name_;
    }

    public Date getDate() {
        return date_;
    }

    public void setDate(Date date) {
        this.date_ = date;
    }

    private String text_description_;
    private String name_;
    private Date date_;

    public Task(String name, String text_description, Date date) {
        this.text_description_ = text_description;
        this.name_ = name;
        this.date_ = date;
    }
}
