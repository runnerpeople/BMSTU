package iu9.bmstu.ru.lab10.adapter;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import iu9.bmstu.ru.lab10.MainActivity;
import iu9.bmstu.ru.lab10.data.Meeting;
import iu9.bmstu.ru.lab10.R;
import iu9.bmstu.ru.lab10.db.DBContract;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataHolder> {

    private ArrayList<Meeting> meetings;
    private ArrayList<Uri> uris;

    public DataAdapter() {
        meetings = new ArrayList<>();
        uris = new ArrayList<>();
    }

    public DataAdapter(ArrayList<Meeting> meetings) {
        this.meetings = meetings;
    }

    public void addElem(Meeting meeting) {
        meetings.add(meeting);
        this.notifyDataSetChanged();
    }

    public void setMeetings(ArrayList<Meeting> meetings) {
        this.meetings = meetings;
        this.notifyDataSetChanged();
    }

    public ArrayList<Meeting> getMeetings() {
        return meetings;
    }

    public ArrayList<Uri> getUris() {
        return uris;
    }

    public ArrayList<Meeting> remove(Meeting meeting) {
        for (int i = 0; i < meetings.size(); i++) {
            if (meetings.get(i).equals(meeting)) {
                meetings.remove(i);
                uris.remove(i);
                break;
            }
        }
        return meetings;
    }

    public int index(Meeting meeting) {
        for (int i = 0; i < meetings.size(); i++) {
            if (meetings.get(i).equals(meeting)) {
                return i;
            }
        }
        return -1;
    }

    @NonNull
    @Override
    public DataHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.data_item, viewGroup, false);
        return new DataHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DataHolder dataHolder, final int i) {
        final String name = meetings.get(i).getName();
        final String place = meetings.get(i).getPlace();
        final Date date = meetings.get(i).getMeetingTime();

        dataHolder.mDateTextView.setText(new SimpleDateFormat("HH:mm dd MMM yyyy", Locale.getDefault()).format(date));
        dataHolder.mMeetingTextView.setText(name);
        dataHolder.mPlaceTextView.setText(place);

        dataHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(v.getContext());
                LayoutInflater inflater = LayoutInflater.from(v.getContext());
                View dialogView = inflater.inflate(R.layout.editor, null);
                dialogBuilder.setView(dialogView);
                dialogBuilder.setTitle(R.string.title_update_alert_dialog);

                final EditText mEditMeeting = dialogView.findViewById(R.id.editMeetingName);
                final EditText mEditPlace = dialogView.findViewById(R.id.editPlace);
                final EditText mEditDate = dialogView.findViewById(R.id.editDate);

                mEditMeeting.setText(meetings.get(i).getName());
                mEditPlace.setText(meetings.get(i).getPlace());
                mEditDate.setText(new SimpleDateFormat("HH:mm dd MMM yyyy", Locale.getDefault()).format(date));

                final Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND,0);

                mEditDate.setText(DateUtils.formatDateTime(v.getContext(),
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

                dialogBuilder.setPositiveButton("Изменить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String meetingName = mEditMeeting.getText().toString();
                        String place = mEditPlace.getText().toString();

                        Date date = calendar.getTime();

                        ContentValues values = new ContentValues();
                        values.put(DBContract.DBEntry.COLUMN1, meetingName);
                        values.put(DBContract.DBEntry.COLUMN3, date.getTime());
                        values.put(DBContract.DBEntry.COLUMN2, place);

                        v.getContext().getContentResolver().update(uris.get(i),values,null,null);

                        meetings.get(i).setName(meetingName);
                        meetings.get(i).setPlace(place);
                        meetings.get(i).setMeetingTime(date);

                        notifyDataSetChanged();

                    }
                });

                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
                return true;
            }
        });
    }


    @Override
    public int getItemCount() {
        return meetings.size();
    }

    public class DataHolder extends RecyclerView.ViewHolder {

        private TextView mMeetingTextView;
        private TextView mPlaceTextView;
        private TextView mDateTextView;

        public DataHolder(@NonNull final View itemView) {
            super(itemView);
            this.mMeetingTextView = itemView.findViewById(R.id.meetingTextView);
            this.mPlaceTextView = itemView.findViewById(R.id.placeTextView);
            this.mDateTextView = itemView.findViewById(R.id.dateTextView);

        }
    }
}
