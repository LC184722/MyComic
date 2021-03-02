package com.qc.mycomic.ui.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qc.common.constant.Constant;
import com.qc.common.constant.TmpData;
import com.qc.common.self.ImageConfig;
import com.qc.common.self.MySpacesItemDecoration;
import com.qc.common.util.AnimationUtil;
import com.qc.common.util.ImgUtil;
import com.qc.common.util.PopupUtil;
import com.qc.mycomic.R;
import com.qc.mycomic.ui.adapter.ChapterAdapter;
import com.qc.mycomic.ui.presenter.ChapterPresenter;
import com.qc.mycomic.ui.view.ChapterView;
import com.qc.mycomic.util.ComicHelper;
import com.qc.mycomic.util.ComicUtil;
import com.qc.mycomic.util.DBUtil;
import com.qmuiteam.qmui.qqface.QMUIQQFaceView;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import the.one.base.ui.fragment.BaseDataFragment;
import the.one.base.ui.presenter.BasePresenter;
import the.one.base.util.QMUIDialogUtil;
import the.one.base.util.QMUIPopupUtil;
import the.one.base.widge.decoration.SpacesItemDecoration;
import top.luqichuang.common.model.ChapterInfo;
import top.luqichuang.common.util.MapUtil;
import top.luqichuang.common.util.SourceUtil;
import top.luqichuang.common.util.StringUtil;
import top.luqichuang.mycomic.model.Comic;
import top.luqichuang.mycomic.model.ComicInfo;

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

    private ChapterAdapter chapterAdapter;

    public static ChapterFragment getInstance(Comic comic) {
        ChapterFragment fragment = new ChapterFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("comic", comic);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        this.comic = (Comic) getArguments().get("comic");
        super.onCreate(savedInstanceState);
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
            chapterAdapter.setChapterId(comic.getComicInfo().getCurChapterId());
            setValue();
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    private QMUIPopup mSettingPopup;
    private String[] mMenus = new String[]{"更新漫画源", "查看信息"};
    private ImageButton ibMenu;
    private ImageButton ibSwap;
    private View headerView;
    private View bottomView;

    private QMUIRadiusImageView imageView;
    private RelativeLayout relativeLayout;
    private TextView tvTitle;
    private TextView tvSource;
    private TextView tvSourceSize;
    private TextView tvUpdateTime;
    private TextView tvUpdateChapter;
    private LinearLayout favLayout;
    private ImageView ivFav;
    private TextView tvFav;

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
        relativeLayout = headerView.findViewById(R.id.imageRelativeLayout);
        imageView = headerView.findViewById(R.id.imageView);
        tvTitle = headerView.findViewById(R.id.tvTitle);
        tvSource = headerView.findViewById(R.id.tvSource);
        tvSourceSize = headerView.findViewById(R.id.tvSourceSize);
        tvUpdateTime = headerView.findViewById(R.id.tvUpdateTime);
        tvUpdateChapter = headerView.findViewById(R.id.tvUpdateChapter);
        favLayout = bottomView.findViewById(R.id.favLayout);
        ivFav = favLayout.findViewById(R.id.ivFav);
        tvFav = favLayout.findViewById(R.id.tvFav);
        setValue();
    }

    private void setListener() {
        //菜单按钮: 更新漫画源、查看信息
        ibMenu.setOnClickListener(v -> {
            if (null == mSettingPopup) {
                mSettingPopup = QMUIPopupUtil.createListPop(_mActivity, mMenus, (adapter, view, position) -> {
                    if (position == 0) {
                        showProgressDialog(0, SourceUtil.size(), "正在更新漫画源");
                        presenter.updateSource(comic);
                    } else if (position == 1) {
                        QMUIDialogUtil.showSimpleDialog(getContext(), "查看信息", ComicHelper.toStringView(comic)).show();
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
            Map<Integer, String> map = PopupUtil.getMap(comic.getComicInfoList());
            PopupUtil.showSimpleBottomSheetList(getContext(), map, comic.getSourceId(), "切换漫画源", new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                @Override
                public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                    Integer integer = MapUtil.getKeyByValue(map, tag);
                    int sourceId = position;
                    if (integer != null) {
                        sourceId = integer;
                    }
                    comic.setSourceId(sourceId);
                    if (ComicHelper.changeComicInfo(comic)) {
                        showLoadingPage();
                        isChangeSource = true;
                        requestServer();
                        DBUtil.saveComic(comic, DBUtil.SAVE_ONLY);
                    }
                    dialog.dismiss();
                }
            });
        });

        //阅读最新章节
        TextView tvUpdateChapter = headerView.findViewById(R.id.tvUpdateChapter);
        tvUpdateChapter.setOnClickListener(v -> {
            if (checkNotEmpty()) {
                ComicHelper.newestChapter(comic.getComicInfo());
                start();
                DBUtil.saveComic(comic, DBUtil.SAVE_CUR);
            } else {
                showFailTips("暂无漫画章节");
            }
        });

        //收藏漫画
        LinearLayout favLayout = bottomView.findViewById(R.id.favLayout);
        favLayout.setOnClickListener(v -> {
            boolean isFav = comic.getStatus() != Constant.STATUS_FAV;
            setFavLayout(isFav, true);
            ComicUtil.removeComic(comic);
            comic.setStatus(isFav ? Constant.STATUS_FAV : Constant.STATUS_HIS);
            //Log.i(TAG, "setListener: " + comic);
            ComicUtil.first(comic);
            DBUtil.saveComic(comic, DBUtil.SAVE_CUR);
        });

        //开始阅读
        TextView tvRead = bottomView.findViewById(R.id.tvRead);
        tvRead.setOnClickListener(v -> {
            if (checkNotEmpty()) {
                int chapterId = ((ChapterAdapter) adapter).getChapterId();
                if (ComicHelper.checkChapterId(comic.getComicInfo(), chapterId)) {
                    ComicHelper.initChapterId(comic.getComicInfo(), chapterId);
                    start();
                } else {
                    start(ComicHelper.getPosition(comic.getComicInfo(), 0));
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
        ImageConfig config = ImgUtil.getDefaultConfig(getContext(), comic.getComicInfo().getImgUrl(), relativeLayout);
        config.setSave(true);
        config.setSaveKey(comic.getComicInfo().getId());
        ImgUtil.loadImage(getContext(), config);
        tvTitle.setText(comic.getComicInfo().getTitle());
        tvSource.setText(ComicHelper.sourceName(comic));
        tvSourceSize.setText(String.format(Locale.CHINA, "(%d)", ComicHelper.sourceSize(comic)));
        tvUpdateChapter.setText(comic.getComicInfo().getUpdateChapter());
        tvUpdateTime.setText(comic.getComicInfo().getUpdateTime());
        setFavLayout(comic.getStatus() == Constant.STATUS_FAV);
    }

    @Override
    protected void initAdapter() {
        super.initAdapter();
        adapter.getLoadMoreModule().setOnLoadMoreListener(null);
    }

    public void setFavLayout(boolean isFav) {
        setFavLayout(isFav, false);
    }

    public void setFavLayout(boolean isFav, boolean needAnimation) {
        if (isFav) {
            AnimationUtil.changeDrawable(ivFav, getDrawablee(R.drawable.ic_baseline_favorite_24), needAnimation);
            tvFav.setText("已收藏");
        } else {
            AnimationUtil.changeDrawable(ivFav, getDrawablee(R.drawable.ic_baseline_favorite_border_24), needAnimation);
            tvFav.setText("未收藏");
        }
    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        if (chapterAdapter == null) {
            chapterAdapter = new ChapterAdapter(R.layout.item_chapter, comic);
        }
        return chapterAdapter;
    }

    @Override
    protected void requestServer() {
        List<ChapterInfo> chapterInfoList = comic.getComicInfo().getChapterInfoList();
        //Log.i(TAG, "requestServer: Constant.toStatus = " + Constant.toStatus);
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
            comicInfo.setOrder(comicInfo.getOrder() == ComicInfo.ASC ? ComicInfo.DESC : ComicInfo.ASC);
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
        //Log.i(TAG, "onItemClick: position = " + position);
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
            if (TmpData.toStatus == Constant.RANK_TO_CHAPTER) {
                TmpData.toStatus = Constant.NORMAL;
                showProgressDialog("正在更新漫画源");
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
                return order != ComicInfo.DESC;
            } else if (firstId < lastId) {
                return order != ComicInfo.ASC;
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
        if (count == SourceUtil.size()) {
            count = 0;
            hideProgressDialog();
            showSuccessTips("搜索完毕");
            setValue();
            DBUtil.saveComic(comic, DBUtil.SAVE_ALL);
        } else {
            progressDialog.setMessage(getMsg("正在更新漫画源", count, SourceUtil.size()));
            progressDialog.setProgress(getValue(count, SourceUtil.size()), 100);
            tvSourceSize.setText(String.format(Locale.CHINA, "(%d)", ComicHelper.sourceSize(comic)));
        }
    }

    private int getValue(int count, int max) {
        if (max > 0) {
            return count * 100 / max;
        } else {
            return count;
        }
    }

    private String getMsg(String msg, int count, int max) {
        return String.format(Locale.CHINA, "%s %d/%d", msg, count, max);
    }

    private void updateComicInfo(ComicInfo info) {
        if (!comic.getTitle().equals(info.getTitle())) {
            return;
        }
        int index = comic.getComicInfoList().indexOf(info);
        if (index > -1) {
            ComicInfo oInfo = comic.getComicInfoList().remove(index);
            info.setCurChapterId(oInfo.getCurChapterId());
            info.setCurChapterTitle(oInfo.getCurChapterTitle());
        }
        ComicHelper.addComicInfo(comic, info);
    }

    public void start() {
        adapter.notifyDataSetChanged();
        startFragment(ReaderFragment.getInstance(comic));
    }

    public void start(int position) {
        ComicHelper.setPosition(comic.getComicInfo(), position);
        start();
    }

    private boolean checkNotEmpty() {
        return !comic.getComicInfo().getChapterInfoList().isEmpty();
    }
}
