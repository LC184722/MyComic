package com.qc.mynovel.ui.fragment;

import android.graphics.Bitmap;
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
import com.qc.common.self.MySpacesItemDecoration;
import com.qc.common.util.PopupUtil;
import com.qc.mycomic.R;
import com.qc.mynovel.ui.adapter.NChapterAdapter;
import com.qc.mynovel.ui.presenter.NChapterPresenter;
import com.qc.mynovel.ui.view.NChapterView;
import com.qc.mynovel.util.NovelHelper;
import com.qc.mynovel.util.NovelUtil;
import com.qc.mynovel.util.DBUtil;
import com.qc.common.util.ImgUtil;
import com.qmuiteam.qmui.qqface.QMUIQQFaceView;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIViewHelper;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;

import java.util.Date;
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
import top.luqichuang.common.util.NSourceUtil;
import top.luqichuang.common.util.StringUtil;
import top.luqichuang.mynovel.model.Novel;
import top.luqichuang.mynovel.model.NovelInfo;

/**
 * @author LuQiChuang
 * @desc 章节详情界面
 * @date 2020/8/12 15:18
 * @ver 1.0
 */
public class NChapterFragment extends BaseDataFragment<ChapterInfo> implements NChapterView {

    private Novel novel;

    private boolean isChangeOrder = false;

    private boolean isChangeSource = false;

    private NChapterPresenter presenter = new NChapterPresenter();

    public static NChapterFragment getInstance(Novel novel) {
        NChapterFragment fragment = new NChapterFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("novel", novel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        this.novel = (Novel) getArguments().get("novel");
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
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    private QMUIPopup mSettingPopup;
    private String[] mMenus = new String[]{"更新小说源", "查看信息"};
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
    private ImageView ivFavNot;
    private TextView tvFav;

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        QMUIQQFaceView mTitle = mTopLayout.setTitle("小说详情");
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
        ivFavNot = favLayout.findViewById(R.id.ivFavNot);
        tvFav = favLayout.findViewById(R.id.tvFav);
        setValue();
    }

    private void setListener() {
        //菜单按钮: 更新小说源、查看信息
        ibMenu.setOnClickListener(v -> {
            if (null == mSettingPopup) {
                mSettingPopup = QMUIPopupUtil.createListPop(_mActivity, mMenus, (adapter, view, position) -> {
                    if (position == 0) {
                        showProgressDialog(0, NSourceUtil.size(), "正在更新小说源");
                        presenter.updateNSource(novel);
                    } else if (position == 1) {
                        QMUIDialogUtil.showSimpleDialog(getContext(), "查看信息", NovelHelper.toStringView(novel)).show();
                    }
                    mSettingPopup.dismiss();
                });
            }
            mSettingPopup.show(ibMenu);
        });

        //更换小说顺序
        ibSwap.setOnClickListener(v -> {
            if (checkNotEmpty()) {
                isChangeOrder = true;
                changeView();
            } else {
                showFailTips("暂无小说章节");
            }
        });

        //改变小说源
        TextView tvSource = headerView.findViewById(R.id.tvSource);
        tvSource.setOnClickListener(v -> {
            Map<String, String> map = PopupUtil.getNMap(novel.getNovelInfoList());
            String key = PopupUtil.getNMapKey(novel);
            PopupUtil.showSimpleBottomSheetList(getContext(), map, key, "切换小说源", new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                @Override
                public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                    String key = MapUtil.getKeyByValue(map, tag);
                    String[] ss = key.split("-", 2);
                    int sourceId = Integer.parseInt(ss[0]);
                    String author = ss[1];
                    if (NovelHelper.changeNovelInfo(novel, sourceId, author)) {
                        showLoadingPage();
                        isChangeSource = true;
                        requestServer();
                        DBUtil.saveNovel(novel, DBUtil.SAVE_ONLY);
                    }
                    dialog.dismiss();
                }
            });
        });

        //阅读最新章节
        TextView tvUpdateChapter = headerView.findViewById(R.id.tvUpdateChapter);
        tvUpdateChapter.setOnClickListener(v -> {
            if (checkNotEmpty()) {
                NovelHelper.newestChapter(novel.getNovelInfo());
                start();
                DBUtil.saveNovel(novel, DBUtil.SAVE_CUR);
            } else {
                showFailTips("暂无小说章节");
            }
        });

        //收藏小说
        LinearLayout favLayout = bottomView.findViewById(R.id.favLayout);
        favLayout.setOnClickListener(v -> {
            boolean isFav = novel.getStatus() != Constant.STATUS_FAV;
            startAnimation(isFav);
            tvFav.setText(isFav ? "已收藏" : "未收藏");
            NovelUtil.removeNovel(novel);
            novel.setStatus(isFav ? Constant.STATUS_FAV : Constant.STATUS_HIS);
            //Log.i(TAG, "setListener: " + novel);
            NovelUtil.first(novel);
            DBUtil.saveNovel(novel, DBUtil.SAVE_ALL);
        });

        //开始阅读
        TextView tvRead = bottomView.findViewById(R.id.tvRead);
        tvRead.setOnClickListener(v -> {
            if (checkNotEmpty()) {
                int chapterId = ((NChapterAdapter) adapter).getChapterId();
                if (NovelHelper.checkChapterId(novel.getNovelInfo(), chapterId)) {
                    NovelHelper.initChapterId(novel.getNovelInfo(), chapterId);
                    start();
                } else {
                    start(NovelHelper.getPosition(novel.getNovelInfo(), 0));
                }
            } else {
                showFailTips("暂无小说章节");
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
        String saveKey = novel.getNovelInfo().getId() != 0 ? "N" + novel.getNovelInfo().getId() : null;
        if (novel.getNovelInfo().getImgUrl() != null) {
            ImgUtil.loadImage(getContext(), novel.getNovelInfo().getImgUrl(), relativeLayout, saveKey);
        } else {
            ImageView imageView = relativeLayout.findViewById(R.id.imageView);
            Bitmap bitmap = ImgUtil.drawableToBitmap(getDrawablee(R.drawable.ic_image_none));
            imageView.setImageBitmap(bitmap);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        tvTitle.setText(novel.getNovelInfo().getTitle());
        tvSource.setText(NovelHelper.nSourceName(novel));
        tvSourceSize.setText(String.format(Locale.CHINA, "(%d)", NovelHelper.nSourceSize(novel)));
        tvUpdateChapter.setText(novel.getNovelInfo().getUpdateChapter());
        tvUpdateTime.setText(novel.getNovelInfo().getUpdateTime());
        setFavLayout(novel.getStatus() == Constant.STATUS_FAV);
    }

    @Override
    protected void initAdapter() {
        super.initAdapter();
        adapter.getLoadMoreModule().setOnLoadMoreListener(null);
    }

    public void setFavLayout(boolean isFav) {
        if (isFav) {
            ivFavNot.setVisibility(View.GONE);
            ivFav.setVisibility(View.VISIBLE);
            tvFav.setText("已收藏");
        } else {
            ivFav.setVisibility(View.GONE);
            ivFavNot.setVisibility(View.VISIBLE);
            tvFav.setText("未收藏");
        }
    }

    public void startAnimation(boolean isFav) {
        View outView;
        View inView;
        if (isFav) {
            outView = ivFavNot;
            inView = ivFav;
        } else {
            outView = ivFav;
            inView = ivFavNot;
        }
        QMUIViewHelper.fadeOut(outView, 200, new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                QMUIViewHelper.fadeIn(inView, 200, null, true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        }, true);
    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new NChapterAdapter(R.layout.item_chapter, novel);
    }

    @Override
    protected void requestServer() {
        List<ChapterInfo> chapterInfoList = novel.getNovelInfo().getChapterInfoList();
        //Log.i(TAG, "requestServer: Constant.toStatus = " + Constant.toStatus);
        if (chapterInfoList == null || chapterInfoList.size() == 0) {
            presenter.load(novel);
        } else {
            loadComplete();
        }
    }

    private void changeView() {
        showLoadingPage();
        if (isChangeOrder) {
            isChangeOrder = false;
            NovelInfo novelInfo = novel.getNovelInfo();
            StringUtil.swapList(novelInfo.getChapterInfoList());
            novelInfo.setOrder(novelInfo.getOrder() == NovelInfo.ASC ? NovelInfo.DESC : NovelInfo.ASC);
            adapter.notifyDataSetChanged();
            DBUtil.saveNovelInfo(novelInfo);
        }
        if (isChangeSource) {
            isChangeSource = false;
            onFirstComplete(novel.getNovelInfo().getChapterInfoList());
            setValue();
            adapter.notifyDataSetChanged();
        }
        showContentPage();
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view,
                            int position) {
        //Log.i(TAG, "onItemClick: position = " + position);
        NovelUtil.first(novel);
        start(position);
        DBUtil.saveNovel(novel, DBUtil.SAVE_ONLY);
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
            List<ChapterInfo> list = novel.getNovelInfo().getChapterInfoList();
            if (isNeedSwap(list, novel.getNovelInfo().getOrder())) {
                StringUtil.swapList(list);
            }
            setValue();
            onFirstComplete(list);
            adapter.notifyDataSetChanged();
            if (TmpData.toStatus == Constant.RANK_TO_CHAPTER) {
                TmpData.toStatus = Constant.NORMAL;
                showProgressDialog("正在更新小说源");
                presenter.updateNSource(novel);
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
                return order != NovelInfo.DESC;
            } else if (firstId < lastId) {
                return order != NovelInfo.ASC;
            } else {
                return false;
            }
        }
        return false;
    }

    private int count = 0;

    @Override
    public void updateNSourceComplete(List<NovelInfo> infoList) {
        count++;
        if (infoList != null) {
            for (NovelInfo info : infoList) {
                updateNovelInfo(info);
            }
        }
        if (count == NSourceUtil.size()) {
            count = 0;
            hideProgressDialog();
            showSuccessTips("搜索完毕");
            setValue();
            DBUtil.saveNovel(novel, DBUtil.SAVE_ALL);
        } else {
            progressDialog.setMessage(getMsg("正在更新小说源", count, NSourceUtil.size()));
            progressDialog.setProgress(getValue(count, NSourceUtil.size()), 100);
            tvSourceSize.setText(String.format(Locale.CHINA, "(%d)", NovelHelper.nSourceSize(novel)));
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

    private void updateNovelInfo(NovelInfo info) {
        if (!novel.getTitle().equals(info.getTitle())) {
            return;
        }
        int index = novel.getNovelInfoList().indexOf(info);
        if (index > -1) {
            NovelInfo oInfo = novel.getNovelInfoList().remove(index);
            info.setCurChapterId(oInfo.getCurChapterId());
            info.setCurChapterTitle(oInfo.getCurChapterTitle());
        }
        NovelHelper.addNovelInfo(novel, info);
    }

    public void start() {
        adapter.notifyDataSetChanged();
        startFragment(NReaderFragment.getInstance(novel));
    }

    public void start(int position) {
        NovelHelper.setPosition(novel.getNovelInfo(), position);
        start();
    }

    private boolean checkNotEmpty() {
        return !novel.getNovelInfo().getChapterInfoList().isEmpty();
    }
}
