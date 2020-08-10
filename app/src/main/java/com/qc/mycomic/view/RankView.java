package com.qc.mycomic.view;

import com.qc.mycomic.model.Comic;

import java.util.List;

import the.one.base.ui.view.BaseView;

public interface RankView extends BaseView {

    void loadComplete(List<Comic> comicList);

}
