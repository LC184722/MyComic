package com.qc.mycomic.ui.fragment;

import android.view.Gravity;
import android.view.View;

import com.qc.mycomic.R;
import com.qc.mycomic.model.ImageInfo;
import com.qmuiteam.qmui.qqface.QMUIQQFaceView;

import java.util.LinkedList;
import java.util.List;

import the.one.base.ui.fragment.BaseImageSnapFragment;
import the.one.base.ui.presenter.BasePresenter;

/**
 * @author LuQiChuang
 * @desc 图片放大界面
 * @date 2020/8/12 15:24
 * @ver 1.0
 */
public class ReaderDetailFragment extends BaseImageSnapFragment<ImageInfo> {

    private ImageInfo imageInfo;

    private List<ImageInfo> imageInfoList;

    public ReaderDetailFragment(ImageInfo imageInfo) {
        this.imageInfo = imageInfo;
        this.imageInfoList = new LinkedList<>();
        this.imageInfoList.add(imageInfo);
    }

    @Override
    protected void initAdapter() {
        super.initAdapter();
        adapter.getLoadMoreModule().setOnLoadMoreListener(null);
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        String title = imageInfo.toStringProgress();
        QMUIQQFaceView mTitle = mTopLayout.setTitle(title);
        mTopLayout.setTitleGravity(Gravity.CENTER);
        mTitle.setTextColor(getColorr(R.color.qmui_config_color_gray_1));
        mTitle.getPaint().setFakeBoldText(true);
    }

    @Override
    protected void onScrollChanged(ImageInfo item, int position) {

    }

    @Override
    public void onVideoClick(ImageInfo data) {

    }

    @Override
    public boolean onImageLongClick(ImageInfo data) {
        return false;
    }

    @Override
    protected void requestServer() {
        onComplete(imageInfoList);
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }
}
