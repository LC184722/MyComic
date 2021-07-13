package com.qc.common.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.qc.common.ui.presenter.ReaderPresenter;
import com.qc.common.ui.view.ReaderView;
import com.qc.mycomic.R;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.util.List;

import the.one.base.ui.presenter.BasePresenter;
import the.one.base.util.ToastUtil;
import top.luqichuang.common.model.Content;
import top.luqichuang.common.model.Entity;
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/7/12 22:23
 * @ver 1.0
 */
public class GsyVideoActivity extends BaseGsyVideoActivity<StandardGSYVideoPlayer> implements ReaderView {

    private Entity entity;
    private Content content;
    private ReaderPresenter presenter = new ReaderPresenter();
    private StandardGSYVideoPlayer player;
    private GSYVideoOptionBuilder builder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.entity = (Entity) getIntent().getSerializableExtra("entity");
        QMUIDisplayHelper.setFullScreen(this);
        PlayerFactory.setPlayManager(Exo2PlayerManager.class);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_video;
    }

    @Override
    protected void initView(View mRootView) {
        player = mRootView.findViewById(R.id.player);
        requestServer();
    }

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    public StandardGSYVideoPlayer getGSYVideoPlayer() {
        return player;
    }

    @Override
    public GSYVideoOptionBuilder getGSYVideoOptionBuilder() {
        if (builder == null) {
            builder = new GSYVideoOptionBuilder()
                    .setUrl(null)
                    .setCacheWithPlay(true)
                    .setVideoTitle(entity.getTitle() + "-" + entity.getCurChapterTitle())
                    .setIsTouchWiget(true)
                    .setRotateViewAuto(false)
                    .setLockLand(false)
                    .setShowFullAnimation(false)//打开动画
                    .setNeedLockFull(true)
                    .setSeekRatio(3);
            if (content != null) {
                builder.setUrl(content.getUrl());
                builder.setMapHeadData(content.getHeaderMap());
            }
        }
        return builder;
    }

    @Override
    public void clickForFullScreen() {

    }

    @Override
    public boolean getDetailOrientationRotateAuto() {
        return true;
    }

    protected void requestServer() {
        showLoadingDialog("");
        if (entity == null) {
            entity = (Entity) getIntent().getSerializableExtra("entity");
        }
        if (entity != null) {
            presenter.loadContentInfoList(entity);
        }
    }

    @Override
    public void loadReadContentComplete(List<Content> contentList, String errorMsg) {
        if (errorMsg != null) {
            showErrorPage(errorMsg, v -> {
                requestServer();
            });
        } else {
            content = contentList.get(0);
            String url = content.getUrl();
            System.out.println("url = " + url);
            initVideoBuilderMode();
            hideLoadingDialog();
            if (url == null) {
                ToastUtil.show("视频地址无效！");
            }
        }
    }

    @Override
    public void onClickStartError(String url, Object... objects) {
        super.onClickStartError(url, objects);
        ToastUtil.show("视频地址无效！");
    }
}
