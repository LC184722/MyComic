package com.qc.mycomic.view;

import com.qc.mycomic.model.ImageInfo;

import java.util.List;

import the.one.base.ui.view.BaseView;

public interface ReaderView extends BaseView {

    void loadImageInfoListComplete(List<ImageInfo> imageInfoList);

}
