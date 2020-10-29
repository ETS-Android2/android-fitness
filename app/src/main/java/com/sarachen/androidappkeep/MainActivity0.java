package com.sarachen.androidappkeep;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sarachen.androidappkeep.model.Course;
import com.sarachen.androidappkeep.ui.discover.DiscoverFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.parceler.Parcels;

public class MainActivity0 extends AppCompatActivity implements DiscoverFragment.CourseOnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main0);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
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