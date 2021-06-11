package com.qc.common.ui.presenter;

import com.qc.common.constant.AppConstant;
import com.qc.common.constant.TmpData;
import com.qc.common.ui.view.RankView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Request;
import the.one.base.ui.presenter.BasePresenter;
import top.luqichuang.common.model.Entity;
import top.luqichuang.common.model.EntityInfo;
import top.luqichuang.common.model.Source;
import top.luqichuang.common.self.CommonCallback;
import top.luqichuang.common.util.NetUtil;
import top.luqichuang.mycomic.model.Comic;
import top.luqichuang.mycomic.model.ComicInfo;
import top.luqichuang.mynovel.model.Novel;
import top.luqichuang.mynovel.model.NovelInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/11 11:46
 * @ver 1.0
 */
public class RankPresenter extends BasePresenter<RankView> {

    private Source source;
    private List<Entity> entityList;
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
        NetUtil.startLoad(request, new CommonCallback(request, source, Source.RANK) {
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
            public void onResponse(String html, Map<String, Object> map) {
                RankView view = getView();
                AndroidSchedulers.mainThread().scheduleDirect(() -> {
                    if (view != null) {
                        RankPresenter.this.map.put(request.url().toString(), html);
                        dealHtml(view, html);
                    }
                });
            }
        });
    }

    private void dealHtml(RankView view, String html) {
        if (view != null) {
            List<EntityInfo> infoList = source.getRankInfoList(html);
            mergeInfoList(infoList);
            view.loadComplete(entityList);
        }
    }

    public void mergeInfoList(List<EntityInfo> infoList) {
        entityList = new ArrayList<>();
        for (EntityInfo entityInfo : infoList) {
            Entity entity;
            if (TmpData.contentCode == AppConstant.COMIC_CODE) {
                entity = new Comic((ComicInfo) entityInfo);
            } else {
                entity = new Novel((NovelInfo) entityInfo);
            }
            entityList.add(entity);
        }
    }

}