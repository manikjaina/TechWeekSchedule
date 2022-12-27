package com.example.techweekschedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

import model.Presenter;

public class ManageActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener {

    EditText edName,edDescription,edTime1, edTime2, edActivityMode;
    RadioGroup rgLearningActivity, rgDays;

    RadioButton rbLecture, rbWorkshop, rbProjectDemo,
                rbMonday, rbTuesday, rbWednesday, rbThursday, rbFriday;
    CheckBox chEnglish, chFrench;
    Button btnRegisterActivity, btnListActivity, btnReturn;
    DatabaseReference objDatabase;
    Presenter presenter = null;
    String presenterDay = "";
    String activityIds = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        initialize();
    }

    private void initialize() {

        edName = findViewById(R.id.edName);
        edDescription = findViewById(R.id.edDescription);
        edTime1 = findViewById(R.id.edTime1);
        edTime2 = findViewById(R.id.edTime2);
        rgLearningActivity = findViewById(R.id.rgLearningActivity);
        rgDays = findViewById(R.id.rgDay);
        edActivityMode = findViewById(R.id.edActivityMode);
        rbLecture = findViewById(R.id.rbLecture);
        rbWorkshop = findViewById(R.id.rbWorkshop);
        rbProjectDemo = findViewById(R.id.rbProject);
        rbMonday = findViewById(R.id.rbMonday);
        rbTuesday = findViewById(R.id.rbTuesday);
        rbWednesday = findViewById(R.id.rbWednesday);
        rbThursday = findViewById(R.id.rbThursday);
        rbFriday = findViewById(R.id.rbFriday);
        chEnglish = findViewById(R.id.chEnglish);
        chFrench = findViewById(R.id.chFrench);
        btnListActivity = findViewById(R.id.btnActivity);
        btnRegisterActivity = findViewById(R.id.btnAddActivity);
        btnReturn = findViewById(R.id.btnReturn);
        btnListActivity.setOnClickListener(this);
        btnRegisterActivity.setOnClickListener(this);
        btnReturn.setOnClickListener(this);
        
        presenter = (Presenter) getIntent().getExtras().getSerializable("presenter");


        

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){

            case R.id.btnActivity:

                    Intent intent
                            = new Intent(ManageActivity.this,
                            PresenterActivitiesListActivity.class);
                    intent.putExtra("presenter",presenter);
                    startActivity(intent);


                break;
            case R.id.btnAddActivity:
                //Validate fields
                if (validateFields()) return;
                registerNewActivity();

            case R.id.btnReturn:
                finish();
                

        }
    }

    private boolean validateFields() {


        String name, description, time1, time2;


        name = edName.getText().toString();
        description = edDescription.getText().toString();
        time1 = edTime1.getText().toString();
        time2 = edTime2.getText().toString();


        // Validations for input email and password
        if (isFieldEmpty(name,edName.getResources().getResourceEntryName(edName.getId()))) return true;
        if (isFieldEmpty(description, edDescription.getResources().getResourceEntryName(edDescription.getId()))) return true;
        if (isFieldEmpty(time1, edTime1.getResources().getResourceEntryName(edTime1.getId()))) return true;
        if (isFieldEmpty(time2, edTime2.getResources().getResourceEntryName(edTime2.getId()))) return true;

        if(rbProjectDemo.isChecked() == false && rbWorkshop.isChecked() == false && rbLecture.isChecked() == false){
            Toast.makeText(this,"Please select a learning activity",Toast.LENGTH_LONG).show();
            return true;
        }

        if(rbMonday.isChecked() == false && rbTuesday.isChecked() == false && rbWednesday.isChecked() == false
            && rbThursday.isChecked() == false && rbFriday.isChecked() == false){
            Toast.makeText(this,"Please select a day of the week",Toast.LENGTH_LONG).show();
            return true;
        }

        if(chFrench.isChecked() == false && chEnglish.isChecked() == false){
            Toast.makeText(this,"Please select a language",Toast.LENGTH_LONG).show();
            return true;
        }

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

    private void registerNewActivity() {
       

        if (presenter!=null) {

            if (rbMonday.isChecked())
                getReferenceToDBObject("monday");
            else if (rbTuesday.isChecked())
                getReferenceToDBObject("tuesday");
            else if (rbWednesday.isChecked())
                getReferenceToDBObject("wednesday");
            else if (rbThursday.isChecked())
                getReferenceToDBObject("thursday");
            else
                getReferenceToDBObject("friday");

        }
    }

    private void getReferenceToDBObject(String day) {
        presenterDay = day;
        objDatabase = FirebaseDatabase
                .getInstance()
                .getReference(day);
        objDatabase.addValueEventListener(this);
    }

    private void listActivities() {

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if(snapshot.exists()){

            ArrayList<Integer> objIds = new ArrayList<Integer>();
            String objId;

            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                objIds.add(Integer.valueOf((snapshot1.getKey())));
            }
            if (!objIds.isEmpty())
                objId = String.valueOf(Collections.max(objIds) + 1);
            else
                objId = "1";




            objDatabase.child(objId).child("name").setValue(edName.getText().toString());
            objDatabase.child(objId).child("description").setValue(edDescription.getText().toString());
            objDatabase.child(objId).child("time").setValue(edTime1.getText().toString() + " - "+ edTime2.getText().toString());
            if (rbLecture.isChecked())
                objDatabase.child(objId).child("learning activity").setValue("Lecture");
            else if (rbWorkshop.isChecked())
                objDatabase.child(objId).child("learning activity").setValue("Workshop");
            else
                objDatabase.child(objId).child("learning activity").setValue("Project demonstration");
            objDatabase.child(objId).child("activity mode").setValue(edActivityMode.getText().toString());
            if(chEnglish.isChecked() && chFrench.isChecked())
                objDatabase.child(objId).child("language").setValue("English, French");
            else if (chFrench.isChecked())
                objDatabase.child(objId).child("language").setValue("French");
            else
                objDatabase.child(objId).child("language").setValue("English");

            objDatabase.child(objId).child("photo").setValue(presenter.getPhoto().toString());
            objDatabase.child(objId).child("presenter").setValue(presenter.getName().toString());

            objDatabase.removeEventListener(this);

            objDatabase = FirebaseDatabase
                    .getInstance()
                    .getReference("presenter");
            objDatabase.addValueEventListener(new ValueEventListener() {
                                                  @Override
                                                  public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                      String presenterId = presenter.getUUID();
                                                      activityIds = "";
                                                      activityIds = presenter.getActivityId() +
                                                              "\t"+ presenterDay + "\t" + objId;
                                                      Presenter presenter1 = new Presenter(presenterId,
                                                              presenter.getEmail(),
                                                              presenter.getName(),
                                                              presenter.getPhoto(),
                                                              activityIds);
                                                      objDatabase.child(presenterId).setValue(presenter1);
                                                      presenter =presenter1;
                                                      objDatabase.removeEventListener(this);
                                                  }

                                                  @Override
                                                  public void onCancelled(@NonNull DatabaseError error) {

                                                  }
                                              });

                    Toast.makeText(this, "Activity added successfully", Toast.LENGTH_LONG).show();

            clearWidgets();


        }
    }

    private void clearWidgets() {
        edName.setText("");
        edTime2.setText("");
        edTime1.setText("");
        edActivityMode.setText("");
        rbWorkshop.setChecked(false);
        rbLecture.setChecked(false);
        rbProjectDemo.setChecked(false);
        rbMonday.setChecked(false);
        rbTuesday.setChecked(false);
        rbWednesday.setChecked(false);
        rbThursday.setChecked(false);
        rbFriday.setChecked(false);
        chFrench.setChecked(false);
        chEnglish.setChecked(false);

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}