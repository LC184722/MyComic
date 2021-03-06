package com.qc.common.ui.view;

import java.util.List;

import the.one.base.ui.view.BaseView;
import top.luqichuang.common.model.EntityInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public interface ChapterView extends BaseView {

    void loadComplete();

    void updateSourceComplete(List<EntityInfo> infoList);

}
