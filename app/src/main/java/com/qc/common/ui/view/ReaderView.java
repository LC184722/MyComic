package com.qc.common.ui.view;

import java.util.List;

import the.one.base.ui.view.BaseView;
import top.luqichuang.common.model.Content;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public interface ReaderView extends BaseView {

    void loadReadContentComplete(List<Content> contentList, String errorMsg);

}
