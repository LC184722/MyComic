package com.qc.mycomic.ui.adapter;

import android.content.res.ColorStateList;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.qc.mycomic.R;
import com.qc.mycomic.model.ChapterInfo;
import com.qc.mycomic.model.Comic;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButtonDrawable;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundLinearLayout;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import the.one.base.adapter.TheBaseQuickAdapter;
import the.one.base.adapter.TheBaseViewHolder;

/**
 * @author LuQiChuang
 * @description
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class ChapterAdapter extends TheBaseQuickAdapter<ChapterInfo> {

    Comic comic;

    public ChapterAdapter(int layoutResId) {
        super(layoutResId);
    }

    public ChapterAdapter(int layoutResId, @Nullable List<ChapterInfo> data) {
        super(layoutResId, data);
    }

    public ChapterAdapter(int layoutResId, Comic comic) {
        super(layoutResId);
        this.comic = comic;
    }

    @Override
    protected void convert(@NotNull TheBaseViewHolder holder, ChapterInfo chapterInfo) {
        holder.setText(R.id.tvTitle, chapterInfo.getTitle());
        if (comic.getComicInfo().getCurPosition() == holder.getAdapterPosition() - 1) {
            TextView tvTitle = holder.findView(R.id.tvTitle);
            tvTitle.setTextColor(getColor(R.color.white));
            QMUIRoundLinearLayout linearLayout = holder.findView(R.id.linearLayout);
            QMUIRoundButtonDrawable drawable = (QMUIRoundButtonDrawable) linearLayout.getBackground();
            ColorStateList colorStateList = ColorStateList.valueOf(getColor(R.color.colorPrimary));
            drawable.setBgData(colorStateList);
            Log.i(TAG, "convert: chapterInfo " + chapterInfo);
        } else {
            TextView tvTitle = holder.findView(R.id.tvTitle);
            tvTitle.setTextColor(getColor(R.color.black));
            QMUIRoundLinearLayout linearLayout = holder.findView(R.id.linearLayout);
            QMUIRoundButtonDrawable drawable = (QMUIRoundButtonDrawable) linearLayout.getBackground();
            ColorStateList colorStateList = ColorStateList.valueOf(getColor(R.color.white));
            drawable.setBgData(colorStateList);
        }
    }
}
