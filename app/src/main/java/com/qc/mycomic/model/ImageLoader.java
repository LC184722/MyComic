package com.qc.mycomic.model;

import com.qc.mycomic.ui.view.ReaderView;

/**
 * @author LuQiChuang
 * @desc 重写获得图片信息链表
 * @date 2020/8/12 15:24
 * @ver 1.0
 */
public interface ImageLoader {

    /**
     * 通过章节链接获得图片信息
     *
     * @param view
     * @param chapterUrl
     * @param chapterId
     * @return void
     */
    void loadImageInfoList(ReaderView view, String chapterUrl, int chapterId);

}
