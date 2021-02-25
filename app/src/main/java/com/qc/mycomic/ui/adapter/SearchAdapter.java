package com.qc.mycomic.ui.adapter;

import android.widget.RelativeLayout;

import com.qc.common.self.ImageConfig;
import com.qc.mycomic.R;
import com.qc.common.util.ImgUtil;

import org.jetbrains.annotations.NotNull;

import the.one.base.adapter.TheBaseQuickAdapter;
import the.one.base.adapter.TheBaseViewHolder;
import top.luqichuang.mycomic.model.Comic;

import com.qc.mycomic.util.ComicHelper;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class SearchAdapter extends TheBaseQuickAdapter<Comic> {

    public SearchAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NotNull TheBaseViewHolder holder, Comic comic) {
        holder.setText(R.id.tvTitle, comic.getComicInfo().getTitle());
        holder.setText(R.id.tvSource, "漫画源数量：" + ComicHelper.sourceSize(comic));
        holder.setText(R.id.tvAuthor, comic.getComicInfo().getAuthor());
        holder.setText(R.id.tvUpdateTime, comic.getComicInfo().getUpdateTime());
        RelativeLayout layout = holder.findView(R.id.imageRelativeLayout);
        ImageConfig config = ImgUtil.getDefaultConfig(getContext(), comic.getComicInfo().getImgUrl(), layout);
        ImgUtil.loadImage(getContext(), config);
    }

}
