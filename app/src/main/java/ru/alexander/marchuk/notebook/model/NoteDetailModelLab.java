package ru.alexander.marchuk.notebook.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ru.alexander.marchuk.notebook.database.NoteCursorWrapper;
import ru.alexander.marchuk.notebook.database.NoteBaseHelper;
import static ru.alexander.marchuk.notebook.database.NoteDbScheme.NoteDetailTable;

public class NoteDetailModelLab {

    private static NoteDetailModelLab sNoteDetailModelLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static NoteDetailModelLab get(Context context) {
        if (sNoteDetailModelLab == null) {
            sNoteDetailModelLab = new NoteDetailModelLab(context);
        }
        return sNoteDetailModelLab;
    }

    private NoteDetailModelLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new NoteBaseHelper(mContext).getWritableDatabase();
    }

    private NoteCursorWrapper queryNoteDetail(String selection, String[] selectionArgs, String orderBy) {

        Cursor cursor = mDatabase.query(
                NoteDetailTable.NAME,
                null, // Colums - null выбирает все столбцы
                selection,
                selectionArgs,
                null, // groupBy
                null, // having
                orderBy // orderBy
        );

        return new NoteCursorWrapper(cursor);
    }

    public List<NoteDetailModel> getNotesDetail(String selection, String[] selectionArgs, String orderBy) {

        List<NoteDetailModel> mNotesDetailModel = new ArrayList<>();

        NoteCursorWrapper cursor = queryNoteDetail(selection, selectionArgs, orderBy);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                mNotesDetailModel.add(cursor.getNoteDetail());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return mNotesDetailModel;
    }

    public NoteDetailModel getNoteDetail(String selection, String[] selectionArgs, String orderBy){

        NoteCursorWrapper cursor = queryNoteDetail(selection, selectionArgs, orderBy);

        try {
            if(cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getNoteDetail();
        }finally {
            cursor.close();
        }
    }
}