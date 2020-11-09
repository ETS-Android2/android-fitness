package com.sarachen.androidappkeep.ui.discover;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sarachen.androidappkeep.R;
import com.sarachen.androidappkeep.model.Course;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Course}.
 * TODO: Replace the implementation with code for your data type.
 */
public class DiscoverCourseRecyclerViewAdapter extends RecyclerView.Adapter<DiscoverCourseRecyclerViewAdapter.ViewHolder> {

    private final List<Course> mValues;
    private DiscoverFragment.CourseOnClickListener listener;

    public DiscoverCourseRecyclerViewAdapter(List<Course> items, DiscoverFragment.CourseOnClickListener listener) {
        mValues = items;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_discover_course, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).getTitle());
        holder.mTypeView.setText(mValues.get(position).getType());
//        Log.d("chenxirui", "onBindViewHolder: " + mValues.get(position).getImage());
//        Picasso.get().setLoggingEnabled(true);
        Picasso.get().load((mValues.get(position).getImage()).trim()).into(holder.mImageView);
        // bind CourseOnClickListener
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
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
        public final TextView mTitleView;
        public final TextView mTypeView;
        public final ImageView mImageView;
        public Course mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.course_title);
            mTypeView = (TextView) view.findViewById(R.id.course_type);
            mImageView = (ImageView) view.findViewById(R.id.course_image);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTypeView.getText() + "'";
        }
    }
}