package ru.alexander.marchuk.notebook.database;

public class NoteDbScheme {

    public static final class NoteTable {
        public static final String NAME = "note";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String CLOCK = "clock";
            public static final String STATUS = "status";
        }
    }

    public  static final class NoteDetailTable {

        public static final String NAME = "note_more";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String NOTEID = "note_id";
            public static final String TITLE = "title";
            public static final String DESCRIPTION = "description";
            public static final String STATUS = "status";

        }
    }
}
