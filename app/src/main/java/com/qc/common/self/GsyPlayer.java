package com.qc.common.self;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.qc.common.constant.TmpData;
import com.qc.common.en.SettingEnum;
import com.qc.common.util.SettingUtil;
import com.qc.mycomic.R;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.util.Map;

import top.luqichuang.common.util.MapUtil;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/7/12 23:05
 * @ver 1.0
 */
public class GsyPlayer extends StandardGSYVideoPlayer {

    protected TextView tvSpeed;

    private final Map<String, String> PROGRESS_MAP = (Map<String, String>) SettingUtil.getSettingKey(SettingEnum.VIDEO_PROGRESS);

    public GsyPlayer(Context context) {
        super(context);
    }

    public GsyPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GsyPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    @Override
    public int getLayoutId() {
        return R.layout.video_player_gsy;
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        tvSpeed = findViewById(R.id.tvSpeed);
        tvSpeed.setOnClickListener(this);
    }

    @Override
    public void onPrepared() {
        super.onPrepared();
        try {
            int progress = Integer.parseInt(PROGRESS_MAP.get(mOriginUrl));
            seekTo(progress);
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateSpeed();
    }

    @Override
    public void onVideoSizeChanged() {
        super.onVideoSizeChanged();
        updateSpeed();
    }

    @Override
    public void onCompletion() {
        super.onCompletion();
        PROGRESS_MAP.remove(mOriginUrl);
        SettingUtil.putSetting(SettingEnum.VIDEO_PROGRESS, PROGRESS_MAP);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        super.onProgressChanged(seekBar, progress, fromUser);
        if (PROGRESS_MAP.size() > 30) {
            Map.Entry<String, String> entry = MapUtil.getFirst(PROGRESS_MAP);
            PROGRESS_MAP.remove(entry.getKey());
        }
        long curPosition = getGSYVideoManager().getCurrentPosition();
        PROGRESS_MAP.put(mOriginUrl, String.valueOf(curPosition));
        SettingUtil.putSetting(SettingEnum.VIDEO_PROGRESS, PROGRESS_MAP);
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
            updateSpeed();
            cancelDismissControlViewTimer();
        }
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

    protected void updateSpeed() {
        if (TmpData.videoSpeed == 2) {
            tvSpeed.setText("倍速");
        } else {
            tvSpeed.setText(getSpeedFromIndex(TmpData.videoSpeed) + "X");
        }
        setSpeed(getSpeedFromIndex(TmpData.videoSpeed));
    }

}
