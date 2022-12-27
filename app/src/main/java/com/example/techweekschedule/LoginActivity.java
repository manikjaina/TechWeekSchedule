package com.example.techweekschedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener, OnCompleteListener<AuthResult> {

    private EditText emailTextView, passwordTextView;
    private Button Btn,BtnSignUp,BtnAgenda;
    private ProgressBar progressbar;
    DatabaseReference objDatabase;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();


    }

    private void initialize() {


        // taking instance of FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // initialising all views through id defined above
        emailTextView = findViewById(R.id.edEmail);
        passwordTextView = findViewById(R.id.edPassword);
        Btn = findViewById(R.id.btnLogin);
        BtnSignUp = findViewById(R.id.btnSignUp);
        BtnAgenda = findViewById(R.id.btnAgenda);
        progressbar = findViewById(R.id.progressBar);
        BtnSignUp.setOnClickListener(this);
        // Set on Click Listener on Sign-in button
        Btn.setOnClickListener(this);
        BtnAgenda.setOnClickListener(this);


    }

    private void loginUserAccount()
    {

        // show the visibility of progress bar to show loading
        progressbar.setVisibility(View.VISIBLE);

        // Take the value of two edit texts in Strings
        String email, password;
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();

        // validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter email!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter password!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // signin existing user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.btnLogin:
                loginUserAccount();
                break;
            case R.id.btnSignUp:
                intent
                        = new Intent(LoginActivity.this,
                        RegistrationActivity.class);
                intent.putExtra("userId", "");
                startActivity(intent);
                break;
            case R.id.btnAgenda:
                intent
                        = new Intent(LoginActivity.this,
                        MainActivity2.class);
                intent.putExtra("userId", "");
                startActivity(intent);
                break;

        }
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {

        if (task.isSuccessful()) {
            Toast.makeText(getApplicationContext(),
                            "Login successful!!",
                            Toast.LENGTH_LONG)
                    .show();

            // hide the progress bar
            progressbar.setVisibility(View.GONE);

            // if sign-in is successful
            // intent to home activity

            Intent intent
                    = new Intent(LoginActivity.this,
                    PresenterProfileActivity.class);
            intent.putExtra("userId",mAuth.getCurrentUser().getUid());
            startActivity(intent);


        }

        else {

            // sign-in failed
            Toast.makeText(getApplicationContext(),
                            "Login failed!!",
                            Toast.LENGTH_LONG)
                    .show();

            // hide the progress bar
            progressbar.setVisibility(View.GONE);
        }
    }

}