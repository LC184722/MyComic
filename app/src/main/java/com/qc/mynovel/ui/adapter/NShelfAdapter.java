package com.qc.mynovel.ui.adapter;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.qc.common.self.ImageConfig;
import com.qc.common.util.ImgUtil;
import com.qc.mycomic.R;

import org.jetbrains.annotations.NotNull;

import the.one.base.adapter.TheBaseQuickAdapter;
import the.one.base.adapter.TheBaseViewHolder;
import top.luqichuang.mynovel.model.Novel;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class NShelfAdapter extends TheBaseQuickAdapter<Novel> {

    public NShelfAdapter() {
        super(R.layout.item_shelf);
    }

    @Override
    protected void convert(@NotNull TheBaseViewHolder holder, Novel novel) {
        holder.setText(R.id.tvTitle, novel.getTitle());
        String readText = novel.getNovelInfo().getCurChapterTitle() == null ? "未阅读" : "已读至：" + novel.getNovelInfo().getCurChapterTitle();
        holder.setText(R.id.tvCurChapter, readText);
        String updateChapter = novel.getNovelInfo().getUpdateChapter();
        if (updateChapter == null) {
            updateChapter = "未知";
        }
        holder.setText(R.id.tvChapter, "更新至：" + updateChapter);
        LinearLayout updateLayout = holder.findView(R.id.llTextUpdate);
        if (updateLayout != null) {
            if (novel.isUpdate()) {
                updateLayout.setVisibility(View.VISIBLE);
            } else {
                updateLayout.setVisibility(View.GONE);
            }
        }
        RelativeLayout layout = holder.findView(R.id.imageRelativeLayout);
        String saveKey = novel.getNovelInfo().getId() != 0 ? "N" + novel.getNovelInfo().getId() : null;
        ImageConfig config = ImgUtil.getDefaultConfig(getContext(), novel.getNovelInfo().getImgUrl(), layout);
        config.setSave(true);
        config.setSaveKey(saveKey);
        ImgUtil.loadImage(getContext(), config);
    }
}
