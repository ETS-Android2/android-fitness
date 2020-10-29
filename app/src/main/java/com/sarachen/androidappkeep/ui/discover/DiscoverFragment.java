package com.sarachen.androidappkeep.ui.discover;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sarachen.androidappkeep.R;
import com.sarachen.androidappkeep.database.Database;
import com.sarachen.androidappkeep.model.Course;
import com.sarachen.androidappkeep.model.Exercise;

import java.util.ArrayList;
import java.util.List;

public class DiscoverFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 2;
    DatabaseReference db = Database.DB;
    private List<Course> courses;// all courses in database
    private DiscoverFragment.CourseOnClickListener listener;
    private DiscoverCourseRecyclerViewAdapter adapter;
    private VideoView videoView;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DiscoverFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static DiscoverFragment newInstance(int columnCount) {
        DiscoverFragment fragment = new DiscoverFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        videoView.start();
    }

    /*
     * get all courses data from database, and pass it to adapter
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_discover_course_list, container, false);
        videoView = view.findViewById(R.id.course_fragment_videoView);
        playVideo();
//        dashboardViewModel =
//                ViewModelProviders.of(this).get(DashboardViewModel.class);
//        final TextView textView = view.findViewById(R.id.text_dashboard);
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        //begin fetching data from firebase database
        courses = new ArrayList<>();
        DatabaseReference coursesDf = db.child("courses");
        coursesDf.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.d("chenxirui","inside ondatachange method");
                for (DataSnapshot courseDs : dataSnapshot.getChildren()) {
                    List<Exercise> exercises = getExercises(courseDs);
                    Course course = getCourse(courseDs, exercises);
                    courses.add(course);
                }
                // Set the adapter
                if (view instanceof RecyclerView) {
                    Context context = view.getContext();
                    RecyclerView recyclerView = (RecyclerView) view;
                    if (mColumnCount <= 1) {
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    } else {
                        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                        recyclerView.setLayoutManager(gridLayoutManager);
                    }
                    adapter = new DiscoverCourseRecyclerViewAdapter(courses, listener);
                    recyclerView.setAdapter(adapter);
                }
                else {
                    RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_course_list);
                    Context context = recyclerView.getContext();
                    if (mColumnCount <= 1) {
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    } else {
                        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                        recyclerView.setLayoutManager(gridLayoutManager);
                    }
                    adapter = new DiscoverCourseRecyclerViewAdapter(courses, listener);
                    recyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("chenxirui", databaseError.getMessage());
            }
        });
        return view;
    }

    /*
     * Read one course's exercises data from database.
     * Returns a list of exercise object
     * @param DataSnapshot of the current course
     */
    private List<Exercise> getExercises(DataSnapshot courseDs) {
        //Log.d("chenxirui","inside getExercises");
        List<Exercise> exercises = new ArrayList<>();
        DataSnapshot exercisesDs = courseDs.child("exercises");
        int id = 0;
        for (DataSnapshot ds : exercisesDs.getChildren()) {
            String name = ds.child("name").getValue(String.class);
            String image = ds.child("image").getValue(String.class);
            String video = ds.child("video").getValue(String.class);
            List<String> steps = new ArrayList<>();
            for (DataSnapshot ds2 : ds.child("steps").getChildren()) {
                steps.add(ds2.child("1").getValue(String.class));
            }
            List<String> breathes = new ArrayList<>();
            for (DataSnapshot ds2 : ds.child("breathes").getChildren()) {
                breathes.add(ds2.child("1").getValue(String.class));
            }
            List<String> movementfeelings = new ArrayList<>();
            for (DataSnapshot ds2 : ds.child("movementfeelings").getChildren()) {
                movementfeelings.add(ds2.child("1").getValue(String.class));
            }
            List<String> movementfeelingPics = new ArrayList<>();
            for (DataSnapshot ds2 : ds.child("movementfeelingPics").getChildren()) {
                movementfeelingPics.add(ds2.child("1").getValue(String.class));
            }
            List<String> commonMistakes = new ArrayList<>();
            for (DataSnapshot ds2 : ds.child("commonmistakes").getChildren()) {
                commonMistakes.add(ds2.child("1").getValue(String.class));
            }

            Exercise exercise = new Exercise(id, name, image, video, steps, breathes, commonMistakes, movementfeelingPics, movementfeelings);
            //Log.d("xiruichen", "getExercise: " + exercise.getName());
            exercises.add(exercise);
            id++;
        }
        return  exercises;
    }

    /*
     * Read one course data from database.
     * Returns a Course object
     * @param DataSnapshot of the current course and list of exercises
     */
    private Course getCourse(DataSnapshot courseDs, List<Exercise> exercises) {
        if (courseDs.exists()) {
            //Log.d("chenxirui", "inside getCourse: ");
            DataSnapshot metaDs = courseDs.child("meta");
            int id = metaDs.child("id").getValue(Integer.class);
            String title = metaDs.child("title").getValue(String.class);
            String detail = metaDs.child("detail").getValue(String.class);
            String ctype = metaDs.child("ctype").getValue(String.class);
            String image = metaDs.child("image").getValue(String.class);
            Course course = new Course(id, title, detail, image, ctype, exercises);
            //Log.d("chenxirui", "getCourse: " + title);
            return  course;
        }
        return null;
    }
    public void playVideo() {
        videoView.setVideoURI(Uri.parse("android.resource://" + "com.sarachen.androidappkeep" + "/" + R.raw.intro_android_app));
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.start();
            }
        });
        videoView.start();
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (DiscoverFragment.CourseOnClickListener) context;
    }

    /*
     * CourseOnClickListener interface
     */
    public interface CourseOnClickListener {
        void onCLick(Course course);
    }

}