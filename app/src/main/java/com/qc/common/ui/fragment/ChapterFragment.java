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

import androidx.annotation.Nullable;

import com.qc.common.constant.AppConstant;
import com.qc.common.constant.Constant;
import com.qc.common.constant.TmpData;
import com.qc.common.self.ImageConfig;
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
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import the.one.base.ui.fragment.BaseFragment;
import the.one.base.ui.fragment.BaseTabFragment;
import the.one.base.util.QMUIDialogUtil;
import the.one.base.util.QMUIPopupUtil;
import top.luqichuang.common.model.ChapterInfo;
import top.luqichuang.common.model.Entity;
import top.luqichuang.common.model.EntityInfo;
import top.luqichuang.common.util.MapUtil;
import top.luqichuang.common.util.SourceUtil;
import top.luqichuang.common.util.StringUtil;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/24 12:27
 * @ver 1.0
 */
public class ChapterFragment extends BaseTabFragment implements ChapterView {

    private Entity entity;
    private boolean isChangeOrder = false;
    private boolean isChangeSource = false;
    private ChapterPresenter presenter = new ChapterPresenter();
    private int toStatus = Constant.NORMAL;
    private int size;

    private QMUIPopup mSettingPopup;
    private String[] mMenus = new String[]{
            "更新" + TmpData.content + "源",
            "查看信息",
    };
    private ImageButton ibMenu;
    private ImageButton ibSwap;

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
    private View llIndicator;

    public static ChapterFragment getInstance(Entity entity) {
        ChapterFragment fragment = new ChapterFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("entity", entity);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter && entity != null && !fragments.isEmpty()) {
            ((ChapterItemFragment) fragments.get(INDEX)).updateData();
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        this.entity = (Entity) getArguments().get("entity");
        if (TmpData.contentCode == AppConstant.COMIC_CODE) {
            size = SourceUtil.size();
        } else if (TmpData.contentCode == AppConstant.READER_CODE) {
            size = SourceUtil.nSize();
        } else {
            size = SourceUtil.vSize();
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected boolean isTabFromNet() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        showLoadingPage();
        mMagicIndicator = rootView.findViewById(R.id.indicator);
        mViewPager = rootView.findViewById(R.id.viewPager);

        QMUIQQFaceView mTitle = mTopLayout.setTitle("" + TmpData.content + "详情");
        mTopLayout.setTitleGravity(Gravity.CENTER);
        mTitle.setTextColor(getColorr(R.color.qmui_config_color_gray_1));
        mTitle.getPaint().setFakeBoldText(true);
        addTopBarBackBtn();
    }

    private void setListener() {
        //菜单按钮: 更新源、查看信息
        ibMenu.setOnClickListener(v -> {
            if (null == mSettingPopup) {
                mSettingPopup = QMUIPopupUtil.createListPop(_mActivity, mMenus, (adapter, view, position) ->
                {
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
        TextView tvSource = mRootView.findViewById(R.id.tvSource);
        tvSource.setOnClickListener(v -> {
            Map<String, String> map = PopupUtil.getMap(entity.getInfoList());
            String key = PopupUtil.getKey(entity);
            PopupUtil.showSimpleBottomSheetList(getContext(), map, key, "切换" + TmpData.content + "源", new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                @Override
                public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                    String key = MapUtil.getKeyByValue(map, tag);
                    String[] ss = key.split("#");
                    if (EntityHelper.changeInfo(entity, ss)) {
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
        TextView tvUpdateChapter = mRootView.findViewById(R.id.tvUpdateChapter);
        tvUpdateChapter.setOnClickListener(v -> {
            if (checkNotEmpty()) {
                EntityHelper.newestChapter(entity.getInfo());
                ((ChapterItemFragment) fragments.get(INDEX)).start();
            } else {
                showFailTips("暂无" + TmpData.content + "章节");
            }
        });

        //收藏
        LinearLayout favLayout = mRootView.findViewById(R.id.favLayout);
        favLayout.setOnClickListener(v -> {
            boolean isFav = entity.getStatus() != Constant.STATUS_FAV;
            setFavLayout(isFav, true);
            EntityUtil.removeEntity(entity);
            entity.setStatus(isFav ? Constant.STATUS_FAV : Constant.STATUS_HIS);
            //Log.i(TAG, "setListener: " + entity);
            EntityUtil.first(entity);
        });

        //开始阅读
        TextView tvRead = mRootView.findViewById(R.id.tvRead);
        tvRead.setOnClickListener(v -> {
            if (checkNotEmpty()) {
                ((ChapterItemFragment) fragments.get(INDEX)).startRead();
            } else {
                showFailTips("暂无" + TmpData.content + "章节");
            }
        });
    }

    private void addView() {
        ibMenu = mTopLayout.addRightImageButton(R.drawable.ic_baseline_menu_24, R.id.topbar_right_button1);
        ibSwap = mTopLayout.addRightImageButton(R.drawable.ic_baseline_swap_vert_24, R.id.topbar_right_button2);
        relativeLayout = mRootView.findViewById(R.id.imageRelativeLayout);
        imageView = mRootView.findViewById(R.id.imageView);
        tvTitle = mRootView.findViewById(R.id.tvTitle);
        tvSource = mRootView.findViewById(R.id.tvSource);
        tvSourceSize = mRootView.findViewById(R.id.tvSourceSize);
        tvUpdateTime = mRootView.findViewById(R.id.tvUpdateTime);
        tvUpdateChapter = mRootView.findViewById(R.id.tvUpdateChapter);
        favLayout = mRootView.findViewById(R.id.favLayout);
        ivFav = favLayout.findViewById(R.id.ivFav);
        tvFav = favLayout.findViewById(R.id.tvFav);
        llIndicator = mRootView.findViewById(R.id.llIndicator);
    }

    private void setValue() {
        ImageConfig config = ImgUtil.getDefaultConfig(getContext(), entity.getInfo().getImgUrl(), relativeLayout);
        config.setSave(true);
        if (entity.getInfoId() == 0) {
            config.setSaveKey(null);
        } else {
            if (TmpData.contentCode == AppConstant.COMIC_CODE) {
                config.setSaveKey(entity.getInfoId());
            } else if (TmpData.contentCode == AppConstant.READER_CODE) {
                config.setSaveKey("N" + entity.getInfoId());
            } else {
                config.setSaveKey("V" + entity.getInfoId());
            }
        }
        ImgUtil.loadImage(getContext(), config);
        tvTitle.setText(entity.getInfo().getTitle());
        tvSource.setText(EntityHelper.sourceName(entity));
        tvSourceSize.setText(String.format(Locale.CHINA, "(%d)", EntityHelper.sourceSize(entity)));
        tvUpdateChapter.setText(entity.getInfo().getUpdateChapter());
        tvUpdateTime.setText(entity.getInfo().getUpdateTime());
        setFavLayout(entity.getStatus() == Constant.STATUS_FAV);
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

    private void changeView() {
        showLoadingPage();
        if (isChangeOrder) {
            isChangeOrder = false;
            EntityInfo entityInfo = entity.getInfo();
            StringUtil.swapList(entityInfo.getChapterInfoList());
            entityInfo.setOrder(entityInfo.getOrder() == EntityInfo.ASC ? EntityInfo.DESC : EntityInfo.ASC);
            ((ChapterItemFragment) fragments.get(INDEX)).updateData();
            DBUtil.saveInfoData(entityInfo);
        }
        showContentPage();
    }

    private boolean firstLoad = true;

    @Override
    protected void requestServer() {
        showLoadingPage();
        Map<String, List<ChapterInfo>> map = entity.getInfo().getChapterInfoMap();
        if (map == null || map.size() == 0) {
            presenter.load(entity);
        } else {
            loadComplete();
        }
    }

    @Override
    public ChapterPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected void startInit() {
        mTabs.clear();
        super.startInit();
        Map<String, List<ChapterInfo>> map = entity.getInfo().getChapterInfoMap();
        int i = 0;
        for (List<ChapterInfo> list : map.values()) {
            ChapterItemFragment fragment = (ChapterItemFragment) fragments.get(i++);
            fragment.setList(list);
        }
    }

    @Override
    public void loadComplete() {
        try {
            List<ChapterInfo> list = entity.getInfo().getChapterInfoList();
            if (isNeedSwap(list, entity.getInfo().getOrder())) {
                StringUtil.swapList(list);
            }
            if (firstLoad) {
                firstLoad = false;
                addView();
                setListener();
            }
            setValue();
            startInit();
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
            info.setId(oInfo.getId());
            info.setCurChapterId(oInfo.getCurChapterId());
            info.setCurChapterTitle(oInfo.getCurChapterTitle());
        }
        EntityHelper.addInfo(entity, info);
    }

    private boolean checkNotEmpty() {
        return !entity.getInfo().getChapterInfoList().isEmpty();
    }

    @Override
    protected void addTabs() {
        Map<String, List<ChapterInfo>> map = entity.getInfo().getChapterInfoMap();
        if (map.size() == 1 && map.containsKey("正文")) {
            llIndicator.setVisibility(View.GONE);
        } else {
            llIndicator.setVisibility(View.VISIBLE);
            for (String key : entity.getInfo().getChapterInfoMap().keySet()) {
                addTab(key);
            }
        }
    }

    @Override
    protected void addFragment(ArrayList<BaseFragment> fragments) {
        int mapSize = entity.getInfo().getChapterInfoMap().size();
        int fSize = fragments.size();
        if (mapSize > fSize) {
            for (int i = 0; i < mapSize - fSize; i++) {
                ChapterItemFragment fragment = ChapterItemFragment.getInstance(entity);
                fragments.add(fragment);
            }
        } else if (mapSize < fSize) {
            if (fSize > mapSize + 1) {
                fragments.subList(mapSize, fSize).clear();
            }
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_chapter;
    }
}
