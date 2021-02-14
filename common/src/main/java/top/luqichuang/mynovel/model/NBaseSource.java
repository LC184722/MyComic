package top.luqichuang.mynovel.model;

import okhttp3.Request;
import top.luqichuang.common.en.NSourceEnum;
import top.luqichuang.common.util.NetUtil;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/10/7 21:30
 * @ver 1.0
 */
public abstract class NBaseSource implements NSource {

    public abstract NSourceEnum getNSourceEnum();

    @Override
    public final int getNSourceId() {
        return getNSourceEnum().ID;
    }

    @Override
    public final String getNSourceName() {
        return getNSourceEnum().NAME;
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
    public Request getContentRequest(String imageUrl) {
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
