package com.sarachen.androidappkeep;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sarachen.androidappkeep.helper.GlideApp;
import com.sarachen.androidappkeep.model.Course;
import com.sarachen.androidappkeep.model.Exercise;

import org.parceler.Parcels;

import java.util.EventListener;


public class PlayCourseActivity extends AppCompatActivity {
    FirebaseStorage storage ;
    StorageReference imageStorageRef;
    private Bundle bundle;//course and userId
    private Course course;
    private int userId;
    private ImageView pauseImageView;
    private SimpleExoPlayer player;
    private StyledPlayerView playerView;
    private AppCompatImageButton exoPlayPauseBtn;
    private StorageReference videoStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_course);
        storage = FirebaseStorage.getInstance();
        // get course and userId
        bundle = getIntent().getExtras();
        course = Parcels.unwrap(bundle.getParcelable("course_parcel"));
        userId = bundle.getInt("userId");
        //set pauseImageView
        pauseImageView = (ImageView)findViewById(R.id.play_course_imageView);
        buidPlayer();

    }
    private void buidPlayer() {
        // setup player
        player = new SimpleExoPlayer.Builder(getApplicationContext()).build();
        playerView = (StyledPlayerView)findViewById(R.id.player_view);
        exoPlayPauseBtn = (AppCompatImageButton)findViewById(R.id.exo_play_pause);
        // Attach player to the view.
        playerView.setPlayer(player);
        // Build the media items.
        for (final Exercise exercise : course.getExercises()) {
            String path = exercise.getVideoUrl();
            videoStorageRef = storage.getReferenceFromUrl(path.trim());
            videoStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    //Log.d("chenxirui", String.valueOf(uri));
                    //set mediaId and uri
                    MediaItem.Builder builder = new MediaItem.Builder();
                    builder.setUri(uri);
                    builder.setMediaId(String.valueOf(exercise.getId()));
                    MediaItem item = builder.build();
                    player.addMediaItem(item);
                    //add interval
                    player.addMediaItem(MediaItem.fromUri("https://firebasestorage.googleapis.com/v0/b/quickstart-1592333099507.appspot.com/o/interval.mp4?alt=media&token=908ee9e8-04f2-46e6-b36e-c77170ae1047"));
                }
            });
        }
        player.addListener(new Player.EventListener() {
            @Override
            public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
                //Log.d("chenxirui", "onMediaItemTransition: " + mediaItem.mediaId);
                //insert image displayed on pause-detail
                if (mediaItem.mediaId.charAt(0) != 'h') {
                    int index = Integer.parseInt(mediaItem.mediaId);
                    Exercise exercise = course.getExercises().get(index);
                    String image = exercise.getImageUrl();
                    imageStorageRef = storage.getReferenceFromUrl(image.trim());
                    GlideApp  
                            .with(getApplicationContext())
                            .load(imageStorageRef)
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .into(pauseImageView);
                }
            }
        });

        exoPlayPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player.isPlaying()) {
                    findViewById(R.id.play_course_modal).setVisibility(View.VISIBLE);
                    player.setPlayWhenReady(false);
                }
                else {
                    findViewById(R.id.play_course_modal).setVisibility(View.INVISIBLE);
                    player.setPlayWhenReady(true);
                }

            }
        });

        // Prepare the player.
        player.prepare();
        // Start the playback.
        player.seekTo(0);
        player.play();
    }


}