package com.qc.mycomic.ui.presenter;

import android.util.Log;

import com.qc.mycomic.model.Comic;
import com.qc.mycomic.model.ComicInfo;
import com.qc.mycomic.model.Source;
import com.qc.mycomic.util.Codes;
import com.qc.mycomic.util.NetUtil;
import com.qc.mycomic.ui.view.RankView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import the.one.base.ui.presenter.BasePresenter;

public class RankPresenter extends BasePresenter<RankView> {

    private Source source;
    private List<Comic> comicList;
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
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "load: fail url = " + request.url());
                AndroidSchedulers.mainThread().scheduleDirect(() -> showErrorPage(e.getMessage(), v -> load(request.url().toString())));
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
                map.put(request.url().toString(), html);
                RankView view = getView();
                AndroidSchedulers.mainThread().scheduleDirect(() -> dealHtml(view, html));
            }
        };
        NetUtil.startLoad(request, callback);
    }

    private void dealHtml(RankView view, String html) {
        if (view != null) {
            List<ComicInfo> infoList = source.getRankComicInfoList(html);
            mergeInfoList(infoList);
            view.loadComplete(comicList);
        }
    }

    public void mergeInfoList(List<ComicInfo> infoList) {
        comicList = new ArrayList<>();
        for (ComicInfo comicInfo : infoList) {
            Comic comic = new Comic(comicInfo);
            comicList.add(comic);
        }
    }

}
