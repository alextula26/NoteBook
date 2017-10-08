package ru.alexander.marchuk.notebook.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ru.alexander.marchuk.notebook.database.NoteCursorWrapper;
import ru.alexander.marchuk.notebook.database.NoteBaseHelper;
import ru.alexander.marchuk.notebook.database.NoteDbScheme.NoteTable;

public class NoteModelLab {

    private static NoteModelLab sNoteModelLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static NoteModelLab get(Context context) {
        if (sNoteModelLab == null) {
            sNoteModelLab = new NoteModelLab(context);
        }
        return sNoteModelLab;
    }

    private NoteModelLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new NoteBaseHelper(mContext).getWritableDatabase();
    }

    private NoteCursorWrapper queryNote(String selection, String[] selectionArgs, String orderBy) {
        Cursor cursor = mDatabase.query(
                NoteTable.NAME,
                null, // Colums - null выбирает все столбцы
                selection,
                selectionArgs,
                null, // groupBy
                null, // having
                orderBy // orderBy
        );
        return new NoteCursorWrapper(cursor);
    }

    // Получить полный список упражнений
    public List<NoteModel> getNotes(String selection, String[] selectionArgs, String orderBy) {

        List<NoteModel> mNotesModel = new ArrayList<>();

        NoteCursorWrapper cursor = queryNote(selection, selectionArgs, orderBy);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                mNotesModel.add(cursor.getNote());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return mNotesModel;
    }

    public NoteModel getNote(String selection, String[] selectionArgs, String orderBy){

        NoteCursorWrapper cursor = queryNote(selection, selectionArgs, orderBy);

        try {
            if(cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getNote();
        }finally {
            cursor.close();
        }
    }

    private static ContentValues getContentValues(NoteModel noteModel) {
        ContentValues values = new ContentValues();
        values.put(NoteTable.Cols.UUID, noteModel.getId().toString());
        values.put(NoteTable.Cols.TITLE, noteModel.getTitle());
        values.put(NoteTable.Cols.DATE, noteModel.getDate().getTime());
        values.put(NoteTable.Cols.CLOCK, noteModel.getTime().getTime());
        values.put(NoteTable.Cols.STATUS, noteModel.getStatus());

        return values;
    }

    public void insertNote(NoteModel noteModel) {
        ContentValues values = getContentValues(noteModel);
        mDatabase.insert(NoteTable.NAME, null, values);
    }

    public void updateNote(NoteModel noteModel) {

        String uuid = noteModel.getId().toString();
        ContentValues values = getContentValues(noteModel);

        mDatabase.update(NoteTable.NAME, values,
                NoteBaseHelper.SELECTION_UUID,
                new String[]{uuid});

    }

    public void updateTitle(String uuid, String title) {
        updateNote(NoteTable.Cols.TITLE, uuid, title);
    }

    public void updateDate(String uuid, String date) {
        updateNote(NoteTable.Cols.DATE, uuid, date);
    }

    public void updateStatus(String uuid, String status) {
        updateNote(NoteTable.Cols.STATUS, uuid, status);
    }

    private static ContentValues getColumnContentValues(String column, String value) {
        ContentValues values = new ContentValues();
        values.put(column, value);
        return values;
    }

    private void updateNote(String column, String key, String value) {

        ContentValues values = getColumnContentValues(column, value);

        mDatabase.update(NoteTable.NAME, values,
                NoteBaseHelper.SELECTION_UUID,
                new String[]{key});
    }

    public void removeNote(String key){
        mDatabase.delete(NoteTable.NAME, NoteBaseHelper.SELECTION_UUID, new String[]{key});
    }


}
