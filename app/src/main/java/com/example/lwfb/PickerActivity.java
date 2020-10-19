package com.example.lwfb;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class PickerActivity extends DialogFragment {
    private DatePickerDialog.OnDateSetListener listener;
    public Calendar cal = Calendar.getInstance();

    private int MAX_YEAR = cal.get(Calendar.YEAR);
    private int MIN_YEAR = cal.get(Calendar.YEAR)-10;

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    Button btnConfirm;
    Button btnCancel;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialog = inflater.inflate(R.layout.activity_picker, null);

        btnConfirm = dialog.findViewById(R.id.btn_confirm);
        btnCancel = dialog.findViewById(R.id.btn_cancel);

        final NumberPicker monthPicker = dialog.findViewById(R.id.picker_month);
        final NumberPicker yearPicker = dialog.findViewById(R.id.picker_year);

        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                PickerActivity.this.getDialog().cancel();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDateSet(null, yearPicker.getValue(), monthPicker.getValue(), 0);
                MonthActivity.sday=0;
                MonthActivity.smonth=monthPicker.getValue();
                MonthActivity.syear=yearPicker.getValue();
                PickerActivity.this.getDialog().cancel();
            }
        });

        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setValue(cal.get(Calendar.MONTH) + 1);

        int year = cal.get(Calendar.YEAR);
        yearPicker.setMinValue(MIN_YEAR);
        yearPicker.setMaxValue(MAX_YEAR);
        yearPicker.setValue(year);

        builder.setView(dialog);

        return builder.create();
    }
}