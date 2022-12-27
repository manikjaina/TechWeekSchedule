package com.example.techweekschedule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity3 extends AppCompatActivity implements View.OnClickListener {

    ArrayList<String> activityList = new ArrayList<>();
    ArrayList<String> idList = new ArrayList<>();
    ListView simpleList;
    String weekday = "";
    DatabaseReference scheduleDatabase;
    Button btnReturn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        simpleList = (ListView)findViewById(R.id.activitiesListView);
        btnReturn = findViewById(R.id.btnReturn);
        btnReturn.setOnClickListener(this);

        weekday = getIntent().getStringExtra("Weekday").toString();


        scheduleDatabase = FirebaseDatabase
                .getInstance()
                .getReference(weekday.toLowerCase());//pinpoint location node

        scheduleDatabase.addValueEventListener(new ValueEventListener() { //attach listener

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) { //something changed!
                for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                    String name  = locationSnapshot.child("name").getValue(String.class)
                            + " " + locationSnapshot.child("time").getValue(String.class);
                    activityList.add(name);
                    String activityId  =locationSnapshot.getKey();
                    idList.add(activityId);
                }

                initialize();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { //update UI here if error occurred.

            }
        });




    }

    private void initialize() {


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.listview2, R.id.textView, activityList);
        simpleList.setAdapter(arrayAdapter);
        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainActivity3.this,MainActivity4.class);
                intent.putExtra("activityId", idList.get(position) +"\t"+ weekday.toLowerCase());
                startActivity(intent);


            }
        });

    }


    @Override
    public void onClick(View view) {
        finish();
    }
}

