package com.qc.mynovel.ui.fragment;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qc.common.constant.Constant;
import com.qc.common.constant.TmpData;
import com.qc.common.util.ImgUtil;
import com.qc.common.util.RestartUtil;
import com.qc.mycomic.R;
import com.qc.mynovel.ui.adapter.NReaderAdapter;
import com.qc.mynovel.ui.presenter.NReaderPresenter;
import com.qc.mynovel.ui.view.NReaderView;
import com.qc.mynovel.util.DBUtil;
import com.qc.mynovel.util.NovelHelper;
import com.qc.mynovel.util.NovelUtil;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIViewHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import the.one.base.ui.fragment.BaseDataFragment;
import the.one.base.ui.presenter.BasePresenter;
import top.luqichuang.mynovel.model.ContentInfo;
import top.luqichuang.mynovel.model.Novel;
import top.luqichuang.mynovel.model.NovelInfo;

import static android.view.View.VISIBLE;
import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;

/**
 * @author LuQiChuang
 * @desc 小说阅读界面
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class NReaderFragment extends BaseDataFragment<ContentInfo> implements NReaderView {

    private Novel novel;
    private NovelInfo novelInfo;
    private boolean isLoadNext;
    private NReaderPresenter presenter = new NReaderPresenter();
    private List<ContentInfo> contentInfoList;
    private NReaderAdapter readerAdapter;
    private int curChapterId;

    private View topView;
    private View bottomView;
    private TextView tvChapter;
    private TextView tvProgress;
    private TextView tvInfo;
    private LinearLayout llLeft;
    private LinearLayout llRight;
    private SeekBar seekBar;
    private boolean firstLoad = true;

    public NReaderFragment() {
        RestartUtil.restart(_mActivity);
    }

    public NReaderFragment(Novel novel) {
        this.novel = novel;
        this.novelInfo = novel.getNovelInfo();
        this.curChapterId = novelInfo.getCurChapterId();
        this.isLoadNext = true;
        TmpData.toStatus = Constant.READER_TO_CHAPTER;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mTopLayout.setVisibility(View.GONE);
    }

    private void addView() {
        if (topView == null) {
            topView = getView(R.layout.top_reader);
            tvChapter = topView.findViewById(R.id.tvChapter);
            tvProgress = topView.findViewById(R.id.tvProgress);
            tvProgress.setVisibility(View.GONE);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(QMUIDisplayHelper.getScreenWidth(_mActivity), QMUIDisplayHelper.getScreenHeight(_mActivity));
            mStatusLayout.addView(topView, 1, layoutParams);
        }
        if (bottomView == null) {
            bottomView = getView(R.layout.bottom_reader);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(QMUIDisplayHelper.getScreenWidth(_mActivity), QMUIDisplayHelper.getScreenHeight(_mActivity));
            mStatusLayout.addView(bottomView, 1, layoutParams);
            bottomView.setVisibility(View.GONE);
            llLeft = bottomView.findViewById(R.id.llLeft);
            llRight = bottomView.findViewById(R.id.llRight);
            seekBar = bottomView.findViewById(R.id.seekBar);
            seekBar.setVisibility(View.GONE);
            tvInfo = bottomView.findViewById(R.id.tvInfo);
        }
        setValue();
    }

    private void initOtherView() {
        addView();
        if (firstLoad) {
            firstLoad = false;
            setListener();
        }
    }

    private void setValue() {
        if (!contentInfoList.isEmpty()) {
            if (contentInfoList.size() <= first) {
                first = 0;
            }
            ContentInfo contentInfo = contentInfoList.get(first);
            tvChapter.setText(novelInfo.getCurChapterTitle());
            tvInfo.setText(String.format(Locale.CHINA, "%d章/%d章", contentInfo.getChapterId() + 1, novelInfo.getChapterInfoList().size()));
        }
    }

    private boolean isSmooth = false;
    private boolean isFresh = false;

    private void setListener() {
        llLeft.setOnClickListener(v -> {
            if (NovelHelper.checkChapterId(novelInfo, NovelHelper.getPrevChapterId(novelInfo))) {
                isFresh = true;
                onRefresh();
            } else {
                showFailTips("没有上一章");
            }
        });

        llRight.setOnClickListener(v -> {
            if (NovelHelper.checkChapterId(novelInfo, NovelHelper.getNextChapterId(novelInfo))) {
                isFresh = true;
                super.onRefresh();
            } else {
                showFailTips("没有下一章");
            }
        });
    }

    private int first;

    @Override
    protected RecyclerView.OnScrollListener getOnScrollListener() {
        return new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (null == _mActivity) return;
                if (newState == SCROLL_STATE_IDLE) {
                    isGlidePause = false;
                    Glide.with(_mActivity).resumeRequests();
                } else if (!isGlidePause) {
                    isGlidePause = true;
                    Glide.with(_mActivity).pauseRequests();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (manager != null) {
                    first = manager.findFirstVisibleItemPosition();//得到显示屏内的第一个list的位置数position
                    ContentInfo contentInfo = contentInfoList.get(first);
                    //设置chapterId,chapterTitle
                    NovelHelper.initChapterId(novelInfo, contentInfo.getChapterId());
                    //图片id和当前id是否相等，相等则清除adapter中map数据
                    if (curChapterId != contentInfo.getChapterId()) {
                        curChapterId = contentInfo.getChapterId();
                        tvInfo.setText(String.format(Locale.CHINA, "%d章/%d章", contentInfo.getChapterId() + 1, novelInfo.getChapterInfoList().size()));
                    }
                    //设置数据
                    tvChapter.setText(novelInfo.getCurChapterTitle());

                    //防止滑动seekBar与onScrolled发生冲突
                    if (!isSmooth) {
                        //改变bottomView visible
                        if (bottomView.getTag() != "GONE") {
                            changeVisibility(bottomView, false);
                        }
                        if (bottomView.getVisibility() != View.GONE) {
                            bottomView.setVisibility(View.GONE);
                        }
                    }

                } else {
                    //Log.i(TAG, "onScrolled: is null");
                }
            }
        };
    }

    @Override
    public void onRefresh() {
        isLoadNext = false;
        super.onRefresh();
    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        readerAdapter = new NReaderAdapter(R.layout.item_reader_novel, novelInfo);
        return readerAdapter;
    }

    @Override
    protected void requestServer() {
        if (contentInfoList == null) {
            presenter.loadContentInfoList(novel);
        } else {
            NovelHelper.initChapterId(novelInfo, contentInfoList.get(contentInfoList.size() - 1).getChapterId());
            if (NovelHelper.canLoad(novelInfo, isLoadNext)) {
                presenter.loadContentInfoList(novel);
            } else if (isLoadNext) {
                onComplete(null);
            } else {
                isLoadNext = true;
                showFailTips("没有上一章");
                setPullLayoutEnabled(false);
            }
        }
        DBUtil.saveNovel(novel, DBUtil.SAVE_CUR);
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        changeVisibility(bottomView, bottomView.getVisibility() != VISIBLE);
    }

    private void changeVisibility(View view, boolean isVisible) {
        if (isVisible) {
            bottomView.setTag("VISIBLE");
            QMUIViewHelper.fadeIn(view, 300, null, true);
        } else {
            bottomView.setTag("GONE");
            QMUIViewHelper.fadeOut(view, 300, null, true);
        }
    }

    @Override
    public boolean onItemLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        return true;
    }

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void loadContentInfoListComplete(ContentInfo contentInfo) {
        List<ContentInfo> list = new ArrayList<>();
        list.add(contentInfo);
        onComplete(list);
        this.contentInfoList = adapter.getData();
        initOtherView();
        isLoadNext = true;
        if (isFresh) {
            isFresh = false;
            recycleView.scrollToPosition(0);
        }
        bottomView.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NovelUtil.first(novel);
        DBUtil.saveNovel(novel, DBUtil.SAVE_CUR);
    }
}
