package com.example.lets_walk_firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;


@IgnoreExtraProperties

public class FirebasePost {

    public String id;

    public String pw;

    public String name;

    public Long age;

    public String gender;

    public int step;

    public int goal_step;

    private DatabaseReference mDatabase;



    public FirebasePost(){

        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)

    }



    public FirebasePost(String id, String pw, String name, Long age, String gender, int step, int goal_step) {

        this.id = id;

        this.pw = pw;

        this.name = name;

        this.age = age;

        this.gender = gender;

        this.step = step;

        this.goal_step = goal_step;

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

        result.put("goal_step", goal_step);

        return result;

    }
    public void WriteStep(String userId, int stepValue){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("MEMBER").child(userId).child("step").setValue(stepValue);
    }

    public void WriteGoal(String userId, int Goal){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("MEMBER").child(userId).child("goal_step").setValue(Goal);
    }



}