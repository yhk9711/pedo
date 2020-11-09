package com.example.lets_walk_firebase;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class PickerActivity extends DialogFragment {
    private DatePickerDialog.OnDateSetListener listener;
    public Calendar cal = Calendar.getInstance();

    private DatabaseReference databaseReference;


    private int MAX_YEAR = cal.get(Calendar.YEAR);
    //private int MIN_YEAR = cal.get(Calendar.YEAR)-10;
    //private int MIN_YEAR;
    String syear;
    String smonth;
    String sday;

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

        databaseReference = FirebaseDatabase.getInstance().getReference("MEMBER").child(PedoActivity.my_id);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Map<String, String> map = (Map) dataSnapshot.getValue();

                syear = map.get("year");
                smonth = map.get("month");
                sday = map.get("day");
                Log.e("picker_year", syear);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                PickerActivity.this.getDialog().cancel();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // listener.onDateSet(null, yearPicker.getValue(), monthPicker.getValue(), 0);

                Date currentTime = Calendar.getInstance().getTime();
                SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
                String month = monthFormat.format(currentTime);

                MyInfo.sday=0;
                MyInfo.smonth=monthPicker.getValue();
                MyInfo.syear=yearPicker.getValue();

                if(Integer.parseInt(syear) > yearPicker.getValue()){
                    Toast.makeText(getContext(), "회원님의 가입 년도는 " + syear + "년 입니다. 새로운 날짜를 선택해주세요.", Toast.LENGTH_LONG).show();
                    PickerActivity.this.getDialog().cancel();
                } else if(Integer.parseInt(smonth) > monthPicker.getValue()){
                    Toast.makeText(getContext(), "회원님의 가입 월은 " + smonth + "월 입니다. 새로운 날짜를 선택해주세요.", Toast.LENGTH_LONG).show();
                    PickerActivity.this.getDialog().cancel();
                } else if(Integer.parseInt(month) < monthPicker.getValue()){
                    Toast.makeText(getContext(), "입력하신 날짜는 현재 날짜보다 큽니다. 새로운 날짜를 선택해주세요.", Toast.LENGTH_LONG).show();
                    PickerActivity.this.getDialog().cancel();
                } else{
                    listener.onDateSet(null, yearPicker.getValue(), monthPicker.getValue(), 0);
                    PickerActivity.this.getDialog().cancel();
                }

            }
        });

        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setValue(cal.get(Calendar.MONTH) + 1);

        int year = cal.get(Calendar.YEAR);
        yearPicker.setMinValue(2015);
        yearPicker.setMaxValue(MAX_YEAR);
        yearPicker.setValue(year);

        builder.setView(dialog);

        return builder.create();
    }
}