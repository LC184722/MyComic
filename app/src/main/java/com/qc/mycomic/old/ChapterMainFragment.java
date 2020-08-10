//package com.qc.mycomic.old;
//
//import android.view.View;
//
//import androidx.annotation.NonNull;
//
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.qc.mycomic.R;
//import com.qc.mycomic.adapter.ChapterAdapter;
//import com.qc.mycomic.model.ChapterInfo;
//import com.qc.mycomic.model.Comic;
//import com.qc.mycomic.presenter.ChapterPresenter;
//import com.qc.mycomic.view.ChapterView;
//
//import the.one.base.ui.fragment.BaseDataFragment;
//import the.one.base.ui.presenter.BasePresenter;
//
//public class ChapterMainFragment extends BaseDataFragment<ChapterInfo> implements ChapterView {
//
//    private ChapterPresenter presenter = new ChapterPresenter();
//
//    private Comic comic;
//
//    public ChapterMainFragment(Comic comic) {
//        this.comic = comic;
//    }
//
//    @Override
//    protected boolean showTitleBar() {
//        return false;
//    }
//
//    @Override
//    protected BaseQuickAdapter getAdapter() {
//        return new ChapterAdapter(R.layout.item_chapter_main);
//    }
//
//    private void initChapter() {
//        if (comic.getComicInfo().getChapterInfoList() != null) {
//            onComplete(comic.getComicInfo().getChapterInfoList());
//            showContentPage();
//            adapter.getLoadMoreModule().setEnableLoadMore(false);
//        }
//    }
//
//    @Override
//    protected void requestServer() {
//        if (comic.getComicInfo().getChapterInfoList() == null) {
//            presenter.load(comic, comic.getSource());
//        } else {
//            initChapter();
//        }
//    }
//
//    @Override
//    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
//
//    }
//
//    @Override
//    public boolean onItemLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
//        return false;
//    }
//
//    @Override
//    public BasePresenter getPresenter() {
//        return presenter;
//    }
//
//    @Override
//    public void loadComplete(Comic comic) {
//        initChapter();
//    }
//}
