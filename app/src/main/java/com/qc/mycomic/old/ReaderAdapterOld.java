//package com.qc.mycomic.adapter;
//
//import android.content.Context;
//import android.graphics.drawable.Drawable;
//import android.util.Log;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.model.GlideUrl;
//import com.bumptech.glide.load.model.Headers;
//import com.bumptech.glide.request.RequestOptions;
//import com.bumptech.glide.request.target.CustomTarget;
//import com.bumptech.glide.request.transition.Transition;
//import com.qc.mycomic.R;
//import com.qc.mycomic.model.ChapterInfo;
//import com.qc.mycomic.model.Comic;
//import com.qc.mycomic.model.ImageInfo;
//import com.qc.mycomic.util.Codes;
//import com.qc.mycomic.other.MyHeaders;
//import com.qmuiteam.qmui.util.QMUIDisplayHelper;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import the.one.base.adapter.TheBaseQuickAdapter;
//import the.one.base.adapter.TheBaseViewHolder;
//import the.one.base.util.glide.GlideUtil;
//
//public class ReaderAdapter extends TheBaseQuickAdapter<ImageInfo> {
//
//    private int count;
//    private Comic comic;
//    private List<ChapterInfo> chapterInfoList;
//    private Map<String, Drawable> map = new HashMap<>();
//    private LinearLayout.LayoutParams layoutParams;
//    private String chapterUrl;
//    private int preLoadNum = 3;
//
//    public static final int LOAD_NO = 0;
//    public static final int LOAD_ING = 1;
//    public static final int LOAD_SUCCESS = 2;
//    public static final int LOAD_FAIL = 3;
//
//    public ReaderAdapter(int layoutResId, @Nullable List<ImageInfo> data) {
//        super(layoutResId, data);
//    }
//
//    public ReaderAdapter(int layoutResId) {
//        super(layoutResId);
//    }
//
//    public ReaderAdapter(int layoutResId, Comic comic) {
//        super(layoutResId);
//        this.comic = comic;
//        this.chapterInfoList = comic.getComicInfo().getChapterInfoList();
//    }
//
//    @Override
//    protected void convert(@NotNull TheBaseViewHolder holder, ImageInfo imageInfo) {
//        if (layoutParams == null) {
//            int sWidth = QMUIDisplayHelper.getScreenWidth(getContext());
//            int sHeight = QMUIDisplayHelper.dp2px(getContext(), 300);
//            layoutParams = new LinearLayout.LayoutParams(sWidth, sHeight);
//        }
//        Log.i(TAG, "convert: " + getData());
////        int position = holder.getAdapterPosition();
////        int loadNum = position + preLoadNum < getData().size() ? preLoadNum : getData().size() - 1 - position;
////        for (int i = 1; i <= loadNum; i++) {
////        }
//        ImageView imageView = holder.findView(R.id.imageView);
//        if (imageView != null) {
//            imageView.setTag(imageInfo.toStringProgressDetail());
//            initImageView(imageView, imageInfo);
//        }
//        Log.i(TAG, "convert: position = " + holder.getAdapterPosition() + ", status = " + imageInfo.getStatus() + ", count = " + (++count));
//    }
//
//    public void clearMap() {
//        map.clear();
//    }
//
//    public void initImageView(ImageView imageView, ImageInfo imageInfo) {
//        initImageView(imageView, imageInfo, imageInfo.getStatus());
//    }
//
//    public void initImageView(ImageView imageView, ImageInfo imageInfo, int status) {
//        imageInfo.setStatus(status);
//        if (imageInfo.getStatus() == LOAD_NO) {
//            imageInfo.setStatus(LOAD_ING);
//            imageView.setLayoutParams(layoutParams);
//            imageView.setScaleType(ImageView.ScaleType.CENTER);
//            imageView.setImageDrawable(getDrawable(R.drawable.ic_baseline_image_search_24));
//            loadImage(getContext(), imageInfo, imageView);
//        } else if (imageInfo.getStatus() == LOAD_ING) {
//            imageView.setLayoutParams(layoutParams);
//            imageView.setScaleType(ImageView.ScaleType.CENTER);
//            imageView.setImageDrawable(getDrawable(R.drawable.ic_baseline_image_search_24));
//        } else if (imageInfo.getStatus() == LOAD_SUCCESS) {
//            if (imageView.getTag().equals(imageInfo.toStringProgressDetail())) {
//                Drawable resource = map.get(imageInfo.toStringProgressDetail());
//                if (resource != null) {
//                    imageView.setLayoutParams(getLP(resource));
//                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//                    imageView.setImageDrawable(resource);
//                } else {
//                    initImageView(imageView, imageInfo, LOAD_NO);
//                }
//            }
//        } else if (imageInfo.getStatus() == LOAD_FAIL) {
//            imageView.setLayoutParams(layoutParams);
//            imageView.setScaleType(ImageView.ScaleType.CENTER);
//            imageView.setImageDrawable(getDrawable(R.drawable.ic_baseline_broken_image_24));
//        }
//    }
//
//    private LinearLayout.LayoutParams getLP(Drawable drawable) {
//        if (drawable == null) {
//            return layoutParams;
//        }
//        int sWidth = QMUIDisplayHelper.getScreenWidth(getContext());
//        int bWidth = drawable.getIntrinsicWidth();
//        int bHeight = drawable.getIntrinsicHeight();
//        int sHeight = bHeight * sWidth / bWidth;
//        return new LinearLayout.LayoutParams(sWidth, sHeight);
//    }
//
//    public void loadImage(Context context, ImageInfo imageInfo, ImageView imageView) {
//        GlideUrl url;
//        if (comic.getComicInfo().getSourceId() == Codes.MI_TUI) {
//            Headers headers = new MyHeaders(comic.getComicInfo().getCurChapterInfo().getChapterUrl());
//            url = new GlideUrl(imageInfo.getUrl(), headers);
//        } else {
//            url = new GlideUrl(imageInfo.getUrl());
//        }
//        RequestOptions options = GlideUtil.getDefaultOptions();
//        Glide.with(context)
//                .asDrawable()
//                .load(url)
//                .apply(options)
//                .into(new CustomTarget<Drawable>() {
//                    @Override
//                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                        map.put(imageInfo.toStringProgressDetail(), resource);
//                        initImageView(imageView, imageInfo, LOAD_SUCCESS);
//                        Log.i(TAG, "onResourceReady: success " + imageInfo.toStringProgressDetail());
//                    }
//
//                    @Override
//                    public void onLoadCleared(@Nullable Drawable placeholder) {
//
//                    }
//
//                    @Override
//                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
//                        Log.e(TAG, "onLoadFailed: url = " + imageInfo.getUrl());
//                        Log.e(TAG, "onLoadFailed: load fail " + imageInfo.toStringProgressDetail());
//                        initImageView(imageView, imageInfo, LOAD_FAIL);
//                    }
//                });
//    }
//
//}
