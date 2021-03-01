package com.qc.mynovel.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qc.common.constant.Constant;
import com.qc.common.constant.TmpData;
import com.qc.common.ui.adapter.ReaderListAdapter;
import com.qc.common.util.AnimationUtil;
import com.qc.mycomic.R;
import com.qc.mynovel.ui.adapter.NReaderAdapter;
import com.qc.mynovel.ui.presenter.NReaderPresenter;
import com.qc.mynovel.ui.view.NReaderView;
import com.qc.mynovel.util.DBUtil;
import com.qc.mynovel.util.NovelHelper;
import com.qc.mynovel.util.NovelUtil;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import the.one.base.ui.fragment.BaseDataFragment;
import the.one.base.ui.presenter.BasePresenter;
import top.luqichuang.common.model.ChapterInfo;
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
    private ReaderListAdapter readerListAdapter;
    private int curChapterId;

    private View topView;
    private View bottomView;
    private View darkView;
    private View rightView;
    private TextView tvChapter;
    private TextView tvProgress;
    private TextView tvInfo;
    private TextView tvChapterName;
    private TextView tvChapterProgress;
    private LinearLayout llLeft;
    private LinearLayout llRight;
    private LinearLayout llList;
    private LinearLayout llDark;
    private LinearLayout llFav;
    private LinearLayout llSettings;
    private LinearLayout llChapter;
    private SeekBar seekBar;
    private boolean firstLoad = true;

    public static NReaderFragment getInstance(Novel novel) {
        NReaderFragment fragment = new NReaderFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("novel", novel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        this.novel = (Novel) getArguments().get("novel");
        this.novelInfo = novel.getNovelInfo();
        this.curChapterId = novelInfo.getCurChapterId();
        this.isLoadNext = true;
        TmpData.toStatus = Constant.READER_TO_CHAPTER;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mTopLayout.setVisibility(View.GONE);
        setStatusBarVisible(false);
    }

    protected void setStatusBarVisible(boolean show) {
        if (show) {
            QMUIDisplayHelper.cancelFullScreen(_mActivity);
        } else {
            QMUIDisplayHelper.setFullScreen(_mActivity);
        }
    }

    private void addView() {
        if (darkView == null) {
            darkView = getView(R.layout.fragment_dark);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(QMUIDisplayHelper.getScreenWidth(_mActivity), QMUIDisplayHelper.getScreenHeight(_mActivity));
            mStatusLayout.addView(darkView, 1, layoutParams);
        }
        if (topView == null) {
            topView = getView(R.layout.top_reader);
            tvChapter = topView.findViewById(R.id.tvChapter);
            tvProgress = topView.findViewById(R.id.tvProgress);
            tvProgress.setVisibility(View.GONE);
            int height = QMUIDisplayHelper.getScreenHeight(_mActivity) + QMUIDisplayHelper.getStatusBarHeight(_mActivity);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(QMUIDisplayHelper.getScreenWidth(_mActivity), height);
            mStatusLayout.addView(topView, 2, layoutParams);
        }
        if (bottomView == null) {
            bottomView = getView(R.layout.bottom_reader);
            int height = QMUIDisplayHelper.getScreenHeight(_mActivity) + QMUIDisplayHelper.getStatusBarHeight(_mActivity);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(QMUIDisplayHelper.getScreenWidth(_mActivity), height);
            mStatusLayout.addView(bottomView, 3, layoutParams);
            llLeft = bottomView.findViewById(R.id.llLeft);
            llRight = bottomView.findViewById(R.id.llRight);
            seekBar = bottomView.findViewById(R.id.seekBar);
            llList = bottomView.findViewById(R.id.llList);
            llDark = bottomView.findViewById(R.id.llDark);
            llFav = bottomView.findViewById(R.id.llFav);
            llChapter = bottomView.findViewById(R.id.llChapter);
            llSettings = bottomView.findViewById(R.id.llSettings);
            tvChapterName = bottomView.findViewById(R.id.tvChapterName);
            tvChapterProgress = bottomView.findViewById(R.id.tvChapterProgress);
            seekBar.setVisibility(View.GONE);
            tvProgress.setVisibility(View.GONE);
            tvChapterProgress.setVisibility(View.GONE);
            TextView tvTitleCenter = bottomView.findViewById(R.id.tvTitleCenter);
            tvTitleCenter.setText(novel.getTitle());
            tvTitleCenter.setVisibility(VISIBLE);
        }
        if (rightView == null) {
            rightView = getView(R.layout.fragment_reader_list);
            List<ChapterInfo> items = novelInfo.getChapterInfoList();
            readerListAdapter = new ReaderListAdapter(R.layout.item_reader_list, items);
            readerListAdapter.setPosition(NovelHelper.getPosition(novelInfo));
            RecyclerView listView = rightView.findViewById(R.id.recycleView);
            listView.setLayoutManager(getLayoutManager(TYPE_LIST));
            listView.setAdapter(readerListAdapter);
            listView.addItemDecoration(new DividerItemDecoration(_mActivity, DividerItemDecoration.VERTICAL));
            readerListAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    if (readerListAdapter.getPosition() != position) {
                        readerListAdapter.setPosition(position);
                        NovelHelper.initChapterId(novelInfo, NovelHelper.positionToChapterId(novelInfo, position));
                        changeVisibility(rightView, false);
                        showLoadingPage();
                        isForce = true;
                        isFresh = true;
                        onRefresh();
                    } else {
                        changeVisibility(rightView, false);
                    }
                }
            });
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(QMUIDisplayHelper.getScreenWidth(_mActivity), QMUIDisplayHelper.getScreenHeight(_mActivity));
            mStatusLayout.addView(rightView, 4, layoutParams);
            tvInfo = rightView.findViewById(R.id.tvInfo);
            TextView tvTitle = rightView.findViewById(R.id.tvTitle);
            tvTitle.setText(novel.getTitle());
        }
        bottomView.setVisibility(View.GONE);
        rightView.setVisibility(View.GONE);
        if (TmpData.isLight) {
            darkView.setVisibility(View.GONE);
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
            tvChapterName.setText(novelInfo.getCurChapterTitle());
            tvInfo.setText(String.format(Locale.CHINA, "%d章/%d章", contentInfo.getChapterId() + 1, novelInfo.getChapterInfoList().size()));
            readerListAdapter.setPosition(NovelHelper.getPosition(novelInfo));
            DBUtil.saveNovelInfo(novelInfo);
        }
    }

    private boolean isSmooth = false;
    private boolean isFresh = false;
    private boolean isForce = false;

    private void setListener() {
        llLeft.setOnClickListener(v -> {
            if (NovelHelper.checkChapterId(novelInfo, NovelHelper.getPrevChapterId(novelInfo))) {
                isFresh = true;
                changeVisibility(bottomView, false);
                onRefresh();
            } else {
                showFailTips("没有上一章");
            }
        });

        llRight.setOnClickListener(v -> {
            if (NovelHelper.checkChapterId(novelInfo, NovelHelper.getNextChapterId(novelInfo))) {
                isFresh = true;
                changeVisibility(bottomView, false);
                super.onRefresh();
            } else {
                showFailTips("没有下一章");
            }
        });

        llList.setOnClickListener(v -> {
            changeVisibility(bottomView, false);
            changeVisibility(rightView, true);
        });

        TextView tvDark = bottomView.findViewById(R.id.tvDark);
        ImageButton ibDark = bottomView.findViewById(R.id.ibDark);
        if (darkView.getVisibility() == VISIBLE) {
            TmpData.isLight = false;
            tvDark.setText("日间");
            AnimationUtil.changeDrawable(ibDark, getDrawablee(R.drawable.ic_baseline_brightness_1_24), false);
        } else {
            TmpData.isLight = true;
            tvDark.setText("夜间");
            AnimationUtil.changeDrawable(ibDark, getDrawablee(R.drawable.ic_baseline_brightness_2_24), false);
        }
        llDark.setOnClickListener(v -> {
            if (darkView.getVisibility() == VISIBLE) {
                changeVisibility(darkView, false, false);
                tvDark.setText("夜间");
                AnimationUtil.changeDrawable(ibDark, getDrawablee(R.drawable.ic_baseline_brightness_2_24));
            } else {
                changeVisibility(darkView, true, false);
                tvDark.setText("日间");
                AnimationUtil.changeDrawable(ibDark, getDrawablee(R.drawable.ic_baseline_brightness_1_24));
            }
        });

        TextView tvFav = bottomView.findViewById(R.id.tvFav);
        ImageButton ibFav = bottomView.findViewById(R.id.ibFav);
        if (novel.getStatus() == Constant.STATUS_FAV) {
            tvFav.setText("已收藏");
            ibFav.setImageDrawable(getDrawablee(R.drawable.ic_baseline_favorite_24));
        } else {
            tvFav.setText("未收藏");
            ibFav.setImageDrawable(getDrawablee(R.drawable.ic_baseline_favorite_border_24));
        }
        llFav.setOnClickListener(v -> {
            if (novel.getStatus() == Constant.STATUS_FAV) {
                tvFav.setText("未收藏");
                AnimationUtil.changeDrawable(ibFav, getDrawablee(R.drawable.ic_baseline_favorite_border_24));
            } else {
                tvFav.setText("已收藏");
                AnimationUtil.changeDrawable(ibFav, getDrawablee(R.drawable.ic_baseline_favorite_24));
            }
            NovelUtil.removeNovel(novel);
            novel.setStatus(novel.getStatus() == Constant.STATUS_FAV ? Constant.STATUS_HIS : Constant.STATUS_FAV);
            NovelUtil.first(novel);
            DBUtil.saveNovel(novel, DBUtil.SAVE_CUR);
        });

        llSettings.setOnClickListener(v -> {
            showToast("待完善");
        });

        llChapter.setOnClickListener(v -> {
            onBackPressed();
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
                        DBUtil.saveNovelInfo(novelInfo);
                    }
                    //设置数据
                    tvChapter.setText(novelInfo.getCurChapterTitle());
                    tvChapterName.setText(novelInfo.getCurChapterTitle());

                    //防止滑动seekBar与onScrolled发生冲突
                    if (!isSmooth) {
                        //改变bottomView visible
                        changeVisibility(bottomView, false);
                        changeVisibility(rightView, false);
                    }

                } else {
                    //Log.i(TAG, "onScrolled: is null");
                }
            }
        };
    }

    @Override
    public void onRefresh() {
        changeVisibility(bottomView, false);
        changeVisibility(rightView, false);
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
        if (contentInfoList == null || isForce) {
            isForce = false;
            presenter.loadContentInfoList(novel);
        } else {
            if (isLoadNext) {
                NovelHelper.initChapterId(novelInfo, contentInfoList.get(contentInfoList.size() - 1).getChapterId());
            }
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
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        if (!changeVisibility(rightView, false)) {
            changeVisibility(bottomView, bottomView.getVisibility() != VISIBLE);
        }
    }

    private boolean changeVisibility(View view, boolean isVisible) {
        return changeVisibility(view, isVisible, true);
    }

    private boolean changeVisibility(View view, boolean isVisible, boolean isChangeStatusBar) {
        if (isChangeStatusBar) {
            setStatusBarVisible(isVisible);
            setLP(bottomView, topView, darkView);
        }
        return AnimationUtil.changeVisibility(view, isVisible);
    }

    private void setLP(View... views) {
        for (View view : views) {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            lp.width = QMUIDisplayHelper.getScreenWidth(_mActivity);
            lp.height = QMUIDisplayHelper.getScreenHeight(_mActivity) + QMUIDisplayHelper.getStatusBarHeight(_mActivity);
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
    public void loadContentInfoListComplete(ContentInfo contentInfo, String errorMsg) {
        if (errorMsg != null) {
            if (adapter.getData().isEmpty()) {
                showErrorPage(errorMsg, v -> {
                    showLoadingPage();
                    requestServer();
                });
            } else {
                adapter.getLoadMoreModule().loadMoreFail();
            }
        } else {
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
        }
    }

    @Override
    public void onDestroy() {
        setStatusBarVisible(true);
        super.onDestroy();
    }
}
