package com.qc.mycomic.ui.fragment;

import android.view.Gravity;
import android.view.View;

import com.qc.mycomic.R;
import com.qc.mycomic.self.SnapImageInfo;
import com.qc.mycomic.util.RestartUtil;
import com.qmuiteam.qmui.qqface.QMUIQQFaceView;

import java.util.ArrayList;
import java.util.List;

import the.one.base.ui.fragment.BaseImageSnapFragment;
import the.one.base.ui.presenter.BasePresenter;
import top.luqichuang.common.mycomic.model.ImageInfo;

/**
 * @author LuQiChuang
 * @desc 图片放大界面
 * @date 2020/8/12 15:24
 * @ver 1.0
 */
public class ReaderDetailFragment extends BaseImageSnapFragment<SnapImageInfo> {

    private SnapImageInfo snapImageInfo;

    private final List<SnapImageInfo> LIST = new ArrayList<>();

    public ReaderDetailFragment() {
        RestartUtil.restart(_mActivity);
    }

    public ReaderDetailFragment(ImageInfo imageInfo) {
        this.snapImageInfo = new SnapImageInfo(imageInfo);
        this.LIST.add(snapImageInfo);
    }

    @Override
    protected void initAdapter() {
        super.initAdapter();
        adapter.getLoadMoreModule().setOnLoadMoreListener(null);
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        String title = snapImageInfo.INFO.toStringProgress();
        QMUIQQFaceView mTitle = mTopLayout.setTitle(title);
        mTopLayout.setTitleGravity(Gravity.CENTER);
        mTitle.setTextColor(getColorr(R.color.qmui_config_color_gray_1));
        mTitle.getPaint().setFakeBoldText(true);
    }


    @Override
    protected void onScrollChanged(SnapImageInfo item, int position) {

    }

    @Override
    public void onVideoClick(SnapImageInfo data) {

    }

    @Override
    public boolean onImageLongClick(SnapImageInfo data) {
        return false;
    }

    @Override
    protected void requestServer() {
        onComplete(LIST);
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }
}
