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
import com.qc.mycomic.util.ComicUtil;
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

/**
 * @author LuQiChuang
 * @desc 章节详情界面
 * @date 2020/8/12 15:18
 * @ver 1.0
 */
public class ChapterFragment extends BaseDataFragment<ChapterInfo> implements ChapterView {

    private Comic comic;

    private boolean isChangeOrder = false;

    private boolean isChangeSource = false;

    private ChapterPresenter presenter = new ChapterPresenter();

    public ChapterFragment(Comic comic) {
        this.comic = comic;
        Log.i(TAG, "ChapterFragment: start: comic.getComicInfo().getOrder() " + comic.getComicInfo().getOrder());
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
            adapter.notifyDataSetChanged();
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
            if (checkNotEmpty()) {
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
                        DBUtil.saveComic(comic, DBUtil.SAVE_ONLY);
                    }
                    dialog.dismiss();
                }
            }).show();
        });

        //阅读最新章节
        TextView tvUpdateChapter = headerView.findViewById(R.id.tvUpdateChapter);
        tvUpdateChapter.setOnClickListener(v -> {
            if (checkNotEmpty()) {
                comic.getComicInfo().newestChapter();
                start();
                DBUtil.saveComic(comic, DBUtil.SAVE_CUR);
            } else {
                showFailTips("暂无漫画章节");
            }
        });

        //收藏漫画
        LinearLayout favLayout = bottomView.findViewById(R.id.favLayout);
        favLayout.setOnClickListener(v -> {
            setFavLayout(favLayout, comic.getStatus() != Codes.STATUS_FAV);
            comic.setStatus(comic.getStatus() != Codes.STATUS_FAV ? Codes.STATUS_FAV : Codes.STATUS_HIS);
            DBUtil.saveComic(comic, DBUtil.SAVE_CUR);
        });

        //开始阅读
        TextView tvRead = bottomView.findViewById(R.id.tvRead);
        tvRead.setOnClickListener(v -> {
            if (checkNotEmpty()) {
                if (comic.getComicInfo().checkChapterId(comic.getComicInfo().getCurChapterId())) {
                    start();
                } else {
                    start(comic.getComicInfo().getPosition(0));
                }
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
        Log.i(TAG, "requestServer: Codes.toStatus = " + Codes.toStatus);
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
            comicInfo.setOrder(comicInfo.getOrder() == Codes.ASC ? Codes.DESC : Codes.ASC);
            adapter.notifyDataSetChanged();
            DBUtil.saveComicInfo(comicInfo);
        }
        if (isChangeSource) {
            isChangeSource = false;
            onFirstComplete(comic.getComicInfo().getChapterInfoList());
            setValue();
            adapter.notifyDataSetChanged();
        }
        showContentPage();
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view,
                            int position) {
        Log.i(TAG, "onItemClick: position = " + position);
        comic.setDate(new Date());
        ComicUtil.first(comic);
        start(position);
        DBUtil.saveComic(comic, DBUtil.SAVE_ONLY);
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
            List<ChapterInfo> list = comic.getComicInfo().getChapterInfoList();
            if (isNeedSwap(list, comic.getComicInfo().getOrder())) {
                StringUtil.swapList(list);
            }
            setValue();
            onFirstComplete(list);
            adapter.notifyDataSetChanged();
            if (Codes.toStatus == Codes.RANK_TO_CHAPTER) {
                Codes.toStatus = Codes.NORMAL;
                showLoadingDialog("正在更新漫画源");
                presenter.updateSource(comic);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isNeedSwap(List<ChapterInfo> list, int order) {
        if (list != null && !list.isEmpty()) {
            int firstId = list.get(0).getId();
            int lastId = list.get(list.size() - 1).getId();
            if (firstId > lastId) {
                return order != Codes.DESC;
            } else if (firstId < lastId) {
                return order != Codes.ASC;
            } else {
                return false;
            }
        }
        return false;
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
            DBUtil.saveComic(comic, DBUtil.SAVE_ALL);
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

    public void start() {
        adapter.notifyDataSetChanged();
        startFragment(new ReaderFragment(comic));
    }

    public void start(int position) {
        comic.getComicInfo().setPosition(position);
        start();
    }

    private boolean checkNotEmpty() {
        return !comic.getComicInfo().getChapterInfoList().isEmpty();
    }
}
