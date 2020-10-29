package com.sarachen.androidappkeep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.sarachen.androidappkeep.model.Course;
import com.sarachen.androidappkeep.model.CourseFragment;

import org.parceler.Parcels;

public class MainActivity extends AppCompatActivity implements CourseFragment.CourseOnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        CourseFragment courseFragment = new CourseFragment();
        //add fragment-------------------------------------------------------
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.course_frame, courseFragment, "courseFragment")
                .commit();
    }


    @Override
    public void onCLick(Course course) {
        Intent intent = new Intent(this, ViewCourseActivity.class);
        Bundle itemBundle = new Bundle();
        itemBundle.putParcelable("course_parcel", Parcels.wrap(course));
        itemBundle.putInt("userId", 1);
        intent.putExtras(itemBundle);
        startActivityForResult(intent, 2000);
    }
}