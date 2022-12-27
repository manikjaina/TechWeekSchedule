package com.example.techweekschedule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class MainActivity2 extends AppCompatActivity {

    // Array of strings...
    ListView simpleList;
    ArrayList<String> weekDayList= new ArrayList<>();
    ActivityResultLauncher<Intent> activityResultLauncher;


    @Override   protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        simpleList = (ListView)findViewById(R.id.weekdaysListView);

        weekDayList.add(getString(R.string.v_day1));
        weekDayList.add(getString(R.string.v_day2));
        weekDayList.add(getString(R.string.v_day3));
        weekDayList.add(getString(R.string.v_day4));
        weekDayList.add(getString(R.string.v_day5));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_list_view, R.id.textView, weekDayList);
        simpleList.setAdapter(arrayAdapter);
        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);


                Intent intent = new Intent(MainActivity2.this,MainActivity3.class);
                intent.putExtra("Weekday",selectedItem);
                startActivity(intent);


            }
        });


    }



}