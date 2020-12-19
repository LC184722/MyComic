package com.qc.mycomic.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.qc.mycomic.R;
import com.qc.mycomic.model.Comic;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/12/19 16:58
 * @ver 1.0
 */
public class ImgUtil {

    public static void loadImg(Context context, Comic comic, ImageView imageView) {
        if (imageView != null) {
            imageView.setTag(getKey(comic));
            imageView.setBackground(ContextCompat.getDrawable(context, R.drawable.no_image));
            imageView.setImageBitmap(null);
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap bitmap = BitmapFactory.decodeFile(getLocalImgUrl(comic), newOpts);
            if (bitmap != null) {
//                Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
//                imageView.setBackground(drawable);
                imageView.setImageBitmap(bitmap);
            } else {
                loadImgNet(context, comic, imageView);
            }
        }
    }

    private static void loadImgNet(Context context, Comic comic, ImageView imageView) {
        Glide.with(context)
                .asBitmap()
                .load(comic.getComicInfo().getImgUrl())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        if (Objects.equals(getKey(comic), imageView.getTag())) {
                            imageView.setImageBitmap(resource);
                        }
                        try {
                            saveBitmapBackPath(resource, comic);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    private static String saveDrawableBackPath(Drawable drawable, Comic comic) throws IOException {
        String path = Codes.IMG_PATH;
        File targetDir = new File(path);
        if (!targetDir.exists()) {
            try {
                targetDir.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String fileName = getFilename(comic);
        File savedFile = new File(path, fileName);
        if (!savedFile.exists()) {
            savedFile.createNewFile();
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(savedFile));
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        bitmapDrawable.getBitmap().compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
        return savedFile.getAbsolutePath();
    }

    private static String saveBitmapBackPath(Bitmap bm, Comic comic) throws IOException {
        String path = Codes.IMG_PATH;
        File targetDir = new File(path);
        if (!targetDir.exists()) {
            try {
                targetDir.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String fileName = getFilename(comic);
        File savedFile = new File(path, fileName);
        if (!savedFile.exists()) {
            savedFile.createNewFile();
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(savedFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
        return savedFile.getAbsolutePath();
    }

    private static String getKey(Comic comic) {
        return String.valueOf(comic.getComicInfo().getId());
    }

    private static String getFilename(Comic comic) {
        return "img_" + getKey(comic);
    }

    private static String getLocalImgUrl(Comic comic) {
        return Codes.IMG_PATH + "/" + getFilename(comic);
    }

}
