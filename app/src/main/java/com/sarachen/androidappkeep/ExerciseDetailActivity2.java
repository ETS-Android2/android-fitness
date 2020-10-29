package com.sarachen.androidappkeep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sarachen.androidappkeep.model.Exercise;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

public class ExerciseDetailActivity2 extends AppCompatActivity {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private Bundle bundle;
    private Exercise exercise;
    private SimpleExoPlayer player;
    private StyledPlayerView playerView;
    private StorageReference videoStorageRef;
    private TextView titleView, stepsView, breatheView, movementFeelingView, commonMistakesView;
    private LinearLayout imageGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail2);
        // get exercise
        bundle = getIntent().getExtras();
        exercise = Parcels.unwrap(bundle.getParcelable("exercise_parcel"));
        //setup all views
        player = new SimpleExoPlayer.Builder(getApplicationContext()).build();
        playerView = (StyledPlayerView)findViewById(R.id.exercise_detail2_player_view);
        titleView = (TextView) findViewById(R.id.exercise_detail2_title);
        stepsView = (TextView) findViewById(R.id.exercise_detail2_steps);
        breatheView = (TextView) findViewById(R.id.exercise_detail2_breathe);
        movementFeelingView = (TextView) findViewById(R.id.exercise_detail2_movementFeeling);
        commonMistakesView = (TextView) findViewById(R.id.exercise_detail2_commonMistakes);
        imageGroup = (LinearLayout)findViewById(R.id.exercise_detail2_imagesGroup);

        putContent();
        buildPlayer();
    }

    private void putContent(){
        titleView.setText(exercise.getName());
        stepsView.setText(getStringFromList(exercise.getSteps()));
        breatheView.setText(getStringFromList(exercise.getBreathes()));
        movementFeelingView.setText(getStringFromList(exercise.getMovementFeelinigs()));
        commonMistakesView.setText(getStringFromList(exercise.getCommonMistakes()));
        putMovementFeelingPics();
    }
    private String getStringFromList(List<String> list) {
        String output = "";
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1)
                output += "\u2726 " + list.get(i) + "\n";
            else output += "\u2726 " + list.get(i) + "\n\n";
        }
        return output;
    }
    private void putMovementFeelingPics() {
        List<String> urls = exercise.getMoveFeelingPictures();
        for (String url : urls) {
            ImageView imgView = new ImageView(getApplicationContext());
            Picasso.Builder builder = new Picasso.Builder(getApplicationContext());
            builder.listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    Log.e("===========================Picasso Error=========================", exception.getMessage());
                }
            });
            Picasso pic = builder.build();
            pic.load(url.trim()).into(imgView);
            imageGroup.addView(imgView);
        }
    }
    private void buildPlayer() {
        playerView.setPlayer(player);
        String path = exercise.getVideoUrl();
        videoStorageRef = storage.getReferenceFromUrl(path.trim());
        videoStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                MediaItem item = MediaItem.fromUri(uri);
                player.addMediaItem(item);
            }
        });
        player.setRepeatMode(Player.REPEAT_MODE_ONE);
        player.prepare();
        player.seekTo(0);
        player.play();
    }
}