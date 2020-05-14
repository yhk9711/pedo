package com.example.lets_walk_firebase;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FriendsActivity extends AppCompatActivity {

    EditText idSelect;
    Button select;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        idSelect = (EditText)findViewById(R.id.id_select);
        select = (Button)findViewById(R.id.select_button);

        databaseReference = FirebaseDatabase.getInstance().getReference("MEMBER");
    }
}
