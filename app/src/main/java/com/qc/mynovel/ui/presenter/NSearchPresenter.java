package com.qc.mynovel.ui.presenter;

import com.qc.mynovel.util.NovelHelper;
import com.qc.mynovel.ui.view.NSearchView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Request;
import the.one.base.ui.presenter.BasePresenter;
import top.luqichuang.common.self.SourceCallback;
import top.luqichuang.common.util.NetUtil;
import top.luqichuang.common.util.NSourceUtil;
import top.luqichuang.mynovel.model.NSource;
import top.luqichuang.mynovel.model.Novel;
import top.luqichuang.mynovel.model.NovelInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class NSearchPresenter extends BasePresenter<NSearchView> {

    private List<Novel> novelList;

    public void search(String searchString) {
        novelList = new ArrayList<>();
        List<NSource> nSourceList = NSourceUtil.getNSourceList();
        for (NSource nSource : nSourceList) {
            Request request = nSource.getSearchRequest(searchString);
            NetUtil.startLoad(request, new SourceCallback(request, nSource, NSource.SEARCH) {
                @Override
                public void onFailure(String errorMsg) {
                    NSearchView view = getView();
                    AndroidSchedulers.mainThread().scheduleDirect(() -> {
                        if (view != null) {
                            view.searchComplete(novelList, nSource.getNSourceName());
                        }
                    });
                }

                @Override
                public void onResponse(String html) {
                    NSearchView view = getView();
                    AndroidSchedulers.mainThread().scheduleDirect(() -> {
                        if (view != null) {
                            List<NovelInfo> infoList = nSource.getNovelInfoList(html);
                            mergeInfoList(infoList);
                            view.searchComplete(novelList, null);
                        }
                    });
                }
            });
        }
    }

    private void mergeInfoList(List<NovelInfo> infoList) {
        for (NovelInfo novelInfo : infoList) {
            boolean isExists = false;
            for (Novel novel : novelList) {
                if (novel.getTitle().equals(novelInfo.getTitle())) {
                    isExists = true;
                    NovelHelper.addNovelInfo(novel, novelInfo);
                }
            }
            if (!isExists) {
                Novel novel = new Novel(novelInfo);
                novelList.add(novel);
            }
        }
    }

}
