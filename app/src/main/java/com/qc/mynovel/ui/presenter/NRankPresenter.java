package com.qc.mynovel.ui.presenter;

import com.qc.mynovel.ui.view.NRankView;

import java.util.ArrayList;
import java.util.HashMap;
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
public class NRankPresenter extends BasePresenter<NRankView> {

    private NSource nSource;
    private List<Novel> novelList;
    private Map<String, String> map = new HashMap<>();

    public NRankPresenter(NSource nSource) {
        this.nSource = nSource;
    }

    public void load(String url) {
        if (url == null) {
            getView().loadComplete(null);
        } else {
            if (map.containsKey(url)) {
                dealHtml(getView(), map.get(url));
            } else {
                loadWithNet(nSource.getRankRequest(url));
            }
        }
    }

    private void loadWithNet(Request request) {
        NetUtil.startLoad(request, new SourceCallback(request, nSource, NSource.RANK) {
            @Override
            public void onFailure(String errorMsg) {
                NRankView view = getView();
                AndroidSchedulers.mainThread().scheduleDirect(() -> {
                    if (view != null) {
                        showErrorPage(errorMsg, v -> {
                            view.showLoadingPage();
                            load(request.url().toString());
                        });
                    }
                });
            }

            @Override
            public void onResponse(String html, Map<String, Object> map) {
                NRankView view = getView();
                AndroidSchedulers.mainThread().scheduleDirect(() -> {
                    if (view != null) {
                        NRankPresenter.this.map.put(request.url().toString(), html);
                        dealHtml(view, html);
                    }
                });
            }
        });
    }

    private void dealHtml(NRankView view, String html) {
        if (view != null) {
            List<NovelInfo> infoList = nSource.getRankNovelInfoList(html);
            mergeInfoList(infoList);
            view.loadComplete(novelList);
        }
    }

    public void mergeInfoList(List<NovelInfo> infoList) {
        novelList = new ArrayList<>();
        for (NovelInfo novelInfo : infoList) {
            Novel novel = new Novel(novelInfo);
            novelList.add(novel);
        }
    }

}
