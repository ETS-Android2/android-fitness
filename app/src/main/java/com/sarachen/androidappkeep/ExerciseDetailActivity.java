package com.sarachen.androidappkeep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sarachen.androidappkeep.helper.GlideApp;
import com.sarachen.androidappkeep.model.Exercise;

import org.parceler.Parcels;

public class ExerciseDetailActivity extends Activity {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private Bundle bundle;
    private Exercise exercise;
    private ImageView pauseImageView;
    private TextView pauseTextView;
    private ConstraintLayout constraintLayoutViewDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //set pauseImageView, pauseTextView, constraintLayoutViewDetail
        pauseImageView = (ImageView)findViewById(R.id.exercise_detail_pause_imageView);
        pauseTextView = (TextView)findViewById(R.id.exercise_detail_pause_imageView_group_text);
        constraintLayoutViewDetail = (ConstraintLayout)findViewById(R.id.exercise_detail_pause_imageView_group);
        // get exercise
        bundle = getIntent().getExtras();
        exercise = Parcels.unwrap(bundle.getParcelable("exercise_parcel"));
        //get image ref and download into image view using GlideUI for Firebase storage
        StorageReference imageStorageRef = storage.getReferenceFromUrl(exercise.getImageUrl().trim());
        GlideApp
                .with(getApplicationContext())
                .load(imageStorageRef)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(pauseImageView);
        //setup exercise title
        pauseTextView.setText(exercise.getName());
        setListenerForViewDetail();
    }

    public void setListenerForViewDetail() {
        constraintLayoutViewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ExerciseDetailActivity2.class);
                Bundle itemBundle = new Bundle();
                itemBundle.putParcelable("exercise_parcel", Parcels.wrap(exercise));
                intent.putExtras(itemBundle);
                startActivityForResult(intent, 5000);
            }
        });
    }
}