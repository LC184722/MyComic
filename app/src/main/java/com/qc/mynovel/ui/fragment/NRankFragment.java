package com.qc.mynovel.ui.fragment;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qc.common.constant.Constant;
import com.qc.common.util.RestartUtil;
import com.qc.common.constant.TmpData;
import com.qc.mycomic.R;
import com.qc.mynovel.ui.adapter.NRankAdapter;
import com.qc.mynovel.ui.adapter.NRankLeftAdapter;
import com.qc.mynovel.ui.presenter.NRankPresenter;
import com.qc.mynovel.ui.view.NRankView;
import com.qc.mynovel.util.NovelHelper;
import com.qc.mynovel.util.DBUtil;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import the.one.base.ui.fragment.BaseDataFragment;
import the.one.base.ui.presenter.BasePresenter;
import top.luqichuang.common.util.MapUtil;
import top.luqichuang.common.util.StringUtil;
import top.luqichuang.mynovel.model.Novel;
import top.luqichuang.mynovel.model.NovelInfo;
import top.luqichuang.mynovel.model.NSource;

/**
 * @author LuQiChuang
 * @desc 排行榜界面
 * @date 2020/8/12 15:19
 * @ver 1.0
 */
public class NRankFragment extends BaseDataFragment<Novel> implements NRankView {

    private NRankAdapter rankAdapter;

    private NRankPresenter presenter;

    private NSource nSource;

    private Map<String, String> map;

    private String url;

    private List<Novel> novelList;

    public NRankFragment() {
        RestartUtil.restart(_mActivity);
    }

    public NRankFragment(NSource nSource) {
        this.nSource = nSource;
        this.rankAdapter = new NRankAdapter(R.layout.item_rank_right);
        this.presenter = new NRankPresenter(nSource);
        if (nSource.getRankMap() != null) {
            this.map = nSource.getRankMap();
            if (!map.isEmpty()) {
                url = MapUtil.getFirstValue(map);
            }
        } else {
            this.map = new LinkedHashMap<>();
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
            List<String> items = MapUtil.getKeyList(map);
            NRankLeftAdapter rankLeftAdapter = new NRankLeftAdapter(R.layout.item_rank_left, items);
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
                    url = MapUtil.getValueByIndex(map, position);
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
        Novel novel = (Novel) adapter.getData().get(position);
        novelList = DBUtil.findNovelListByStatus(Constant.STATUS_ALL);
        int index = novelList.indexOf(novel);
        if (index != -1) {
            Novel myNovel = novelList.get(index);
            for (NovelInfo novelInfo : novel.getNovelInfoList()) {
                if (!myNovel.getNovelInfoList().contains(novelInfo)) {
                    NovelHelper.addNovelInfo(novel, novelInfo);
                }
            }
            startFragment(new NChapterFragment(myNovel));
        } else {
            TmpData.toStatus = Constant.RANK_TO_CHAPTER;
            startFragment(new NChapterFragment(novel));
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
    public void loadComplete(List<Novel> novelList) {
        if (!isLoadMore) {
            isLoadMore = true;
            onFirstComplete(novelList);
            recycleView.scrollToPosition(0);
        } else {
            onComplete(novelList);
        }
    }
}
