package com.example.techweekschedule;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

public class MainActivity4 extends AppCompatActivity  implements View.OnClickListener {

    String activity[];
    DatabaseReference scheduleDatabase;
    TextView tvActivity, tvPresenter, tvDescription,
            tvTime, tvLearningActivity, tvActivityMode, tvLanguage;
    ImageView photoPresenter;
    Button btnReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        initialize();


    }

    private void initialize() {

        tvActivity = findViewById(R.id.tvActivityName);
        tvDescription = findViewById(R.id.tvActivityDescription);
        tvDescription.setMovementMethod(new ScrollingMovementMethod());
        tvPresenter = findViewById(R.id.tvPresenter);
        tvTime = findViewById(R.id.tvTime);
        tvActivityMode = findViewById(R.id.tvActivityMode);
        tvLearningActivity = findViewById(R.id.tvLearningActivity);
        tvLanguage = findViewById(R.id.tvLanguage);
        photoPresenter = (ImageView) findViewById(R.id.imgPresenter);
        btnReturn = findViewById(R.id.btnReturn);
        btnReturn.setOnClickListener(this);

        activity = getIntent().getStringExtra("activityId").toString().split("\t");


        scheduleDatabase = FirebaseDatabase
                .getInstance()
                .getReference(activity[1])
                .child(activity[0]);

        scheduleDatabase.addValueEventListener(new ValueEventListener() { //attach listener

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) { //something changed!
                if (dataSnapshot.exists()){

                    tvActivity.setText(dataSnapshot.child("name").getValue().toString());
                    tvDescription.setText(dataSnapshot.child("description").getValue().toString());
                    tvPresenter.setText(dataSnapshot.child("presenter").getValue().toString());
                    tvTime.setText(dataSnapshot.child("time").getValue().toString());
                    tvActivityMode.setText(dataSnapshot.child("activity mode").getValue().toString());
                    tvLearningActivity.setText(dataSnapshot.child("learning activity").getValue().toString());
                    tvLanguage.setText(dataSnapshot.child("language").getValue().toString());
                    loadImage(dataSnapshot);

                }else{
                    Toast.makeText(MainActivity4.this, "Activity not found!", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { //update UI here if error occurred.

            }
        });
    }

    private void loadImage(DataSnapshot dataSnapshot) {

        //StorageReference mImageRef = FirebaseStorage.getInstance().getReference().child(String.valueOf(dataSnapshot.child("photo")));

        Glide.with(this /* context */)
                .load(dataSnapshot.child("photo").getValue().toString())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.imagenotfound)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        // log exception
                        Toast.makeText(MainActivity4.this, "Error loading image!", Toast.LENGTH_SHORT).show();
                        return false; // important to return false so the error placeholder can be placed
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(photoPresenter);

        /*final long ONE_MEGABYTE = 1024 * 1024;
        mImageRef.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        DisplayMetrics dm = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(dm);
                        photoPresenter.setImageBitmap(bm);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        Toast.makeText(MainActivity4.this, "Photo presenter not available!", Toast.LENGTH_SHORT).show();
                    }
                });*/

    }

    @Override
    public void onClick(View view) {
        finish();
    }
}

