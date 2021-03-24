package com.qc.mynovel.ui.presenter;

import com.qc.mynovel.ui.view.NShelfView;
import com.qc.mynovel.util.DBUtil;
import com.qc.mynovel.util.NovelHelper;
import com.qc.mynovel.util.NovelUtil;

import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Request;
import the.one.base.ui.presenter.BasePresenter;
import top.luqichuang.common.self.SourceCallback;
import top.luqichuang.common.util.NetUtil;
import top.luqichuang.mynovel.model.NSource;
import top.luqichuang.mynovel.model.Novel;
import top.luqichuang.mynovel.model.NovelInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class NShelfPresenter extends BasePresenter<NShelfView> {

    private boolean isAll = false;

    private int priority = 0;

    private List<Novel> novelList;

    public void checkUpdate(List<Novel> novelList) {
        this.novelList = novelList;
        for (Novel novel : novelList) {
            if (isAll) {
                for (NovelInfo info : novel.getNovelInfoList()) {
                    checkUpdate(novel, info);
                }
            } else {
                checkUpdate(novel, novel.getNovelInfo());
            }
        }
    }

    private void checkUpdate(Novel novel, NovelInfo info) {
        NSource nSource = NovelHelper.nSource(novel);
        Request request = nSource.getDetailRequest(info.getDetailUrl());
        NetUtil.startLoad(request, new SourceCallback(request, nSource, NSource.DETAIL) {
            @Override
            public void onFailure(String errorMsg) {
                NShelfView view = getView();
                AndroidSchedulers.mainThread().scheduleDirect(() -> {
                    if (view != null) {
                        view.checkUpdateComplete(info.getTitle());
                    }
                });
            }

            @Override
            public void onResponse(String html, Map<String, Object> map) {
                NShelfView view = getView();
                AndroidSchedulers.mainThread().scheduleDirect(() -> {
                    if (view != null) {
                        String curUpdateChapter = info.getUpdateChapter();
                        nSource.setNovelDetail(info, html);
                        if (curUpdateChapter == null || !curUpdateChapter.equals(info.getUpdateChapter())) {
                            if (info.getUpdateChapter() != null) {
                                novel.setUpdate(NovelHelper.changeNovelInfo(novel, info.getNSourceId()));
                                novel.setPriority(++priority);
                                NovelUtil.first(novel);
                            }
                        }
                        DBUtil.saveNovel(novel, DBUtil.SAVE_CUR);
                        view.checkUpdateComplete(null);
                    }
                });
            }
        });
    }

    public void initPriority() {
        this.priority = 0;
    }

}
