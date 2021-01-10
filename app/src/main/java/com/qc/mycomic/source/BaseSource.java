package com.qc.mycomic.source;

import com.qc.mycomic.model.Source;
import com.qc.mycomic.util.NetUtil;

import okhttp3.Request;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/10/7 21:30
 * @ver 1.0
 */
public abstract class BaseSource implements Source {

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public Request getDetailRequest(String detailUrl) {
        return NetUtil.getRequest(detailUrl);
    }

    @Override
    public Request getRankRequest(String rankUrl) {
        return NetUtil.getRequest(rankUrl);
    }
}
