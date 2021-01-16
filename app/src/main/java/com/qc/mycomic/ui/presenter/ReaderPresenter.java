package com.qc.mycomic.ui.presenter;

import com.qc.mycomic.model.ChapterInfo;
import com.qc.mycomic.model.Comic;
import com.qc.mycomic.model.ImageInfo;
import com.qc.mycomic.model.Source;
import com.qc.mycomic.self.SourceCallback;
import com.qc.mycomic.util.NetUtil;
import com.qc.mycomic.ui.view.ReaderView;

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
public class ReaderPresenter extends BasePresenter<ReaderView> {

    public void loadImageInfoList(Comic comic) {
        List<ChapterInfo> chapterInfoList = comic.getComicInfo().getChapterInfoList();
        int position = comic.getComicInfo().getPosition();
        int chapterId = comic.getComicInfo().getCurChapterId();
        String url = chapterInfoList.get(position).getChapterUrl();
        Source source = comic.getSource();
        Request request = source.getImageRequest(url);
        NetUtil.startLoad(request, new SourceCallback(request, source, Source.IMAGE) {
            @Override
            public void onFailure(String errorMsg) {
                ReaderView view = getView();
                AndroidSchedulers.mainThread().scheduleDirect(() -> {
                    if (view != null) {
                        showErrorPage(errorMsg, v -> {
                            view.showLoadingPage();
                            loadImageInfoList(comic);
                        });
                    }
                });
            }

            @Override
            public void onResponse(String html) {
                ReaderView view = getView();
                List<ImageInfo> imageInfoList = comic.getImageInfoList(html, chapterId);
                if (imageInfoList.isEmpty()) {
                    onFailure("解析失败！");
                } else {
                    AndroidSchedulers.mainThread().scheduleDirect(() -> {
                        if (view != null) {
                            view.loadImageInfoListComplete(imageInfoList);
                        }
                    });
                }
            }
        });
    }
}
