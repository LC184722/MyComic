package com.qc.mycomic.ui.adapter;

import android.view.View;
import android.widget.LinearLayout;

import com.qc.mycomic.R;
import com.qc.mycomic.model.Comic;
import com.qc.mycomic.util.ImgUtil;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;

import org.jetbrains.annotations.NotNull;

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
        ImgUtil.loadShelfImg(getContext(), comic, qivImg);
    }
}
