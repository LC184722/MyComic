package com.qc.common.self;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/2/24 22:30
 * @ver 1.0
 */
public class ImageConfig {

    private String url;

    private RelativeLayout layout;

    private Object saveKey;

    private boolean isSave;

    private boolean isForce;

    private Bitmap defaultBitmap;

    private Bitmap errorBitmap;

    private Drawable drawable;

    private ImageView.ScaleType scaleType;

    private RelativeLayout.LayoutParams layoutParams;

    public ImageConfig(String url, RelativeLayout layout) {
        this.url = url;
        this.layout = layout;
    }

    public ImageConfig() {

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public RelativeLayout getLayout() {
        return layout;
    }

    public void setLayout(RelativeLayout layout) {
        this.layout = layout;
    }

    public Object getSaveKey() {
        return saveKey;
    }

    public void setSaveKey(Object saveKey) {
        this.saveKey = saveKey;
    }

    public boolean isSave() {
        return isSave;
    }

    public void setSave(boolean save) {
        isSave = save;
    }

    public boolean isForce() {
        return isForce;
    }

    public void setForce(boolean force) {
        isForce = force;
    }

    public Bitmap getDefaultBitmap() {
        return defaultBitmap;
    }

    public void setDefaultBitmap(Bitmap defaultBitmap) {
        this.defaultBitmap = defaultBitmap;
    }

    public Bitmap getErrorBitmap() {
        return errorBitmap;
    }

    public void setErrorBitmap(Bitmap errorBitmap) {
        this.errorBitmap = errorBitmap;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public ImageView.ScaleType getScaleType() {
        return scaleType;
    }

    public void setScaleType(ImageView.ScaleType scaleType) {
        this.scaleType = scaleType;
    }

    public RelativeLayout.LayoutParams getLayoutParams() {
        return layoutParams;
    }

    public void setLayoutParams(RelativeLayout.LayoutParams layoutParams) {
        this.layoutParams = layoutParams;
    }
}
