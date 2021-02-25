package com.qc.common.util;

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
import com.qc.common.self.ImageConfig;
import com.qc.mycomic.R;
import com.qc.common.constant.AppConstant;
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
import top.luqichuang.mycomic.model.ImageInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/12/19 16:58
 * @ver 1.0
 */
public class ImgUtil {

    private static final String TAG = ImgUtil.class.getSimpleName();

    private static final String SHELF_IMG_PATH = AppConstant.SHELF_IMG_PATH;

    private static final Map<String, Integer> MAP = new HashMap<>();

    private static final Map<String, Integer> PROGRESS_MAP = new HashMap<>();

    private static int screenWidth;

    public static final int LOAD_ING = 1;
    public static final int LOAD_SUCCESS = 2;
    public static final int LOAD_FAIL = 3;

    public static ImageConfig getDefaultConfig(Context context, String url, RelativeLayout layout) {
        ImageConfig config = new ImageConfig(url, layout);
        config.setDefaultBitmap(null);
        config.setErrorBitmap(drawableToBitmap(getDrawable(context, R.drawable.ic_image_none)));
        config.setDrawable(getDrawable(context, R.drawable.ic_image_background));
        config.setScaleType(ImageView.ScaleType.FIT_XY);
        config.setLayoutParams(getLP(context));
        return config;
    }

    private static void initLayout(ImageConfig config, ImageView imageView, QMUIProgressBar progressBar) {
        imageView.setLayoutParams(config.getLayoutParams());
        imageView.setImageBitmap(config.getDefaultBitmap());
        imageView.setImageDrawable(config.getDrawable());
        imageView.setScaleType(config.getScaleType());
        imageView.setTag(config.getUrl());
        progressBar.setTag(config.getUrl());
    }

    public static void loadImage(Context context, ImageConfig config) {
        if (config != null && config.getLayout() != null) {
            ImageView imageView = config.getLayout().findViewById(R.id.imageView);
            QMUIProgressBar progressBar = config.getLayout().findViewById(R.id.progressBar);
            initLayout(config, imageView, progressBar);
            if (config.isForce()) {
                loadImageNet(context, config, imageView, progressBar);
            } else if (Objects.equals(MAP.get(config.getUrl()), LOAD_FAIL) || config.getUrl() == null) {
                imageView.setImageBitmap(config.getErrorBitmap());
            } else if (config.isSave() && !loadImageLocal(config, imageView)) {
                loadImageNet(context, config, imageView, progressBar);
            } else {
                loadImageNet(context, config, imageView, progressBar);
            }
        }
    }

    private static boolean loadImageLocal(ImageConfig config, ImageView imageView) {
        if (config.getSaveKey() != null && !Objects.equals(config.getSaveKey(), 0)) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            File file = new File(getLocalImgUrl(config.getSaveKey()));
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

    private static void loadImageNet(Context context, ImageConfig config, ImageView imageView, QMUIProgressBar progressBar) {
        String url = config.getUrl();
        GlideProgressInterceptor.addListener(url, new GlideProgressListener() {
            private int count;

            @Override
            public void onProgress(int progress, boolean success) {
                if (progress < 0 && count < 99) {
                    progress = ++count;
                }
                if (Objects.equals(url, progressBar.getTag())) {
                    PROGRESS_MAP.put(url, progress);
                    if (success && progress != 100) {
                        progressBar.setProgress(progress);
                    }
                    if (!success) {
                        PROGRESS_MAP.remove(url);
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
        });

        Glide.with(context)
                .asBitmap()
                .load(url)
                .transition(new BitmapTransitionOptions().crossFade())
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        GlideProgressListener listener = GlideProgressInterceptor.LISTENER_MAP.get(url);
                        if (listener != null) {
                            listener.onProgress(0, false);
                        }
                        GlideProgressInterceptor.removeListener(url);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        GlideProgressListener listener = GlideProgressInterceptor.LISTENER_MAP.get(url);
                        if (listener != null) {
                            listener.onProgress(100, false);
                        }
                        GlideProgressInterceptor.removeListener(url);
                        return false;
                    }
                })
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        if (Objects.equals(url, imageView.getTag())) {
                            imageView.setImageBitmap(config.getDefaultBitmap());
                            imageView.setLayoutParams(config.getLayoutParams());
                        }
                        if (Objects.equals(url, progressBar.getTag())) {
                            Integer integer = PROGRESS_MAP.get(url);
                            int progress = integer == null ? 0 : integer;
                            progressBar.setProgress(progress, false);
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
                        if (config.isSave() && config.getSaveKey() != null) {
                            try {
                                saveBitmapBackPath(resource, config.getSaveKey());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        if (Objects.equals(url, imageView.getTag())) {
                            imageView.setImageBitmap(config.getErrorBitmap());
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
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inSampleSize = 2;
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
        int length = bitmap.getByteCount();
        if (length / 1024 > 2000) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inSampleSize = 2;
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
