package com.qc.mycomic.fragment;

import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qc.mycomic.R;
import com.qc.mycomic.adapter.ReaderAdapter;
import com.qc.mycomic.model.Comic;
import com.qc.mycomic.model.ComicInfo;
import com.qc.mycomic.model.ImageInfo;
import com.qc.mycomic.presenter.ReaderPresenter;
import com.qc.mycomic.util.Codes;
import com.qc.mycomic.util.DBUtil;
import com.qc.mycomic.view.ReaderView;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

import the.one.base.ui.fragment.BaseDataFragment;
import the.one.base.ui.presenter.BasePresenter;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;

public class ReaderFragment extends BaseDataFragment<ImageInfo> implements ReaderView {

    private Comic comic;

    private ComicInfo comicInfo;

    private int curPosition;

    private boolean isLoadNext;

    private ReaderPresenter presenter = new ReaderPresenter();

    private List<ImageInfo> imageInfoList;

    private ReaderAdapter readerAdapter;

    private View topView;
    private View bottomView;
    private TextView tvChapter;
    private TextView tvProgress;
    private ImageButton ibLeft;
    private ImageButton ibRight;
    private SeekBar seekBar;
    private boolean firstLoad = true;

    public ReaderFragment(Comic comic, int curPosition) {
        this.comic = comic;
        this.comicInfo = comic.getComicInfo();
        this.curPosition = curPosition;
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
            ibLeft = bottomView.findViewById(R.id.ibLeft);
            ibRight = bottomView.findViewById(R.id.ibRight);
            seekBar = bottomView.findViewById(R.id.seekBar);
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
            curPosition = imageInfo.getChapterId();
            comicInfo.setCurPosition(imageInfo.getChapterId());
            tvChapter.setText(comicInfo.getCurChapterTitle());
            tvProgress.setText(imageInfo.toStringProgress());
            seekBar.setMax(imageInfo.getTotal() - 1);
            seekBar.setProgress(imageInfo.getCur());
        }
    }

    private boolean isSmooth = false;

    private void setListener() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (isSmooth) {
                    Log.i(TAG, "onProgressChanged: value = " + (first - imageInfoList.get(first).getCur() + progress));
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

        ibLeft.setOnClickListener(v -> {
            onRefresh();
        });

        ibRight.setOnClickListener(v -> {
            super.onRefresh();
        });
    }

    private int first;
    private int total;

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
//                        int count = manager.getChildCount();    //得到显示屏幕内的list数量
//                        int total = manager.getItemCount();    //得到list的总数量
                    first = manager.findFirstVisibleItemPosition();//得到显示屏内的第一个list的位置数position
//                        Log.i(TAG, "onScrolled: first = " + first);
//                        Log.i(TAG, "onScrolled: " + imageInfoList.size());
                    ImageInfo imageInfo = imageInfoList.get(first);
                    curPosition = imageInfo.getChapterId();
                    comicInfo.setCurPosition(imageInfo.getChapterId());
                    tvChapter.setText(comicInfo.getCurChapterTitle());
                    tvProgress.setText(imageInfo.toStringProgress());
                    Log.i(TAG, "onScrolled: first = " + first);
                    if (!isSmooth) {
                        seekBar.setProgress(imageInfo.getCur());
                        if (bottomView.getVisibility() == View.VISIBLE) {
                            bottomView.setVisibility(View.GONE);
                        }
                    }
                    if (total != imageInfo.getTotal() - 1) {
                        total = imageInfo.getTotal() - 1;
                        seekBar.setMax(total);
                    }
                } else {
                    Log.i(TAG, "onScrolled: is null");
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
        readerAdapter = new ReaderAdapter(R.layout.item_reader, comic);
        return readerAdapter;
    }

    @Override
    protected void requestServer() {
        if (imageInfoList == null) {
            presenter.loadImageInfoList(comic, curPosition);
        } else {
            int nextPosition = getNextPosition(curPosition);
            if (comicInfo.canLoad(nextPosition)) {
                presenter.loadImageInfoList(comic, nextPosition);
                readerAdapter.clearMap();
            } else {
                onComplete(null);
            }
        }
        DBUtil.saveData(comicInfo);
    }

    private int getNextPosition(int position) {
        if (isLoadNext) {
            if (comicInfo.getOrder() == Codes.ASC) {
                return position + 1;
            } else {
                return position - 1;
            }
        } else {
            isLoadNext = true;
            if (comicInfo.getOrder() == Codes.ASC) {
                return position - 1;
            } else {
                return position + 1;
            }
        }
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        Log.i(TAG, "onItemClick: click...");
        changeVisibility(bottomView);
    }

    private void changeVisibility(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onItemLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        ImageInfo imageInfo = (ImageInfo) adapter.getData().get(position);
        ReaderAdapter readerAdapter = (ReaderAdapter) adapter;
        Log.i(TAG, "onItemLongClick: " + imageInfo.toStringProgress());
        if (imageInfo.getStatus() == ReaderAdapter.LOAD_FAIL) {
            ImageView imageView = view.findViewById(R.id.imageView);
            readerAdapter.initImageView(imageView, imageInfo, ReaderAdapter.LOAD_NO);
            readerAdapter.loadImage(getContext(), imageInfo, imageView);
        } else if (imageInfo.getStatus() == ReaderAdapter.LOAD_SUCCESS) {
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
//        if (Codes.isFirstLoadWebView && comic.getSourceId() == Codes.MI_TUI) {
//            WebView webView = new WebView(getContext());
//            webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
//            webView.getSettings().setJavaScriptEnabled(true);
//            webView.setWebViewClient(new WebViewClient() {
//                @Override
//                public void onPageFinished(WebView view, String url) {
//                    super.onPageFinished(view, url);
//                    Log.i(TAG, "onPageFinished: " + url);
//                    Codes.isFirstLoadWebView = false;
//                    webView.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
//                    view.onPause();
//                }
//            });
//            String chapterUrl = chapterInfoList.get(curPosition).getChapterUrl();
//            webView.loadUrl(chapterUrl);
//            Log.i(TAG, "loadImageInfoListComplete: mitui " + chapterUrl);
//        }
    }

    class MyJavaScriptInterface {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processHTML(String html) {
            // 注意啦，此处就是执行了js以后 的网页源码
            Log.i(TAG, "processHTML: " + html.length());
            Document document = Jsoup.parse(html);
            String src = document.selectFirst("img#image").attr("src");
            if (src != null) {
                Codes.miTuiServer = src.substring(0, src.indexOf('/', src.indexOf('.')));
            }
            Log.i(TAG, "processHTML: server = " + Codes.miTuiServer);
        }
    }

}
