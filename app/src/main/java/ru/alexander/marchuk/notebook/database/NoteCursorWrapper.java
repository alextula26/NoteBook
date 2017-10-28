package ru.alexander.marchuk.notebook.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import ru.alexander.marchuk.notebook.database.NoteDbScheme.NoteTable;
import ru.alexander.marchuk.notebook.database.NoteDbScheme.NoteDetailTable;
import ru.alexander.marchuk.notebook.model.NoteModel;
import ru.alexander.marchuk.notebook.model.NoteDetailModel;


public class NoteCursorWrapper extends CursorWrapper {

    public NoteCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public NoteModel getNote(){
        String uuid = getString(getColumnIndex(NoteTable.Cols.UUID));
        String title = getString(getColumnIndex(NoteTable.Cols.TITLE));
        long date = getLong(getColumnIndex(NoteTable.Cols.DATE));
        long time = getLong(getColumnIndex(NoteTable.Cols.CLOCK));
        int status = getInt(getColumnIndex(NoteTable.Cols.STATUS));

        NoteModel noteModel = new NoteModel(UUID.fromString(uuid));
        noteModel.setTitle(title);
        noteModel.setDate(new Date(date));
        noteModel.setTime(new Date(time));
        noteModel.setStatus(status);

        return noteModel;
    }

    public NoteDetailModel getNoteDetail(){

        String uuid = getString(getColumnIndex(NoteDetailTable.Cols.UUID));
        String noteID = getString(getColumnIndex(NoteDetailTable.Cols.NOTEID));
        String title = getString(getColumnIndex(NoteDetailTable.Cols.TITLE));
        int status = getInt(getColumnIndex(NoteDetailTable.Cols.STATUS));

        NoteDetailModel noteDetailModel = new NoteDetailModel(UUID.fromString(uuid));
        noteDetailModel.setNoteId(noteID);
        noteDetailModel.setTitle(title);
        noteDetailModel.setStatus(status);

        return noteDetailModel;
    }
}