package com.qc.mycomic.ui.fragment;

import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qc.mycomic.ui.adapter.ShelfAdapter;
import com.qc.mycomic.model.Comic;
import com.qc.mycomic.model.ComicInfo;
import com.qc.mycomic.ui.presenter.ShelfPresenter;
import com.qc.mycomic.util.DBUtil;
import com.qc.mycomic.util.SourceUtil;
import com.qc.mycomic.ui.view.ShelfView;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import the.one.base.model.PopupItem;
import the.one.base.ui.fragment.BaseDataFragment;
import the.one.base.ui.presenter.BasePresenter;
import the.one.base.util.QMUIBottomSheetUtil;
import the.one.base.util.QMUIDialogUtil;

public class ShelfItemFragment extends BaseDataFragment<Comic> implements ShelfView {

    private List<Comic> comicList;

    private ShelfPresenter presenter = new ShelfPresenter();

    private ShelfAdapter shelfAdapter = new ShelfAdapter();

    private int status;

    public ShelfItemFragment(int status) {
        this.status = status;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter && adapter != null) {
            requestServer();
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    protected boolean showTitleBar() {
        return false;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
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
    protected BaseQuickAdapter getAdapter() {
        return shelfAdapter;
    }

    @Override
    protected void initAdapter() {
        super.initAdapter();
        adapter.getLoadMoreModule().setOnLoadMoreListener(null);
    }

    @Override
    protected void requestServer() {
        if (comicList == null) {
            comicList = DBUtil.findComicListByStatus(status);
            onFirstComplete(comicList);
        } else if (comicList == shelfAdapter.getData()) {
            Log.i(TAG, "requestServer: " + comicList);
            List<Comic> list = DBUtil.findComicListByStatus(status);
            List<Comic> nList = new LinkedList<>();
            for (Comic comic : list) {
                int index = comicList.indexOf(comic);
                if (index != -1) {
                    nList.add(comicList.get(index));
                } else {
                    nList.add(comic);
                }
            }
            comicList.clear();
            comicList.addAll(nList);
            Log.i(TAG, "requestServer: " + comicList);
            onFirstComplete(comicList);
        } else if (sList == shelfAdapter.getData()) {
            onFirstComplete(sList);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        Comic comic = shelfAdapter.getItem(position);
        if (comic.isUpdate()) {
            comic.setUpdate(false);
            comic.setDate(new Date());
        }
        comic.setPriority(0);
        DBUtil.saveData(comic, false);
        startFragment(new ChapterFragment(comic));
    }

    @Override
    public boolean onItemLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        Comic comic = shelfAdapter.getItem(position);
        Log.i(TAG, "onItemLongClick: info " + comic);
        String[] items = new String[]{
                "1、查看信息",
                "2、切换漫画源",
                "3、删除漫画"
        };
        QMUIDialogUtil.showMenuDialog(getContext(), "选项", items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (which == 0) {
                    QMUIDialogUtil.showSimpleDialog(getContext(), "查看信息", comic.toStringView()).show();
                } else if (which == 1) {
                    List<PopupItem> list = SourceUtil.getPopupItemList(comic.getComicInfoList());
                    int index = SourceUtil.getPopupItemIndex(comic);
                    QMUIBottomSheetUtil.showSimpleBottomSheetList(getContext(), list, "切换漫画源", index, new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                        @Override
                        public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                            int sourceId = SourceUtil.getSourceId(tag);
                            comic.setSourceId(sourceId);
                            if (comic.changeComicInfo()) {
                                adapter.notifyDataSetChanged();
                                DBUtil.saveData(comic, false);
                            }
                            dialog.dismiss();
                        }
                    }).show();
                } else if (which == 2) {
                    QMUIDialogUtil.showSimpleDialog(getContext(), "删除漫画", "是否删除该漫画？", new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            if (sList == shelfAdapter.getData()) {
                                Comic c = sList.remove(position);
                                comicList.remove(c);
                            } else {
                                comicList.remove(position);
                            }
                            DBUtil.deleteData(comic);
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                            onRefresh();
                        }
                    }).show();
                }
            }
        }).show();
        return true;
    }

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }

    public void startCheckUpdate() {
        if (comicList.size() > 0) {
            presenter.checkUpdate(comicList);
            if (progressDialog == null) {
                showProgressDialog(getPercent(), total, getMsg());
            } else {
                progressDialog.setProgress(getPercent(), total);
                progressDialog.setMessage(getMsg());
                progressDialog.show();
            }
        } else {
            showFailTips("没有漫画");
        }
    }

    private int count = 0;
    private int total = 100;
    private List<String> errorList = new LinkedList<>();

    @Override
    public void checkUpdateComplete(String title) {
        adapter.notifyDataSetChanged();
        if (title != null) {
            errorList.add(title);
        }
        count++;
        if (comicList.size() == count) {
            count = 0;
            hideProgressDialog();
            presenter.initPriority();
            sortList(comicList);
            adapter.notifyDataSetChanged();
            if (errorList.isEmpty()) {
                showSuccessTips("检查更新完成");
            } else {
                StringBuilder tip = new StringBuilder();
                for (String s : errorList) {
                    tip.append(s).append("\n");
                }
                QMUIDialogUtil.showSimpleDialog(getContext(), "检查更新结果", "检查更新完毕，失败数：" + errorList.size() + "\n" + tip);
                errorList.clear();
            }
        } else {
            showProgressDialog(getPercent(), total);
            progressDialog.setMessage(getMsg());
        }
        Log.i(TAG, "checkUpdateComplete: " + getLoadProcess());
    }

    private String getLoadProcess() {
        return count + "/" + comicList.size();
    }

    private int getPercent() {
        return count * total / comicList.size();
    }

    private String getMsg() {
        return "正在检查更新 " + getLoadProcess();
    }

    private void sortList(List<Comic> list) {
        List<Comic> nList = new LinkedList<>();
        while (!list.isEmpty()) {
            int index = -1;
            int max = -1;
            for (int i = 0; i < list.size(); i++) {
                int priority = list.get(i).getPriority();
                if (priority > max) {
                    index = i;
                    max = priority;
                }
            }
            nList.add(list.remove(index));
            Log.i(TAG, "orderList: remove -> " + index);
        }
        list.addAll(nList);
    }

    private List<Comic> sList;

    public void screen(boolean isScreen) {
        if (isScreen) {
            String[] items = {
                    "未读完漫画",
                    "据标题筛选",
            };
            QMUIDialogUtil.showMenuDialog(getContext(), "选项", items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if (which == 0) {
                        sList = new LinkedList<>();
                        for (Comic comic : comicList) {
                            ComicInfo comicInfo = comic.getComicInfo();
                            if (comicInfo.getCurChapterTitle() == null || !comicInfo.getCurChapterTitle().equals(comicInfo.getUpdateChapter())) {
                                sList.add(comic);
                            }
                        }
                        onFirstComplete(sList);
                        adapter.notifyDataSetChanged();
                    } else {
                        QMUIDialogUtil.showEditTextDialog(getContext(), "筛选漫画", "输入漫画标题", new QMUIDialogUtil.OnEditTextConfirmClickListener() {
                            @Override
                            public void getEditText(QMUIDialog dialog, String content, int index) {
                                if (!content.trim().equals("")) {
                                    sList = new LinkedList<>();
                                    for (Comic comic : comicList) {
                                        if (comic.getTitle().contains(content)) {
                                            sList.add(comic);
                                        }
                                    }
                                    onFirstComplete(sList);
                                    adapter.notifyDataSetChanged();
                                }
                                dialog.dismiss();
                            }
                        }).show();
                    }
                }
            }).show();
        } else {
            if (shelfAdapter.getData() != comicList) {
                onFirstComplete(comicList);
                requestServer();
                adapter.notifyDataSetChanged();
            }
        }
    }

}
