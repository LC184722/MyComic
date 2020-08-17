package com.qc.mycomic.ui.fragment;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qc.mycomic.R;
import com.qc.mycomic.ui.adapter.RankAdapter;
import com.qc.mycomic.ui.adapter.RankLeftAdapter;
import com.qc.mycomic.model.Comic;
import com.qc.mycomic.model.ComicInfo;
import com.qc.mycomic.model.MyMap;
import com.qc.mycomic.model.Source;
import com.qc.mycomic.ui.presenter.RankPresenter;
import com.qc.mycomic.util.Codes;
import com.qc.mycomic.util.DBUtil;
import com.qc.mycomic.util.StringUtil;
import com.qc.mycomic.ui.view.RankView;

import java.util.List;

import okhttp3.Request;
import the.one.base.ui.fragment.BaseDataFragment;
import the.one.base.ui.presenter.BasePresenter;

/**
 * @author LuQiChuang
 * @desc 排行榜界面
 * @date 2020/8/12 15:19
 * @ver 1.0
 */
public class RankFragment extends BaseDataFragment<Comic> implements RankView {

    private RankAdapter rankAdapter;

    private RankPresenter presenter;

    private Source source;

    private MyMap<String, String> map;

    private String url;

    private List<Comic> comicList;

    public RankFragment(Source source) {
        this.source = source;
        this.rankAdapter = new RankAdapter(R.layout.item_rank_right);
        this.presenter = new RankPresenter(source);
        if (source.getRankMap() != null) {
            this.map = source.getRankMap();
            if (!map.isEmpty()) {
                url = map.getFirstValue();
            }
        } else {
            this.map = new MyMap<>();
        }
    }

    @Override
    protected boolean isNeedAround() {
        return true;
    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        return rankAdapter;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mTopLayout.setVisibility(View.GONE);
        if (!map.isEmpty()) {
            View leftView = getView(R.layout.fragment_rank_left);
            List<String> items = map.getKeyList();
            RankLeftAdapter rankLeftAdapter = new RankLeftAdapter(R.layout.item_rank_left, items);
            RecyclerView leftRecyclerView = leftView.findViewById(R.id.recycleView);
            initRecycleView(leftRecyclerView, TYPE_LIST, rankLeftAdapter);
            leftRecyclerView.addItemDecoration(new DividerItemDecoration(_mActivity, DividerItemDecoration.VERTICAL));
            rankLeftAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    rankLeftAdapter.setPosition(position);
                    showLoadingPage();
                    isLoadMore = false;
                    pageNum = 1;
                    url = map.getByIndex(position).getValue();
                    requestServer();
                }
            });
            flLeftLayout.addView(leftView);
        }
    }

    private boolean isLoadMore = false;
    private int pageNum = 1;

    @Override
    public void onRefresh() {
        pageNum = 0;
        isLoadMore = false;
        super.onRefresh();
    }

    @Override
    protected void requestServer() {
        if (url != null) {
            if (!isLoadMore) {
                presenter.load(url);
            } else {
                presenter.load(checkUrl(url, ++pageNum));
            }
        } else {
            showEmptyPage("暂无数据");
        }
    }

    private String checkUrl(String url, int pageNum) {
        String[] pageStrings = {
                "page=",
                "page/",
        };
        for (String pageString : pageStrings) {
            String tmp = StringUtil.match(pageString + "(\\d+)", url);
            if (tmp != null) {
                String page = pageString + tmp;
                String nPage = pageString + pageNum;
                return url.replaceFirst(page, nPage);
            }
        }
        return null;
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        Comic comic = (Comic) adapter.getData().get(position);
        comicList = DBUtil.findComicListByStatus(Codes.STATUS_ALL);
        int index = comicList.indexOf(comic);
        if (index != -1) {
            Comic myComic = comicList.get(index);
            for (ComicInfo comicInfo : comic.getComicInfoList()) {
                if (!myComic.getComicInfoList().contains(comicInfo)) {
                    myComic.addComicInfo(comicInfo);
                }
            }
            startFragment(new ChapterFragment(myComic));
        } else {
            Codes.toStatus = Codes.RANK_TO_CHAPTER;
            startFragment(new ChapterFragment(comic));
        }
    }

    @Override
    public boolean onItemLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        return false;
    }

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void loadComplete(List<Comic> comicList) {
        if (!isLoadMore) {
            isLoadMore = true;
            onFirstComplete(comicList);
            recycleView.scrollToPosition(0);
        } else {
            onComplete(comicList);
        }
    }
}
