package com.qc.mynovel.ui.presenter;

import com.qc.mynovel.ui.view.NReaderView;
import com.qc.mynovel.util.NovelHelper;

import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Request;
import the.one.base.ui.presenter.BasePresenter;
import top.luqichuang.common.model.ChapterInfo;
import top.luqichuang.common.self.SourceCallback;
import top.luqichuang.common.util.NetUtil;
import top.luqichuang.mynovel.model.ContentInfo;
import top.luqichuang.mynovel.model.NSource;
import top.luqichuang.mynovel.model.Novel;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class NReaderPresenter extends BasePresenter<NReaderView> {

    public void loadContentInfoList(Novel novel) {
        List<ChapterInfo> chapterInfoList = novel.getNovelInfo().getChapterInfoList();
        int position = NovelHelper.getPosition(novel.getNovelInfo());
        int chapterId = novel.getNovelInfo().getCurChapterId();
        String url = chapterInfoList.get(position).getChapterUrl();
        NSource source = NovelHelper.nSource(novel);
        Request request = source.getContentRequest(url);
        NetUtil.startLoad(request, new SourceCallback(request, source, NSource.CONTENT) {
            @Override
            public void onFailure(String errorMsg) {
                NReaderView view = getView();
                AndroidSchedulers.mainThread().scheduleDirect(() -> {
                    if (view != null) {
                        view.loadContentInfoListComplete(null, errorMsg);
                    }
                });
            }

            @Override
            public void onResponse(String html, Map<String, Object> map) {
                NReaderView view = getView();
                ContentInfo contentInfo = NovelHelper.getContentInfo(novel, html, chapterId);
                if (contentInfo == null || contentInfo.getContent() == null) {
                    onFailure("解析失败！");
                } else {
                    AndroidSchedulers.mainThread().scheduleDirect(() -> {
                        if (view != null) {
                            view.loadContentInfoListComplete(contentInfo, null);
                        }
                    });
                }
            }
        });
    }
}
