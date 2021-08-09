package com.qc.common.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
 * @date 2021/6/11 11:45
 * @ver 1.0
 */
public class RankAdapter extends TheBaseQuickAdapter<Entity> {

    public static final int NO_IMG = -1;

    public RankAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NotNull TheBaseViewHolder holder, Entity entity) {
        holder.setText(R.id.tvTitle, entity.getInfo().getTitle());
        holder.setText(R.id.tvAuthor, entity.getInfo().getAuthor());
        TextView tvUpdateTime = holder.findView(R.id.tvUpdateTime);
        String updateTime = entity.getInfo().getUpdateTime();
        if (tvUpdateTime != null && updateTime != null) {
            tvUpdateTime.setVisibility(View.VISIBLE);
            tvUpdateTime.setText(updateTime);
        } else if (tvUpdateTime != null) {
            goneView(tvUpdateTime);
        }
        holder.setText(R.id.tvIndex, String.valueOf(holder.getAdapterPosition() + 1));
        RelativeLayout relativeLayout = holder.findView(R.id.imageRelativeLayout);
        ImageConfig config = ImgUtil.getDefaultConfig(getContext(), entity.getInfo().getImgUrl(), relativeLayout);
        Source source = EntityHelper.commonSource(entity);
        config.setHeaders(source.getImageHeaders());
        ImgUtil.loadImage(getContext(), config);
    }

    @Override
    protected int getDefItemViewType(int position) {
        if (getData().get(position).getInfo().getImgUrl() == null) {
            return NO_IMG;
        }
        return super.getDefItemViewType(position);
    }

    @NotNull
    @Override
    protected TheBaseViewHolder onCreateDefViewHolder(@NotNull ViewGroup parent, int viewType) {
        if (viewType == NO_IMG) {
            return super.createBaseViewHolder(parent, R.layout.item_rank_right_simple);
        } else {
            return super.onCreateDefViewHolder(parent, viewType);
        }
    }
}