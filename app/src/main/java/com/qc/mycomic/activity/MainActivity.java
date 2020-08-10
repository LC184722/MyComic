package com.qc.mycomic.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.qc.mycomic.fragment.MyHomeFragment;
import com.qc.mycomic.util.Codes;
import com.qc.mycomic.util.Utils;

import org.litepal.LitePal;

import java.io.File;

import the.one.base.ui.activity.BaseFragmentActivity;
import the.one.base.ui.fragment.BaseFragment;

public class MainActivity extends BaseFragmentActivity {

    @Override
    protected BaseFragment getFirstFragment() {
//        Utils.verifyStoragePermissions(this);
        LitePal.initialize(this);
        return new MyHomeFragment();
    }

}