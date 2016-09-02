package tv.true123.cn.tv;


import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;


public class TVPlayView extends Activity {
    private static final String TAG = "VideoView";
    private String path = "";
    private io.vov.vitamio.widget.VideoView mVideoView;
    private long mPosition = 0;
//    private int maxAudioVolume;
//    private int currentAudioVolume;
    private AudioManager audioManager;
    private int currentLayout = 0;
    private CircularProgressBar progressBar;
    private List list;
    private int index = -1;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Boolean init = Vitamio.isInitialized(getApplicationContext());
        Log.i(TAG, "onCreate: init=" + init);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//        maxAudioVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//        currentAudioVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);


        setContentView(R.layout.videoview);
        progressBar = (CircularProgressBar) findViewById(R.id.progressBar);
        mVideoView = (io.vov.vitamio.widget.VideoView) findViewById(R.id.surface_view);
        mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);
        Bundle bundle = getIntent().getExtras();
        list = bundle.getStringArrayList("url");
        Log.i(TAG, "list=" + list);

        Log.i(TAG, "onCreate: " + path);
        next();
        mVideoView.requestFocus();
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                Log.i(TAG, "onPrepared: ");
                mediaPlayer.setPlaybackSpeed(1.0f);
                mVideoView.start();

            }
        });

        mVideoView.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                Log.i(TAG, "onBufferingUpdate: " + percent);
                if (percent >= 90) {
                    buffering(false);
                } else {

                    buffering(true);
                }
            }
        });

        mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                    buffering(true);
                } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                    buffering(false);
                }
                return false;
            }
        });
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.e(TAG, "onError: " + extra);
                Toast.makeText(TVPlayView.this, "无法播放！", Toast.LENGTH_SHORT).show();
                Util.updateInvalidUrl(TVPlayView.this, path);
                next();
                return true;
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.i(TAG, "onCompletion: ");
                buffering(false);
            }
        });

    }

    private void next() {
        index++;
        if (list != null && list.size() > index) {
            path = (String) list.get(index);
            Log.i(TAG, "next: =" + path);
            setPath(path);
            return;
        }
        finish();
    }

    private void setPath(String path) {
        mVideoView.setVideoPath(path);
        buffering(true);
    }

    private void buffering(boolean showing) {
        Log.i(TAG, "buffering: =" + mVideoView.isBuffering());
        Log.i(TAG, "buffering: =" + mVideoView.isPlaying());
        if (showing) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                if (mVideoView != null && mVideoView.isPlaying()) {
                    mVideoView.pause();
                } else if (mVideoView != null && !mVideoView.isPlaying()) {
                    mVideoView.resume();
                }
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                changeLayout(false);
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                changeLayout(true);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FX_FOCUS_NAVIGATION_UP);
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FX_FOCUS_NAVIGATION_UP);
                break;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void changeLayout(boolean isRight) {
        if (isRight) {
            currentLayout++;
        } else {
            currentLayout--;
        }
        if (currentLayout > 4) currentLayout = 0;
        if (currentLayout < 0) currentLayout = 4;
        mVideoView.setVideoLayout(currentLayout, 0);
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause: ");
        mPosition = mVideoView.getCurrentPosition();
        mVideoView.stopPlayback();
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume: ");
        if (mPosition > 0) {
            mVideoView.seekTo(mPosition);
            mPosition = 0;
        }
        super.onResume();

        mVideoView.start();
    }
}
