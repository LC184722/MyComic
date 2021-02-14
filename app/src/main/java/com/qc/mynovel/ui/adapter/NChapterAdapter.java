package com.qc.mynovel.ui.adapter;

import android.content.res.ColorStateList;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.qc.mycomic.R;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButtonDrawable;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundLinearLayout;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import the.one.base.adapter.TheBaseQuickAdapter;
import the.one.base.adapter.TheBaseViewHolder;
import top.luqichuang.common.model.ChapterInfo;
import top.luqichuang.mynovel.model.Novel;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class NChapterAdapter extends TheBaseQuickAdapter<ChapterInfo> {

    private Novel novel;

    private int chapterId = -1;

    public NChapterAdapter(int layoutResId) {
        super(layoutResId);
    }

    public NChapterAdapter(int layoutResId, @Nullable List<ChapterInfo> data) {
        super(layoutResId, data);
    }

    public NChapterAdapter(int layoutResId, Novel novel) {
        super(layoutResId);
        this.novel = novel;
        this.chapterId = novel.getNovelInfo().getCurChapterId();
    }

    @Override
    protected void convert(@NotNull TheBaseViewHolder holder, ChapterInfo chapterInfo) {
        holder.setText(R.id.tvTitle, chapterInfo.getTitle());
        TextView tvTitle = holder.findView(R.id.tvTitle);
        if (chapterInfo.getTitle().equals(novel.getNovelInfo().getCurChapterTitle())) {
            tvTitle.setTextColor(getColor(R.color.white));
            QMUIRoundLinearLayout linearLayout = holder.findView(R.id.linearLayout);
            QMUIRoundButtonDrawable drawable = (QMUIRoundButtonDrawable) linearLayout.getBackground();
            ColorStateList colorStateList = ColorStateList.valueOf(getColor(R.color.colorPrimary));
            drawable.setBgData(colorStateList);
            chapterId = chapterInfo.getId();
            //Log.i(TAG, "convert: chapterInfo " + chapterInfo);
        } else {
            tvTitle.setTextColor(getColor(R.color.black));
            QMUIRoundLinearLayout linearLayout = holder.findView(R.id.linearLayout);
            QMUIRoundButtonDrawable drawable = (QMUIRoundButtonDrawable) linearLayout.getBackground();
            ColorStateList colorStateList = ColorStateList.valueOf(getColor(R.color.white));
            drawable.setBgData(colorStateList);
        }
    }

    public int getChapterId() {
        return chapterId;
    }
}
