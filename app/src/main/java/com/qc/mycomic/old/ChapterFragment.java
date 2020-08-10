//package com.qc.mycomic.old;
//
//import android.util.Log;
//import android.view.Gravity;
//import android.view.View;
//import android.widget.TextView;
//
//import com.qc.mycomic.R;
//import com.qc.mycomic.model.Comic;
//import com.qmuiteam.qmui.qqface.QMUIQQFaceView;
//import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
//
//import java.util.ArrayList;
//
//import the.one.base.ui.fragment.BaseFragment;
//import the.one.base.ui.fragment.BaseTitleTabFragment;
//import the.one.base.util.glide.GlideEngine;
//
//public class ChapterFragment extends BaseTitleTabFragment {
//
//    private QMUIQQFaceView mTitle;
//
//    private Comic comic;
//
//    public ChapterFragment(Comic comic) {
//        this.comic = comic;
//        Log.i(TAG, "ChapterFragment: " + comic);
//    }
//
//    @Override
//    protected boolean isAdjustMode() {
//        return true;
//    }
//
//    @Override
//    protected boolean showTitleBar() {
//        return true;
//    }
//
//    @Override
//    protected boolean showElevation() {
//        return true;
//    }
//
//    @Override
//    protected boolean isNeedAround() {
//        return true;
//    }
//
//    @Override
//    protected Object getTopLayout() {
//        return getView(R.layout.fragment_chapter);
//    }
//
//    @Override
//    protected void initView(View rootView) {
//        super.initView(rootView);
//        Log.i("TAG", "initView: perform");
//        mTitle = mTopLayout.setTitle("漫画详情");
//        mTopLayout.setTitleGravity(Gravity.CENTER);
//        mTitle.setTextColor(getColorr(R.color.qmui_config_color_gray_1));
//        mTitle.getPaint().setFakeBoldText(true);
//        addTopBarBackBtn();
//        TextView tvTitle = findViewByTopView(R.id.tvTitle);
//        TextView tvSource = findViewByTopView(R.id.tvSource);
//        TextView tvUpdateTime = findViewByTopView(R.id.tvUpdateTime);
//        tvTitle.setText(comic.getComicInfo().getTitle());
//        tvSource.setText("漫画源：" + comic.getSource().getSourceName());
//        tvUpdateTime.setText("更新时间：" + comic.getComicInfo().getUpdateTime());
//        QMUIRadiusImageView qivImg = findViewByTopView(R.id.qivImg);
//        GlideEngine.createGlideEngine().loadImage(getContext(), comic.getComicInfo().getImgUrl(), qivImg);
//    }
//
//    @Override
//    protected void addTabs() {
//        addTab("详情");
//        addTab("章节");
//    }
//
//    @Override
//    protected void addFragment(ArrayList<BaseFragment> fragments) {
//        fragments.add(new ChapterDetailFragment(comic));
//        fragments.add(new ChapterMainFragment(comic));
//    }
//
//}
