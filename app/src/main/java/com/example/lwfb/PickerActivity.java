package com.example.lwfb;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Map;

public class PickerActivity extends DialogFragment {
    private DatePickerDialog.OnDateSetListener listener;
    public Calendar cal = Calendar.getInstance();

    private DatabaseReference databaseReference;

    String syear;
    String smonth;
    String sday;

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    Button btnConfirm;
    Button btnCancel;
    Integer regyear;
    Integer regmonth;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View dialog = inflater.inflate(R.layout.activity_picker, null);

        btnConfirm = dialog.findViewById(R.id.btn_confirm);
        btnCancel = dialog.findViewById(R.id.btn_cancel);

        final NumberPicker monthPicker = dialog.findViewById(R.id.picker_month);
        final NumberPicker yearPicker = dialog.findViewById(R.id.picker_year);

        setNumberPickerTextColor(monthPicker, Color.rgb(99, 156, 212));
        setNumberPickerTextColor(yearPicker, Color.rgb(99, 156, 212));


        Calendar cal = Calendar.getInstance();
        final int nowyear = cal.get(Calendar.YEAR);
        final int nowmonth = cal.get(Calendar.MONTH) + 1;

        databaseReference = FirebaseDatabase.getInstance().getReference("MEMBER").child(PedoActivity.my_id);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Map<String, Integer> map3 = (Map) dataSnapshot.getValue();
                regyear = Integer.parseInt(String.valueOf(map3.get("regyear")));
                regmonth = Integer.parseInt(String.valueOf(map3.get("regmonth")));
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
//                listener.onDateSet(null, yearPicker.getValue(), monthPicker.getValue(), 0);

                MyInfo.sday=0;
                if (yearPicker.getValue() < regyear){
                    System.out.println("가입 전 ");
                    Toast.makeText(getContext(),"가입 전 날짜입니다. 새로운 날짜를 선택해주십시오. ", Toast.LENGTH_LONG).show();
                    PickerActivity.this.getDialog().cancel();
                }
                else if ((yearPicker.getValue() == regyear)&& (monthPicker.getValue() < regmonth )){
                    System.out.println("가입 전 ");
                    Toast.makeText(getContext(),"가입 전 날짜입니다. 새로운 날짜를 선택해주십시오. ", Toast.LENGTH_LONG).show();
                    PickerActivity.this.getDialog().cancel();
                }
                else if (yearPicker.getValue() > nowyear){
                    System.out.println("미래  ");
                    Toast.makeText(getContext(),"현재 시간보다 미래의 날짜입니다. 새로운 날짜를 선택해주십시오. ", Toast.LENGTH_LONG).show();
                    PickerActivity.this.getDialog().cancel();
                }
                else if ((yearPicker.getValue() == nowyear)&& (monthPicker.getValue() > nowmonth )){
                    System.out.println("미래 ");
                    Toast.makeText(getContext(),"현재 시간보다 미래의 날짜입니다. 새로운 날짜를 선택해주십시오. ", Toast.LENGTH_LONG).show();
                    PickerActivity.this.getDialog().cancel();
                }
                else {
                    listener.onDateSet(null, yearPicker.getValue(), monthPicker.getValue(), 0);
                    System.out.println("이 안에 드러옴? ");
                    MyInfo.smonth=monthPicker.getValue();
                    MyInfo.syear=yearPicker.getValue();
                    PickerActivity.this.getDialog().cancel();
                }
            }
        });

        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setValue(cal.get(Calendar.MONTH) + 1);

        int year = cal.get(Calendar.YEAR);
        yearPicker.setMinValue(2015);
        yearPicker.setMaxValue(2023);
        yearPicker.setValue(year);

        builder.setView(dialog);
//
       return builder.create();
    }

    public static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color) {

        final int count = numberPicker.getChildCount();



        for (int i=0;i<count;i++) {

            View child = numberPicker.getChildAt(i);

            if (child instanceof EditText) {

                try {

                    Field selectorWheelPaintField = numberPicker.getClass()

                            .getDeclaredField("mSelectorWheelPaint");

                    selectorWheelPaintField.setAccessible(true);

                    ((Paint) selectorWheelPaintField.get(numberPicker)).setColor(color);

                    ((EditText) child).setTextColor(color);

                    numberPicker.invalidate();

                    return true;

                } catch (NoSuchFieldException e) {

//Log.w("setNumberPickerTextColor", e);

                } catch (IllegalAccessException e) {

//Log.w("setNumberPickerTextColor", e);

                } catch (IllegalArgumentException e) {

//Log.w("setNumberPickerTextColor", e);

                }

            }

        }

        return false;

    }
}