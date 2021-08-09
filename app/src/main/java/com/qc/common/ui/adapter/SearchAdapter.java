package com.qc.common.ui.adapter;

import android.widget.RelativeLayout;

import com.qc.common.constant.TmpData;
import com.qc.common.self.ImageConfig;
import com.qc.common.util.EntityHelper;
import com.qc.common.util.ImgUtil;
import com.qc.mycomic.R;

import org.jetbrains.annotations.NotNull;

import the.one.base.adapter.TheBaseQuickAdapter;
import the.one.base.adapter.TheBaseViewHolder;
import top.luqichuang.common.model.Entity;
import top.luqichuang.common.model.Source;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/11 15:33
 * @ver 1.0
 */
public class SearchAdapter extends TheBaseQuickAdapter<Entity> {

    public SearchAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NotNull TheBaseViewHolder holder, Entity entity) {
        holder.setText(R.id.tvTitle, entity.getInfo().getTitle());
        holder.setText(R.id.tvSource, TmpData.content + "源数量：" + EntityHelper.sourceSize(entity));
        holder.setText(R.id.tvAuthor, entity.getInfo().getAuthor());
        holder.setText(R.id.tvUpdateTime, entity.getInfo().getUpdateTime());
        RelativeLayout layout = holder.findView(R.id.imageRelativeLayout);
        ImageConfig config = ImgUtil.getDefaultConfig(getContext(), entity.getInfo().getImgUrl(), layout);
        Source source = EntityHelper.commonSource(entity);
        config.setHeaders(source.getImageHeaders());
        ImgUtil.loadImage(getContext(), config);
    }
}
