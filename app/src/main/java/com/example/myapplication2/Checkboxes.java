package com.example.myapplication2;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Checkboxes extends AppCompatActivity {

    private Map<String, Integer> emotions;
    private String[] typeEmotions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkboxes);

        typeEmotions = new String[]{"Happy", "Sad", "Angry", "Fear", "Surprise", "Disgust"};
        emotions = new HashMap<>();
        for(int i = 0; i < typeEmotions.length; i++)
            emotions.put(typeEmotions[i], 0);
        CheckBox happyBox = (CheckBox) findViewById(R.id.happyBox);
        CheckBox sadBox = (CheckBox) findViewById(R.id.sadBox);
        CheckBox angryBox = (CheckBox) findViewById(R.id.angryBox);
        CheckBox fearBox = (CheckBox) findViewById(R.id.fearBox);
        CheckBox surpriseBox = (CheckBox) findViewById(R.id.surpriseBox);
        CheckBox disgustBox = (CheckBox) findViewById(R.id.disgustBox);
        Button button = (Button) findViewById(R.id.button);
        TextView textView = (TextView) findViewById(R.id.textView);
        button.setOnClickListener(new View.OnClickListener() {

            HashMap<String,String> map = new HashMap<String,String>();

            String emotion = "";

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if(happyBox.isChecked()){
                    emotions.replace("Happy", 1);
                    emotion = emotion + ",1";}
                else{
                    emotions.replace("Happy", 0);
                    emotion = emotion + ",0";}
                if(sadBox.isChecked()){
                    emotions.replace("Sad", 1);
                    emotion = emotion + ",1";}
                else{
                    emotions.replace("Sad", 0);
                    emotion = emotion + ",0";}
                if(angryBox.isChecked()){
                    emotions.replace("Angry", 1);
                    emotion = emotion + ",1";}
                else{
                    emotions.replace("Angry", 0);
                    emotion = emotion + ",0";}
                if(fearBox.isChecked()){
                    emotions.replace("Fear", 1);
                    emotion = emotion + ",1";}
                else{
                    emotions.replace("Fear", 0);
                    emotion = emotion + ",0";}
                if(surpriseBox.isChecked()){
                    emotions.replace("Surprise", 1);
                    emotion = emotion + ",1";}
                else{
                    emotions.replace("Surprise", 0);
                    emotion = emotion + ",0";}
                if(disgustBox.isChecked()){
                    emotions.replace("Disgust", 1);
                    emotion = emotion + ",1";}
                else{
                    emotions.replace("Disgust", 0);
                    emotion = emotion + ",0";}

                textView.setText("Happy: " + emotions.get("Happy") + "\n" +
                        "Sad: " + emotions.get("Sad") + "\n" +
                        "Angry: " + emotions.get("Angry") + "\n" +
                        "Fear: " + emotions.get("Fear") + "\n" +
                        "Surprise: " + emotions.get("Surprise") + "\n" +
                        "Disgust: " + emotions.get("Disgust") + "\n");

                Intent startIntent = new Intent(getApplicationContext(), Graph.class);
                startIntent.putExtra("hashMap", (Serializable) emotions);
                startIntent.putExtra("date", getIntent().getStringExtra("date"));
                startActivity(startIntent);

                System.out.println("EMOTION CHECK: " + emotion);

//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference ref = database.getReference(MainActivity.getIdentifier());
//                ref.child("2021,9").child("19").child("emotion").setValue(emotion);
            }
        });
    }
}