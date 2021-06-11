package com.qc.common.ui.adapter;

import android.widget.TextView;

import androidx.annotation.Dimension;

import com.qc.common.en.SettingEnum;
import com.qc.common.util.EntityHelper;
import com.qc.common.util.SettingUtil;
import com.qc.mycomic.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import the.one.base.adapter.TheBaseQuickAdapter;
import the.one.base.adapter.TheBaseViewHolder;
import top.luqichuang.common.model.ChapterInfo;
import top.luqichuang.common.model.Content;
import top.luqichuang.mynovel.model.NovelInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/11 9:52
 * @ver 1.0
 */
public class NovelReaderAdapter extends TheBaseQuickAdapter<Content> {

    private List<ChapterInfo> chapterInfoList;
    private NovelInfo novelInfo;
    private int fontSize = (int) SettingUtil.getSettingKey(SettingEnum.NOVEL_FONT_SIZE);

    public NovelReaderAdapter(int layoutResId, NovelInfo novelInfo) {
        super(layoutResId);
        this.novelInfo = novelInfo;
        this.chapterInfoList = novelInfo.getChapterInfoList();
    }

    @Override
    protected void convert(@NotNull TheBaseViewHolder holder, Content content) {
        int position = EntityHelper.getPosition(novelInfo, content.getChapterId());
        holder.setText(R.id.tvTitle, chapterInfoList.get(position).getTitle());
        TextView tvContent = holder.getView(R.id.tvContent);
        tvContent.setText(content.getContent());
        tvContent.setTextSize(Dimension.SP, fontSize);
    }

    public String addFont() {
        return changeFontSize(fontSize + 1);
    }

    public String subFont() {
        return changeFontSize(fontSize - 1);
    }

    private String changeFontSize(int value) {
        if (value >= 16 && value <= 30) {
            fontSize = value;
            SettingUtil.putSetting(SettingEnum.NOVEL_FONT_SIZE, fontSize);
            notifyDataSetChanged();
        }
        return getFontSizeDesc();
    }

    public String getFontSizeDesc() {
        return fontSize + "sp";
    }
}