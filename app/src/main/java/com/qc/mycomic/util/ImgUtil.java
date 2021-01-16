package com.qc.mycomic.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.qc.mycomic.R;
import com.qc.mycomic.en.Codes;
import com.qc.mycomic.model.ImageInfo;
import com.qc.mycomic.setting.SettingFactory;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIProgressBar;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import the.one.base.Interface.GlideProgressListener;
import the.one.base.util.glide.GlideProgressInterceptor;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/12/19 16:58
 * @ver 1.0
 */
public class ImgUtil {

    private static final String TAG = ImgUtil.class.getSimpleName();

    private static final String SHELF_IMG_PATH = Codes.SHELF_IMG_PATH;

    private static final Map<String, Integer> MAP = new HashMap<>();

    private static int screenWidth;

    public static final int LOAD_ING = 1;
    public static final int LOAD_SUCCESS = 2;
    public static final int LOAD_FAIL = 3;

    public static void loadImage(Context context, String url, RelativeLayout layout) {
        loadImage(context, url, layout, null);
    }

    public static void loadImage(Context context, String url, RelativeLayout layout, Object saveKey) {
        if (layout != null) {
            ImageView imageView = layout.findViewById(R.id.imageView);
            QMUIProgressBar progressBar = layout.findViewById(R.id.progressBar);
            if (imageView != null && progressBar != null) {
                if (!loadImageLocal(imageView, saveKey)) {
                    imageView.setTag(url);
                    progressBar.setTag(url);
                    loadImageNet(context, url, imageView, progressBar, saveKey);
                }
            }
        }
    }

    private static boolean loadImageLocal(ImageView imageView, Object saveKey) {
        //Log.i(TAG, "loadImageLocal: saveKey = " + saveKey);
        if (saveKey != null && !Objects.equals(saveKey, 0)) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            File file = new File(getLocalImgUrl(saveKey));
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), options);
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                    return true;
                }
            }
        }
        return false;
    }

    private static void loadImageNet(Context context, String url, ImageView imageView, QMUIProgressBar progressBar, Object saveKey) {
        GlideProgressListener listener = (progress, success) -> {
            //Log.i(TAG, "onProgress: url = " + url.substring(url.length() - 10) + ", progress = " + progress);
            if (Objects.equals(url, progressBar.getTag())) {
                progressBar.setProgress(progress);
                if (!success) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        };
        //Log.i(TAG, "loadImageNet: with url = " + url.substring(url.length() - 10));
        GlideProgressInterceptor.addListener(url, listener);
        Glide.with(context)
                .asBitmap()
                .load(url)
                .transition(new BitmapTransitionOptions().crossFade())
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        GlideProgressInterceptor.removeListener(url);
                        listener.onProgress(0, false);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        GlideProgressInterceptor.removeListener(url);
                        listener.onProgress(100, false);
                        return false;
                    }
                })
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        if (Objects.equals(url, imageView.getTag())) {
                            imageView.setImageBitmap(null);
                            setLP(context, (RelativeLayout.LayoutParams) imageView.getLayoutParams());
                        }
                        if (Objects.equals(url, progressBar.getTag())) {
                            progressBar.setProgress(0, false);
                            progressBar.setVisibility(View.VISIBLE);
                        }
                        MAP.put(url, LOAD_ING);
                    }

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        if (Objects.equals(url, imageView.getTag())) {
                            imageView.setLayoutParams(getLP(context, resource));
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                            imageView.setImageBitmap(resource);
                            MAP.put(url, LOAD_SUCCESS);
                        }
                        if (saveKey != null) {
                            try {
                                saveBitmapBackPath(resource, saveKey);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        if (Objects.equals(url, imageView.getTag())) {
                            Bitmap bitmap = drawableToBitmap(getDrawable(context, R.drawable.ic_image_error_24));
                            imageView.setLayoutParams(getLP(context));
                            imageView.setScaleType(ImageView.ScaleType.CENTER);
                            imageView.setImageBitmap(bitmap);
                            MAP.put(url, LOAD_FAIL);
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    private static RelativeLayout.LayoutParams getLP(Context context, Bitmap bitmap) {
        int sWidth = getScreenWidth(context);
        int bWidth = bitmap.getWidth();
        int bHeight = bitmap.getHeight();
        int sHeight = bHeight * sWidth / bWidth;
        return new RelativeLayout.LayoutParams(sWidth, sHeight);
    }

    private static void setLP(Context context, RelativeLayout.LayoutParams lp) {
        lp.width = getScreenWidth(context);
        lp.height = QMUIDisplayHelper.dp2px(context, 300);
    }

    private static RelativeLayout.LayoutParams getLP(Context context) {
        int width = getScreenWidth(context);
        int height = QMUIDisplayHelper.dp2px(context, 300);
        return new RelativeLayout.LayoutParams(width, height);
    }

    private static int getScreenWidth(Context context) {
        if (screenWidth == 0) {
            screenWidth = QMUIDisplayHelper.getScreenWidth(context);
        }
        return screenWidth;
    }

    public static void preloadReaderImg(Context context, ImageInfo imageInfo) {
        if (imageInfo != null) {
            String url = imageInfo.getUrl();
            Glide.with(context)
                    .load(url)
                    .preload();
        }
    }

    /**
     * 清理MAP
     *
     * @return void
     */
    public static void clearMap() {
        MAP.clear();
    }

    /**
     * 根据url获得图片加载状态
     *
     * @param imageInfo imageInfo
     * @return int
     */
    public static int getLoadStatus(ImageInfo imageInfo) {
        if (imageInfo != null) {
            String url = imageInfo.getUrl();
            Integer status = MAP.get(url);
            if (status != null) {
                return status;
            }
        }
        return LOAD_ING;
    }

    /*=============================================================================*/

    /**
     * 保存bitmap到本地并返回地址
     *
     * @param bm  bm
     * @param key key
     * @return String
     */
    private static String saveBitmapBackPath(Bitmap bm, Object key) throws IOException {
        File targetDir = new File(SHELF_IMG_PATH);
        if (!targetDir.exists()) {
            try {
                targetDir.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        File savedFile = new File(getLocalImgUrl(key));
        if (!savedFile.exists()) {
            savedFile.createNewFile();
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(savedFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
        return savedFile.getAbsolutePath();
    }

    /**
     * 根据key获得本地图片地址
     *
     * @param key key
     * @return String
     */
    public static String getLocalImgUrl(Object key) {
        return SHELF_IMG_PATH + "/img_" + key.toString();
    }

    /**
     * 根据id获得drawable
     *
     * @param context    context
     * @param context    context
     * @param drawableId drawableId
     * @return Drawable
     */
    public static Drawable getDrawable(Context context, int drawableId) {
        return ContextCompat.getDrawable(context, drawableId);
    }

    /**
     * Drawable -> Bitmap
     *
     * @param drawable drawable
     * @return Bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else {
            return drawableToBitmapByCanvas(drawable);
        }
    }

    /**
     * Bitmap -> Drawable
     *
     * @param context context
     * @param bitmap  bitmap
     * @return Drawable
     */
    public static Drawable bitmapToDrawable(Context context, Bitmap bitmap) {
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    /**
     * Drawable -> Bitmap
     *
     * @param drawable drawable
     * @return Bitmap
     */
    public static Bitmap drawableToBitmapByCanvas(Drawable drawable) {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * byte[] -> Bitmap
     *
     * @param bytes      bytes
     * @param isCompress isCompress
     * @return Bitmap
     */
    private static Bitmap bytesToBitmap(byte[] bytes, boolean isCompress) {
        Bitmap bitmap;
        if (isCompress) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            //Log.i("TAG", "bytesToBitmap: bytes size = " + bytes.length / 1024 + "KB");
            String data = SettingFactory.getInstance().getSetting(SettingFactory.SETTING_COMPRESS_IMAGE).getData();
            if (data.equals("1")) {
                options.inPreferredConfig = Bitmap.Config.RGB_565;
            } else if (data.equals("2")) {
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.inSampleSize = 2;
            }
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        } else {
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        //Log.i("TAG", "bytesToBitmap: bitmap.size = " + bitmap.getByteCount() / 1024 + "KB");
        return bitmap;
    }

    /**
     * 图片压缩
     *
     * @param bitmap bitmap
     * @return Bitmap
     */
    private static Bitmap compressBitmap(Bitmap bitmap) {
        String data = SettingFactory.getInstance().getSetting(SettingFactory.SETTING_COMPRESS_IMAGE).getData();
        if (data.equals("0")) {
            return bitmap;
        }
        int length = bitmap.getByteCount();
        if (length / 1024 > 2000) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            if (data.equals("2")) {
                options.inSampleSize = 2;
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
            bitmap = BitmapFactory.decodeStream(isBm, null, options);
        }
        return bitmap;
    }

    /**
     * 计算bitmap大小
     *
     * @param bitmap bitmap
     * @return int
     */
    public static int getBitmapSize(Bitmap bitmap) {
        return bitmap.getByteCount();
    }

}
