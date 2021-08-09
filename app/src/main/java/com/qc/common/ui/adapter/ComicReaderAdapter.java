package com.qc.common.ui.adapter;

import android.widget.RelativeLayout;

import com.qc.common.self.ImageConfig;
import com.qc.common.util.EntityHelper;
import com.qc.common.util.ImgUtil;
import com.qc.mycomic.R;

import org.jetbrains.annotations.NotNull;

import the.one.base.adapter.TheBaseQuickAdapter;
import the.one.base.adapter.TheBaseViewHolder;
import top.luqichuang.common.model.Content;
import top.luqichuang.common.model.Entity;
import top.luqichuang.common.model.Source;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/11 9:52
 * @ver 1.0
 */
public class ComicReaderAdapter extends TheBaseQuickAdapter<Content> {

    private Entity entity;

    public ComicReaderAdapter(int layoutResId, Entity entity) {
        super(layoutResId);
        this.entity = entity;
    }

    @Override
    protected void convert(@NotNull TheBaseViewHolder holder, Content content) {
        RelativeLayout layout = holder.findView(R.id.imageRelativeLayout);
        ImageConfig config = ImgUtil.getReaderConfig(getContext(), content.getUrl(), layout);
        Source source = EntityHelper.commonSource(entity);
        config.setHeaders(source.getImageHeaders());
        ImgUtil.loadImage(getContext(), config);
    }

}
