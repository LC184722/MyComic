package com.qc.common.self;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.qc.common.en.SettingEnum;
import com.qc.common.util.SettingUtil;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/4/16 10:21
 * @ver 1.0
 */
public class ScrollSpeedLinearLayoutManger extends LinearLayoutManager {
    private float MILLISECONDS_PER_INCH = 3f;

    private Context context;

    private int level;

    public ScrollSpeedLinearLayoutManger(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        LinearSmoothScroller linearSmoothScroller =
                new LinearSmoothScroller(recyclerView.getContext()) {
                    @Override
                    public PointF computeScrollVectorForPosition(int targetPosition) {
                        return ScrollSpeedLinearLayoutManger.this
                                .computeScrollVectorForPosition(targetPosition);
                    }

                    //This returns the milliseconds it takes to
                    //scroll one pixel.
                    @Override
                    protected float calculateSpeedPerPixel
                    (DisplayMetrics displayMetrics) {
                        return MILLISECONDS_PER_INCH / displayMetrics.density;
                        //返回滑动一个pixel需要多少毫秒
                    }

                };
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }

    public String addSpeed() {
        return changeSpeed(level + 1);
    }

    public String subSpeed() {
        return changeSpeed(level - 1);
    }

    public String changeSpeed(int value) {
        if (value >= 1 && value <= 7) {
            level = value;
            SettingUtil.putSetting(SettingEnum.NOVEL_AUTO_SPEED, level);
            MILLISECONDS_PER_INCH = context.getResources().getDisplayMetrics().density * 1.2f * (7 - level + 1);
        }
        return getSpeedDesc();
    }

    public String getSpeedDesc() {
        return String.valueOf(level);
    }

}