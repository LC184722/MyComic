package com.qc.common.self;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.qc.common.constant.TmpData;
import com.qc.common.self.media.JzMediaAliyun;
import com.qc.mycomic.R;

import cn.jzvd.JzvdStd;
import top.luqichuang.common.model.Content;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/23 1:04
 * @ver 1.0
 */
public class JzPlayer extends JzvdStd {

    private TextView tvSpeed;
    private View batteryLevel;
    private Content content;

    public JzPlayer(Context context) {
        super(context);
    }

    public JzPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setContent(Content content) {
        this.content = content;
    }

    @Override
    public void startVideo() {
        super.startVideo();
        if (mediaInterface instanceof JzMediaAliyun) {
            ((JzMediaAliyun) mediaInterface).setContent(content);
        }
    }

    @Override
    public void onPrepared() {
        super.onPrepared();
        setSpeed();
        if (CURRENT_JZVD == null) {
            setCurrentJzvd(this);
        }
    }

    @Override
    public void dissmissControlView() {
        if (state != STATE_NORMAL
                && state != STATE_ERROR
                && state != STATE_AUTO_COMPLETE) {
            post(() -> {
                bottomContainer.setVisibility(View.INVISIBLE);
                topContainer.setVisibility(View.INVISIBLE);
                startButton.setVisibility(View.INVISIBLE);

                if (screen != SCREEN_TINY) {
                    bottomProgressBar.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public void init(Context context) {
        super.init(context);
        View retryLayout = findViewById(R.id.retry_layout);
        retryLayout.setVisibility(GONE);
        tvSpeed = findViewById(R.id.tvSpeed);
        batteryLevel = findViewById(R.id.battery_level);
        batteryLevel.setVisibility(GONE);
        tvSpeed.setOnClickListener(this);
        PROGRESS_DRAG_RATE = 4f;
    }

    @Override
    public void setScreenNormal() {
        super.setScreenNormal();
        batteryLevel.setVisibility(GONE);
    }

    @Override
    public void setScreenFullscreen() {
        super.setScreenFullscreen();
        batteryLevel.setVisibility(VISIBLE);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.tvSpeed) {//0.5 0.75 1.0 1.25 1.5 1.75 2.0
            if (TmpData.videoSpeed == 6) {
                TmpData.videoSpeed = 0;
            } else {
                TmpData.videoSpeed += 1;
            }
            setSpeed();
            cancelDismissControlViewTimer();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_std_speed;
    }

    private float getSpeedFromIndex(int index) {
        float ret = 0f;
        if (index == 0) {
            ret = 0.5f;
        } else if (index == 1) {
            ret = 0.75f;
        } else if (index == 2) {
            ret = 1.0f;
        } else if (index == 3) {
            ret = 1.25f;
        } else if (index == 4) {
            ret = 1.5f;
        } else if (index == 5) {
            ret = 1.75f;
        } else if (index == 6) {
            ret = 2.0f;
        }
        return ret;
    }

    protected void setSpeed() {
        if (TmpData.videoSpeed == 2) {
            tvSpeed.setText("倍速");
        } else {
            tvSpeed.setText(getSpeedFromIndex(TmpData.videoSpeed) + "X");
        }
        if (mediaInterface != null) {
            mediaInterface.setSpeed(getSpeedFromIndex(TmpData.videoSpeed));
            if (state == STATE_PAUSE) {
                mediaInterface.pause();
            }
        }
    }

}
