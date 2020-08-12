package com.qc.mycomic.ui.view;

import com.qc.mycomic.model.ComicInfo;

import java.util.List;

import the.one.base.ui.view.BaseView;

public interface ChapterView extends BaseView {

    void loadComplete();

    void updateSourceComplete(List<ComicInfo> infoList);

}
