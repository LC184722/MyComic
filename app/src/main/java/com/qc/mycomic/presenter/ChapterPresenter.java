package com.qc.mycomic.presenter;

import android.util.Log;

import com.qc.mycomic.model.Comic;
import com.qc.mycomic.model.ComicInfo;
import com.qc.mycomic.model.Source;
import com.qc.mycomic.util.Codes;
import com.qc.mycomic.util.NetUtil;
import com.qc.mycomic.util.SourceUtil;
import com.qc.mycomic.util.StringUtil;
import com.qc.mycomic.view.ChapterView;

import java.io.IOException;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import the.one.base.ui.presenter.BasePresenter;

public class ChapterPresenter extends BasePresenter<ChapterView> {

    public void load(Comic comic) {
        String url = comic.getComicInfo().getDetailUrl();
        Log.i(TAG, "load: url = " + url);
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "load: fail url = " + url);
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
        NetUtil.startLoad(url, callback);
    }

    public void updateSource(Comic comic) {
        List<Source> sourceList = SourceUtil.getSourceList();
        for (Source source : sourceList) {
            String url = source.getSearchUrl(comic.getTitle());
            Log.i(TAG, "search: url = " + url);
            Callback callback = new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ChapterView view = getView();
                    Log.e(TAG, "updateSource: get fail url = " + url);
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
            NetUtil.startLoad(url, callback);
        }
    }
}
