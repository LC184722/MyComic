package com.qc.common.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qc.common.constant.AppConstant;
import com.qc.common.constant.TmpData;
import com.qc.common.ui.adapter.ShelfAdapter;
import com.qc.common.ui.presenter.ShelfPresenter;
import com.qc.common.ui.view.ShelfView;
import com.qc.common.util.DBUtil;
import com.qc.common.util.EntityHelper;
import com.qc.common.util.EntityUtil;
import com.qc.common.util.PopupUtil;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import the.one.base.ui.fragment.BaseDataFragment;
import the.one.base.ui.presenter.BasePresenter;
import the.one.base.util.QMUIDialogUtil;
import top.luqichuang.common.model.Entity;
import top.luqichuang.common.model.Source;
import top.luqichuang.common.util.MapUtil;
import top.luqichuang.common.util.SourceUtil;
import top.luqichuang.common.util.StringUtil;
import top.luqichuang.mycomic.model.Comic;
import top.luqichuang.mycomic.model.ComicInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/9 18:05
 * @ver 1.0
 */
public class ShelfItemFragment extends BaseDataFragment<Entity> implements ShelfView {

    private List<Entity> entityList;

    private ShelfPresenter presenter = new ShelfPresenter();

    private ShelfAdapter shelfAdapter = new ShelfAdapter();

    private int status;

    public static ShelfItemFragment getInstance(int status) {
        ShelfItemFragment fragment = new ShelfItemFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("status", status);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        this.status = (int) getArguments().get("status");
        this.entityList = EntityUtil.getEntityList(status);
        super.onCreate(savedInstanceState);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter && adapter != null) {
            if (entityList != EntityUtil.getEntityList(status)) {
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
        if (entityList == null) {
            entityList = EntityUtil.getEntityList(status);
            if (EntityUtil.getEntityList().isEmpty() && status == EntityUtil.STATUS_FAV) {
                showToast("快去搜索" + TmpData.content + "吧！");
            }
            onFirstComplete(entityList);
        } else if (sList == shelfAdapter.getData()) {
            onFirstComplete(sList);
        } else if (entityList != EntityUtil.getEntityList(status)) {
            entityList = EntityUtil.getEntityList(status);
            onFirstComplete(entityList);
        } else {
            onFirstComplete(entityList);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        Entity entity = shelfAdapter.getItem(position);
        if (entity.isUpdate()) {
            entity.setUpdate(false);
            EntityUtil.first(entity);
        }
        entity.setPriority(0);
        DBUtil.save(entity, DBUtil.SAVE_ONLY);
        startFragment(ChapterFragment.getInstance(entity));
    }

    @Override
    public boolean onItemLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        Entity entity = shelfAdapter.getItem(position);
        String[] items = new String[]{
                "1、查看信息",
                "2、切换" + TmpData.content + "源",
                "3、删除" + TmpData.content + ""
        };
        QMUIDialogUtil.showMenuDialog(getContext(), "选项", items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (which == 0) {
                    QMUIDialogUtil.showSimpleDialog(getContext(), "查看信息", EntityHelper.toStringView(entity)).show();
                } else if (which == 1) {
                    Map<String, String> map = PopupUtil.getMap(entity.getInfoList());
                    String key = PopupUtil.getKey(entity);
                    PopupUtil.showSimpleBottomSheetList(getContext(), map, key, "切换" + TmpData.content + "源", new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                        @Override
                        public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                            String key = MapUtil.getKeyByValue(map, tag);
                            String[] ss = key.split("-", 2);
                            int sourceId = Integer.parseInt(ss[0]);
                            String author = ss[1];
                            entity.setSourceId(sourceId);
                            entity.setAuthor(author);
                            if (EntityHelper.changeInfo(entity)) {
                                adapter.notifyDataSetChanged();
                                DBUtil.save(entity, DBUtil.SAVE_ONLY);
                            }
                            dialog.dismiss();
                        }
                    });
                } else if (which == 2) {
                    QMUIDialogUtil.showSimpleDialog(getContext(), "删除" + TmpData.content + "", "是否删除该" + TmpData.content + "？", new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            if (sList == shelfAdapter.getData()) {
                                Entity c = sList.remove(position);
                                entityList.remove(c);
                            } else {
                                entityList.remove(position);
                            }
                            DBUtil.deleteData(entity);
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
        if (entityList.size() > 0) {
            presenter.checkUpdate(entityList);
            if (progressDialog == null) {
                showProgressDialog(getPercent(), total, getMsg());
            } else {
                progressDialog.setProgress(getPercent(), total);
                progressDialog.setMessage(getMsg());
                progressDialog.show();
            }
        } else {
            showFailTips("没有" + TmpData.content + "!");
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
        if (entityList.size() == count) {
            count = 0;
            hideProgressDialog();
            presenter.initPriority();
//            sortList(entityList);
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
        return count + "/" + entityList.size();
    }

    private int getPercent() {
        return count * total / entityList.size();
    }

    private String getMsg() {
        return "正在检查更新 " + getLoadProcess();
    }

    private List<Entity> sList = new ArrayList<>();

    public void screen(boolean isScreen) {
        if (isScreen) {
            String[] items = {
                    "未读完" + TmpData.content,
                    "据标题筛选",
            };
            QMUIDialogUtil.showMenuDialog(getContext(), "选项", items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if (which == 0) {
                        sList.clear();
                        for (Entity entity : entityList) {
                            ComicInfo comicInfo = (ComicInfo) entity.getInfo();
                            if (comicInfo.getCurChapterTitle() == null || !comicInfo.getCurChapterTitle().equals(comicInfo.getUpdateChapter())) {
                                sList.add(entity);
                            }
                        }
                        onFirstComplete(sList);
                        adapter.notifyDataSetChanged();
                    } else {
                        QMUIDialogUtil.showEditTextDialog(getContext(), "筛选" + TmpData.content + "", "输入" + TmpData.content + "标题", new QMUIDialogUtil.OnEditTextConfirmClickListener() {
                            @Override
                            public void getEditText(QMUIDialog dialog, String content, int index) {
                                if (!content.trim().equals("")) {
                                    sList.clear();
                                    for (Entity entity : entityList) {
                                        if (entity.getTitle().contains(content)) {
                                            sList.add(entity);
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
            if (shelfAdapter.getData() != entityList) {
                onFirstComplete(entityList);
                requestServer();
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void importMH() {
        QMUIDialogUtil.showEditTextDialog(getContext(), "导入" + TmpData.content + "", "输入" + TmpData.content + "url", new QMUIDialogUtil.OnEditTextConfirmClickListener() {
            @Override
            public void getEditText(QMUIDialog dialog, String content, int index) {
                String regex = "//(.*?)\\.(.*?)\\.";
                String flg = StringUtil.match(regex, content, 2);
                Source source = null;
                if (TmpData.contentCode == AppConstant.COMIC_CODE) {
                    String cIndex;
                    if (flg != null) {
                        List<Source<ComicInfo>> list = SourceUtil.getSourceList();
                        for (Source<ComicInfo> s : list) {
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
                }
                dialog.dismiss();
                if (source != null) {
                    ComicInfo comicInfo = new ComicInfo();
                    comicInfo.setSourceId(source.getSourceId());
                    comicInfo.setDetailUrl(content);
                    Entity entity = new Comic(comicInfo);
                    entity.setPriority(0);
                    startFragment(ChapterFragment.getInstance(entity));
                } else {
                    showFailTips("url解析失败！");
                }
            }
        });
    }

}
