package com.example.myapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.api.client.util.Value;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Input extends AppCompatActivity {

    Button save;
    EditText input;
    FirebaseDatabase database;

    public String[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        data = getIntent().getStringExtra("date").split(",");
        String year = data[0];
        String month = data[1];
        String day = data[2];

        database = FirebaseDatabase.getInstance();
        input = (EditText) findViewById(R.id.input);

        String identifier = Settings.Secure.getString(getBaseContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

            DatabaseReference userRef = database.getReference(identifier).child(year + "," + month).child(day);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        HashMap<String,String> map = (HashMap<String, String>) snapshot.getValue();
                        input.setText(map.get("entry"));
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            save = (Button) findViewById(R.id.save_input);
            save.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    String inputValue = input.getText().toString();
                    if(inputValue.isEmpty())
                        Toast.makeText(getApplicationContext(), "Enter your thoughts!", Toast.LENGTH_SHORT).show();

                    else {
                        userRef.child("entry").setValue(inputValue);

                        Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                        startActivity(intent);
                    }
                }
            });

            Button button = findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent startIntent = new Intent(getApplicationContext(), Checkboxes.class);
                    startIntent.putExtra("date", data);
                    startActivity(startIntent);
                }
            });


    }
}