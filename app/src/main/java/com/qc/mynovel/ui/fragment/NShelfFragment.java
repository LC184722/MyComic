package com.qc.mynovel.ui.fragment;

import android.view.Gravity;
import android.view.View;

import com.qc.mycomic.R;
import com.qc.common.constant.Constant;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.qqface.QMUIQQFaceView;
import com.qmuiteam.qmui.util.QMUIColorHelper;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;

import java.util.ArrayList;

import the.one.base.ui.fragment.BaseFragment;
import the.one.base.ui.fragment.BaseTitleTabFragment;
import the.one.base.util.QMUIPopupUtil;

/**
 * @author LuQiChuang
 * @desc 书架基础界面
 * @date 2020/8/12 15:31
 * @ver 1.0
 */
public class NShelfFragment extends BaseTitleTabFragment {

    private QMUIQQFaceView mTitle;
    private QMUIAlphaImageButton mSettingIcon;
    private QMUIPopup mSettingPopup;

    @Override
    protected boolean isAdjustMode() {
        return true;
    }

    @Override
    protected boolean showElevation() {
        return true;
    }

    @Override
    protected boolean isFoldTitleBar() {
        return true;
    }

    @Override
    protected void onScrollChanged(float percent) {
        mTitle.setTextColor(QMUIColorHelper.setColorAlpha(getColorr(R.color.qmui_config_color_gray_1), percent));
        mSettingIcon.setAlpha(percent);
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mTitle = mTopLayout.setTitle("我的书架");
        mTopLayout.setNeedChangedWithTheme(false);
        mTopLayout.setTitleGravity(Gravity.CENTER);
        mTitle.setTextColor(getColorr(R.color.qmui_config_color_gray_1));
        mTitle.getPaint().setFakeBoldText(true);

        mSettingIcon = mTopLayout.addRightImageButton(R.drawable.ic_baseline_menu_24, R.id.topbar_right_button1);
        mSettingIcon.setOnClickListener(v -> {
            showSettingPopup();
        });
    }

    private String[] mMenus = new String[]{
            "检查更新",
            "筛选小说",
            "取消筛选",
            "导入小说",
//            "Test",
//            "Test2",
    };

    private void showSettingPopup() {
        if (null == mSettingPopup) {
            mSettingPopup = QMUIPopupUtil.createListPop(_mActivity, mMenus, (adapter, view, position) -> {
                NShelfItemFragment fragment = (NShelfItemFragment) fragments.get(INDEX);
                if (position == 0) {
                    fragment.startCheckUpdate();
                } else if (position == 1) {
                    fragment.screen(true);
                } else if (position == 2) {
                    fragment.screen(false);
                } else if (position == 3) {
                    fragment.importInfo();
                } else if (position == 4) {
                }
                mSettingPopup.dismiss();
            });
        }
        mSettingPopup.show(mSettingIcon);
    }

    @Override
    protected void addTabs() {
        addTab("收藏小说");
        addTab("历史小说");
    }

    @Override
    protected void addFragment(ArrayList<BaseFragment> fragments) {
        fragments.add(NShelfItemFragment.getInstance(Constant.STATUS_FAV));
        fragments.add(NShelfItemFragment.getInstance(Constant.STATUS_HIS));
    }

}
