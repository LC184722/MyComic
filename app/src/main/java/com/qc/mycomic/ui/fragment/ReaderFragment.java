package com.qc.mycomic.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qc.mycomic.R;
import com.qc.common.constant.Constant;
import com.qc.common.constant.TmpData;
import com.qc.common.en.SettingEnum;
import com.qc.mycomic.ui.adapter.ReaderAdapter;
import com.qc.mycomic.ui.presenter.ReaderPresenter;
import com.qc.mycomic.ui.view.ReaderView;
import com.qc.mycomic.util.ComicUtil;
import com.qc.mycomic.util.DBUtil;
import com.qc.common.util.ImgUtil;
import com.qc.common.util.SettingUtil;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIViewHelper;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import the.one.base.ui.fragment.BaseDataFragment;
import the.one.base.ui.presenter.BasePresenter;
import top.luqichuang.mycomic.model.Comic;
import top.luqichuang.mycomic.model.ComicInfo;
import top.luqichuang.mycomic.model.ImageInfo;
import com.qc.mycomic.util.ComicHelper;

import static android.view.View.VISIBLE;
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
        this.curChapterId = comicInfo.getCurChapterId();
        this.isLoadNext = true;
        TmpData.toStatus = Constant.READER_TO_CHAPTER;
        super.onCreate(savedInstanceState);
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
        if (!imageInfoList.isEmpty()) {
            if (imageInfoList.size() <= first) {
                first = 0;
            }
            ImageInfo imageInfo = imageInfoList.get(first);
            tvChapter.setText(comicInfo.getCurChapterTitle());
            tvProgress.setText(ComicHelper.toStringProgress(imageInfo));
            seekBar.setMax(imageInfo.getTotal() - 1);
            seekBar.setProgress(imageInfo.getCur());
            tvInfo.setText(String.format(Locale.CHINA, "%d章/%d章", imageInfo.getChapterId() + 1, comicInfo.getChapterInfoList().size()));
        }
    }

    private boolean isSmooth = false;
    private boolean isFresh = false;

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
                isFresh = true;
                changeVisibility(bottomView, false);
                onRefresh();
            } else {
                showFailTips("没有上一章");
            }
        });

        llRight.setOnClickListener(v -> {
            if (ComicHelper.checkChapterId(comicInfo, ComicHelper.getNextChapterId(comicInfo))) {
                isFresh = true;
                changeVisibility(bottomView, false);
                super.onRefresh();
            } else {
                showFailTips("没有下一章");
            }
        });
    }

    private int first;
    private int total;
    private int bottomIndex;

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
                    int count = manager.getChildCount();    //得到显示屏幕内的list数量
//                        int total = manager.getItemCount();    //得到list的总数量
                    first = manager.findFirstVisibleItemPosition();//得到显示屏内的第一个list的位置数position
                    ImageInfo imageInfo = imageInfoList.get(first);
                    //设置chapterId,chapterTitle
                    ComicHelper.initChapterId(comicInfo, imageInfo.getChapterId());
                    //图片id和当前id是否相等，相等则清除adapter中map数据
                    if (curChapterId != imageInfo.getChapterId()) {
                        curChapterId = imageInfo.getChapterId();
                        tvInfo.setText(String.format(Locale.CHINA, "%d章/%d章", imageInfo.getChapterId() + 1, comicInfo.getChapterInfoList().size()));
                        DBUtil.saveComicInfo(comicInfo);
                    }
                    //设置数据
                    tvChapter.setText(comicInfo.getCurChapterTitle());
                    tvProgress.setText(ComicHelper.toStringProgress(imageInfo));

                    //防止滑动seekBar与onScrolled发生冲突
                    if (!isSmooth) {
                        //设置seekBar position
                        seekBar.setProgress(imageInfo.getCur());
                        //改变bottomView visible
                        if (bottomView.getTag() != "GONE") {
                            changeVisibility(bottomView, false);
                        }
                        if (bottomView.getVisibility() != View.GONE) {
                            bottomView.setVisibility(View.GONE);
                        }
                    }
                    //设置seekBar最大值
                    if (total != imageInfo.getTotal() - 1) {
                        total = imageInfo.getTotal() - 1;
                        seekBar.setMax(total);
                    }
                    //预加载
                    int bottom = first + count;
//                    String data = SettingFactory.getInstance().getSetting(SettingFactory.SETTING_PRELOAD_NUM).getData();
//                    int preloadNum = Integer.parseInt(data);
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
        bottomView.setVisibility(View.GONE);
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
        if (imageInfoList == null) {
            presenter.loadImageInfoList(comic);
        } else {
            if (isLoadNext) {
                ComicHelper.initChapterId(comicInfo, imageInfoList.get(imageInfoList.size() - 1).getChapterId());
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
        ImageInfo imageInfo = (ImageInfo) adapter.getData().get(position);
        ReaderAdapter readerAdapter = (ReaderAdapter) adapter;
        //Log.i(TAG, "onItemLongClick: " + imageInfo.toStringProgress());
        if (ImgUtil.getLoadStatus(imageInfo) == ImgUtil.LOAD_FAIL) {
            RelativeLayout layout = view.findViewById(R.id.imageRelativeLayout);
            ImgUtil.loadImage(getContext(), imageInfo.getUrl(), layout);
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
    public void loadImageInfoListComplete(List<ImageInfo> imageInfoList) {
        onComplete(imageInfoList);
        this.imageInfoList = adapter.getData();
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
        ImgUtil.clearMap();
        comic.setDate(new Date());
        ComicUtil.first(comic);
        DBUtil.saveComic(comic, DBUtil.SAVE_CUR);
    }
}
