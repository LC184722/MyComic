package com.qc.mycomic.ui.view;

import com.qc.mycomic.model.Comic;

import java.util.List;

import the.one.base.ui.view.BaseView;

public interface SearchView extends BaseView {

    void searchComplete(List<Comic> comicList, String sourceName);

}
