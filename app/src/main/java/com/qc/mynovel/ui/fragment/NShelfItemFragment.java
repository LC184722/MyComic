package com.qc.mynovel.ui.fragment;

import android.content.DialogInterface;
import android.view.View;
import android.view.animation.Animation;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qc.common.util.PopupUtil;
import com.qc.common.util.RestartUtil;
import com.qc.mynovel.ui.adapter.NShelfAdapter;
import com.qc.mynovel.ui.presenter.NShelfPresenter;
import com.qc.mynovel.ui.view.NShelfView;
import com.qc.mynovel.util.DBUtil;
import com.qc.mynovel.util.NovelHelper;
import com.qc.mynovel.util.NovelUtil;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import the.one.base.ui.fragment.BaseDataFragment;
import the.one.base.ui.presenter.BasePresenter;
import the.one.base.util.QMUIDialogUtil;
import top.luqichuang.common.util.MapUtil;
import top.luqichuang.common.util.NSourceUtil;
import top.luqichuang.common.util.StringUtil;
import top.luqichuang.mynovel.model.NSource;
import top.luqichuang.mynovel.model.Novel;
import top.luqichuang.mynovel.model.NovelInfo;

/**
 * @author LuQiChuang
 * @desc 小说书架界面
 * @date 2020/8/12 15:32
 * @ver 1.0
 */
public class NShelfItemFragment extends BaseDataFragment<Novel> implements NShelfView {

    private List<Novel> novelList;

    private NShelfPresenter presenter = new NShelfPresenter();

    private NShelfAdapter shelfAdapter = new NShelfAdapter();

    private int status;

    public NShelfItemFragment() {
        RestartUtil.restart(_mActivity);
    }

    public NShelfItemFragment(int status) {
        this.status = status;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter && adapter != null) {
            if (novelList != NovelUtil.getNovelList(status)) {
                requestServer();
            } else {
                adapter.notifyDataSetChanged();
            }
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mTopLayout.setVisibility(View.GONE);
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
        if (novelList == null) {
            novelList = NovelUtil.getNovelList(status);
            if (NovelUtil.getNovelList().isEmpty() && status == NovelUtil.STATUS_FAV) {
                showToast("快去搜索小说吧！");
            }
            onFirstComplete(novelList);
        } else if (sList == shelfAdapter.getData()) {
            onFirstComplete(sList);
        } else if (novelList != NovelUtil.getNovelList(status)) {
            novelList = NovelUtil.getNovelList(status);
            onFirstComplete(novelList);
        } else {
            onFirstComplete(novelList);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        Novel novel = shelfAdapter.getItem(position);
        if (novel.isUpdate()) {
            novel.setUpdate(false);
            NovelUtil.first(novel);
        }
        novel.setPriority(0);
        DBUtil.saveNovel(novel, DBUtil.SAVE_ONLY);
        startFragment(new NChapterFragment(novel));
    }

    @Override
    public boolean onItemLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        Novel novel = shelfAdapter.getItem(position);
        //Log.i(TAG, "onItemLongClick: info " + novel);
        String[] items = new String[]{
                "1、查看信息",
                "2、切换小说源",
                "3、删除小说"
        };
        QMUIDialogUtil.showMenuDialog(getContext(), "选项", items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (which == 0) {
                    QMUIDialogUtil.showSimpleDialog(getContext(), "查看信息", NovelHelper.toStringView(novel)).show();
                } else if (which == 1) {
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
                                adapter.notifyDataSetChanged();
                                DBUtil.saveNovel(novel, DBUtil.SAVE_ONLY);
                            }
                            dialog.dismiss();
                        }
                    });
                } else if (which == 2) {
                    QMUIDialogUtil.showSimpleDialog(getContext(), "删除小说", "是否删除该小说？", new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            if (sList == shelfAdapter.getData()) {
                                Novel c = sList.remove(position);
                                novelList.remove(c);
                            } else {
                                novelList.remove(position);
                            }
                            DBUtil.deleteData(novel);
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
        if (novelList.size() > 0) {
            presenter.checkUpdate(novelList);
            if (progressDialog == null) {
                showProgressDialog(getPercent(), total, getMsg());
            } else {
                progressDialog.setProgress(getPercent(), total);
                progressDialog.setMessage(getMsg());
                progressDialog.show();
            }
        } else {
            showFailTips("没有小说");
        }
    }

    private int count = 0;
    private int total = 100;
    private List<String> errorList = new ArrayList<>();

    @Override
    public void checkUpdateComplete(String title) {
        adapter.notifyDataSetChanged();
        if (title != null) {
            errorList.add(title);
        }
        count++;
        if (novelList.size() == count) {
            count = 0;
            hideProgressDialog();
            presenter.initPriority();
//            sortList(novelList);
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
        //Log.i(TAG, "checkUpdateComplete: " + getLoadProcess());
    }

    private String getLoadProcess() {
        return count + "/" + novelList.size();
    }

    private int getPercent() {
        return count * total / novelList.size();
    }

    private String getMsg() {
        return "正在检查更新 " + getLoadProcess();
    }

    private List<Novel> sList = new ArrayList<>();

    public void screen(boolean isScreen) {
        if (isScreen) {
            String[] items = {
                    "未读完小说",
                    "据标题筛选",
            };
            QMUIDialogUtil.showMenuDialog(getContext(), "选项", items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if (which == 0) {
                        sList.clear();
                        for (Novel novel : novelList) {
                            NovelInfo novelInfo = novel.getNovelInfo();
                            if (novelInfo.getCurChapterTitle() == null || !novelInfo.getCurChapterTitle().equals(novelInfo.getUpdateChapter())) {
                                sList.add(novel);
                            }
                        }
                        onFirstComplete(sList);
                        adapter.notifyDataSetChanged();
                    } else {
                        QMUIDialogUtil.showEditTextDialog(getContext(), "筛选小说", "输入小说标题", new QMUIDialogUtil.OnEditTextConfirmClickListener() {
                            @Override
                            public void getEditText(QMUIDialog dialog, String content, int index) {
                                if (!content.trim().equals("")) {
                                    sList.clear();
                                    for (Novel novel : novelList) {
                                        if (novel.getTitle().contains(content)) {
                                            sList.add(novel);
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
            if (shelfAdapter.getData() != novelList) {
                onFirstComplete(novelList);
                requestServer();
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void importInfo() {
        QMUIDialogUtil.showEditTextDialog(getContext(), "导入小说", "输入小说url", new QMUIDialogUtil.OnEditTextConfirmClickListener() {
            @Override
            public void getEditText(QMUIDialog dialog, String content, int index) {
                String regex = "//(.*?)\\.(.*?)\\.";
                String flg = StringUtil.match(regex, content, 2);
                NSource source = null;
                String cIndex;
                if (flg != null) {
                    List<NSource> list = NSourceUtil.getNSourceList();
                    for (NSource s : list) {
                        String suffix = StringUtil.match(regex, s.getIndex(), 2);
                        if (flg.equals(suffix)) {
                            source = s;
                            break;
                        }
                    }
                    if (source != null) {
                        cIndex = source.getIndex();
                        content = cIndex.substring(0, cIndex.indexOf('.')) + content.substring(content.indexOf('.'));
                    }
                }
                dialog.dismiss();
                if (source != null) {
                    NovelInfo novelInfo = new NovelInfo();
                    novelInfo.setNSourceId(source.getNSourceId());
                    novelInfo.setDetailUrl(content);
                    Novel novel = new Novel(novelInfo);
                    novel.setPriority(0);
                    startFragment(new NChapterFragment(novel));
                } else {
                    showFailTips("url解析失败！");
                }
            }
        });
    }

}
