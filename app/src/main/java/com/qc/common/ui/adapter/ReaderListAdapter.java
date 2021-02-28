package com.qc.common.ui.adapter;

import android.content.res.ColorStateList;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.qc.mycomic.R;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButtonDrawable;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundLinearLayout;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import the.one.base.adapter.TheBaseQuickAdapter;
import the.one.base.adapter.TheBaseViewHolder;
import top.luqichuang.common.model.ChapterInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/2/28 12:48
 * @ver 1.0
 */
public class ReaderListAdapter extends TheBaseQuickAdapter<ChapterInfo> {

    private int position = 0;

    public ReaderListAdapter(int layoutResId, @Nullable List<ChapterInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NotNull TheBaseViewHolder holder, ChapterInfo chapterInfo) {
        TextView textView = holder.getView(R.id.textView);
        QMUIRoundLinearLayout linearLayout = holder.findView(R.id.linearLayout);
        if (linearLayout != null) {
            if (position == holder.getAdapterPosition()) {
                textView.setText(chapterInfo.getTitle());
                textView.setTextColor(getColor(R.color.white));
                QMUIRoundButtonDrawable drawable = (QMUIRoundButtonDrawable) linearLayout.getBackground();
                ColorStateList colorStateList = ColorStateList.valueOf(getColor(R.color.qmui_config_color_blue));
                drawable.setBgData(colorStateList);
            } else {
                textView.setText(chapterInfo.getTitle());
                textView.setTextColor(getColor(R.color.black));
                QMUIRoundButtonDrawable drawable = (QMUIRoundButtonDrawable) linearLayout.getBackground();
                ColorStateList colorStateList = ColorStateList.valueOf(getColor(R.color.white));
                drawable.setBgData(colorStateList);
            }
        }
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
        notifyDataSetChanged();
    }

}
