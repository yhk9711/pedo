package com.example.lets_walk_firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.List;
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
    public int height;
    public static List<String> friends;
    public static List<Integer> steps;
    public int index;


    private DatabaseReference mDatabase;



    public FirebasePost(){

        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)

    }



    public FirebasePost(String id, String pw, String name, Long age, String gender, int step, int goal_step, int height, List<String>friends, List<Integer>steps, int index) {

        this.id = id;
        this.pw = pw;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.step = step;
        this.height = height;
        this.goal_step = goal_step;
        this.friends = friends;
        this.steps = steps;
        this.index = index;


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
        result.put("height", height);
        result.put("friends", friends);
        result.put("steps", steps);
        result.put("index", index);

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

    public void WriteFriends(String userId, List<String> friend){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("MEMBER").child(userId).child("friends").setValue(friend);
    }

    public void WriteHeight(String userId, int Height){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("MEMBER").child(userId).child("height").setValue(Height);
    }

    public void WriteSteps(String userId, List<Integer> steps){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("MEMBER").child(userId).child("steps").setValue(steps);
    }

    public void WriteIndex(String userId, int index){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("MEMBER").child(userId).child("index").setValue(index);
    }


}