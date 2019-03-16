package iu9.bmstu.ru.lab10;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import iu9.bmstu.ru.lab10.adapter.DataAdapter;
import iu9.bmstu.ru.lab10.data.Meeting;
import iu9.bmstu.ru.lab10.db.DBContract;
import iu9.bmstu.ru.lab10.db.DBHelper;
import iu9.bmstu.ru.lab10.provider.SampleContentProvider;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    private Button addButton;

    private DataAdapter mDataAdapter;
    private RecyclerView mRecyclerView;

    private EditText mEditMeeting;
    private EditText mEditPlace;
    private EditText mEditDate;

    private void initDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.editor, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle(R.string.title_alert_dialog);

        mEditMeeting = dialogView.findViewById(R.id.editMeetingName);
        mEditPlace = dialogView.findViewById(R.id.editPlace);
        mEditDate = dialogView.findViewById(R.id.editDate);

        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND,0);

        mEditDate.setText(DateUtils.formatDateTime(this,
                calendar.getTime().getTime(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
                        | DateUtils.FORMAT_SHOW_TIME));

        mEditDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onFocusChange(final View v, boolean hasFocus) {
                if (hasFocus) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext());
                    final TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(),
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                    calendar.set(Calendar.MINUTE, minute);

                                    mEditDate.setText(DateUtils.formatDateTime(v.getContext(),
                                            calendar.getTime().getTime(),
                                            DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
                                                    | DateUtils.FORMAT_SHOW_TIME));
                                }
                            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                    datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, dayOfMonth);
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                            timePickerDialog.show();
                        }
                    });
                    datePickerDialog.show();
                }
            }
        });

        dialogBuilder.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String meetingName = mEditMeeting.getText().toString();
                String place = mEditPlace.getText().toString();

                Date date = calendar.getTime();

                Uri uri = insertData(meetingName, place, date);
                Meeting meeting = new Meeting(meetingName, place, date);
                mDataAdapter.addElem(meeting);
                mDataAdapter.getUris().add(uri);
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private Uri insertData(String meetingName, String place, Date date) {
        ContentValues values = new ContentValues();
        values.put(DBContract.DBEntry.COLUMN1, meetingName);
        values.put(DBContract.DBEntry.COLUMN3, date.getTime());
        values.put(DBContract.DBEntry.COLUMN2, place);

        Uri uri = getContentResolver().insert(
                Uri.parse(SampleContentProvider.URI_MEETING).buildUpon().build(),
                values);
        if (uri == null)
            throw new IllegalArgumentException("URI is null");
        return uri;
    }

    private void deleteData(Uri uri) {
        getContentResolver().delete(uri,null,null);
    }

    private void readData() {
        Cursor mCursor = getContentResolver().query(Uri.parse(SampleContentProvider.URI_MEETING).buildUpon().build(), null, null,
                        null, null);

        if (mCursor != null) {
            for (int i = 0;i < mCursor.getCount(); i++) {
                if (mCursor.moveToPosition(i)) {

                    String meetingName = mCursor.getString(mCursor.getColumnIndex(DBContract.DBEntry.COLUMN1));
                    long date = mCursor.getLong(mCursor.getColumnIndex(DBContract.DBEntry.COLUMN3));
                    String place = mCursor.getString(mCursor.getColumnIndex(DBContract.DBEntry.COLUMN2));

                    long id = mCursor.getLong(mCursor.getColumnIndex(DBContract.DBEntry._ID));

                    mDataAdapter.addElem(new Meeting(meetingName, place, new Date(date)));
                    mDataAdapter.getUris().add(ContentUris.withAppendedId(Uri.parse(SampleContentProvider.URI_MEETING).buildUpon().build(),id));
                }
            }
        }
    }

    private void readContacts() {
        Cursor mCursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null,
                null, null);


        if (mCursor != null) {

            for (int i = 0;i < mCursor.getCount(); i++) {
                if (mCursor.moveToPosition(i)) {

                    String id = mCursor.getString(mCursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = mCursor.getString(mCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    if (mCursor.getInt(mCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        Cursor pCur = getContentResolver().query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id}, null);
                        while (pCur.moveToNext()) {
                            String phoneNo = pCur.getString(pCur.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER));
                            Toast.makeText(getApplicationContext(),"Имя: " + name + "\nНомер телефона: " + phoneNo,Toast.LENGTH_LONG).show();
                        }
                        pCur.close();
                    }

                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // okey
                }
                else {
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
                }
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton = findViewById(R.id.addValue);
        addButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 initDialog();
             }
        });

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        }

        mRecyclerView = findViewById(R.id.recyclerView);
        mDataAdapter = new DataAdapter();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mDataAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        decoration.setDrawable(Objects.requireNonNull(getDrawable(R.drawable.line_divider)));

        mRecyclerView.addItemDecoration(decoration);

        readContacts();
        readData();

        ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                View view = viewHolder.itemView;

                TextView mMeetingTextView = view.findViewById(R.id.meetingTextView);
                TextView mPlaceTextView = view.findViewById(R.id.placeTextView);
                TextView mDateTextView = view.findViewById(R.id.dateTextView);

                try {
                    deleteData(mDataAdapter.getUris().get(mDataAdapter.index(new Meeting(mMeetingTextView.getText().toString(), mPlaceTextView.getText().toString(),new SimpleDateFormat("hh:mm dd MMM yyyy", Locale.getDefault()).parse(mDateTextView.getText().toString())))));
                    mDataAdapter.setMeetings(mDataAdapter.remove(new Meeting(mMeetingTextView.getText().toString(), mPlaceTextView.getText().toString(),new SimpleDateFormat("hh:mm dd MMM yyyy", Locale.getDefault()).parse(mDateTextView.getText().toString()))));
                }
                catch (ParseException ex) {}

            }


        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }
}
