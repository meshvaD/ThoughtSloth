package com.example.myapplication2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.HashMap;

public class Login extends AppCompatActivity {

    GoogleSignInAccount account;
    FirebaseDatabase database;
    String identifier;
    private static final int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView loginGoogle = findViewById(R.id.googletext);
        TextView loginSkip = findViewById(R.id.skiplogin);
        EditText loginInput = findViewById(R.id.inputstring);

        database = FirebaseDatabase.getInstance();

//        loginGoogle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("working");
//
//                //sign into google account and use unique google id
//                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                        .requestEmail()
//                        .build();
//                GoogleSignInClient signInClient = GoogleSignIn.getClient(getBaseContext(), gso);
//
//                Intent intent = signInClient.getSignInIntent();
//                startActivityForResult(intent, RC_SIGN_IN);
//            }
//        });

        loginSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                identifier = Settings.Secure.getString(getBaseContext().getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                System.out.println("IDENTIFIER: " + identifier);

                addIfUnique(database, identifier);

                //startActivity(new Intent(MainActivity2.this, MainActivity2.class)); // 2nd arg to be replaced with new activityName.class
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
                    userInfo.put("Name: ", "Ask for Preferred Name");
                    userRef.setValue(userInfo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Intent startIntent = new Intent(getApplicationContext(), CalendarActivity.class);
        startActivity(startIntent);
    }

    public void checkPermission(String permission, int requestCode)
    {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(Login.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(Login.this, new String[] { permission }, requestCode);
        }
        else {
            Toast.makeText(Login.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            account = completedTask.getResult(ApiException.class);
            identifier = account.getEmail().toString().split("@")[0];

            addIfUnique(database, identifier);

            //DatabaseReference userRef = database.getReference(identifier);

        } catch (Exception e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please' refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("SIGN IN", "signInResult:failed code=" + e.getMessage());
        }
    }
}