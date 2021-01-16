package com.qc.mycomic.ui.presenter;

import com.qc.mycomic.model.Comic;
import com.qc.mycomic.model.ComicInfo;
import com.qc.mycomic.model.DetailLoader;
import com.qc.mycomic.model.Source;
import com.qc.mycomic.self.SourceCallback;
import com.qc.mycomic.util.NetUtil;
import com.qc.mycomic.util.SourceUtil;
import com.qc.mycomic.ui.view.ChapterView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Request;
import the.one.base.ui.presenter.BasePresenter;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class ChapterPresenter extends BasePresenter<ChapterView> {

    public void load(Comic comic) {
        Source source = comic.getSource();
        if (!(source instanceof DetailLoader)) {
            loadInfoDetail(comic);
        } else {
            ((DetailLoader) source).loadInfoDetail(getView(), comic);
        }
    }

    private void loadInfoDetail(Comic comic) {
        Source source = comic.getSource();
        ComicInfo comicInfo = comic.getComicInfo();
        Request request = source.getDetailRequest(comicInfo.getDetailUrl());
        NetUtil.startLoad(request, new SourceCallback(request, source, Source.DETAIL) {
            @Override
            public void onFailure(String errorMsg) {
                ChapterView view = getView();
                AndroidSchedulers.mainThread().scheduleDirect(() -> {
                    if (view != null) {
                        showErrorPage(errorMsg, v -> {
                            view.showLoadingPage();
                            load(comic);
                        });
                    }
                });
            }

            @Override
            public void onResponse(String html) {
                ChapterView view = getView();
                AndroidSchedulers.mainThread().scheduleDirect(() -> {
                    if (view != null) {
                        comic.setInfoDetail(html);
                        view.loadComplete();
                    }
                });
            }
        });
    }

    public void updateSource(Comic comic) {
        List<Source> sourceList = SourceUtil.getSourceList();
        for (Source source : sourceList) {
            Request request = source.getSearchRequest(comic.getTitle());
            NetUtil.startLoad(request, new SourceCallback(request, source, Source.SEARCH) {
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
                public void onResponse(String html) {
                    ChapterView view = getView();
                    AndroidSchedulers.mainThread().scheduleDirect(() -> {
                        if (view != null) {
                            List<ComicInfo> infoList = source.getComicInfoList(html);
                            view.updateSourceComplete(infoList);
                        }
                    });
                }
            });
        }
    }
}
