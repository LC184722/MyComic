package com.qc.mycomic.ui.view;

import com.qc.mycomic.model.ImageInfo;

import java.util.List;

import the.one.base.ui.view.BaseView;

/**
 * @author LuQiChuang
 * @description
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public interface ReaderView extends BaseView {

    void loadImageInfoListComplete(List<ImageInfo> imageInfoList);

}
