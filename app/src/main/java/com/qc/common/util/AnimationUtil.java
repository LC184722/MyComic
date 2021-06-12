package com.qc.common.util;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.qmuiteam.qmui.util.QMUIViewHelper;

import java.util.Objects;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/2/28 21:54
 * @ver 1.0
 */
public class AnimationUtil {

    public static void changeDrawable(ImageView imageView, Drawable drawable) {
        changeDrawable(imageView, drawable, true);
    }

    public static void changeDrawable(ImageView imageView, Drawable drawable, boolean needAnimation) {
        if (needAnimation) {
            QMUIViewHelper.fadeOut(imageView, 200, new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    imageView.setImageDrawable(drawable);
                    QMUIViewHelper.fadeIn(imageView, 200, null, true);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            }, true);
        } else {
            imageView.setImageDrawable(drawable);
        }
    }

    public static void changeViewVisibility(View view) {
        Object tag = initTag(view);
        if (Objects.equals(tag, View.VISIBLE)) {
            view.setTag(View.GONE);
            QMUIViewHelper.fadeOut(view, 300, null, true);
        } else {
            view.setTag(View.VISIBLE);
            QMUIViewHelper.fadeIn(view, 300, null, true);
        }
    }

    public static boolean changeViewVisibility(View view, boolean visible) {
        return changeViewVisibility(view, visible, null);
    }

    public static boolean changeViewVisibility(View view, boolean visible, Animation.AnimationListener listener) {
        Object tag = initTag(view);
        if (Objects.equals(tag, View.VISIBLE) && !visible) {
            view.setTag(View.GONE);
            QMUIViewHelper.fadeOut(view, 300, listener, true);
            return true;
        } else if (Objects.equals(tag, View.GONE) && visible) {
            view.setTag(View.VISIBLE);
            QMUIViewHelper.fadeIn(view, 300, listener, true);
            return true;
        }
        return false;
    }

    private static Object initTag(View view) {
        Object tag = view.getTag();
        if (tag == null) {
            if (view.getVisibility() == View.VISIBLE) {
                view.setTag(View.VISIBLE);
                tag = View.VISIBLE;
            } else {
                view.setTag(View.GONE);
                tag = View.GONE;
            }
        }
        return tag;
    }

}
