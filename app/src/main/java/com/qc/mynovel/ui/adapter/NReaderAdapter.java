package com.qc.mynovel.ui.adapter;

import android.widget.RelativeLayout;

import com.qc.mycomic.R;
import com.qc.common.util.ImgUtil;
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
        holder.setText(R.id.tvContent, contentInfo.getContent());
    }
}
