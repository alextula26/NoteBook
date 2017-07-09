package ru.alexander.marchuk.notebook.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import ru.alexander.marchuk.notebook.R;

public class TimePickerFragment extends DialogFragment {

    private static final String ARG_TIME = "time";
    public static final String EXTRA_TIME = "ru.alexander.marchuk.notebook.dialog.time";

    private TimePicker mTimePicker;

    public static TimePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, date);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Date date = (Date) getArguments().getSerializable(ARG_TIME);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);

        mTimePicker = (TimePicker) view.findViewById(R.id.dialog_time_picker);
        mTimePicker.setIs24HourView(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTimePicker.setHour(hour);
            mTimePicker.setMinute(minutes);
        } else {
            mTimePicker.setCurrentHour(hour);
            mTimePicker.setCurrentMinute(minutes);
        }


        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    int hour = mTimePicker.getHour();
                                    int minutes = mTimePicker.getMinute();
                                    Date time = new GregorianCalendar(year, month, day, hour, minutes).getTime();
                                    sendResult(Activity.RESULT_OK, time);
                                } else {
                                    int hour = mTimePicker.getCurrentHour();
                                    int minutes = mTimePicker.getCurrentMinute();
                                    Date time = new GregorianCalendar(year, month, day, hour, minutes).getTime();
                                    sendResult(Activity.RESULT_OK, time);
                                }

                            }
                        })
                .create();
    }

    private void sendResult(int resultCode, Date time){
        if(getTargetFragment() == null){
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, time);

        //  передаем данные в onActivityResult целевого фрагмента
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
