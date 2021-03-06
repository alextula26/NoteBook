package ru.alexander.marchuk.notebook.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import ru.alexander.marchuk.notebook.database.NoteDbScheme.NoteTable;

import static ru.alexander.marchuk.notebook.database.NoteDbScheme.NoteDetailTable;


public class NoteBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 6;
    private static final String DATABASE_NAME = "notebook.db";

    public static final String SELECTION_NOTE_STATUS = NoteTable.Cols.STATUS + " = ?";
    public static final String SELECTION_NOTE_UUID = NoteTable.Cols.UUID + " = ?";
    public static final String SELECTION_NOTE_LIKE_TITLE = NoteTable.Cols.TITLE + " LIKE ?";

    public static final String SELECTION_NOTE_DETAIL_NOTEID = NoteDetailTable.Cols.NOTEID + " = ?";
    public static final String SELECTION_NOTE_DETAIL_UUID = NoteDetailTable.Cols.UUID + " = ?";
    public static final String SELECTION_NOTE_DETAIL_STATUS = NoteDetailTable.Cols.STATUS + " = ?";

    public NoteBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + NoteTable.NAME + "(" +
                BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NoteTable.Cols.UUID + " LONG, " +
                NoteTable.Cols.TITLE + " TEXT NOT NULL, " +
                NoteTable.Cols.DATE + " DATE, " +
                NoteTable.Cols.CLOCK + " DATE, " +
                NoteTable.Cols.STATUS + " INTEGER" +
                ")"
        );

        db.execSQL("CREATE TABLE " + NoteDetailTable.NAME + "(" +
                BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NoteDetailTable.Cols.UUID + " LONG, " +
                NoteDetailTable.Cols.NOTEID + " LONG, " +
                NoteDetailTable.Cols.TITLE + " TEXT NOT NULL, " +
                NoteDetailTable.Cols.STATUS + " INTEGER" +
                ")"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + NoteTable.NAME);
        db.execSQL("DROP TABLE " + NoteDetailTable.NAME);
        onCreate(db);
    }
}
