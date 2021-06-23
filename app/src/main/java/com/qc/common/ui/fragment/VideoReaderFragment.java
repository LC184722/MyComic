package com.qc.common.ui.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.qc.common.constant.TmpData;
import com.qc.common.self.JzPlayer;
import com.qc.common.ui.presenter.ReaderPresenter;
import com.qc.common.ui.view.ReaderView;
import com.qc.mycomic.R;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import java.util.List;

import cn.jzvd.Jzvd;
import the.one.base.ui.fragment.BaseFragment;
import the.one.base.ui.presenter.BasePresenter;
import top.luqichuang.common.model.Content;
import top.luqichuang.common.model.Entity;
import top.luqichuang.common.util.SourceHelper;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/23 1:15
 * @ver 1.0
 */
public class VideoReaderFragment extends BaseFragment implements ReaderView {

    private Entity entity;
    private ReaderPresenter presenter = new ReaderPresenter();
    private JzPlayer player;

    public static VideoReaderFragment getInstance(Entity entity) {
        VideoReaderFragment fragment = new VideoReaderFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("entity", entity);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.entity = (Entity) getArguments().get("entity");
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_reader_video;
    }

    @Override
    protected void initView(View rootView) {
        mTopLayout.setVisibility(View.GONE);
        QMUIDisplayHelper.setFullScreen(_mActivity);
        player = rootView.findViewById(R.id.player);
        showLoadingPage();
        requestServer();
    }

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }

    public void requestServer() {
        String key = SourceHelper.chapterKey(entity);
        String value = TmpData.map.get(key);
        if (value != null) {
            player.setUp(value, entity.getCurChapterTitle());
            showContentPage();
        } else {
            presenter.loadContentInfoList(entity);
        }
    }

    @Override
    public void loadReadContentComplete(List<Content> contentList, String errorMsg) {
        if (errorMsg != null) {
            showErrorPage(errorMsg, v -> {
                showLoadingPage();
                requestServer();
            });
        } else {
            String url = contentList.get(0).getUrl();
            String key = SourceHelper.chapterKey(entity);
            TmpData.map.put(key, url);
            player.setUp(url, entity.getCurChapterTitle());
            showContentPage();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Jzvd.releaseAllVideos();
        QMUIDisplayHelper.cancelFullScreen(_mActivity);
    }

    @Override
    public void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

}


//http://www.milimili.cc/e/action/player_i.php?id=3402&pid=13


//public class VideoReaderFragment extends BaseDataFragment<Content> implements ReaderView {
//
//    protected Entity entity;
//    protected EntityInfo entityInfo;
//    private boolean isLoadNext;
//    private ReaderPresenter presenter = new ReaderPresenter();
//    private List<Content> contentList;
//    private ReaderListAdapter readerListAdapter;
//    private int curChapterId;
//
//    private VideoView videoView;
//
//    public static VideoReaderFragment getInstance(Entity entity) {
//        VideoReaderFragment fragment = new VideoReaderFragment();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("entity", entity);
//        fragment.setArguments(bundle);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        this.entity = (Entity) getArguments().get("entity");
//        this.entityInfo = entity.getInfo();
//        this.curChapterId = -1;
//        this.isLoadNext = true;
//        TmpData.toStatus = Constant.READER_TO_CHAPTER;
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    protected void requestServer() {
//        if (contentList == null) {
//            presenter.loadContentInfoList(entity);
//        }
//    }
//
//    @Override
//    protected void initView(View rootView) {
//        super.initView(rootView);
//        View view = getView(R.layout.fragment_reader_video);
//        videoView = view.findViewById(R.id.videoView);
//        mStatusLayout.addView(view);
//    }
//
//    @Override
//    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
//
//    }
//
//    @Override
//    public boolean onItemLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
//        return false;
//    }
//
//    @Override
//    protected BaseQuickAdapter getAdapter() {
//        return new ComicReaderAdapter(R.layout.item_reader);
//    }
//
//    @Override
//    public BasePresenter getPresenter() {
//        return presenter;
//    }
//
//    @Override
//    public void loadReadContentComplete(List<Content> contentList, String errorMsg) {
//        if (errorMsg != null) {
//            if (adapter.getData().isEmpty()) {
//                showErrorPage(errorMsg, v -> {
//                    showLoadingPage();
//                    requestServer();
//                });
//            } else {
//                adapter.getLoadMoreModule().loadMoreFail();
//            }
//        } else {
//            onComplete(contentList);
//            videoView.setVideoPath(contentList.get(0).getUrl());
//        }
//    }
//}
