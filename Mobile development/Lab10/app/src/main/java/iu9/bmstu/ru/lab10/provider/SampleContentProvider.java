package iu9.bmstu.ru.lab10.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import iu9.bmstu.ru.lab10.db.DBContract;
import iu9.bmstu.ru.lab10.db.DBHelper;

public class SampleContentProvider extends ContentProvider {

    public static final String URI_MEETING = "content://" + SampleContentProvider.AUTHORITY + "/meeting";

    private static final String TAG = SampleContentProvider.class.getName();

    private static final String AUTHORITY = "iu9.bmstu.ru.lab10.provider";

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int MULTIPLE_ROWS = 1;
    private static final int SINGLE_ROW = 2;

    static {
        MATCHER.addURI(AUTHORITY, "meeting", MULTIPLE_ROWS);
        MATCHER.addURI(AUTHORITY, "meeting" + "/#", SINGLE_ROW);
    }

    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        DBHelper dbHelper = new DBHelper(getContext());
        database = dbHelper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        String sort = sortOrder;
        String select = selection;
        switch (MATCHER.match(uri)) {
            case 1: {
                if (TextUtils.isEmpty(sortOrder))
                    sort = DBContract.DBEntry.COLUMN3 + " ASC;";
                break;
            }
            case 2: {
                select = DBContract.DBEntry._ID + " = " + uri.getLastPathSegment() + ";";
                break;
            }
        }

        return database.query(
                DBContract.DBEntry.TABLE_NAME,
                new String[]{DBContract.DBEntry._ID, DBContract.DBEntry.COLUMN1,DBContract.DBEntry.COLUMN2,DBContract.DBEntry.COLUMN3},
                select,
                selectionArgs,
                null, null,
                sort);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (MATCHER.match(uri)) {
            case MULTIPLE_ROWS: {
                Context context = getContext();
                if (context == null || values == null) {
                    return null;
                }

                long employeeId = database.insert(DBContract.DBEntry.TABLE_NAME, null, values);

                context.getContentResolver().notifyChange(uri, null);

                Log.d(TAG + " insert", uri.toString());
                return ContentUris.withAppendedId(uri, employeeId);
            }
            case SINGLE_ROW: {
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            }
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        if (getContext() == null)
            return 0;
        Context context = getContext();

        int count;

        switch (MATCHER.match(uri)) {
            case MULTIPLE_ROWS: {
                count = database.delete(DBContract.DBEntry.TABLE_NAME, null, null);
                break;
            }
            case SINGLE_ROW: {
                count = database.delete(DBContract.DBEntry.TABLE_NAME,DBContract.DBEntry._ID + "=" + ContentUris.parseId(uri), null);
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        context.getContentResolver().notifyChange(uri, null);

        Log.d(TAG + " delete", uri.toString() + " - " + count);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d("Update! ", "Provider");
        database.update(DBContract.DBEntry.TABLE_NAME, values, selection, null);

        Cursor cursor = database.query(
                DBContract.DBEntry.TABLE_NAME,
                new String[]{DBContract.DBEntry._ID, DBContract.DBEntry.COLUMN1, DBContract.DBEntry.COLUMN2,DBContract.DBEntry.COLUMN3},
                null, null, null, null, null);
        for (int pos = 0; pos < cursor.getCount(); pos++) {
            cursor.moveToPosition(pos);
            long date = cursor.getLong(cursor.getColumnIndex(DBContract.DBEntry.COLUMN3));

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(date));

            Log.d(TAG + " update value", uri.toString() + " - " + new SimpleDateFormat("HH:mm dd MMM yyyy", Locale.getDefault()).format(calendar.getTime()));
        }
        return 0;
    }


}
