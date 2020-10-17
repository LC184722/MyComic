package com.qc.mycomic.ui.presenter;

import com.qc.mycomic.model.Comic;
import com.qc.mycomic.model.ComicInfo;
import com.qc.mycomic.util.Codes;
import com.qc.mycomic.util.ComicUtil;
import com.qc.mycomic.util.DBUtil;
import com.qc.mycomic.util.NetUtil;
import com.qc.mycomic.util.SourceUtil;
import com.qc.mycomic.ui.view.ShelfView;

import java.io.IOException;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import the.one.base.ui.presenter.BasePresenter;

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
        String url = info.getDetailUrl();
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ShelfView view = getView();
                AndroidSchedulers.mainThread().scheduleDirect(() -> {
                    if (view != null) {
                        view.checkUpdateComplete(info.getTitle());
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String html = ComicUtil.getHtml(response, comic.getSourceId());
                ShelfView view = getView();
                AndroidSchedulers.mainThread().scheduleDirect(new Runnable() {
                    @Override
                    public void run() {
                        if (view != null) {
                            String curUpdateChapter = info.getUpdateChapter();
                            SourceUtil.getSource(info.getSourceId()).setComicDetail(info, html);
                            if (curUpdateChapter == null || !curUpdateChapter.equals(info.getUpdateChapter())) {
                                comic.setUpdate(comic.changeComicInfo(info.getSourceId()));
                                comic.setPriority(++priority);
                                ComicUtil.first(comic);
                            }
                            DBUtil.saveComic(comic, DBUtil.SAVE_CUR);
                            view.checkUpdateComplete(null);
                        }
                    }
                });
            }
        };
        NetUtil.startLoad(url, callback);
    }

    public void initPriority() {
        this.priority = 0;
    }

}
