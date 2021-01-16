package com.qc.mycomic.ui.fragment;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qc.mycomic.R;
import com.qc.mycomic.en.Codes;
import com.qc.mycomic.en.SettingEnum;
import com.qc.mycomic.model.Comic;
import com.qc.mycomic.model.ComicInfo;
import com.qc.mycomic.model.ImageInfo;
import com.qc.mycomic.ui.adapter.ReaderAdapter;
import com.qc.mycomic.ui.presenter.ReaderPresenter;
import com.qc.mycomic.ui.view.ReaderView;
import com.qc.mycomic.util.ComicUtil;
import com.qc.mycomic.util.DBUtil;
import com.qc.mycomic.util.ImgUtil;
import com.qc.mycomic.util.RestartUtil;
import com.qc.mycomic.util.SettingUtil;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIViewHelper;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import the.one.base.ui.fragment.BaseDataFragment;
import the.one.base.ui.presenter.BasePresenter;

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

    public ReaderFragment() {
        RestartUtil.restart(_mActivity);
    }

    public ReaderFragment(Comic comic) {
        this.comic = comic;
        this.comicInfo = comic.getComicInfo();
        this.curChapterId = comicInfo.getCurChapterId();
        this.isLoadNext = true;
        Codes.toStatus = Codes.READER_TO_CHAPTER;
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
            tvProgress.setText(imageInfo.toStringProgress());
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
            if (comicInfo.checkChapterId(comicInfo.getPrevChapterId())) {
                isFresh = true;
                onRefresh();
            } else {
                showFailTips("没有上一章");
            }
        });

        llRight.setOnClickListener(v -> {
            if (comicInfo.checkChapterId(comicInfo.getNextChapterId())) {
                isFresh = true;
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
                    comicInfo.initChapterId(imageInfo.getChapterId());
                    //图片id和当前id是否相等，相等则清除adapter中map数据
                    if (curChapterId != imageInfo.getChapterId()) {
                        curChapterId = imageInfo.getChapterId();
                        tvInfo.setText(String.format(Locale.CHINA, "%d/%d", imageInfo.getChapterId() + 1, comicInfo.getChapterInfoList().size()));
                        readerAdapter.clearMap();
                    }
                    //设置数据
                    tvChapter.setText(comicInfo.getCurChapterTitle());
                    tvProgress.setText(imageInfo.toStringProgress());

                    //防止滑动seekBar与onScrolled发生冲突
                    if (!isSmooth) {
                        //设置seekBar position
                        seekBar.setProgress(imageInfo.getCur());
                        //改变bottomView visible
                        if (bottomView.getTag() != "GONE") {
                            changeVisibility(bottomView, false);
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
            comicInfo.initChapterId(imageInfoList.get(imageInfoList.size() - 1).getChapterId());
            //Log.i(TAG, "requestServer: " + comicInfo.getCurChapterId());
            if (comicInfo.canLoad(isLoadNext)) {
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
            startFragment(new ReaderDetailFragment(imageInfo));
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
//        if (Codes.isFirstLoadWebView && comic.getSourceId() == Codes.MI_TUI) {
//            WebView webView = new WebView(getContext());
//            webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
//            webView.getSettings().setJavaScriptEnabled(true);
//            webView.setWebViewClient(new WebViewClient() {
//                @Override
//                public void onPageFinished(WebView view, String url) {
//                    super.onPageFinished(view, url);
//                    //Log.i(TAG, "onPageFinished: " + url);
//                    Codes.isFirstLoadWebView = false;
//                    webView.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
//                    view.onPause();
//                }
//            });
//            String chapterUrl = chapterInfoList.get(curPosition).getChapterUrl();
//            webView.loadUrl(chapterUrl);
//            //Log.i(TAG, "loadImageInfoListComplete: mitui " + chapterUrl);
//        }
    }

//    class MyJavaScriptInterface {
//        @JavascriptInterface
//        @SuppressWarnings("unused")
//        public void processHTML(String html) {
//            // 注意啦，此处就是执行了js以后 的网页源码
//            //Log.i(TAG, "processHTML: " + html.length());
//            Document document = Jsoup.parse(html);
//            String src = document.selectFirst("img#image").attr("src");
//            if (src != null) {
//                Codes.miTuiServer = src.substring(0, src.indexOf('/', src.indexOf('.')));
//            }
//            //Log.i(TAG, "processHTML: server = " + Codes.miTuiServer);
//        }
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ImgUtil.clearMap();
        comic.setDate(new Date());
        ComicUtil.first(comic);
        DBUtil.saveComic(comic, DBUtil.SAVE_CUR);
    }
}
