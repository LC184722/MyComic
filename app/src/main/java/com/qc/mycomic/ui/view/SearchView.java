package com.qc.mycomic.ui.view;

import java.util.List;

import the.one.base.ui.view.BaseView;
import top.luqichuang.common.mycomic.model.Comic;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public interface SearchView extends BaseView {

    void searchComplete(List<Comic> comicList, String sourceName);

}
