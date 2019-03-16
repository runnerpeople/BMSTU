package iu9.bmstu.ru.lab10.db;

import android.provider.BaseColumns;

public class DBContract {

    private DBContract() {
    }

    public static class DBEntry implements BaseColumns {
        public static final String TABLE_NAME = "Meeting";
        public static final String COLUMN1 = "name";
        public static final String COLUMN2 = "place";
        public static final String COLUMN3 = "meetingTime";
    }

}