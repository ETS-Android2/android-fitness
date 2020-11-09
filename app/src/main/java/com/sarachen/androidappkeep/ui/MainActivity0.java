package com.sarachen.androidappkeep.ui;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sarachen.androidappkeep.R;
import com.sarachen.androidappkeep.model.Constants;
import com.sarachen.androidappkeep.model.Course;
import com.sarachen.androidappkeep.ui.discover.DiscoverFragment;
import com.sarachen.androidappkeep.ui.play.PlayCourseActivity;
import com.sarachen.androidappkeep.ui.viewCourse.ViewCourseFragment;
import com.sarachen.androidappkeep.ui.home.HomeFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.parceler.Parcels;

public class MainActivity0 extends AppCompatActivity implements DiscoverFragment.CourseOnClickListener, HomeFragment.CourseOnClickListener, ViewCourseFragment.ViewCourseToPlayCourse {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main0);
        //setup navigation
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_community, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
    }
    /*
    when click a course, go to view course fragment
     */
    @Override
    public void onCLickCourse(Course course) {
        ViewCourseFragment viewCourseFragment = new ViewCourseFragment();
        Bundle itemBundle = new Bundle();
        itemBundle.putParcelable("course_parcel", Parcels.wrap(course));
        itemBundle.putInt("userId", Constants.USER_ID);
        viewCourseFragment.setArguments(itemBundle);

//        getSupportFragmentManager().beginTransaction().
//                remove(getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment)).commit();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frame, viewCourseFragment)
                .addToBackStack(null)
                .commit();
    }
    /*
    when click play in view course fragment, go to play course activity
     */
    @Override
    public void playCourse(Bundle bundle) {
        Intent intent = new Intent(this, PlayCourseActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}