package com.qc.common.self;

import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Map;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/2/24 22:30
 * @ver 1.0
 */
public class ImageConfig {

    private String url;

    private Map<String, String> headers;

    private RelativeLayout layout;

    private Object saveKey;

    private boolean isSave;

    private boolean isForce;

    private int defaultBitmapId;

    private int errorBitmapId;

    private int drawableId;

    private int height;

    private int width;

    private int endHeight;

    private int endWidth;

    private ImageView.ScaleType scaleType;

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

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
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

    public int getDefaultBitmapId() {
        return defaultBitmapId;
    }

    public void setDefaultBitmapId(int defaultBitmapId) {
        this.defaultBitmapId = defaultBitmapId;
    }

    public int getErrorBitmapId() {
        return errorBitmapId;
    }

    public void setErrorBitmapId(int errorBitmapId) {
        this.errorBitmapId = errorBitmapId;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getEndHeight() {
        return endHeight;
    }

    public void setEndHeight(int endHeight) {
        this.endHeight = endHeight;
    }

    public int getEndWidth() {
        return endWidth;
    }

    public void setEndWidth(int endWidth) {
        this.endWidth = endWidth;
    }

    public ImageView.ScaleType getScaleType() {
        return scaleType;
    }

    public void setScaleType(ImageView.ScaleType scaleType) {
        this.scaleType = scaleType;
    }
}
