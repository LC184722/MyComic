package com.qc.mycomic.ui.presenter;

import com.qc.mycomic.ui.view.RankView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Request;
import the.one.base.ui.presenter.BasePresenter;
import top.luqichuang.mycomic.model.Comic;
import top.luqichuang.mycomic.model.ComicInfo;
import top.luqichuang.mycomic.model.Source;
import top.luqichuang.common.self.SourceCallback;
import top.luqichuang.common.util.NetUtil;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class RankPresenter extends BasePresenter<RankView> {

    private Source source;
    private List<Comic> comicList;
    private Map<String, String> map = new HashMap<>();

    public RankPresenter(Source source) {
        this.source = source;
    }

    public void load(String url) {
        if (url == null) {
            getView().loadComplete(null);
        } else {
            if (map.containsKey(url)) {
                dealHtml(getView(), map.get(url));
            } else {
                loadWithNet(source.getRankRequest(url));
            }
        }
    }

    private void loadWithNet(Request request) {
        NetUtil.startLoad(request, new SourceCallback(request, source, Source.RANK) {
            @Override
            public void onFailure(String errorMsg) {
                RankView view = getView();
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
            public void onResponse(String html) {
                RankView view = getView();
                AndroidSchedulers.mainThread().scheduleDirect(() -> {
                    if (view != null) {
                        map.put(request.url().toString(), html);
                        dealHtml(view, html);
                    }
                });
            }
        });
    }

    private void dealHtml(RankView view, String html) {
        if (view != null) {
            List<ComicInfo> infoList = source.getRankComicInfoList(html);
            mergeInfoList(infoList);
            view.loadComplete(comicList);
        }
    }

    public void mergeInfoList(List<ComicInfo> infoList) {
        comicList = new ArrayList<>();
        for (ComicInfo comicInfo : infoList) {
            Comic comic = new Comic(comicInfo);
            comicList.add(comic);
        }
    }

}
