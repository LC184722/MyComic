package com.qc.mycomic.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.qc.mycomic.R;
import com.qc.mycomic.model.Comic;
import com.qc.mycomic.model.ImageInfo;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/12/19 16:58
 * @ver 1.0
 */
public class ImgUtil {

    private static String shelfImgPath = Codes.SHELF_IMG_PATH;

    private static Map<Object, Drawable> map = new HashMap<>();

    private static Set<Object> set = new HashSet<>();

    private static LinearLayout.LayoutParams layoutParams;

    public static final int LOAD_ING = 1;
    public static final int LOAD_SUCCESS = 2;
    public static final int LOAD_FAIL = 3;

    public static void loadShelfImg(Context context, Comic comic, ImageView imageView) {
        loadImg(context, comic.getComicInfo().getImgUrl(), comic.getComicInfo().getId(), imageView, true);
    }

    public static void loadReaderImg(Context context, ImageInfo imageInfo, ImageView imageView) {
        loadImg(context, imageInfo.getUrl(), imageInfo.toStringProgressDetail(), imageView, false);
    }

    public static void preloadReaderImg(Context context, ImageInfo imageInfo) {
        String url = imageInfo.getUrl();
        String key = imageInfo.toStringProgressDetail();
        if (!set.contains(key)) {
            set.add(key);
            Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                            Log.i("BmSize", "onResourceReady o: " + getBitmapSize(resource));
                            resource = compressBitmap(resource);
//                            Log.i("BmSize", "onResourceReady a: " + getBitmapSize(resource));
                            map.put(key, bitmapToDrawable(context, resource));
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }
    }

    public static void clearMap() {
        map.clear();
        set.clear();
    }

    public static int getLoadStatus(ImageInfo imageInfo) {
        if (imageInfo == null) {
            return LOAD_ING;
        }
        Object key = imageInfo.toStringProgressDetail();
        if (!map.containsKey(key)) {
            return LOAD_FAIL;
        } else if (map.get(key) == null) {
            return LOAD_ING;
        } else {
            return LOAD_SUCCESS;
        }
    }

    private static LinearLayout.LayoutParams getLP(Context context, Drawable drawable) {
        if (drawable == null) {
            if (layoutParams == null) {
                int sWidth = QMUIDisplayHelper.getScreenWidth(context);
                int sHeight = QMUIDisplayHelper.dp2px(context, 300);
                layoutParams = new LinearLayout.LayoutParams(sWidth, sHeight);
            }
            return layoutParams;
        }
        int sWidth = QMUIDisplayHelper.getScreenWidth(context);
        int bWidth = drawable.getIntrinsicWidth();
        int bHeight = drawable.getIntrinsicHeight();
        int sHeight = bHeight * sWidth / bWidth;
        return new LinearLayout.LayoutParams(sWidth, sHeight);
    }

    public static void loadImg(Context context, String url, Object key, ImageView imageView, boolean isSave) {
        if (imageView != null) {
            imageView.setTag(key);
            if (isSave) {
                BitmapFactory.Options newOpts = new BitmapFactory.Options();
                newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
                Bitmap bitmap = BitmapFactory.decodeFile(getLocalImgUrl(key), newOpts);
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                    return;
                }
            } else if (map.containsKey(key) && map.get(key) != null) {
                imageView.setLayoutParams(getLP(context, map.get(key)));
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                imageView.setImageBitmap(null);
                imageView.setBackground(map.get(key));
                return;
            } else if (map.containsKey(key) && map.get(key) == null) {
                imageView.setLayoutParams(getLP(context, map.get(key)));
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                imageView.setImageBitmap(drawableToBitmap(getDrawable(context, R.drawable.ic_image_reader_loading_foreground)));
                imageView.setBackground(getDrawable(context, R.drawable.ic_image_reader_background));
                return;
            } else {
                map.put(key, null);
            }
            loadImgNet(context, url, key, imageView, isSave);
        }
    }

    private static void loadImgNet(Context context, String url, Object key, ImageView imageView, boolean isSave) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        if (isSave) {
                            imageView.setImageBitmap(drawableToBitmap(getDrawable(context, R.drawable.ic_image_shelf_loading_foreground)));
                            imageView.setBackground(getDrawable(context, R.drawable.ic_image_background));
                        } else {
                            imageView.setImageBitmap(drawableToBitmap(getDrawable(context, R.drawable.ic_image_reader_loading_foreground)));
                            imageView.setBackground(getDrawable(context, R.drawable.ic_image_reader_background));
                        }
                    }

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Log.i("TAG", "onResourceReady: success " + url);
                        if (Objects.equals(key, imageView.getTag())) {
                            if (isSave) {
                                imageView.setImageBitmap(resource);
                                try {
                                    saveBitmapBackPath(resource, key);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                resource = compressBitmap(resource);
                                map.put(key, bitmapToDrawable(context, resource));
                                imageView.setLayoutParams(getLP(context, map.get(key)));
                                imageView.setScaleType(ImageView.ScaleType.CENTER);
                                imageView.setImageBitmap(null);
                                imageView.setBackground(map.get(key));
                            }
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        if (isSave) {
                            imageView.setImageBitmap(drawableToBitmap(getDrawable(context, R.drawable.ic_image_shelf_error_foreground)));
                            imageView.setBackground(getDrawable(context, R.drawable.ic_image_background));
                        } else {
                            imageView.setImageBitmap(drawableToBitmap(getDrawable(context, R.drawable.ic_image_reader_error_foreground)));
                            imageView.setBackground(getDrawable(context, R.drawable.ic_image_reader_background));
                            map.remove(key);
                        }
                    }
                });
    }

    private static String saveBitmapBackPath(Bitmap bm, Object key) throws IOException {
        File targetDir = new File(shelfImgPath);
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

    private static String getLocalImgUrl(Object key) {
        return shelfImgPath + "/img_" + key.toString();
    }

    public static Drawable getDrawable(Context context, int drawableId) {
        return ContextCompat.getDrawable(context, drawableId);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else {
            return drawableToBitmapByCanvas(drawable);
        }
    }

    public static Drawable bitmapToDrawable(Context context, Bitmap bitmap) {
        return new BitmapDrawable(context.getResources(), bitmap);
    }

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
     * 图片压缩
     *
     * @param bitmap bitmap
     * @return Bitmap
     */
    private static Bitmap compressBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 90;
        int length = baos.toByteArray().length / 1024;

        if (length > 5000) {
            //重置baos即清空baos
            baos.reset();
            //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        } else if (length > 4000) {
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        } else if (length > 3000) {
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        }
        //循环判断如果压缩后图片是否大于2M,大于继续压缩
        while (baos.toByteArray().length / 1024 > 2048) {
            //重置baos即清空baos
            baos.reset();
            //这里压缩options%，把压缩后的数据存放到baos中
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
            //每次都减少10
            options -= 10;
        }
        //把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        //把ByteArrayInputStream数据生成图片
        return BitmapFactory.decodeStream(isBm, null, null);
    }

    public static int getBitmapSize(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray().length / 1024;
    }

}
