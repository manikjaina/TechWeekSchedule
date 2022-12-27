package com.example.techweekschedule;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.StringTokenizer;

import model.Presenter;

public class PresenterActivitiesListActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener {

    ListView simpleList;



    ArrayList<String> activityList = new ArrayList<>();
    ArrayList<String> idList = new ArrayList<>();

    Presenter presenter=null;
    DatabaseReference scheduleDatabase;
    Button btnReturn;
    String nextTokenValue = "";
    Integer flagToKen = 0;
    String[] st;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presenter_activities_list);
        simpleList = (ListView)findViewById(R.id.presenterActivitiesListView);
        btnReturn = findViewById(R.id.btnReturn);
        btnReturn.setOnClickListener(this);

        presenter = (Presenter) getIntent().getExtras().getSerializable("presenter");

        st = presenter.getActivityId().split("\t");


        scheduleDatabase = FirebaseDatabase
                .getInstance()
                .getReference("/");

        scheduleDatabase.addValueEventListener(this);










    }

    private void initialize() {


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_list_view, R.id.textView, activityList);
        simpleList.setAdapter(arrayAdapter);
        /*simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainActivity3.this,MainActivity4.class);
                intent.putExtra("activityId", idList.get(position) +"\t"+ weekday.toLowerCase());
                startActivity(intent);


            }
        });*/

    }


    @Override
    public void onClick(View view) {
        finish();
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Integer i =1;

            try{
                while(true){

                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                    if (snapshot1.getKey().equals(st[i])){


                            activityList.add(snapshot1.child(st[i+1]).child("name").getValue().toString());
                            i+=2;
                        }

                    }
                }
            }catch (Exception e){


                initialize();
            }










    }

    @Override
    public void onCancelled(DatabaseError databaseError) { //update UI here if error occurred.
        Toast.makeText(PresenterActivitiesListActivity.this, "Database error!", Toast.LENGTH_SHORT).show();

    }


}
