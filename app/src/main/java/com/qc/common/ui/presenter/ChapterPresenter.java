package com.qc.common.ui.presenter;

import com.qc.common.constant.AppConstant;
import com.qc.common.constant.TmpData;
import com.qc.common.ui.view.ChapterView;
import com.qc.common.util.EntityHelper;

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
import top.luqichuang.common.util.SourceUtil;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/10 17:52
 * @ver 1.0
 */
public class ChapterPresenter extends BasePresenter<ChapterView> {

    public void load(Entity entity) {
        Source source = EntityHelper.commonSource(entity);
        EntityInfo info = entity.getInfo();
        Request request = source.getDetailRequest(info.getDetailUrl());
        NetUtil.startLoad(request, new CommonCallback(request, source, Source.DETAIL) {
            @Override
            public void onFailure(String errorMsg) {
                ChapterView view = getView();
                AndroidSchedulers.mainThread().scheduleDirect(() -> {
                    if (view != null) {
                        showErrorPage(errorMsg, v -> {
                            view.showLoadingPage();
                            load(entity);
                        });
                    }
                });
            }

            @Override
            public void onResponse(String html, Map<String, Object> map) {
                ChapterView view = getView();
                AndroidSchedulers.mainThread().scheduleDirect(() -> {
                    if (view != null) {
                        EntityHelper.setInfoDetail(entity, html, map);
                        view.loadComplete();
                    }
                });
            }
        });
    }

    public void updateSource(Entity entity) {
        List sourceList;
        if (TmpData.contentCode == AppConstant.COMIC_CODE) {
            sourceList = SourceUtil.getSourceList();
        } else if (TmpData.contentCode == AppConstant.READER_CODE) {
            sourceList = SourceUtil.getNSourceList();
        } else {
            sourceList = SourceUtil.getVSourceList();
        }
        for (Object o : sourceList) {
            Source source = (Source) o;
            Request request = source.getSearchRequest(entity.getTitle());
            NetUtil.startLoad(request, new CommonCallback(request, source, Source.SEARCH) {
                @Override
                public void onFailure(String errorMsg) {
                    ChapterView view = getView();
                    AndroidSchedulers.mainThread().scheduleDirect(() -> {
                        if (view != null) {
                            view.updateSourceComplete(null);
                        }
                    });
                }

                @Override
                public void onResponse(String html, Map<String, Object> map) {
                    ChapterView view = getView();
                    AndroidSchedulers.mainThread().scheduleDirect(() -> {
                        if (view != null) {
                            List infoList = source.getInfoList(html);
                            view.updateSourceComplete(infoList);
                        }
                    });
                }
            });
        }
    }
}
