package com.qc.common.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qc.common.constant.TmpData;
import com.qc.common.en.SettingEnum;
import com.qc.common.self.ImageConfig;
import com.qc.common.ui.adapter.ComicReaderAdapter;
import com.qc.common.util.EntityHelper;
import com.qc.common.util.ImgUtil;
import com.qc.common.util.SettingUtil;
import com.qc.mycomic.R;
import com.qc.mycomic.ui.fragment.ReaderDetailFragment;

import the.one.base.widge.TheCheckBox;
import top.luqichuang.common.model.Content;
import top.luqichuang.common.model.Entity;
import top.luqichuang.common.model.Source;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/10 19:09
 * @ver 1.0
 */
public class ComicReaderFragment extends BaseReaderFragment {

    private ComicReaderAdapter comicReaderAdapter;

    public static ComicReaderFragment getInstance(Entity entity) {
        ComicReaderFragment fragment = new ComicReaderFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("entity", entity);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        comicReaderAdapter = new ComicReaderAdapter(R.layout.item_reader, entity);
    }

    @Override
    protected int getSettingsViewId() {
        return R.layout.fragment_reader_settings;
    }

    @Override
    protected void setSettingsView(View settingsView) {
        LinearLayout llSettingsContent = settingsView.findViewById(R.id.llSettingsContent);
        llSettingsContent.setOnClickListener(v -> {
        });

        LinearLayout llCancel = settingsView.findViewById(R.id.llCancel);
        llCancel.setOnClickListener(v -> {
            hideView(settingsView);
        });

        LinearLayout llFull = settingsView.findViewById(R.id.llFull);
        TheCheckBox checkBox = llFull.findViewById(R.id.checkBox);
        checkBox.setIsCheckDrawable(R.drawable.ic_baseline_check_circle_24);
        checkBox.setCheck(TmpData.isFull);
        checkBox.setOnClickListener(v -> {
            TmpData.isFull = !checkBox.isCheck();
            SettingUtil.putSetting(SettingEnum.IS_FULL_SCREEN, TmpData.isFull);
            checkBox.setCheck(TmpData.isFull);
        });
        llFull.setOnClickListener(v -> {
            TmpData.isFull = !checkBox.isCheck();
            SettingUtil.putSetting(SettingEnum.IS_FULL_SCREEN, TmpData.isFull);
            checkBox.setCheck(TmpData.isFull);
        });
    }

    @Override
    protected void firstLoadView() {

    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        return comicReaderAdapter;
    }

    @Override
    public boolean onItemLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        Content content = (Content) adapter.getData().get(position);
        //Log.i(TAG, "onItemLongClick: " + content.toStringProgress());
        if (ImgUtil.getLoadStatus(content) == ImgUtil.LOAD_FAIL) {
            RelativeLayout layout = view.findViewById(R.id.imageRelativeLayout);
            ImageConfig config = ImgUtil.getReaderConfig(getContext(), content.getUrl(), layout);
            config.setForce(true);
            Source source = EntityHelper.commonSource(entity);
            config.setHeaders(source.getImageHeaders());
            ImgUtil.loadImage(getContext(), config);
        } else if (ImgUtil.getLoadStatus(content) == ImgUtil.LOAD_SUCCESS) {
            startFragment(ReaderDetailFragment.getInstance(content));
        }
        return true;
    }
}
