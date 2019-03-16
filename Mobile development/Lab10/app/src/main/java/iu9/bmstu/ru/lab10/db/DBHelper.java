package iu9.bmstu.ru.lab10.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "database.db";
    private static final int DB_VERSION = 2;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = String.format("CREATE TABLE %s ( " +
                "%s INTEGER PRIMARY KEY, " +
                "%s TEXT NOT NULL, " +
                "%s TEXT NOT NULL, " +
                "%s TIMESTAMP NOT NULL);",
                DBContract.DBEntry.TABLE_NAME, DBContract.DBEntry._ID, DBContract.DBEntry.COLUMN1, DBContract.DBEntry.COLUMN2, DBContract.DBEntry.COLUMN3  );
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       db.execSQL(String.format("DROP TABLE %s ;",DBContract.DBEntry.TABLE_NAME));
       onCreate(db);
    }
}
