package com.qc.mycomic.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.qc.common.en.SettingEnum;
import com.qc.common.self.ImageConfig;
import com.qc.common.ui.adapter.ReaderListAdapter;
import com.qc.common.util.AnimationUtil;
import com.qc.common.util.ImgUtil;
import com.qc.common.util.SettingUtil;
import com.qc.mycomic.R;
import com.qc.mycomic.ui.adapter.ReaderAdapter;
import com.qc.mycomic.ui.presenter.ReaderPresenter;
import com.qc.mycomic.ui.view.ReaderView;
import com.qc.mycomic.util.ComicHelper;
import com.qc.mycomic.util.ComicUtil;
import com.qc.mycomic.util.DBUtil;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import java.util.List;
import java.util.Locale;

import the.one.base.ui.fragment.BaseDataFragment;
import the.one.base.ui.presenter.BasePresenter;
import the.one.base.widge.TheCheckBox;
import top.luqichuang.common.model.ChapterInfo;
import top.luqichuang.mycomic.model.Comic;
import top.luqichuang.mycomic.model.ComicInfo;
import top.luqichuang.mycomic.model.ImageInfo;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;

/**
 * @author LuQiChuang
 * @desc 漫画阅读界面
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class ReaderFragment extends BaseDataFragment<ImageInfo> implements ReaderView {

    private Comic comic;
    private ComicInfo comicInfo;
    private boolean isLoadNext;
    private ReaderPresenter presenter = new ReaderPresenter();
    private List<ImageInfo> imageInfoList;
    private ReaderAdapter readerAdapter;
    private ReaderListAdapter readerListAdapter;
    private int curChapterId;

    private View topView;
    private View bottomView;
    private View darkView;
    private View rightView;
    private View settingsView;
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
    private LinearLayout llFull;
    private SeekBar seekBar;
    private boolean firstLoad = true;

    public static ReaderFragment getInstance(Comic comic) {
        ReaderFragment fragment = new ReaderFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("comic", comic);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        this.comic = (Comic) getArguments().get("comic");
        this.comicInfo = comic.getComicInfo();
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
            List<ChapterInfo> items = comicInfo.getChapterInfoList();
            readerListAdapter = new ReaderListAdapter(R.layout.item_reader_list, items);
            readerListAdapter.setPosition(ComicHelper.getPosition(comicInfo));
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
                        ComicHelper.initChapterId(comicInfo, ComicHelper.positionToChapterId(comicInfo, position));
                        changeVisibility(rightView, false);
                        showLoadingPage();
                        isForce = true;
                        isJump = true;
                        onRefresh();
                    } else {
                        changeVisibility(rightView, false);
                    }
                }
            });
            mStatusLayout.addView(rightView, 4, getLP());
            tvInfo = rightView.findViewById(R.id.tvInfo);
            TextView tvTitle = rightView.findViewById(R.id.tvTitle);
            tvTitle.setText(comic.getTitle());
        }
        if (settingsView == null) {
            settingsView = getView(R.layout.fragment_reader_settings);
            mStatusLayout.addView(settingsView, 5, getLP());
            llFull = settingsView.findViewById(R.id.llFull);
        }
        bottomView.setVisibility(View.GONE);
        rightView.setVisibility(View.GONE);
        settingsView.setVisibility(View.GONE);
        if (TmpData.isLight) {
            darkView.setVisibility(View.GONE);
        }
    }

    private void setScrollValue(ImageInfo imageInfo) {
        //章节变化时需要变化的数据
        if (curChapterId != imageInfo.getChapterId()) {
            //初始化curChapterId info
            ComicHelper.initChapterId(comicInfo, imageInfo.getChapterId());
            curChapterId = imageInfo.getChapterId();

            //设置同一章节中不变的view
            tvChapter.setText(comicInfo.getCurChapterTitle());
            tvChapterName.setText(comicInfo.getCurChapterTitle());
            seekBar.setMax(imageInfo.getTotal() - 1);
            tvInfo.setText(String.format(Locale.CHINA, "%d章/%d章", imageInfo.getChapterId() + 1, comicInfo.getChapterInfoList().size()));
            readerListAdapter.setPosition(ComicHelper.getPosition(comicInfo));

            //保存数据
            DBUtil.saveComicInfo(comicInfo);
        }
        //章节不变时需要实时变化的数据
        tvProgress.setText(ComicHelper.toStringProgress(imageInfo));
        tvChapterProgress.setText(ComicHelper.toStringProgress(imageInfo));
    }

    private boolean isSmooth = false;
    private boolean isJump = false;
    private boolean isForce = false;

    private void setListener() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (isSmooth) {
                    //Log.i(TAG, "onProgressChanged: value = " + (first - imageInfoList.get(first).getCur() + progress));
//                    recycleView.smoothScrollToPosition(first - imageInfoList.get(first).getCur() + progress);
                    recycleView.scrollToPosition(first - imageInfoList.get(first).getCur() + progress);
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
            if (ComicHelper.checkChapterId(comicInfo, ComicHelper.getPrevChapterId(comicInfo))) {
                changeVisibility(bottomView, false);
                onRefresh();
            } else {
                showFailTips("没有上一章");
            }
        });

        llRight.setOnClickListener(v -> {
            if (ComicHelper.checkChapterId(comicInfo, ComicHelper.getNextChapterId(comicInfo))) {
                isJump = true;
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
                changeVisibility(darkView, false, false);
                tvDark.setText("夜间");
                AnimationUtil.changeDrawable(ibDark, getDrawablee(R.drawable.ic_baseline_brightness_2_24));
            } else {
                TmpData.isLight = false;
                changeVisibility(darkView, true, false);
                tvDark.setText("日间");
                AnimationUtil.changeDrawable(ibDark, getDrawablee(R.drawable.ic_baseline_brightness_1_24));
            }
        });

        TextView tvFav = bottomView.findViewById(R.id.tvFav);
        ImageButton ibFav = bottomView.findViewById(R.id.ibFav);
        if (comic.getStatus() == Constant.STATUS_FAV) {
            tvFav.setText("已收藏");
            ibFav.setImageDrawable(getDrawablee(R.drawable.ic_baseline_favorite_24));
        } else {
            tvFav.setText("未收藏");
            ibFav.setImageDrawable(getDrawablee(R.drawable.ic_baseline_favorite_border_24));
        }
        llFav.setOnClickListener(v -> {
            if (comic.getStatus() == Constant.STATUS_FAV) {
                tvFav.setText("未收藏");
                AnimationUtil.changeDrawable(ibFav, getDrawablee(R.drawable.ic_baseline_favorite_border_24));
            } else {
                tvFav.setText("已收藏");
                AnimationUtil.changeDrawable(ibFav, getDrawablee(R.drawable.ic_baseline_favorite_24));
            }
            ComicUtil.removeComic(comic);
            comic.setStatus(comic.getStatus() == Constant.STATUS_FAV ? Constant.STATUS_HIS : Constant.STATUS_FAV);
            ComicUtil.first(comic);
            DBUtil.saveComic(comic, DBUtil.SAVE_CUR);
        });

        llSettings.setOnClickListener(v -> {
            changeVisibility(bottomView, false, false);
            changeVisibility(settingsView, true, false);
        });

        llChapter.setOnClickListener(v -> {
            onBackPressed();
        });

        TheCheckBox checkBox = llFull.findViewById(R.id.checkBox);
        checkBox.setIsCheckDrawable(R.drawable.ic_baseline_check_circle_24);
        checkBox.setCheck(TmpData.isFull);
        checkBox.setOnClickListener(v -> {
            TmpData.isFull = !checkBox.isCheck();
            SettingUtil.putSetting(SettingEnum.IS_FULL_SCREEN, TmpData.isFull);
            checkBox.setCheck(TmpData.isFull);
        });
        llFull.setOnClickListener(v -> {
            TmpData.isFull = !checkBox.isCheck();
            SettingUtil.putSetting(SettingEnum.IS_FULL_SCREEN, TmpData.isFull);
            checkBox.setCheck(TmpData.isFull);
        });
    }

    private int first;
    private int bottomIndex;

    @Override
    protected RecyclerView.OnScrollListener getOnScrollListener() {
        return new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (null == _mActivity) return;
                try {
                    if (newState == SCROLL_STATE_IDLE) {
                        isGlidePause = false;
                        Glide.with(_mActivity).resumeRequests();
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
                    ImageInfo imageInfo = imageInfoList.get(first);
                    setScrollValue(imageInfo);

                    //防止滑动seekBar与onScrolled发生冲突
                    if (!isSmooth) {
                        //设置seekBar position
                        seekBar.setProgress(imageInfo.getCur());
                        //改变views visible
                        hideView();
                    }
                    //预加载
                    int bottom = first + count;
                    int preloadNum = (int) SettingUtil.getSettingKey(SettingEnum.PRELOAD_NUM);
                    int min = Math.min(bottom + preloadNum, imageInfoList.size());
                    if (bottomIndex < min) {
                        for (int i = bottomIndex; i < min; i++) {
                            ImgUtil.preloadReaderImg(getContext(), imageInfoList.get(i));
                        }
                        bottomIndex = min;
                    }
                } else {
                    //Log.i(TAG, "onScrolled: is null");
                }
            }
        };
    }

    @Override
    public void onRefresh() {
        hideView();
        isJump = true;
        isLoadNext = false;
        super.onRefresh();
    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        readerAdapter = new ReaderAdapter(R.layout.item_reader);
        return readerAdapter;
    }

    @Override
    protected void requestServer() {
        if (imageInfoList == null || isForce) {
            isForce = false;
            presenter.loadImageInfoList(comic);
        } else {
            if (isLoadNext) {
                ComicHelper.initChapterId(comicInfo, imageInfoList.get(imageInfoList.size() - 1).getChapterId());
            } else {
                ComicHelper.initChapterId(comicInfo, curChapterId);
            }
            //Log.i(TAG, "requestServer: " + comicInfo.getCurChapterId());
            if (ComicHelper.canLoad(comicInfo, isLoadNext)) {
                presenter.loadImageInfoList(comic);
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
        if (!TmpData.isFull) {
            setFullScreen(false);
        }
        if (!changeVisibility(settingsView, false)) {
            if (!changeVisibility(rightView, false)) {
                changeVisibility(bottomView, bottomView.getVisibility() != View.VISIBLE);
            }
        }
    }

    private void hideView() {
        View[] views = new View[]{bottomView, rightView, settingsView};
        for (View view : views) {
            AnimationUtil.changeVisibility(view, false);
        }
        setFullScreen(TmpData.isFull);
    }

    private boolean changeVisibility(View view, boolean isVisible) {
        return changeVisibility(view, isVisible, TmpData.isFull);
    }

    private boolean changeVisibility(View view, boolean isVisible, boolean isChangeStatusBar) {
        if (isChangeStatusBar) {
            setFullScreen(!isVisible);
        }
        return AnimationUtil.changeVisibility(view, isVisible);
    }

    private ViewGroup.LayoutParams getLP() {
        int width = QMUIDisplayHelper.getScreenWidth(_mActivity);
        int height = QMUIDisplayHelper.getScreenHeight(_mActivity) + QMUIDisplayHelper.getStatusBarHeight(_mActivity) + QMUIDisplayHelper.getActionBarHeight(_mActivity);
        return new ViewGroup.LayoutParams(width, height);
    }

    @Override
    public boolean onItemLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        ImageInfo imageInfo = (ImageInfo) adapter.getData().get(position);
        ReaderAdapter readerAdapter = (ReaderAdapter) adapter;
        //Log.i(TAG, "onItemLongClick: " + imageInfo.toStringProgress());
        if (ImgUtil.getLoadStatus(imageInfo) == ImgUtil.LOAD_FAIL) {
            RelativeLayout layout = view.findViewById(R.id.imageRelativeLayout);
            ImageConfig config = ImgUtil.getReaderConfig(getContext(), imageInfo.getUrl(), layout);
            config.setForce(true);
            ImgUtil.loadImage(getContext(), config);
        } else if (ImgUtil.getLoadStatus(imageInfo) == ImgUtil.LOAD_SUCCESS) {
            startFragment(ReaderDetailFragment.getInstance(imageInfo));
        }
        return true;
    }

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void loadImageInfoListComplete(List<ImageInfo> imageInfoList, String errorMsg) {
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
            onComplete(imageInfoList);
            this.imageInfoList = adapter.getData();
            addView();
            if (firstLoad) {
                firstLoad = false;
                isJump = true;
                setListener();
            }
            if (isJump) {
                isJump = false;
                if (!this.imageInfoList.isEmpty()) {
                    setScrollValue(this.imageInfoList.get(0));
                }
                recycleView.scrollToPosition(0);
            }
            isLoadNext = true;
        }
    }

    @Override
    public void onDestroy() {
        setFullScreen(false);
        super.onDestroy();
    }
}
