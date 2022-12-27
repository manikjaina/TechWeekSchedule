package com.example.techweekschedule;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import model.Presenter;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener,
        ValueEventListener, OnFailureListener, OnCompleteListener, OnSuccessListener<UploadTask.TaskSnapshot> {

    private EditText emailTextView, passwordTextView,
            nameTextView;

    private Button btnRegister;

    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    DatabaseReference objDatabase;
    String userIdValue;
    ImageView imPhoto;
    FirebaseStorage storage;
    StorageReference storageReference,sRef;
    ActivityResultLauncher actRes;
    Uri filePath;
    ProgressDialog prDialog;
    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // taking FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        // initialising all views through id defined above
        emailTextView = findViewById(R.id.email);
        passwordTextView = findViewById(R.id.passwd);
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressbar);
        nameTextView = findViewById(R.id.name);
        imPhoto = findViewById(R.id.imgPresenter);
        imPhoto.setOnClickListener(this);

        // Set on Click Listener on Registration button
        btnRegister.setOnClickListener(this);
        userIdValue = getIntent().getStringExtra("userId");

            objDatabase = FirebaseDatabase
                    .getInstance()
                    .getReference("presenter");
            // Initialize the Firebase storage
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();
            actRes = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            //Display the photo
                            if (result.getResultCode()==RESULT_OK &&
                                    result.getData()!=null){
                                url="";
                                filePath = result.getData().getData();
                                try{
                                    Bitmap bitmap = MediaStore
                                            .Images
                                            .Media
                                            .getBitmap(getContentResolver(),filePath);
                                    imPhoto.setImageBitmap(bitmap);

                                }catch (Exception e){
                                    Toast.makeText(RegistrationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
            );


        if (!getIntent().getExtras().getString("userId").equals("")) {

            emailTextView.setEnabled(false);
            DatabaseReference personChild = objDatabase.child(userIdValue);
            personChild.addValueEventListener(this);

        }



    }

    private void registerNewUser()
    {

        // show the visibility of progress bar to show loading
        progressBar.setVisibility(View.VISIBLE);

        //Update a user profile by credentials authentication
        if (!getIntent().getStringExtra("userId").equals("")) {
            mAuth.signInWithEmailAndPassword(emailTextView.getText().toString(), passwordTextView.getText().toString())
                    .addOnCompleteListener(this);
            addRegister();
        }
        else{

            // create new user or register new user
            mAuth
                    .createUserWithEmailAndPassword(emailTextView.getText().toString(),
                            passwordTextView.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful()) {

                                userIdValue = mAuth.getCurrentUser().getUid();
                                addRegister();
                                progressBar.setVisibility(View.GONE);






                            }
                            else {

                                // Registration failed
                                Toast.makeText(
                                                getApplicationContext(),
                                                "Registration failed!!"
                                                        + " Please try again later",
                                                Toast.LENGTH_LONG)
                                        .show();

                                // hide the progress bar
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
        }


    }

    private boolean validateFields() {


        String email, password, name, postalCode, address,phone;
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();
        name = nameTextView.getText().toString();



        // Validations for input email and password
        if (isFieldEmpty(email,emailTextView.getResources().getResourceEntryName(emailTextView.getId()))) return true;
        if (isFieldEmpty(password, passwordTextView.getResources().getResourceEntryName(passwordTextView.getId()))) return true;
        if (isFieldEmpty(name, nameTextView.getResources().getResourceEntryName(nameTextView.getId()))) return true;



        return false;
    }

    private boolean isFieldEmpty(String textField, String resourceName) {

        if (TextUtils.isEmpty(textField)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter "+ resourceName,
                            Toast.LENGTH_LONG)
                    .show();
            return true;
        }
        return false;
    }

    private void addRegister() {



        if (getIntent().getExtras().getString("userId").equals("")) {



                objDatabase = FirebaseDatabase
                        .getInstance()
                        .getReference("presenter");
                Presenter presenter = new Presenter(userIdValue,
                        emailTextView.getText().toString(),nameTextView.getText().toString(),"","");
                objDatabase.child(mAuth.getCurrentUser().getUid()).setValue(presenter);
                Toast.makeText(this,"The presenter has" +
                        " been registered successfully", Toast.LENGTH_LONG).show();


        }


        else {

                    Presenter presenter = new Presenter(userIdValue,
                    emailTextView.getText().toString(),nameTextView.getText().toString(),url,"");
                    objDatabase.child(userIdValue).setValue(presenter);
                    Toast.makeText(this,"The presenter has" +
                            " been updated successfully", Toast.LENGTH_LONG).show();

        }


        uploadPhoto();

    }



    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){

            case R.id.imgPresenter:
                selectPhoto();
                break;
            case R.id.btnRegister:
                //Validate fields
                if (validateFields()) return;
                registerNewUser();


        }
    }

    private void uploadPhoto() {
        if (filePath!=null){
            prDialog = new ProgressDialog(this);
            prDialog.setTitle("Uploading photo in progress");
            prDialog.show();
            //upload the image
            sRef=storageReference.child(""+ UUID.randomUUID());
            sRef.putFile(filePath).addOnSuccessListener(this);
            sRef.putFile(filePath).addOnFailureListener(this);
        }

    }

    private void selectPhoto() {
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        actRes.launch(Intent.createChooser(intent,"Please select a photo"));
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if(snapshot.exists()){

            nameTextView.setText(snapshot.child("name").getValue().toString());
            emailTextView.setText(snapshot.child("email").getValue().toString());
            if(!snapshot.child("photo").getValue().toString().equals("")){
                url = snapshot.child("photo").getValue().toString();
                Picasso
                        .with(this)
                        .load(url)
                        .placeholder(R.drawable.temp_image)
                        .into(imPhoto);
            }else
                url="";

        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Toast.makeText(this,"error", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onComplete(@NonNull Task task) {
        if (task.isSuccessful()) {





                String urlPhoto = task.getResult().toString();
                objDatabase.child(userIdValue).child("photo").setValue(urlPhoto);







            // hide the progress bar


            // if sign-in is successful
            // intent to home activity





        }

        else {

            // sign-in failed
            Toast.makeText(getApplicationContext(),
                            "Updated failed!!",
                            Toast.LENGTH_LONG)
                    .show();

            // hide the progress bar
        }
        progressBar.setVisibility(View.GONE);


        Intent intent
                = new Intent(RegistrationActivity.this,
                PresenterProfileActivity.class);
        intent.putExtra("userId",mAuth.getCurrentUser().getUid());
        startActivity(intent);


    }



    @Override
    public void onFailure(@NonNull Exception e) {
        Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_LONG).show();
        prDialog.dismiss();
    }

    @Override
    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
        Toast.makeText(this, "The photo has been uploaded successfully", Toast.LENGTH_SHORT).show();
        prDialog.dismiss();
        sRef.getDownloadUrl().addOnCompleteListener(this);
    }
}