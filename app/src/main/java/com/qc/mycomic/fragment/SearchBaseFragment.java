package com.qc.mycomic.fragment;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.qc.mycomic.R;
import com.qc.mycomic.model.Source;
import com.qc.mycomic.util.SourceUtil;
import com.qc.mycomic.util.Utils;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.qqface.QMUIQQFaceView;

import java.util.ArrayList;
import java.util.List;

import the.one.base.ui.fragment.BaseFragment;
import the.one.base.ui.fragment.BaseTitleTabFragment;
import the.one.base.widge.TheSearchView;

public class SearchBaseFragment extends BaseTitleTabFragment {

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        QMUIQQFaceView mTitle = mTopLayout.setTitle("搜索漫画");
        mTopLayout.setNeedChangedWithTheme(false);
        mTopLayout.setTitleGravity(Gravity.CENTER);
        mTitle.setTextColor(getColorr(R.color.qmui_config_color_gray_1));
        mTitle.getPaint().setFakeBoldText(true);

        QMUIAlphaImageButton ibSearch = mTopLayout.addRightImageButton(R.drawable.ic_baseline_search_24, R.id.topbar_right_button1);
        ibSearch.setOnClickListener(v -> {
            startFragment(new SearchFragment());
        });
    }

    @Override
    protected void addTabs() {
        List<Source> sourceList = SourceUtil.getSourceList();
        for (Source source : sourceList) {
            addTab(source.getSourceName());
        }
    }

    @Override
    protected void addFragment(ArrayList<BaseFragment> fragments) {
        List<Source> sourceList = SourceUtil.getSourceList();
        for (Source source : sourceList) {
            fragments.add(new RankFragment(source));
        }
    }
}