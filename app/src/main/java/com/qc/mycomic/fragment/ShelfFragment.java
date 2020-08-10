package com.qc.mycomic.fragment;

import android.content.DialogInterface;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qc.mycomic.R;
import com.qc.mycomic.model.Comic;
import com.qc.mycomic.model.ComicInfo;
import com.qc.mycomic.model.Source;
import com.qc.mycomic.presenter.ShelfPresenter;
import com.qc.mycomic.source.MiTui;
import com.qc.mycomic.util.Codes;
import com.qc.mycomic.util.DBUtil;
import com.qc.mycomic.util.TipUtil;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.qqface.QMUIQQFaceView;
import com.qmuiteam.qmui.util.QMUIColorHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import the.one.base.ui.activity.BaseWebExplorerActivity;
import the.one.base.ui.fragment.BaseFragment;
import the.one.base.ui.fragment.BaseTitleTabFragment;
import the.one.base.util.QMUIDialogUtil;
import the.one.base.util.QMUIPopupUtil;

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
//            "Test"
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
                            comic.getComicInfo().setUpdateChapter("???");
                            System.out.println("comic = " + comic);
                            DBUtil.saveData(comic.getComicInfo());
                        }
                    }
                    fragment.getAdapter().notifyDataSetChanged();
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
