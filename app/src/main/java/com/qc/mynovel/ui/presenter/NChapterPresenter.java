package com.qc.mynovel.ui.presenter;

import com.qc.mynovel.ui.view.NChapterView;
import com.qc.mynovel.util.NovelHelper;

import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Request;
import the.one.base.ui.presenter.BasePresenter;
import top.luqichuang.common.self.SourceCallback;
import top.luqichuang.common.util.NSourceUtil;
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
public class NChapterPresenter extends BasePresenter<NChapterView> {

    public void load(Novel novel) {
        NSource nSource = NovelHelper.nSource(novel);
        NovelInfo novelInfo = novel.getNovelInfo();
        Request request = nSource.getDetailRequest(novelInfo.getDetailUrl());
        NetUtil.startLoad(request, new SourceCallback(request, nSource, NSource.DETAIL) {
            @Override
            public void onFailure(String errorMsg) {
                NChapterView view = getView();
                AndroidSchedulers.mainThread().scheduleDirect(() -> {
                    if (view != null) {
                        showErrorPage(errorMsg, v -> {
                            view.showLoadingPage();
                            load(novel);
                        });
                    }
                });
            }

            @Override
            public void onResponse(String html, Map<String, Object> map) {
                NChapterView view = getView();
                AndroidSchedulers.mainThread().scheduleDirect(() -> {
                    if (view != null) {
                        NovelHelper.setInfoDetail(novel, html);
                        view.loadComplete();
                    }
                });
            }
        });
    }

    public void updateNSource(Novel novel) {
        List<NSource> nSourceList = NSourceUtil.getNSourceList();
        for (NSource nSource : nSourceList) {
            Request request = nSource.getSearchRequest(novel.getTitle());
            NetUtil.startLoad(request, new SourceCallback(request, nSource, NSource.SEARCH) {
                @Override
                public void onFailure(String errorMsg) {
                    NChapterView view = getView();
                    AndroidSchedulers.mainThread().scheduleDirect(() -> {
                        if (view != null) {
                            view.updateNSourceComplete(null);
                        }
                    });
                }

                @Override
                public void onResponse(String html, Map<String, Object> map) {
                    NChapterView view = getView();
                    AndroidSchedulers.mainThread().scheduleDirect(() -> {
                        if (view != null) {
                            List<NovelInfo> infoList = nSource.getNovelInfoList(html);
                            view.updateNSourceComplete(infoList);
                        }
                    });
                }
            });
        }
    }
}
