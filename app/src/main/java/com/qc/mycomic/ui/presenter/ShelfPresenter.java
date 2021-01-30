package com.qc.mycomic.ui.presenter;

import com.qc.mycomic.util.ComicUtil;
import com.qc.mycomic.util.DBUtil;
import com.qc.mycomic.ui.view.ShelfView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Request;
import the.one.base.ui.presenter.BasePresenter;
import top.luqichuang.common.mycomic.model.Comic;
import top.luqichuang.common.mycomic.model.ComicInfo;
import top.luqichuang.common.mycomic.model.Source;
import top.luqichuang.common.mycomic.self.SourceCallback;
import com.qc.mycomic.util.ComicHelper;
import top.luqichuang.common.mycomic.util.NetUtil;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class ShelfPresenter extends BasePresenter<ShelfView> {

    private boolean isAll = false;

    private int priority = 0;

    private List<Comic> comicList;

    public void checkUpdate(List<Comic> comicList) {
        this.comicList = comicList;
        for (Comic comic : comicList) {
            if (isAll) {
                for (ComicInfo info : comic.getComicInfoList()) {
                    checkUpdate(comic, info);
                }
            } else {
                checkUpdate(comic, comic.getComicInfo());
            }
        }
    }

    private void checkUpdate(Comic comic, ComicInfo info) {
        Source source = ComicHelper.source(comic);
        Request request = source.getDetailRequest(info.getDetailUrl());
        NetUtil.startLoad(request, new SourceCallback(request, source, Source.DETAIL) {
            @Override
            public void onFailure(String errorMsg) {
                ShelfView view = getView();
                AndroidSchedulers.mainThread().scheduleDirect(() -> {
                    if (view != null) {
                        view.checkUpdateComplete(info.getTitle());
                    }
                });
            }

            @Override
            public void onResponse(String html) {
                ShelfView view = getView();
                AndroidSchedulers.mainThread().scheduleDirect(() -> {
                    if (view != null) {
                        String curUpdateChapter = info.getUpdateChapter();
                        source.setComicDetail(info, html);
                        if (curUpdateChapter == null || !curUpdateChapter.equals(info.getUpdateChapter())) {
                            if (info.getUpdateChapter() != null) {
                                comic.setUpdate(ComicHelper.changeComicInfo(comic, info.getSourceId()));
                                comic.setPriority(++priority);
                                ComicUtil.first(comic);
                            }
                        }
                        DBUtil.saveComic(comic, DBUtil.SAVE_CUR);
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
