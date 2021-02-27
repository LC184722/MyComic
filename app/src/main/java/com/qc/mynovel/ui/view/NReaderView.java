package com.qc.mynovel.ui.view;

import the.one.base.ui.view.BaseView;
import top.luqichuang.mynovel.model.ContentInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public interface NReaderView extends BaseView {

    void loadContentInfoListComplete(ContentInfo contentInfo, String errorMsg);

}
