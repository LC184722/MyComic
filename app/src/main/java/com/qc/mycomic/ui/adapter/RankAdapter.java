package com.qc.mycomic.ui.adapter;

import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qc.mycomic.R;
import com.qc.common.util.ImgUtil;

import org.jetbrains.annotations.NotNull;

import the.one.base.adapter.TheBaseQuickAdapter;
import the.one.base.adapter.TheBaseViewHolder;
import top.luqichuang.mycomic.model.Comic;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class RankAdapter extends TheBaseQuickAdapter<Comic> {

    public static final int NO_IMG = -1;

    public RankAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NotNull TheBaseViewHolder holder, Comic comic) {
        holder.setText(R.id.tvTitle, comic.getComicInfo().getTitle());
        holder.setText(R.id.tvAuthor, comic.getComicInfo().getAuthor());
        TextView tvUpdateTime = holder.findView(R.id.tvUpdateTime);
        String updateTime = comic.getComicInfo().getUpdateTime();
        if (tvUpdateTime != null && updateTime != null) {
            tvUpdateTime.setText(updateTime);
        } else if (tvUpdateTime != null) {
            goneView(tvUpdateTime);
        }
        holder.setText(R.id.tvIndex, String.valueOf(holder.getAdapterPosition() + 1));
        RelativeLayout relativeLayout = holder.findView(R.id.imageRelativeLayout);
        ImgUtil.loadImage(getContext(), comic.getComicInfo().getImgUrl(), relativeLayout);
    }

    @Override
    protected int getDefItemViewType(int position) {
        if (getData().get(position).getComicInfo().getImgUrl() == null) {
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
