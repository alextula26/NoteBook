package ru.alexander.marchuk.notebook.model;

import java.io.Serializable;
import java.util.UUID;

public class NoteDetailModel implements Item, Serializable {

    private String mTitle;
    private String mDescription;
    private UUID mId;
    private Long mNoteId;
    private int mStatus;

    public NoteDetailModel(){
        this(UUID.randomUUID());
    }

    public NoteDetailModel(UUID id){
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

    public Long getNoteId() {
        return mNoteId;
    }

    public void setNoteId(Long noteId) {
        mNoteId = noteId;
    }

    @Override
    public boolean isNote() {
        return true;
    }
}
