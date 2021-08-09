package com.qc.common.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qc.common.constant.Constant;
import com.qc.common.constant.TmpData;
import com.qc.common.en.SettingEnum;
import com.qc.common.self.ImageConfig;
import com.qc.common.self.ScrollSpeedLinearLayoutManger;
import com.qc.common.ui.adapter.ReaderListAdapter;
import com.qc.common.ui.presenter.ReaderPresenter;
import com.qc.common.ui.view.ReaderView;
import com.qc.common.util.AnimationUtil;
import com.qc.common.util.DBUtil;
import com.qc.common.util.EntityHelper;
import com.qc.common.util.EntityUtil;
import com.qc.common.util.ImgUtil;
import com.qc.common.util.SettingUtil;
import com.qc.mycomic.R;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import java.util.List;
import java.util.Locale;

import the.one.base.ui.fragment.BaseDataFragment;
import the.one.base.ui.presenter.BasePresenter;
import the.one.base.widge.TheCheckBox;
import top.luqichuang.common.model.ChapterInfo;
import top.luqichuang.common.model.Content;
import top.luqichuang.common.model.Entity;
import top.luqichuang.common.model.EntityInfo;
import top.luqichuang.common.model.Source;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/10 22:17
 * @ver 1.0
 */
public abstract class BaseReaderFragment extends BaseDataFragment<Content> implements ReaderView {

    protected Entity entity;
    protected EntityInfo entityInfo;
    private boolean isLoadNext;
    private ReaderPresenter presenter = new ReaderPresenter();
    private List<Content> contentList;
    private ReaderListAdapter readerListAdapter;
    private int curChapterId;

    protected View topView;
    protected View bottomView;
    protected View darkView;
    protected View rightView;
    protected View settingsView;
    protected ScrollSpeedLinearLayoutManger layoutManager;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        this.entity = (Entity) getArguments().get("entity");
        this.entityInfo = entity.getInfo();
        this.curChapterId = -1;
        this.isLoadNext = true;
        TmpData.toStatus = Constant.READER_TO_CHAPTER;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mTopLayout.setVisibility(View.GONE);
        setFullScreen(TmpData.isFull);
    }

    protected void setFullScreen(boolean isFull) {
        if (isFull) {
            QMUIDisplayHelper.setFullScreen(_mActivity);
        } else {
            QMUIDisplayHelper.cancelFullScreen(_mActivity);
        }
    }

    private void addView() {
        if (darkView == null) {
            darkView = getView(R.layout.fragment_dark);
            mStatusLayout.addView(darkView, 1, getLP());
        }
        if (topView == null) {
            topView = getView(R.layout.fragment_reader_display);
            tvChapter = topView.findViewById(R.id.tvChapter);
            tvProgress = topView.findViewById(R.id.tvProgress);
            mStatusLayout.addView(topView, 2, getLP());
        }
        if (bottomView == null) {
            bottomView = getView(R.layout.fragment_reader_bottom);
            mStatusLayout.addView(bottomView, 3, getLP());
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
        }
        if (rightView == null) {
            rightView = getView(R.layout.fragment_reader_list);
            List<ChapterInfo> items = entityInfo.getChapterInfoList();
            readerListAdapter = new ReaderListAdapter(R.layout.item_reader_list, items);
            readerListAdapter.setPosition(EntityHelper.getPosition(entityInfo));
            RecyclerView listView = rightView.findViewById(R.id.recycleView);
            listView.setLayoutManager(getLayoutManager(TYPE_LIST));
            listView.setAdapter(readerListAdapter);
            listView.scrollToPosition(readerListAdapter.getPosition());
            listView.addItemDecoration(new DividerItemDecoration(_mActivity, DividerItemDecoration.VERTICAL));
            readerListAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    if (readerListAdapter.getPosition() != position) {
                        readerListAdapter.setPosition(position);
                        EntityHelper.initChapterId(entityInfo, EntityHelper.positionToChapterId(entityInfo, position));
                        hideView(rightView);
                        showLoadingPage();
                        isForce = true;
                        isJump = true;
                        onRefresh();
                    } else {
                        hideView(rightView);
                    }
                }
            });
            mStatusLayout.addView(rightView, 4, getLP());
            tvInfo = rightView.findViewById(R.id.tvInfo);
            TextView tvTitle = rightView.findViewById(R.id.tvTitle);
            tvTitle.setText(entity.getTitle());
        }
        if (layoutManager == null) {
            layoutManager = (ScrollSpeedLinearLayoutManger) recycleView.getLayoutManager();
        }
        if (settingsView == null) {
            settingsView = getView(getSettingsViewId());
            mStatusLayout.addView(settingsView, 5, getLP());
        }
        bottomView.setVisibility(View.GONE);
        rightView.setVisibility(View.GONE);
        settingsView.setVisibility(View.GONE);
        hideViews();
        if (TmpData.isLight) {
            darkView.setVisibility(View.GONE);
        }
    }

    protected abstract int getSettingsViewId();

    protected abstract void setSettingsView(View settingsView);

    private void setScrollValue(Content content) {
        //章节变化时需要变化的数据
        if (curChapterId != content.getChapterId()) {
            //初始化curChapterId info
            EntityHelper.initChapterId(entityInfo, content.getChapterId());
            curChapterId = content.getChapterId();

            //设置同一章节中不变的view
            tvChapter.setText(entityInfo.getCurChapterTitle());
            tvChapterName.setText(entityInfo.getCurChapterTitle());
            seekBar.setMax(content.getTotal() - 1);
            tvInfo.setText(String.format(Locale.CHINA, "%d章/%d章", content.getChapterId() + 1, entityInfo.getChapterInfoList().size()));
            readerListAdapter.setPosition(EntityHelper.getPosition(entityInfo));

            //保存数据
            DBUtil.saveInfoData(entityInfo);
        }
        //章节不变时需要实时变化的数据
        tvProgress.setText(EntityHelper.toStringProgress(content));
        tvChapterProgress.setText(EntityHelper.toStringProgress(content));
    }


    private boolean isSmooth = false;
    private boolean isJump = false;
    private boolean isForce = false;
    private boolean ignoreScroll = false;
    private float touchX;
    private float touchY;

    @SuppressLint("ClickableViewAccessibility")
    private void setListener() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (isSmooth) {
                    recycleView.scrollToPosition(first - contentList.get(first).getCur() + progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSmooth = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isSmooth = false;
            }
        });

        llLeft.setOnClickListener(v -> {
            if (EntityHelper.checkChapterId(entityInfo, EntityHelper.getPrevChapterId(entityInfo))) {
                showLoadingPage();
                hideView(bottomView);
                onRefresh();
            } else {
                showFailTips("没有上一章");
            }
        });

        llRight.setOnClickListener(v -> {
            if (EntityHelper.checkChapterId(entityInfo, EntityHelper.getNextChapterId(entityInfo))) {
                showLoadingPage();
                isJump = true;
                hideView(bottomView);
                super.onRefresh();
            } else {
                showFailTips("没有下一章");
            }
        });

        llList.setOnClickListener(v -> {
            hideView(bottomView);
            displayView(rightView);
        });

        LinearLayout llBottomMain = bottomView.findViewById(R.id.llBottomMain);
        llBottomMain.setOnClickListener(v -> {
        });

        TextView tvDark = bottomView.findViewById(R.id.tvDark);
        ImageButton ibDark = bottomView.findViewById(R.id.ibDark);
        if (darkView.getVisibility() == View.VISIBLE) {
            TmpData.isLight = false;
            tvDark.setText("日间");
            AnimationUtil.changeDrawable(ibDark, getDrawablee(R.drawable.ic_baseline_brightness_1_24), false);
        } else {
            TmpData.isLight = true;
            tvDark.setText("夜间");
            AnimationUtil.changeDrawable(ibDark, getDrawablee(R.drawable.ic_baseline_brightness_2_24), false);
        }
        llDark.setOnClickListener(v -> {
            if (darkView.getVisibility() == View.VISIBLE) {
                TmpData.isLight = true;
                hideView(darkView);
                tvDark.setText("夜间");
                AnimationUtil.changeDrawable(ibDark, getDrawablee(R.drawable.ic_baseline_brightness_2_24));
            } else {
                TmpData.isLight = false;
                displayView(darkView);
                tvDark.setText("日间");
                AnimationUtil.changeDrawable(ibDark, getDrawablee(R.drawable.ic_baseline_brightness_1_24));
            }
        });

        TextView tvFav = bottomView.findViewById(R.id.tvFav);
        ImageButton ibFav = bottomView.findViewById(R.id.ibFav);
        if (entity.getStatus() == Constant.STATUS_FAV) {
            tvFav.setText("已收藏");
            ibFav.setImageDrawable(getDrawablee(R.drawable.ic_baseline_favorite_24));
        } else {
            tvFav.setText("未收藏");
            ibFav.setImageDrawable(getDrawablee(R.drawable.ic_baseline_favorite_border_24));
        }
        llFav.setOnClickListener(v -> {
            if (entity.getStatus() == Constant.STATUS_FAV) {
                tvFav.setText("未收藏");
                AnimationUtil.changeDrawable(ibFav, getDrawablee(R.drawable.ic_baseline_favorite_border_24));
            } else {
                tvFav.setText("已收藏");
                AnimationUtil.changeDrawable(ibFav, getDrawablee(R.drawable.ic_baseline_favorite_24));
            }
            EntityUtil.removeEntity(entity);
            entity.setStatus(entity.getStatus() == Constant.STATUS_FAV ? Constant.STATUS_HIS : Constant.STATUS_FAV);
            EntityUtil.first(entity);
            DBUtil.save(entity, DBUtil.SAVE_CUR);
        });

        llSettings.setOnClickListener(v -> {
            hideView(bottomView);
            displayView(settingsView);
        });

        llChapter.setOnClickListener(v -> {
            onBackPressed();
        });

        recycleView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                touchX = e.getX();
                touchY = e.getY();
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        setSettingsView(settingsView);
    }

    protected int first;
    private int bottomIndex;

    @Override
    protected RecyclerView.OnScrollListener getOnScrollListener() {
        return new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (null == _mActivity) return;
                try {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        isGlidePause = false;
                        Glide.with(_mActivity).resumeRequests();
                        TheCheckBox checkBoxAuto = settingsView.findViewById(R.id.checkBoxAuto);
                        if (checkBoxAuto != null) {
                            checkBoxAuto.setCheck(false);
                            _mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                        }
                    } else if (!isGlidePause) {
                        isGlidePause = true;
                        Glide.with(_mActivity).pauseRequests();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (manager != null) {
                    int count = manager.getChildCount();    //得到显示屏幕内的list数量
//                        int total = manager.getItemCount();    //得到list的总数量
                    first = manager.findFirstVisibleItemPosition();//得到显示屏内的第一个list的位置数position
                    Content content = contentList.get(first);
                    setScrollValue(content);

                    //防止滑动seekBar与onScrolled发生冲突
                    if (!isSmooth && !ignoreScroll) {
                        //设置seekBar position
                        seekBar.setProgress(content.getCur());
                        //改变views visible
                        hideViews();
                    }

                    //预加载
                    if (content.getUrl() != null) {
                        int bottom = first + count;
                        int preloadNum = (int) SettingUtil.getSettingKey(SettingEnum.PRELOAD_NUM);
                        int min = Math.min(bottom + preloadNum, contentList.size());
                        if (bottomIndex < min) {
                            for (int i = bottomIndex; i < min; i++) {
                                ImageConfig config = new ImageConfig();
                                config.setUrl(contentList.get(i).getUrl());
                                Source source = EntityHelper.commonSource(entity);
                                config.setHeaders(source.getImageHeaders());
                                ImgUtil.preloadReaderImg(getContext(), config);
                            }
                            bottomIndex = min;
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
        hideViews();
        isJump = true;
        isLoadNext = false;
        super.onRefresh();
    }

    @Override
    protected void requestServer() {
        if (contentList == null || isForce) {
            isForce = false;
            presenter.loadContentInfoList(entity);
        } else {
            if (isLoadNext) {
                EntityHelper.initChapterId(entityInfo, contentList.get(contentList.size() - 1).getChapterId());
            } else {
                EntityHelper.initChapterId(entityInfo, curChapterId);
            }
            if (EntityHelper.canLoad(entityInfo, isLoadNext)) {
                presenter.loadContentInfoList(entity);
            } else if (isLoadNext) {
                onComplete(null);
            } else {
                isLoadNext = true;
                showFailTips("没有上一章");
                setPullLayoutEnabled(false);
            }
        }
    }

    protected boolean checkMenuClick(int position) {
        int height = QMUIDisplayHelper.getScreenHeight(getContext());
        int length = height / 3;
        if (touchY < length) {
            recycleView.scrollBy(0, -(height / 2));
        } else if (touchY > length * 2) {
            recycleView.scrollBy(0, (height / 2));
        } else {
            return true;
        }
        return false;
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        if (!checkMenuClick(position)) {
            return;
        }
        if (!hideViews()) {
            ignoreScroll = true;
            setFullScreen(false);
            AnimationUtil.changeViewVisibility(bottomView, true, new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    ignoreScroll = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            return;
        }
        if (TmpData.isFull) {
            setFullScreen(true);
        }
    }

    protected boolean hideView(View view) {
        return AnimationUtil.changeViewVisibility(view, false);
    }

    protected boolean displayView(View view) {
        return AnimationUtil.changeViewVisibility(view, true);
    }

    protected boolean hideViews() {
        boolean success = false;
        View[] views = new View[]{bottomView, rightView, settingsView};
        for (View view : views) {
            boolean result = hideView(view);
            if (result && !success) {
                success = true;
            }
        }
        setFullScreen(TmpData.isFull);
        return success;
    }

    protected ViewGroup.LayoutParams getLP() {
        int width = QMUIDisplayHelper.getScreenWidth(_mActivity);
        int height = QMUIDisplayHelper.getScreenHeight(_mActivity) + QMUIDisplayHelper.getStatusBarHeight(_mActivity) + QMUIDisplayHelper.getActionBarHeight(_mActivity);
        return new ViewGroup.LayoutParams(width, height);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager(int type) {
        Object layoutManager;
        switch (type) {
            case 2:
                layoutManager = new GridLayoutManager(this.getActivity(), this.setColumn());
                break;
            case 3:
                layoutManager = new StaggeredGridLayoutManager(this.setColumn(), 1);
                ((StaggeredGridLayoutManager) layoutManager).setGapStrategy(0);
                break;
            default:
                layoutManager = new ScrollSpeedLinearLayoutManger(this.getActivity());
        }
        return (RecyclerView.LayoutManager) layoutManager;
    }

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void loadReadContentComplete(List<Content> contentList, String errorMsg) {
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
            onComplete(contentList);
            this.contentList = adapter.getData();
            addView();
            if (firstLoad) {
                firstLoad = false;
                isJump = true;
                setListener();
                firstLoadView();
            }
            if (isJump) {
                isJump = false;
                if (!this.contentList.isEmpty()) {
                    setScrollValue(this.contentList.get(0));
                }
                recycleView.scrollToPosition(0);
            }
            isLoadNext = true;
        }
    }

    protected abstract void firstLoadView();

    @Override
    public void onDestroy() {
        setFullScreen(false);
        super.onDestroy();
    }
}