package ru.alexander.marchuk.notebook.model;

import java.io.Serializable;
import java.util.UUID;

public class NoteDetailModel implements Item, Serializable {

    private String mTitle;
    private String mDescription;
    private UUID mId;
    private String mNoteId;
    private int mStatus;

    public static final int STATUS_NEW_NOTE_DETAIL = 1;
    public static final int STATUS_CURRENT_NOTE_DETAIL = 1;
    public static final int STATUS_DONE_NOTE_DETAIL = 0;

    public NoteDetailModel(){
        this(UUID.randomUUID());
    }

    public NoteDetailModel(UUID id){
        mStatus = 1;
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

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getNoteId() {
        return mNoteId;
    }

    public void setNoteId(String noteId) {
        mNoteId = noteId;
    }

    @Override
    public boolean isNote() {
        return true;
    }
}
