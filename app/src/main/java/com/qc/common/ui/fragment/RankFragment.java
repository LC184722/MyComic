package com.qc.common.ui.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qc.common.constant.AppConstant;
import com.qc.common.constant.Constant;
import com.qc.common.constant.TmpData;
import com.qc.common.ui.adapter.RankAdapter;
import com.qc.common.ui.adapter.RankLeftAdapter;
import com.qc.common.ui.presenter.RankPresenter;
import com.qc.common.ui.view.RankView;
import com.qc.common.util.DBUtil;
import com.qc.common.util.EntityHelper;
import com.qc.mycomic.R;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import the.one.base.ui.fragment.BaseDataFragment;
import the.one.base.ui.presenter.BasePresenter;
import top.luqichuang.common.model.Entity;
import top.luqichuang.common.model.EntityInfo;
import top.luqichuang.common.model.Source;
import top.luqichuang.common.util.MapUtil;
import top.luqichuang.common.util.SourceUtil;
import top.luqichuang.common.util.StringUtil;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/11 11:27
 * @ver 1.0
 */
public class RankFragment extends BaseDataFragment<Entity> implements RankView {

    private RankAdapter rankAdapter;

    private RankPresenter presenter;

    private Source source;

    private Map<String, String> map;

    private String url;

    private List<Entity> entityList;

    public static RankFragment getInstance(int sourceId) {
        RankFragment fragment = new RankFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("sourceId", sourceId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        int sourceId = (int) getArguments().get("sourceId");
        if (TmpData.contentCode == AppConstant.COMIC_CODE) {
            this.source = SourceUtil.getSource(sourceId);
        } else if (TmpData.contentCode == AppConstant.READER_CODE) {
            this.source = SourceUtil.getNSource(sourceId);
        } else {
            this.source = SourceUtil.getVSource(sourceId);
        }
        this.rankAdapter = new RankAdapter(R.layout.item_rank_right);
        this.presenter = new RankPresenter(source);
        if (source.getRankMap() != null) {
            this.map = source.getRankMap();
            if (!map.isEmpty()) {
                url = MapUtil.getFirstValue(map);
            }
        } else {
            this.map = new LinkedHashMap<>();
        }
        super.onCreate(savedInstanceState);
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
        Entity entity = (Entity) adapter.getData().get(position);
        entityList = (List<Entity>) DBUtil.findListByStatus(Constant.STATUS_ALL);
        int index = entityList.indexOf(entity);
        if (index != -1) {
            Entity myEntity = entityList.get(index);
            for (EntityInfo entityInfo : entity.getInfoList()) {
                if (!myEntity.getInfoList().contains(entityInfo)) {
                    EntityHelper.addInfo(entity, entityInfo);
                }
            }
            startFragment(ChapterFragment.getInstance(myEntity));
        } else {
            TmpData.toStatus = Constant.RANK_TO_CHAPTER;
            startFragment(ChapterFragment.getInstance(entity));
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
    public void loadComplete(List<Entity> entityList) {
        if (!isLoadMore) {
            isLoadMore = true;
            onFirstComplete(entityList);
            recycleView.scrollToPosition(0);
        } else {
            onComplete(entityList);
        }
    }
}