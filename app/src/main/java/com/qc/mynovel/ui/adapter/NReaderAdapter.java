package com.qc.mynovel.ui.adapter;

import android.widget.TextView;

import androidx.annotation.Dimension;

import com.qc.common.en.SettingEnum;
import com.qc.common.util.SettingUtil;
import com.qc.mycomic.R;
import com.qc.mynovel.util.NovelHelper;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import the.one.base.adapter.TheBaseQuickAdapter;
import the.one.base.adapter.TheBaseViewHolder;
import top.luqichuang.common.model.ChapterInfo;
import top.luqichuang.mynovel.model.ContentInfo;
import top.luqichuang.mynovel.model.NovelInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class NReaderAdapter extends TheBaseQuickAdapter<ContentInfo> {

    private List<ChapterInfo> chapterInfoList;
    private NovelInfo novelInfo;
    private int fontSize = (int) SettingUtil.getSettingKey(SettingEnum.NOVEL_FONT_SIZE);

    public NReaderAdapter(int layoutResId, NovelInfo novelInfo) {
        super(layoutResId);
        this.novelInfo = novelInfo;
        this.chapterInfoList = novelInfo.getChapterInfoList();
    }

    @Override
    protected void convert(@NotNull TheBaseViewHolder holder, ContentInfo contentInfo) {
        int position = NovelHelper.getPosition(novelInfo, contentInfo.getChapterId());
        holder.setText(R.id.tvTitle, chapterInfoList.get(position).getTitle());
//        holder.setText(R.id.tvTitle, novelInfo.getCurChapterTitle());
        TextView tvContent = holder.getView(R.id.tvContent);
        tvContent.setText(contentInfo.getContent());
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
