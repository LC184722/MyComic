package com.qc.common.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.qc.common.self.JzPlayer;
import com.qc.common.ui.presenter.ReaderPresenter;
import com.qc.common.ui.view.ReaderView;
import com.qc.mycomic.R;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import java.util.List;

import cn.jzvd.Jzvd;
import the.one.base.ui.activity.BaseActivity;
import the.one.base.ui.presenter.BasePresenter;
import top.luqichuang.common.model.Content;
import top.luqichuang.common.model.Entity;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/23 12:31
 * @ver 1.0
 */
public class VideoPlayerActivity extends BaseActivity implements ReaderView {

    private Entity entity;
    private ReaderPresenter presenter = new ReaderPresenter();
    private JzPlayer player;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.entity = (Entity) getIntent().getSerializableExtra("entity");
        QMUIDisplayHelper.setFullScreen(this);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_reader_video;
    }

    @Override
    protected void initView(View rootView) {
        player = rootView.findViewById(R.id.player);
        showLoadingDialog("加载中");
        requestServer();
    }

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }

    public void requestServer() {
        if (entity == null) {
            entity = (Entity) getIntent().getSerializableExtra("entity");
        }
        presenter.loadContentInfoList(entity);
    }

    @Override
    public void loadReadContentComplete(List<Content> contentList, String errorMsg) {
        if (errorMsg != null) {
            showErrorPage(errorMsg, v -> {
                showLoadingDialog("加载中");
                requestServer();
            });
        } else {
            String url = contentList.get(0).getUrl();
            System.out.println("url = " + url);
            player.setUp(url, entity.getTitle() + "-" + entity.getCurChapterTitle());
            hideLoadingDialog();
        }
    }

    @Override
    protected void doOnBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.doOnBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

}