package top.luqichuang.common.mycomic.source;

import top.luqichuang.common.mycomic.en.SourceEnum;
import top.luqichuang.common.mycomic.model.Source;
import top.luqichuang.common.mycomic.util.NetUtil;

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
    public Request buildRequest(String requestUrl, String html, String tag) {
        return null;
    }
}
