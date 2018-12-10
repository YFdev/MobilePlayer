package com.elapse.mobileplayer.systemVideoPlayer;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.elapse.mobileplayer.R;

/**
 * Created by YF_lala on 2018/12/10.
 */

public class SystemVideoPlayer extends Activity {

    private VideoView mVideoView;
    private Uri mUri;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_video_player);
        mVideoView = findViewById(R.id.video_view);
        mUri = getIntent().getData();
        mVideoView.setVideoURI(mUri);

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mVideoView.start();
            }
        });

        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(SystemVideoPlayer.this,"Unknown error",Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(SystemVideoPlayer.this,"finished",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        // set control panel
        mVideoView.setMediaController(new MediaController(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
