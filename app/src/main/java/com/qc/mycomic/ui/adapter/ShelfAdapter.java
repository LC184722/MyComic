package com.qc.mycomic.ui.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.qc.mycomic.R;
import com.qc.mycomic.model.Comic;
import com.qc.mycomic.util.Codes;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import the.one.base.adapter.TheBaseQuickAdapter;
import the.one.base.adapter.TheBaseViewHolder;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class ShelfAdapter extends TheBaseQuickAdapter<Comic> {

    public ShelfAdapter() {
        super(R.layout.item_shelf);
    }

    @Override
    protected void convert(@NotNull TheBaseViewHolder holder, Comic comic) {
        holder.setText(R.id.tvTitle, comic.getTitle());
        String readText = comic.getComicInfo().getCurChapterTitle() == null ? "未阅读" : "已读至：" + comic.getComicInfo().getCurChapterTitle();
        holder.setText(R.id.tvCurChapter, readText);
        String updateChapter = comic.getComicInfo().getUpdateChapter();
        if (updateChapter == null) {
            updateChapter = "未知";
        }
        holder.setText(R.id.tvChapter, "更新至：" + updateChapter);
        LinearLayout updateLayout = holder.findView(R.id.llTextUpdate);
        if (updateLayout != null) {
            if (comic.isUpdate()) {
                updateLayout.setVisibility(View.VISIBLE);
            } else {
                updateLayout.setVisibility(View.GONE);
            }
        }
        QMUIRadiusImageView qivImg = holder.findView(R.id.qivImg);
        if (qivImg != null) {
            qivImg.setImageBitmap(null);
            qivImg.setBackground(getDrawable(R.drawable.no_image));
            qivImg.setTag(comic.getComicInfo().getId());
            //判断图片是否缓存
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap bitmap = BitmapFactory.decodeFile(getLocalImgUrl(comic), newOpts);
            if (bitmap != null) {
                qivImg.setImageBitmap(bitmap);
            } else {
                loadImg(comic, qivImg);
            }
        }
    }

    private void loadImg(Comic comic, ImageView qivImg) {
        Glide.with(getContext()).
                asBitmap().
                load(comic.getComicInfo().getImgUrl()).
                into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        if (Objects.equals(comic.getComicInfo().getId(), qivImg.getTag())) {
                            qivImg.setImageBitmap(resource);
                        }
                        try {
                            String imgLocalPath = saveBitmapBackPath(resource, String.valueOf(comic.getComicInfo().getId()));
                            comic.getComicInfo().setLocalImgUrl(imgLocalPath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    private String saveBitmapBackPath(Bitmap bm, String key) throws IOException {
        String path = Codes.IMG_PATH;
        File targetDir = new File(path);
        if (!targetDir.exists()) {
            try {
                targetDir.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String fileName = getFilename(key);
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

    private String getFilename(String key) {
        return "img_" + key;
    }

    private String getLocalImgUrl(Comic comic) {
        return Codes.IMG_PATH + "/" + getFilename(String.valueOf(comic.getComicInfo().getId()));
    }
}
