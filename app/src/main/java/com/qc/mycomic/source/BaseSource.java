package com.qc.mycomic.source;

import com.qc.mycomic.en.SourceEnum;
import com.qc.mycomic.jsoup.JsoupNode;
import com.qc.mycomic.jsoup.JsoupStarter;
import com.qc.mycomic.model.ComicInfo;
import com.qc.mycomic.model.Source;
import com.qc.mycomic.util.NetUtil;

import java.util.List;

import okhttp3.Request;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/10/7 21:30
 * @ver 1.0
 */
public abstract class BaseSource implements Source {

    public abstract SourceEnum getSourceEnum();

    @Override
    public final int getSourceId() {
        return getSourceEnum().ID;
    }

    @Override
    public final String getSourceName() {
        return getSourceEnum().NAME;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public String getCharsetName() {
        return "UTF-8";
    }

    @Override
    public Request getDetailRequest(String detailUrl) {
        return NetUtil.getRequest(detailUrl);
    }

    @Override
    public Request getImageRequest(String imageUrl) {
        return NetUtil.getRequest(imageUrl);
    }

    @Override
    public Request getRankRequest(String rankUrl) {
        return NetUtil.getRequest(rankUrl);
    }

    @Override
    public Request buildRequest(String html, String tag) {
        return null;
    }
}
