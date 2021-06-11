package com.qc.common.ui.fragment;

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
import com.qc.common.constant.AppConstant;
import com.qc.common.constant.Constant;
import com.qc.common.constant.TmpData;
import com.qc.common.self.ImageConfig;
import com.qc.common.self.MySpacesItemDecoration;
import com.qc.common.ui.adapter.ChapterAdapter;
import com.qc.common.ui.presenter.ChapterPresenter;
import com.qc.common.ui.view.ChapterView;
import com.qc.common.util.AnimationUtil;
import com.qc.common.util.DBUtil;
import com.qc.common.util.EntityHelper;
import com.qc.common.util.EntityUtil;
import com.qc.common.util.ImgUtil;
import com.qc.common.util.PopupUtil;
import com.qc.mycomic.R;
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
import top.luqichuang.common.model.Entity;
import top.luqichuang.common.model.EntityInfo;
import top.luqichuang.common.util.MapUtil;
import top.luqichuang.common.util.SourceUtil;
import top.luqichuang.common.util.StringUtil;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/10 17:44
 * @ver 1.0
 */
public class ChapterFragment extends BaseDataFragment<ChapterInfo> implements ChapterView {

    private Entity entity;

    private boolean isChangeOrder = false;

    private boolean isChangeSource = false;

    private ChapterPresenter presenter = new ChapterPresenter();

    private ChapterAdapter chapterAdapter;

    private int toStatus = Constant.NORMAL;

    private int size;

    public static ChapterFragment getInstance(Entity entity) {
        ChapterFragment fragment = new ChapterFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("entity", entity);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        this.entity = (Entity) getArguments().get("entity");
        this.chapterAdapter = new ChapterAdapter(entity);
        if (TmpData.contentCode == AppConstant.COMIC_CODE) {
            size = SourceUtil.size();
        } else {
            size = SourceUtil.nSize();
        }
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
            chapterAdapter.setChapterId(entity.getInfo().getCurChapterId());
            setValue();
        }
        this.toStatus = TmpData.toStatus;
        TmpData.toStatus = Constant.NORMAL;
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    private QMUIPopup mSettingPopup;
    private String[] mMenus = new String[]{
            "更新" + TmpData.content + "源",
            "查看信息",
    };
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
        QMUIQQFaceView mTitle = mTopLayout.setTitle("" + TmpData.content + "详情");
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
        //菜单按钮: 更新源、查看信息
        ibMenu.setOnClickListener(v -> {
            if (null == mSettingPopup) {
                mSettingPopup = QMUIPopupUtil.createListPop(_mActivity, mMenus, (adapter, view, position) -> {
                    if (position == 0) {
                        showProgressDialog(0, size, "正在更新" + TmpData.content + "源");
                        presenter.updateSource(entity);
                    } else if (position == 1) {
                        QMUIDialogUtil.showSimpleDialog(getContext(), "查看信息", EntityHelper.toStringView(entity)).show();
                    }
                    mSettingPopup.dismiss();
                });
            }
            mSettingPopup.show(ibMenu);
        });

        //更换顺序
        ibSwap.setOnClickListener(v -> {
            if (checkNotEmpty()) {
                isChangeOrder = true;
                changeView();
            } else {
                showFailTips("暂无" + TmpData.content + "章节");
            }
        });

        //改变源
        TextView tvSource = headerView.findViewById(R.id.tvSource);
        tvSource.setOnClickListener(v -> {
            Map<String, String> map = PopupUtil.getMap(entity.getInfoList());
            String key = PopupUtil.getKey(entity);
            PopupUtil.showSimpleBottomSheetList(getContext(), map, key, "切换" + TmpData.content + "源", new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                @Override
                public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                    String key = MapUtil.getKeyByValue(map, tag);
                    if (TmpData.contentCode == AppConstant.COMIC_CODE) {
                        int sourceId = Integer.parseInt(key);
                        entity.setSourceId(sourceId);
                    } else {
                        String[] ss = key.split("-", 2);
                        int sourceId = Integer.parseInt(ss[0]);
                        String author = ss[1];
                        entity.setSourceId(sourceId);
                        entity.setAuthor(author);
                    }
                    if (EntityHelper.changeInfo(entity)) {
                        showLoadingPage();
                        isChangeSource = true;
                        requestServer();
                        DBUtil.save(entity, DBUtil.SAVE_ONLY);
                    }
                    dialog.dismiss();
                }
            });
        });

        //阅读最新章节
        TextView tvUpdateChapter = headerView.findViewById(R.id.tvUpdateChapter);
        tvUpdateChapter.setOnClickListener(v -> {
            if (checkNotEmpty()) {
                EntityHelper.newestChapter(entity.getInfo());
                start();
            } else {
                showFailTips("暂无" + TmpData.content + "章节");
            }
        });

        //收藏
        LinearLayout favLayout = bottomView.findViewById(R.id.favLayout);
        favLayout.setOnClickListener(v -> {
            boolean isFav = entity.getStatus() != Constant.STATUS_FAV;
            setFavLayout(isFav, true);
            EntityUtil.removeEntity(entity);
            entity.setStatus(isFav ? Constant.STATUS_FAV : Constant.STATUS_HIS);
            //Log.i(TAG, "setListener: " + entity);
            EntityUtil.first(entity);
        });

        //开始阅读
        TextView tvRead = bottomView.findViewById(R.id.tvRead);
        tvRead.setOnClickListener(v -> {
            if (checkNotEmpty()) {
                int chapterId = ((ChapterAdapter) adapter).getChapterId();
                if (EntityHelper.checkChapterId(entity.getInfo(), chapterId)) {
                    EntityHelper.initChapterId(entity.getInfo(), chapterId);
                    start();
                } else {
                    start(EntityHelper.getPosition(entity.getInfo(), 0));
                }
            } else {
                showFailTips("暂无" + TmpData.content + "章节");
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
        ImageConfig config = ImgUtil.getDefaultConfig(getContext(), entity.getInfo().getImgUrl(), relativeLayout);
        config.setSave(true);
        config.setSaveKey(entity.getInfo().getId());
        ImgUtil.loadImage(getContext(), config);
        tvTitle.setText(entity.getInfo().getTitle());
        tvSource.setText(EntityHelper.sourceName(entity));
        tvSourceSize.setText(String.format(Locale.CHINA, "(%d)", EntityHelper.sourceSize(entity)));
        tvUpdateChapter.setText(entity.getInfo().getUpdateChapter());
        tvUpdateTime.setText(entity.getInfo().getUpdateTime());
        setFavLayout(entity.getStatus() == Constant.STATUS_FAV);
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
        return chapterAdapter;
    }

    @Override
    protected void requestServer() {
        List<ChapterInfo> chapterInfoList = entity.getInfo().getChapterInfoList();
        //Log.i(TAG, "requestServer: Constant.toStatus = " + Constant.toStatus);
        if (chapterInfoList == null || chapterInfoList.size() == 0) {
            presenter.load(entity);
        } else {
            loadComplete();
        }
    }

    private void changeView() {
        showLoadingPage();
        if (isChangeOrder) {
            isChangeOrder = false;
            EntityInfo entityInfo = entity.getInfo();
            StringUtil.swapList(entityInfo.getChapterInfoList());
            entityInfo.setOrder(entityInfo.getOrder() == EntityInfo.ASC ? EntityInfo.DESC : EntityInfo.ASC);
            adapter.notifyDataSetChanged();
            DBUtil.saveInfoData(entityInfo);
        }
        if (isChangeSource) {
            isChangeSource = false;
            onFirstComplete(entity.getInfo().getChapterInfoList());
            setValue();
            adapter.notifyDataSetChanged();
        }
        showContentPage();
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view,
                            int position) {
        //Log.i(TAG, "onItemClick: position = " + position);
        start(position);
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
            List<ChapterInfo> list = entity.getInfo().getChapterInfoList();
            if (isNeedSwap(list, entity.getInfo().getOrder())) {
                StringUtil.swapList(list);
            }
            setValue();
            onFirstComplete(list);
            adapter.notifyDataSetChanged();
            if (toStatus == Constant.RANK_TO_CHAPTER) {
                toStatus = Constant.NORMAL;
                showProgressDialog("正在更新" + TmpData.content + "源");
                presenter.updateSource(entity);
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
                return order != EntityInfo.DESC;
            } else if (firstId < lastId) {
                return order != EntityInfo.ASC;
            } else {
                return false;
            }
        }
        return false;
    }

    private int count = 0;

    @Override
    public void updateSourceComplete(List<EntityInfo> infoList) {
        count++;
        if (infoList != null) {
            for (EntityInfo info : infoList) {
                updateEntityInfo(info);
            }
        }
        if (count == size) {
            count = 0;
            hideProgressDialog();
            showSuccessTips("搜索完毕");
            setValue();
            DBUtil.save(entity, DBUtil.SAVE_ALL);
        } else {
            progressDialog.setMessage(getMsg("正在更新" + TmpData.content + "源", count, size));
            progressDialog.setProgress(getValue(count, size), 100);
            tvSourceSize.setText(String.format(Locale.CHINA, "(%d)", EntityHelper.sourceSize(entity)));
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

    private void updateEntityInfo(EntityInfo info) {
        if (!entity.getTitle().equals(info.getTitle())) {
            return;
        }
        int index = entity.getInfoList().indexOf(info);
        if (index > -1) {
            EntityInfo oInfo = entity.getInfoList().remove(index);
            info.setCurChapterId(oInfo.getCurChapterId());
            info.setCurChapterTitle(oInfo.getCurChapterTitle());
        }
        EntityHelper.addInfo(entity, info);
    }

    public void start() {
        adapter.notifyDataSetChanged();
        if (TmpData.contentCode == AppConstant.COMIC_CODE) {
            startFragment(ComicReaderFragment.getInstance(entity));
        } else {
            startFragment(NovelReaderFragment.getInstance(entity));
        }
        EntityUtil.first(entity);
        if (toStatus == Constant.SEARCH_TO_CHAPTER) {
            DBUtil.save(entity, DBUtil.SAVE_ALL);
        }
    }

    public void start(int position) {
        EntityHelper.setPosition(entity.getInfo(), position);
        start();
    }

    private boolean checkNotEmpty() {
        return !entity.getInfo().getChapterInfoList().isEmpty();
    }
}