package com.example.techweekschedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import model.Presenter;

public class PresenterProfileActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener {

    TextView tvPresenterName, tvPresenterEmail;
    Button btnPresenterProfile, btnManageActivity, btnLogOut;
    DatabaseReference objDatabase;
    String userIdValue;
    Presenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presenter_profile);
        initialize();
    }

    private void initialize() {
        tvPresenterName = findViewById(R.id.tvPresenterName);
        tvPresenterEmail = findViewById(R.id.tvPresenterEmail);
        btnPresenterProfile = findViewById(R.id.btnPresenterProfile);
        btnManageActivity = findViewById(R.id.btnManageActivities);
        btnPresenterProfile.setOnClickListener(this);
        btnManageActivity.setOnClickListener(this);
        btnLogOut = findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(this);
        objDatabase = FirebaseDatabase
                .getInstance()
                .getReference(String.valueOf("presenter"));
        userIdValue = getIntent().getStringExtra("userId");
        DatabaseReference personChild = objDatabase.child(userIdValue);
        personChild.addValueEventListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnPresenterProfile:

                launchActivity(RegistrationActivity.class);
                break;
            case R.id.btnManageActivities:

                Intent intent
                        = new Intent(PresenterProfileActivity.this,
                        ManageActivity.class);
                intent.putExtra("presenter",presenter);
                startActivity(intent);
                break;
            case R.id.btnLogOut:
                userIdValue = "";
                launchActivity(LoginActivity.class);
                break;

        }
    }



    private void launchActivity(Class<?> generalActivity) {

        Intent intent
                = new Intent(PresenterProfileActivity.this,
                generalActivity);
        intent.putExtra("userId",userIdValue);
        startActivity(intent);

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if(snapshot.exists()){
            tvPresenterName.setText("Welcome " + snapshot.child("name").getValue().toString());
            tvPresenterEmail.setText(snapshot.child("email").getValue().toString());
            presenter = new Presenter(snapshot.child("uuid").getValue().toString(),
                    snapshot.child("email").getValue().toString(),
                    snapshot.child("name").getValue().toString(),
                    snapshot.child("photo").getValue().toString(),
                    snapshot.child("activityId").getValue().toString());

        }
        objDatabase.removeEventListener(this);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}