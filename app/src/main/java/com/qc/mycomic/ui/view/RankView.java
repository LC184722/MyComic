package com.qc.mycomic.ui.view;

import com.qc.mycomic.model.Comic;

import java.util.List;

import the.one.base.ui.view.BaseView;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public interface RankView extends BaseView {

    void loadComplete(List<Comic> comicList);

}
