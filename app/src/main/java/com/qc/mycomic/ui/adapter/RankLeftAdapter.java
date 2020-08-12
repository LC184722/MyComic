package com.qc.mycomic.ui.adapter;

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

public class RankLeftAdapter extends TheBaseQuickAdapter<String> {

    private int position = 0;

    public RankLeftAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NotNull TheBaseViewHolder holder, String s) {
        TextView textView = holder.getView(R.id.textView);
        QMUIRoundLinearLayout linearLayout = holder.findView(R.id.linearLayout);
        if (linearLayout != null) {
            if (position == holder.getAdapterPosition()) {
                textView.setText(s);
                textView.setTextColor(getColor(R.color.white));
                QMUIRoundButtonDrawable drawable = (QMUIRoundButtonDrawable) linearLayout.getBackground();
                ColorStateList colorStateList = ColorStateList.valueOf(getColor(R.color.qmui_config_color_blue));
                drawable.setBgData(colorStateList);
            } else {
                textView.setText(s);
                textView.setTextColor(getColor(R.color.black));
                QMUIRoundButtonDrawable drawable = (QMUIRoundButtonDrawable) linearLayout.getBackground();
                ColorStateList colorStateList = ColorStateList.valueOf(getColor(R.color.white));
                drawable.setBgData(colorStateList);
            }
        }
    }

    public void setPosition(int position) {
        this.position = position;
        notifyDataSetChanged();
    }
}
