package com.qc.mycomic.ui.presenter;

import android.util.Log;

import com.qc.mycomic.model.Comic;
import com.qc.mycomic.model.ComicInfo;
import com.qc.mycomic.model.Source;
import com.qc.mycomic.util.Codes;
import com.qc.mycomic.util.NetUtil;
import com.qc.mycomic.util.SourceUtil;
import com.qc.mycomic.ui.view.ChapterView;

import java.io.IOException;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
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
        ComicInfo comicInfo = comic.getComicInfo();
        Request request = source.getDetailRequest(comicInfo.getDetailUrl());
        Log.i(TAG, "load: url = " + request.url());
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "load: fail url = " + request.url());
                e.printStackTrace();
                ChapterView view = getView();
                AndroidSchedulers.mainThread().scheduleDirect(() -> {
                    showErrorPage(e.getMessage(), v -> {
                        view.showLoadingPage();
                        load(comic);
                    });
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "load: " + response.toString());
                String html;
                if (comic.getComicInfo().getSourceId() == Codes.PU_FEI) {
                    byte[] b = response.body().bytes(); //获取数据的bytes
                    html = new String(b, "GB2312"); //然后将其转为gb2312
                } else {
                    html = response.body().string();
                }
                ChapterView view = getView();
                AndroidSchedulers.mainThread().scheduleDirect(() -> {
                    if (view != null) {
                        comic.setInfoDetail(html);
                        Log.i(TAG, "onResponse: hashcode html");
                        view.loadComplete();
                    }
                    Log.i(TAG, "run: get html ok...");
                });
            }
        };
        NetUtil.startLoad(request, callback);
    }

    public void updateSource(Comic comic) {
        List<Source> sourceList = SourceUtil.getSourceList();
        for (Source source : sourceList) {
            Request request = source.getSearchRequest(comic.getTitle());
            Log.i(TAG, "search: url = " + request.url());
            Callback callback = new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ChapterView view = getView();
                    Log.e(TAG, "updateSource: get fail url = " + request.url());
                    AndroidSchedulers.mainThread().scheduleDirect(() -> {
                        view.updateSourceComplete(null);
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    ChapterView view = getView();
                    String html = response.body().string();
                    AndroidSchedulers.mainThread().scheduleDirect(() -> {
                        List<ComicInfo> infoList = source.getComicInfoList(html);
                        view.updateSourceComplete(infoList);
                        Log.i(TAG, "updateSource: infoList = " + infoList);
                    });
                }
            };
            NetUtil.startLoad(request, callback);
        }
    }
}
