package com.qc.mycomic.ui.view;

import com.qc.mycomic.model.ComicInfo;

import java.util.List;

import the.one.base.ui.view.BaseView;

/**
 * @author LuQiChuang
 * @description
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public interface ChapterView extends BaseView {

    void loadComplete();

    void updateSourceComplete(List<ComicInfo> infoList);

}
