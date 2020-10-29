package com.sarachen.androidappkeep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sarachen.androidappkeep.helper.GlideApp;
import com.sarachen.androidappkeep.model.Course;
import com.sarachen.androidappkeep.model.Exercise;
import com.sarachen.androidappkeep.model.ExerciseFragment;

import org.parceler.Parcels;

public class ViewCourseActivity extends AppCompatActivity {
    private Bundle bundle;//course and userId
    private Course course;
    private TextView titleView, detailView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        // get course
        bundle = getIntent().getExtras();
        course = Parcels.unwrap(bundle.getParcelable("course_parcel"));

        //add fragment-------------------------------------------------------
        ExerciseFragment exerciseFragment = new ExerciseFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.exercises_frame, exerciseFragment, "exerciseFragment")
                .commit();
        //pass bundle(course and userid) to fragment
        exerciseFragment.setArguments(bundle);

        //update view course detail
        showCourseData(course);
    }

    public void showCourseData(Course course) {
        titleView = (TextView)findViewById(R.id.course_exercise_courseTitle);
        detailView = findViewById(R.id.course_exercise_courseDetail);
        imageView = findViewById(R.id.course_exercise_image);
        titleView.setText(course.getTitle());
        detailView.setText(course.getDetail());
        Glide
                .with(getApplicationContext())
                .load(course.getImage())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(imageView);
    }

    public void playCourse(View view) {
        Intent intent = new Intent(this, PlayCourseActivity.class);
        // pass course and userId
        intent.putExtras(bundle);
        startActivityForResult(intent, 3000);
    }
}