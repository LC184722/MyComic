package com.qc.common.util;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageButton;

import com.qmuiteam.qmui.util.QMUIViewHelper;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/2/28 21:54
 * @ver 1.0
 */
public class AnimationUtil {

    public static void changeDrawable(ImageButton imageButton, Drawable drawable) {
        QMUIViewHelper.fadeOut(imageButton, 200, new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageButton.setImageDrawable(drawable);
                QMUIViewHelper.fadeIn(imageButton, 200, null, true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        }, true);
    }

    public static boolean changeVisibility(View view, boolean isVisible) {
        if (view.getVisibility() != View.VISIBLE && isVisible) {
            QMUIViewHelper.fadeIn(view, 300, null, true);
            return true;
        } else if (view.getVisibility() != View.GONE && !isVisible) {
            QMUIViewHelper.fadeOut(view, 300, null, true);
            view.setVisibility(View.GONE);
            return true;
        }
        return false;
    }

}
