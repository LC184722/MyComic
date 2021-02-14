package com.qc.mynovel.ui.view;

import java.util.List;

import the.one.base.ui.view.BaseView;
import top.luqichuang.mynovel.model.NovelInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public interface NChapterView extends BaseView {

    void loadComplete();

    void updateNSourceComplete(List<NovelInfo> infoList);

}
