package com.qc.common.ui.adapter;

import android.widget.RelativeLayout;

import com.qc.common.self.ImageConfig;
import com.qc.common.util.ImgUtil;
import com.qc.mycomic.R;

import org.jetbrains.annotations.NotNull;

import the.one.base.adapter.TheBaseQuickAdapter;
import the.one.base.adapter.TheBaseViewHolder;
import top.luqichuang.common.model.Content;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/11 9:52
 * @ver 1.0
 */
public class ComicReaderAdapter extends TheBaseQuickAdapter<Content> {

    public ComicReaderAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NotNull TheBaseViewHolder holder, Content content) {
        RelativeLayout layout = holder.findView(R.id.imageRelativeLayout);
        ImageConfig config = ImgUtil.getReaderConfig(getContext(), content.getUrl(), layout);
        ImgUtil.loadImage(getContext(), config);
    }

}
