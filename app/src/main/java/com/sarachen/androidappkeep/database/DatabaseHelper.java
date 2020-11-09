package com.sarachen.androidappkeep.database;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.sarachen.androidappkeep.model.Course;
import com.sarachen.androidappkeep.model.Exercise;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

    public DatabaseHelper() {

    }


    /*
     * Read one course's exercises data from database.
     * Returns a list of exercise object
     * @param DataSnapshot of the current course
     */
    public static List<Exercise> getExercises(DataSnapshot courseDs) {
        Log.d("chenxirui","inside getExercises");
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
    public static Course getCourse(DataSnapshot courseDs, List<Exercise> exercises) {
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

    /*
    * get registered courses of one user
    * Returns a list of Course
    * @param DataSnapshot of the current course, a list of exercises, userId
     */
    public static List<Course> getCoursesByUserId(DataSnapshot dataSnapshot, List<Integer> courseIds) {
        List<Course> courses = new ArrayList<>();
        for (DataSnapshot courseDs : dataSnapshot.getChildren()) {
            if (courseDs.exists()) {
                DataSnapshot metaDs = courseDs.child("meta");
                int id = metaDs.child("id").getValue(Integer.class);
                if (courseIds.contains(id)) {
                    List<Exercise> exercises = DatabaseHelper.getExercises(courseDs);
                    Course course = DatabaseHelper.getCourse(courseDs, exercises);
                    courses.add(course);
                }
            }
        }
        return courses;
    }

    /*
    * get all courses id of one user
    * Return a list of curse id
    * @param DataSnapshot of the current user
    */
    public static List<Integer> getCoursesIdsByUserId(DataSnapshot snapshot, int userId) {
        List<Integer> coursesIds = new ArrayList<>();
        String coursesByUser = "";
        for (DataSnapshot userDs : snapshot.getChildren()) {
            String id = userDs.child("id").getValue(String.class);
            if (Integer.parseInt(id) == userId) {
                coursesByUser = userDs.child("courses").getValue(String.class);
                break;
            }
        }
        for (int i = 0; i < coursesByUser.length(); i++) {
            if (coursesByUser.charAt(i) != ' ') coursesIds.add(Integer.parseInt(String.valueOf(coursesByUser.charAt(i))));
        }
        return coursesIds;
    }
}
