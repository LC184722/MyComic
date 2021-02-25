package com.qc.mynovel.ui.adapter;

import android.widget.RelativeLayout;

import com.qc.common.self.ImageConfig;
import com.qc.mycomic.R;
import com.qc.mynovel.util.NovelHelper;
import com.qc.common.util.ImgUtil;

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
public class NSearchAdapter extends TheBaseQuickAdapter<Novel> {

    public NSearchAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NotNull TheBaseViewHolder holder, Novel novel) {
        holder.setText(R.id.tvTitle, novel.getNovelInfo().getTitle());
        holder.setText(R.id.tvSource, "小说源数量：" + NovelHelper.nSourceSize(novel));
        holder.setText(R.id.tvAuthor, novel.getNovelInfo().getAuthor());
        holder.setText(R.id.tvUpdateTime, novel.getNovelInfo().getUpdateTime());
        RelativeLayout layout = holder.findView(R.id.imageRelativeLayout);
        ImageConfig config = ImgUtil.getDefaultConfig(getContext(), novel.getNovelInfo().getImgUrl(), layout);
        ImgUtil.loadImage(getContext(), config);
    }

}
