package com.sarachen.androidappkeep.ui.home;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sarachen.androidappkeep.R;
import com.sarachen.androidappkeep.model.Course;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Course}.
 * TODO: Replace the implementation with code for your data type.
 */
public class HomeCourseRecyclerViewAdapter extends RecyclerView.Adapter<HomeCourseRecyclerViewAdapter.ViewHolder> {

    private final List<Course> mValues;
    private HomeFragment.CourseOnClickListener listener;

    public HomeCourseRecyclerViewAdapter(List<Course> items, HomeFragment.CourseOnClickListener listener) {
        mValues = items;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_home_course, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).getTitle());
        holder.mTypeView.setText(mValues.get(position).getType());
        holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCLickCourse(mValues.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final RelativeLayout mRelativeLayout;
        public final TextView mTitleView;
        public final TextView mTypeView;
        public Course mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mRelativeLayout = (RelativeLayout) view.findViewById(R.id.fragment_home_course_relativeLayout);
            mTitleView = (TextView) view.findViewById(R.id.fragment_home_course_title);
            mTypeView = (TextView) view.findViewById(R.id.fragment_home_course_type);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTypeView.getText() + "'";
        }
    }
}