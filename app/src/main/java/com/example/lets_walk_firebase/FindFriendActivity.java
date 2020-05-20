package com.example.lets_walk_firebase;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FindFriendActivity extends AppCompatActivity {

    EditText edit_id;
    Button find_friend;

    private DatabaseReference databaseReference;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findfriend);


        edit_id = (EditText)findViewById(R.id.edit_id);
        find_friend = (Button)findViewById(R.id.find_friend);

        databaseReference = FirebaseDatabase.getInstance().getReference("MEMBER");


    }
}

