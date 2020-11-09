package com.sarachen.androidappkeep.ui.viewCourse;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sarachen.androidappkeep.R;
import com.sarachen.androidappkeep.model.Course;
import com.sarachen.androidappkeep.ui.MainActivity0;

import org.parceler.Parcels;

public class ViewCourseFragment extends Fragment {
    private Bundle bundle;//course and userId
    private Course course;
    private TextView titleView, detailView;
    private ImageView imageView;
    private ViewCourseToPlayCourse mMyInterface;
    public ViewCourseFragment() {
    }

    // TODO: Customize parameter initialization

    public static ViewCourseFragment newInstance(int columnCount) {

        ViewCourseFragment fragment = new ViewCourseFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // get course
        course = Parcels.unwrap(getArguments().getParcelable("course_parcel"));


        View view = inflater.inflate(R.layout.fragment_course, container, false);
        titleView = (TextView)view.findViewById(R.id.course_exercise_courseTitle);
        detailView = view.findViewById(R.id.course_exercise_courseDetail);
        imageView = view.findViewById(R.id.course_exercise_image);

        //add fragment-------------------------------------------------------
        ExerciseFragment exerciseFragment = new ExerciseFragment();
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.exercises_frame, exerciseFragment, "exerciseFragment")
                .commit();
        //pass bundle(course and userid) to fragment
        exerciseFragment.setArguments(getArguments());

        //update view course detail
        showCourseData(course);

        addClickListener(view);
        return view;
    }
    public void addClickListener(View view) {
        view.findViewById(R.id.course_play_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyInterface.playCourse(getArguments());
            }
        });
        view.findViewById(R.id.course_back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }


    public void showCourseData(Course course) {
        titleView.setText(course.getTitle());
        detailView.setText(course.getDetail());
        Glide
                .with(getActivity())
                .load(course.getImage())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(imageView);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity0) {
            mMyInterface = (ViewCourseToPlayCourse) context;
        } else {
            throw new ClassCastException(context + " must implement interface ShowPlayCourseFragment");
        }
    }

    @Override
    public void onDetach() {
        mMyInterface = null;
        super.onDetach();
    }

    public interface ViewCourseToPlayCourse {
        void playCourse(Bundle bundle);
    }
}