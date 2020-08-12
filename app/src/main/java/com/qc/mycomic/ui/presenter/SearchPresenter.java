package com.qc.mycomic.ui.presenter;

import android.util.Log;

import com.qc.mycomic.model.Comic;
import com.qc.mycomic.model.ComicInfo;
import com.qc.mycomic.model.Source;
import com.qc.mycomic.util.Codes;
import com.qc.mycomic.util.NetUtil;
import com.qc.mycomic.util.SourceUtil;
import com.qc.mycomic.ui.view.SearchView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import the.one.base.ui.presenter.BasePresenter;

public class SearchPresenter extends BasePresenter<SearchView> {

    private List<Comic> comicList;

    public void search(String searchString) {
        comicList = new ArrayList<>();
        List<Source> sourceList = SourceUtil.getSourceList();
        for (Source source : sourceList) {
            Request request = source.getSearchRequest(searchString);
            Log.i(TAG, "search: url = " + request.url());
            Callback callback = new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "load: fail url = " + request.url());
                    e.printStackTrace();
                    SearchView view = getView();
                    AndroidSchedulers.mainThread().scheduleDirect(() -> {
                        if (view != null) {
                            view.searchComplete(comicList, source.getSourceName());
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.i(TAG, "load: " + response.toString());
                    String html;
                    if (source.getSourceId() == Codes.PU_FEI) {
                        byte[] b = response.body().bytes(); //获取数据的bytes
                        html = new String(b, "GB2312"); //然后将其转为gb2312
                    } else {
                        html = response.body().string();
                    }
                    SearchView view = getView();
                    AndroidSchedulers.mainThread().scheduleDirect(() -> {
                        if (view != null) {
                            List<ComicInfo> infoList = source.getComicInfoList(html);
                            mergeInfoList(infoList);
                            view.searchComplete(comicList, null);
                        }
                    });
                }
            };
            NetUtil.startLoad(request, callback);
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
