package com.qc.mycomic.ui.presenter;

import android.util.Log;

import com.qc.mycomic.model.ChapterInfo;
import com.qc.mycomic.model.Comic;
import com.qc.mycomic.model.ImageInfo;
import com.qc.mycomic.model.ImageLoader;
import com.qc.mycomic.model.Source;
import com.qc.mycomic.util.Codes;
import com.qc.mycomic.util.NetUtil;
import com.qc.mycomic.ui.view.ReaderView;

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
public class ReaderPresenter extends BasePresenter<ReaderView> {

    List<ChapterInfo> chapterInfoList;

    public void loadImageInfoList(Comic comic) {
        chapterInfoList = comic.getComicInfo().getChapterInfoList();
        int position = comic.getComicInfo().getPosition();
        int chapterId = comic.getComicInfo().getCurChapterId();
        String url = chapterInfoList.get(position).getChapterUrl();
        Source source = comic.getSource();
        if (!(source instanceof ImageLoader)) {
            Log.i(TAG, "loadImageInfoList: url = " + url);
            Callback callback = new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ReaderView view = getView();
                    AndroidSchedulers.mainThread().scheduleDirect(() -> {
                        if (view != null) {
                            showErrorPage(e.getMessage(), v -> {
                                Log.e(TAG, "onClick: getError " + e.getMessage());
                                view.showLoadingPage();
                                loadImageInfoList(comic);
                            });
                        }
                        Log.i(TAG, "run: get html fail ok...");
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String html;
                    if (comic.getComicInfo().getSourceId() == Codes.PU_FEI) {
                        byte[] b = response.body().bytes(); //获取数据的bytes
                        html = new String(b, "GB2312"); //然后将其转为gb2312
                    } else {
                        html = response.body().string();
                    }
                    List<ImageInfo> imageInfoList = comic.getImageInfoList(html, chapterId);
                    Log.i(TAG, "loadImageInfoList: " + imageInfoList);
                    ReaderView view = getView();
                    AndroidSchedulers.mainThread().scheduleDirect(() -> {
                        if (view != null) {
                            if (imageInfoList.size() > 0) {
                                view.loadImageInfoListComplete(imageInfoList);
                            } else {
                                view.showErrorPage("解析失败");
                            }
                        }
                        Log.i(TAG, "run: get html ok...");
                    });
                }
            };
            NetUtil.startLoad(url, callback);
        } else {
            ((ImageLoader) source).loadImageInfoList(getView(), url, chapterId);
        }
    }
}
