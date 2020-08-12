package com.qc.mycomic.ui.fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qc.mycomic.R;
import com.qc.mycomic.ui.adapter.ChapterAdapter;
import com.qc.mycomic.model.ChapterInfo;
import com.qc.mycomic.model.Comic;
import com.qc.mycomic.model.ComicInfo;
import com.qc.mycomic.other.MySpacesItemDecoration;
import com.qc.mycomic.ui.presenter.ChapterPresenter;
import com.qc.mycomic.util.Codes;
import com.qc.mycomic.util.DBUtil;
import com.qc.mycomic.util.SourceUtil;
import com.qc.mycomic.util.StringUtil;
import com.qc.mycomic.ui.view.ChapterView;
import com.qmuiteam.qmui.qqface.QMUIQQFaceView;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;

import java.util.Date;
import java.util.List;

import the.one.base.model.PopupItem;
import the.one.base.ui.fragment.BaseDataFragment;
import the.one.base.ui.presenter.BasePresenter;
import the.one.base.util.QMUIBottomSheetUtil;
import the.one.base.util.QMUIDialogUtil;
import the.one.base.util.QMUIPopupUtil;
import the.one.base.util.glide.GlideEngine;
import the.one.base.widge.decoration.SpacesItemDecoration;

public class ChapterFragment extends BaseDataFragment<ChapterInfo> implements ChapterView {

    private Comic comic;

    private boolean isChangeOrder = false;

    private boolean isChangeSource = false;

    private ChapterPresenter presenter = new ChapterPresenter();

    public ChapterFragment(Comic comic) {
        this.comic = comic;
    }

    @Override
    protected int setType() {
        return TYPE_GRID;
    }

    @Override
    protected int setColumn() {
        return 3;
    }

    @Override
    protected boolean restoreSubWindowWhenDragBack() {
        return false;
    }

    @Override
    protected SpacesItemDecoration getSpacesItemDecoration() {
        int space = QMUIDisplayHelper.dp2px(_mActivity, setSpacing());
        return new MySpacesItemDecoration(setColumn(), 0, space);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter && adapter != null) {
            if (Codes.toStatus == Codes.NORMAL) {
                adapter.notifyDataSetChanged();
            }
            if (Codes.toStatus == Codes.READER_TO_CHAPTER) {
                Codes.toStatus = Codes.NORMAL;
                adapter.notifyDataSetChanged();
                comic.setDate(new Date());
                DBUtil.saveData(comic, false);
                DBUtil.saveData(comic.getComicInfo());
            }
            if (Codes.toStatus == Codes.SEARCH_TO_CHAPTER) {
                Codes.toStatus = Codes.NORMAL;
                adapter.notifyDataSetChanged();
            }
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    private QMUIPopup mSettingPopup;
    private String[] mMenus = new String[]{"更新漫画源", "查看信息"};
    private ImageButton ibMenu;
    private ImageButton ibSwap;
    private View headerView;
    private View bottomView;

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        QMUIQQFaceView mTitle = mTopLayout.setTitle("漫画详情");
        mTopLayout.setTitleGravity(Gravity.CENTER);
        mTitle.setTextColor(getColorr(R.color.qmui_config_color_gray_1));
        mTitle.getPaint().setFakeBoldText(true);
        addTopBarBackBtn();
        headerView = getView(R.layout.fragment_chapter);
        bottomView = getView(R.layout.bottom_chapter);
        setValue();
    }

    private void setListener() {
        //菜单按钮: 更新漫画源、查看信息
        ibMenu.setOnClickListener(v -> {
            if (null == mSettingPopup) {
                mSettingPopup = QMUIPopupUtil.createListPop(_mActivity, mMenus, (adapter, view, position) -> {
                    if (position == 0) {
                        showLoadingDialog("正在更新漫画源");
                        presenter.updateSource(comic);
                    } else if (position == 1) {
                        QMUIDialogUtil.showSimpleDialog(getContext(), "查看信息", comic.toStringView()).show();
                    }
                    mSettingPopup.dismiss();
                });
            }
            mSettingPopup.show(ibMenu);
        });

        //更换漫画顺序
        ibSwap.setOnClickListener(v -> {
            if (checkEmpty()) {
                isChangeOrder = true;
                changeView();
            } else {
                showFailTips("暂无漫画章节");
            }
        });

        //改变漫画源
        TextView tvSource = headerView.findViewById(R.id.tvSource);
        tvSource.setOnClickListener(v -> {
            List<PopupItem> list = SourceUtil.getPopupItemList(comic.getComicInfoList());
            int index = SourceUtil.getPopupItemIndex(comic);
            QMUIBottomSheetUtil.showSimpleBottomSheetList(getContext(), list, "切换漫画源", index, new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                @Override
                public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                    int sourceId = SourceUtil.getSourceId(tag);
                    comic.setSourceId(sourceId);
                    if (comic.changeComicInfo()) {
                        showLoadingPage();
                        isChangeSource = true;
                        requestServer();
                        DBUtil.saveData(comic);
                    }
                    dialog.dismiss();
                }
            }).show();
        });

        //阅读最新章节
        TextView tvUpdateChapter = headerView.findViewById(R.id.tvUpdateChapter);
        tvUpdateChapter.setOnClickListener(v -> {
            int position = comic.getComicInfo().getLastPosition();
            if (checkEmpty()) {
                comic.getComicInfo().setCurPosition(position);
                adapter.notifyDataSetChanged();
                start(position);
                DBUtil.saveData(comic);
            } else {
                showFailTips("暂无漫画章节");
            }
        });

        //收藏漫画
        LinearLayout favLayout = bottomView.findViewById(R.id.favLayout);
        favLayout.setOnClickListener(v -> {
            setFavLayout(favLayout, comic.getStatus() != Codes.STATUS_FAV);
            comic.setStatus(comic.getStatus() != Codes.STATUS_FAV ? Codes.STATUS_FAV : Codes.STATUS_HIS);
            DBUtil.saveData(comic);
        });

        //开始阅读
        TextView tvRead = bottomView.findViewById(R.id.tvRead);
        tvRead.setOnClickListener(v -> {
            if (checkEmpty()) {
                start(comic.getComicInfo().getCurPosition());
            } else {
                showFailTips("暂无漫画章节");
            }
        });
    }

    private void addView() {
        ibMenu = mTopLayout.addRightImageButton(R.drawable.ic_baseline_menu_24, R.id.topbar_right_button1);
        ibSwap = mTopLayout.addRightImageButton(R.drawable.ic_baseline_swap_vert_24, R.id.topbar_right_button2);
        adapter.setHeaderView(headerView);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(QMUIDisplayHelper.getScreenWidth(_mActivity), QMUIDisplayHelper.getScreenHeight(_mActivity));
        mStatusLayout.addView(bottomView, layoutParams);
        View blankView = new View(_mActivity);
        blankView.setLayoutParams(new LinearLayout.LayoutParams(QMUIDisplayHelper.getScreenWidth(_mActivity), QMUIDisplayHelper.dp2px(_mActivity, 50)));
        adapter.setFooterView(blankView);
    }

    private void setValue() {
        QMUIRadiusImageView qivImg = headerView.findViewById(R.id.qivImg);
        GlideEngine.createGlideEngine().loadImage(getContext(), comic.getComicInfo().getImgUrl(), qivImg);
        TextView tvTitle = headerView.findViewById(R.id.tvTitle);
        TextView tvSource = headerView.findViewById(R.id.tvSource);
        TextView tvSourceSize = headerView.findViewById(R.id.tvSourceSize);
        TextView tvUpdateTime = headerView.findViewById(R.id.tvUpdateTime);
        TextView tvUpdateChapter = headerView.findViewById(R.id.tvUpdateChapter);
        tvTitle.setText(comic.getComicInfo().getTitle());
        tvSource.setText(comic.getSourceName());
        tvSourceSize.setText("(" + comic.getSourceSize() + ")");
        tvUpdateChapter.setText(comic.getComicInfo().getUpdateChapter());
        tvUpdateTime.setText(comic.getComicInfo().getUpdateTime());
        LinearLayout favLayout = bottomView.findViewById(R.id.favLayout);
        setFavLayout(favLayout, comic.getStatus() == Codes.STATUS_FAV);
    }

    @Override
    protected void initAdapter() {
        super.initAdapter();
        adapter.getLoadMoreModule().setOnLoadMoreListener(null);
    }

    public void setFavLayout(View favLayout, boolean isFav) {
        ImageView ivFav = favLayout.findViewById(R.id.ivFav);
        TextView tvFav = favLayout.findViewById(R.id.tvFav);
        if (isFav) {
            ivFav.setImageDrawable(getDrawablee(R.drawable.ic_baseline_favorite_24));
            tvFav.setText("已收藏");
        } else {
            ivFav.setImageDrawable(getDrawablee(R.drawable.ic_baseline_favorite_border_24));
            tvFav.setText("未收藏");
        }
    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new ChapterAdapter(R.layout.item_chapter, comic);
    }

    @Override
    protected void requestServer() {
        List<ChapterInfo> chapterInfoList = comic.getComicInfo().getChapterInfoList();
        Log.i(TAG, "requestServer: status = " + Codes.toStatus);
        if (chapterInfoList == null || chapterInfoList.size() == 0) {
            presenter.load(comic);
        } else {
            loadComplete();
        }
    }

    private void changeView() {
        showLoadingPage();
        if (isChangeOrder) {
            isChangeOrder = false;
            ComicInfo comicInfo = comic.getComicInfo();
            StringUtil.swapList(comicInfo.getChapterInfoList());
            adapter.notifyDataSetChanged();
            comicInfo.setOrder(comicInfo.getOrder() == Codes.ASC ? Codes.DESC : Codes.ASC);
            DBUtil.saveData(comicInfo);
        }
        if (isChangeSource) {
            isChangeSource = false;
            ComicInfo comicInfo = comic.getComicInfo();
            if (comicInfo.getOrder() != Codes.DESC) {
                StringUtil.swapList(comicInfo.getChapterInfoList());
            }
            onFirstComplete(comic.getComicInfo().getChapterInfoList());
            adapter.notifyDataSetChanged();
            setValue();
        }
        showContentPage();
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view,
                            int position) {
        Log.i(TAG, "onItemClick: position = " + position);
        comic.getComicInfo().setCurPosition(position);
        comic.setDate(new Date());
        adapter.notifyDataSetChanged();
        Log.i(TAG, "onItemClick: toPosition = " + comic.getComicInfo().getCurPosition());
//        startFragment(new ReaderFragment(comic, comicInfo.getCurPosition()));
        start(comic.getComicInfo().getCurPosition());
        DBUtil.saveData(comic, false);
        DBUtil.saveData(comic.getComicInfo());
    }

    @Override
    public boolean onItemLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view,
                                   int position) {
        return false;
    }

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }

    private boolean firstLoad = true;

    @Override
    public void loadComplete() {
        try {
            if (firstLoad) {
                firstLoad = false;
                addView();
                setListener();
            }
            setValue();
            ComicInfo comicInfo = comic.getComicInfo();
            if (comicInfo.getOrder() != Codes.DESC) {
                StringUtil.swapList(comicInfo.getChapterInfoList());
            }
            onFirstComplete(comicInfo.getChapterInfoList());
            if (Codes.toStatus == Codes.RANK_TO_CHAPTER) {
                Codes.toStatus = Codes.NORMAL;
                showLoadingDialog("正在更新漫画源");
                presenter.updateSource(comic);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int count = 0;

    @Override
    public void updateSourceComplete(List<ComicInfo> infoList) {
        count++;
        if (infoList != null) {
            for (ComicInfo info : infoList) {
                updateComicInfo(info);
            }
        }
        if (count == SourceUtil.getSize()) {
            count = 0;
            hideLoadingDialog();
            showSuccessTips("搜索完毕");
            setValue();
            DBUtil.saveData(comic);
        }
    }

    private void updateComicInfo(ComicInfo info) {
        if (!comic.getTitle().equals(info.getTitle())) {
            return;
        }
        if (comic.getComicInfoList().contains(info)) {
            comic.getComicInfoList().remove(info);
            comic.addComicInfo(info);
        } else {
            comic.addComicInfo(info);
        }
    }

    public void start(int position) {
        startFragment(new ReaderFragment(comic, position));
//        startFragment(new ReaderNewFragment(comic, position));
    }

    private boolean checkEmpty() {
        return !comic.getComicInfo().getChapterInfoList().isEmpty();
    }
}
