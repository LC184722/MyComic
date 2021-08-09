package com.qc.common.ui.adapter;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.qc.common.constant.AppConstant;
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
 * @date 2021/6/9 18:43
 * @ver 1.0
 */
public class ShelfAdapter extends TheBaseQuickAdapter<Entity> {

    public ShelfAdapter() {
        super(R.layout.item_shelf);
    }

    @Override
    protected void convert(@NotNull TheBaseViewHolder holder, Entity entity) {
        holder.setText(R.id.tvTitle, entity.getTitle());
        String readText = entity.getCurChapterTitle();
        if (readText == null) {
            readText = "未阅读";
        }
        holder.setText(R.id.tvCurChapter, "阅读至：" + readText);
        String updateChapter = entity.getUpdateChapter();
        if (updateChapter == null) {
            updateChapter = "未知";
        }
        holder.setText(R.id.tvChapter, "更新至：" + updateChapter);

        LinearLayout updateLayout = holder.findView(R.id.llTextUpdate);
        if (updateLayout != null) {
            if (entity.isUpdate()) {
                updateLayout.setVisibility(View.VISIBLE);
            } else {
                updateLayout.setVisibility(View.GONE);
            }
        }
        RelativeLayout layout = holder.findView(R.id.imageRelativeLayout);
        ImageConfig config = ImgUtil.getDefaultConfig(getContext(), entity.getImgUrl(), layout);
        config.setSave(true);
        if (entity.getInfoId() == 0) {
            config.setSaveKey(null);
        } else {
            if (TmpData.contentCode == AppConstant.COMIC_CODE) {
                config.setSaveKey(entity.getInfoId());
            } else if (TmpData.contentCode == AppConstant.READER_CODE) {
                config.setSaveKey("N" + entity.getInfoId());
            } else {
                config.setSaveKey("V" + entity.getInfoId());
            }
        }
        Source source = EntityHelper.commonSource(entity);
        config.setHeaders(source.getImageHeaders());
        ImgUtil.loadImage(getContext(), config);
    }


}
