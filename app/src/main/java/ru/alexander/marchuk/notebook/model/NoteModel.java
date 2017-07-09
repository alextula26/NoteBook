package ru.alexander.marchuk.notebook.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class NoteModel implements Item, Serializable {

    private String mTitle;
    private Date mDate;
    private Date mTime;
    private UUID mId;
    private int mStatus;
    private int mDateStatus;

    public static final int STATUS_OVERDUE = 0;
    public static final int STATUS_CURRENT_NOTE = 1;
    public static final int STATUS_DONE_NOTE = 2;

    public NoteModel(){
        this(UUID.randomUUID());
    }

    public NoteModel(UUID id){
        mDate = new Date();
        mTime = new Date();
        mStatus = -1;
        mId = id;
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public Date getTime() {
        return mTime;
    }

    public void setTime(Date time) {
        mTime = time;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public int getDateStatus() {
        return mDateStatus;
    }

    public void setDateStatus(int dateStatus) {
        mDateStatus = dateStatus;
    }

    @Override
    public boolean isNote() {
        return true;
    }
}
