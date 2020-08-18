package com.qc.mycomic.ui.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.qc.mycomic.R;
import com.qc.mycomic.model.Comic;
import com.qc.mycomic.ui.activity.LauncherActivity;
import com.qc.mycomic.util.Codes;
import com.qc.mycomic.util.DBUtil;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.qqface.QMUIQQFaceView;
import com.qmuiteam.qmui.util.QMUIColorHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import the.one.base.ui.fragment.BaseFragment;
import the.one.base.ui.fragment.BaseTitleTabFragment;
import the.one.base.util.QMUIPopupUtil;
import the.one.base.util.crash.CrashConfig;
import the.one.base.util.crash.CrashUtil;

/**
 * @author LuQiChuang
 * @desc 漫画书架基础界面
 * @date 2020/8/12 15:31
 * @ver 1.0
 */
public class ShelfFragment extends BaseTitleTabFragment {

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
        mTitle = mTopLayout.setTitle("我的画架");
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
            "筛选漫画",
            "取消筛选",
//            "Test",
//            "Test2",
    };

    private void showSettingPopup() {
        if (null == mSettingPopup) {
            mSettingPopup = QMUIPopupUtil.createListPop(_mActivity, mMenus, (adapter, view, position) -> {
                ShelfItemFragment fragment = (ShelfItemFragment) fragments.get(INDEX);
                if (position == 0) {
                    fragment.startCheckUpdate();
                } else if (position == 1) {
                    fragment.screen(true);
                } else if (position == 2) {
                    fragment.screen(false);
                } else if (position == 3) {
                    List<Comic> comicList = fragment.getAdapter().getData();
                    Random random = new Random();
                    for (Comic comic : comicList) {
                        if (random.nextBoolean()) {
                            comic.getComicInfo().setUpdateChapter("测试新章节");
//                            DBUtil.saveComicInfo(comic.getComicInfo());
                        }
                    }
                    fragment.getAdapter().notifyDataSetChanged();
//                    boolean flag = QMUIStatusBarHelper.setStatusBarDarkMode(getBaseFragmentActivity());
//                    mTopLayout.setBackgroundColor(getColorr(R.color.black));
//                    Log.i(TAG, "showSettingPopup: darkMode " + flag);
                } else if (position == 4) {
                    Intent intent = new Intent(_mActivity, LauncherActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    _mActivity.startActivity(intent);
                    android.os.Process.killProcess(android.os.Process.myPid());
//                    boolean flag = QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
//                    Log.i(TAG, "showSettingPopup: lightMode " + flag);
                }
                mSettingPopup.dismiss();
            });
        }
        mSettingPopup.show(mSettingIcon);
    }

    @Override
    protected void addTabs() {
        addTab("收藏漫画");
        addTab("历史漫画");
    }

    @Override
    protected void addFragment(ArrayList<BaseFragment> fragments) {
        fragments.add(new ShelfItemFragment(Codes.STATUS_FAV));
        fragments.add(new ShelfItemFragment(Codes.STATUS_HIS));
    }

}
