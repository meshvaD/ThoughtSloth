package com.example.myapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database;
    public static String identifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        identifier = Settings.Secure.getString(getBaseContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        System.out.println("IDENTIFIER: " + identifier);

        addIfUnique(database, identifier);

        ConstraintLayout rlayout = (ConstraintLayout) findViewById(R.id.background);
        rlayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CalendarActivity.class));
            }

        });
    }

    void addIfUnique(FirebaseDatabase database, String identifier){
        System.out.println("ADD: ");
        DatabaseReference userRef = database.getReference(identifier);

        System.out.println("ADD: ");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    HashMap<String, String> userInfo = new HashMap<String,String>();
                    userRef.child("Name").setValue("Ask for preferred name");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    public static String getIdentifier(){
//        return identifier;
//    }
}