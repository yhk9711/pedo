package com.example.lwfb;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**

 * Created by DowonYoon on 2017-07-11.

 */


@IgnoreExtraProperties

public class FirebasePost {

    public String id;

    public String pw;

    public String name;

    public Long age;

    public String gender;

    public int step;

    private DatabaseReference mDatabase;



    public FirebasePost(){

        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)

    }



    public FirebasePost(String id, String pw, String name, Long age, String gender, int step) {

        this.id = id;

        this.pw = pw;

        this.name = name;

        this.age = age;

        this.gender = gender;

        this.step = step;

    }



    @Exclude

    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();

        result.put("id", id);

        result.put("pw", pw);

        result.put("name",name);

        result.put("age", age);

        result.put("gender", gender);

        result.put("step", step);

        return result;

    }
    public void WriteStep(String userId, int stepValue){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("MEMBER").child(userId).child("step").setValue(stepValue);
    }



}