package com.qc.mycomic.ui.presenter;

import com.qc.mycomic.ui.view.SearchView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Request;
import the.one.base.ui.presenter.BasePresenter;
import top.luqichuang.common.mycomic.model.Comic;
import top.luqichuang.common.mycomic.model.ComicInfo;
import top.luqichuang.common.mycomic.model.Source;
import top.luqichuang.common.mycomic.self.SourceCallback;
import top.luqichuang.common.mycomic.util.NetUtil;
import top.luqichuang.common.mycomic.util.SourceUtil;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class SearchPresenter extends BasePresenter<SearchView> {

    private List<Comic> comicList;

    public void search(String searchString) {
        comicList = new ArrayList<>();
        List<Source> sourceList = SourceUtil.getSourceList();
        for (Source source : sourceList) {
            Request request = source.getSearchRequest(searchString);
            NetUtil.startLoad(request, new SourceCallback(request, source, Source.SEARCH) {
                @Override
                public void onFailure(String errorMsg) {
                    SearchView view = getView();
                    AndroidSchedulers.mainThread().scheduleDirect(() -> {
                        if (view != null) {
                            view.searchComplete(comicList, source.getSourceName());
                        }
                    });
                }

                @Override
                public void onResponse(String html) {
                    SearchView view = getView();
                    AndroidSchedulers.mainThread().scheduleDirect(() -> {
                        if (view != null) {
                            List<ComicInfo> infoList = source.getComicInfoList(html);
                            mergeInfoList(infoList);
                            view.searchComplete(comicList, null);
                        }
                    });
                }
            });
        }
    }

    private void mergeInfoList(List<ComicInfo> infoList) {
        for (ComicInfo comicInfo : infoList) {
            boolean isExists = false;
            for (Comic comic : comicList) {
                if (comic.getTitle().equals(comicInfo.getTitle())) {
                    isExists = true;
                    comic.addComicInfo(comicInfo);
                }
            }
            if (!isExists) {
                Comic comic = new Comic(comicInfo);
                comicList.add(comic);
            }
        }
    }

}
