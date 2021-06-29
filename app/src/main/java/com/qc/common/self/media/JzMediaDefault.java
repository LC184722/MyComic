package com.qc.common.self.media;

import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Surface;

import java.lang.reflect.Method;
import java.util.Map;

import cn.jzvd.JZMediaInterface;
import cn.jzvd.Jzvd;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/29 17:16
 * @ver 1.0
 */
public class JzMediaDefault extends JZMediaInterface implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, MediaPlayer.OnVideoSizeChangedListener {

    public MediaPlayer mediaPlayer;

    public JzMediaDefault(Jzvd jzvd) {
        super(jzvd);
    }

    @Override
    public void prepare() {
        release();
        mMediaHandlerThread = new HandlerThread("JZVD");
        mMediaHandlerThread.start();
        mMediaHandler = new Handler(mMediaHandlerThread.getLooper());//主线程还是非主线程，就在这里
        handler = new Handler();

        mMediaHandler.post(() -> {
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setLooping(jzvd.jzDataSource.looping);
                mediaPlayer.setOnPreparedListener(JzMediaDefault.this);
                mediaPlayer.setOnCompletionListener(JzMediaDefault.this);
                mediaPlayer.setOnBufferingUpdateListener(JzMediaDefault.this);
                mediaPlayer.setScreenOnWhilePlaying(true);
                mediaPlayer.setOnSeekCompleteListener(JzMediaDefault.this);
                mediaPlayer.setOnErrorListener(JzMediaDefault.this);
                mediaPlayer.setOnInfoListener(JzMediaDefault.this);
                mediaPlayer.setOnVideoSizeChangedListener(JzMediaDefault.this);
                Class<MediaPlayer> clazz = MediaPlayer.class;
                //如果不用反射，没有url和header参数的setDataSource函数
                Method method = clazz.getDeclaredMethod("setDataSource", String.class, Map.class);
                method.invoke(mediaPlayer, jzvd.jzDataSource.getCurrentUrl().toString(), jzvd.jzDataSource.headerMap);
                mediaPlayer.prepareAsync();
                mediaPlayer.setSurface(new Surface(SAVED_SURFACE));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void start() {
        if (mediaPlayer != null) {
            mMediaHandler.post(() -> mediaPlayer.start());
        }
    }

    @Override
    public void pause() {
        if (mediaPlayer != null) {
            mMediaHandler.post(() -> mediaPlayer.pause());
        }
    }

    @Override
    public boolean isPlaying() {
        if (mediaPlayer != null) {
            return mediaPlayer.isPlaying();
        } else {
            return false;
        }
    }

    @Override
    public void seekTo(long time) {
        if (mediaPlayer != null) {
            mMediaHandler.post(() -> {
                try {
                    mediaPlayer.seekTo((int) time);
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public void release() {//not perfect change you later
        if (mMediaHandler != null && mMediaHandlerThread != null && mediaPlayer != null) {//不知道有没有妖孽
            HandlerThread tmpHandlerThread = mMediaHandlerThread;
            MediaPlayer tmpMediaPlayer = mediaPlayer;
            JZMediaInterface.SAVED_SURFACE = null;

            mMediaHandler.post(() -> {
                tmpMediaPlayer.setSurface(null);
                tmpMediaPlayer.release();
                tmpHandlerThread.quit();
            });
            mediaPlayer = null;
        }
    }

    //TODO 测试这种问题是否在threadHandler中是否正常，所有的操作mediaplayer是否不需要thread，挨个测试，是否有问题
    @Override
    public long getCurrentPosition() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }

    @Override
    public long getDuration() {
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        } else {
            return 0;
        }
    }

    @Override
    public void setVolume(float leftVolume, float rightVolume) {
        if (mMediaHandler == null) return;
        mMediaHandler.post(() -> {
            if (mediaPlayer != null) mediaPlayer.setVolume(leftVolume, rightVolume);
        });
    }

    @Override
    public void setSpeed(float speed) {
        if (mediaPlayer != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                PlaybackParams pp = mediaPlayer.getPlaybackParams();
                pp.setSpeed(speed);
                mediaPlayer.setPlaybackParams(pp);
            }
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        handler.post(() -> jzvd.onPrepared());//如果是mp3音频，走这里
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        handler.post(() -> jzvd.onCompletion());
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, final int percent) {
        handler.post(() -> jzvd.setBufferProgress(percent));
    }

    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {
        handler.post(() -> jzvd.onSeekComplete());
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, final int what, final int extra) {
        handler.post(() -> jzvd.onError(what, extra));
        return true;
    }

    @Override
    public boolean onInfo(MediaPlayer mediaPlayer, final int what, final int extra) {
        handler.post(() -> jzvd.onInfo(what, extra));
        return false;
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int width, int height) {
        handler.post(() -> jzvd.onVideoSizeChanged(width, height));
    }

    @Override
    public void setSurface(Surface surface) {
        if (mediaPlayer != null) {
            mediaPlayer.setSurface(surface);
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        if (SAVED_SURFACE == null) {
            SAVED_SURFACE = surface;
            prepare();
        } else {
            jzvd.textureView.setSurfaceTexture(SAVED_SURFACE);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}