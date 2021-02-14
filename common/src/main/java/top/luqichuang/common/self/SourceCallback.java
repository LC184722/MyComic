package top.luqichuang.common.self;

import top.luqichuang.common.util.StringUtil;
import top.luqichuang.mycomic.model.Source;
import top.luqichuang.common.util.NetUtil;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import top.luqichuang.mynovel.model.NSource;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/1/13 23:11
 * @ver 1.0
 */
public abstract class SourceCallback implements Callback {

    private Request request;
    private Source source;
    private NSource nSource;
    private String tag;

    public SourceCallback(Request request, Source source, String tag) {
        this.request = request;
        this.source = source;
        this.tag = tag;
    }

    public SourceCallback(Request request, NSource nSource, String tag) {
        this.request = request;
        this.nSource = nSource;
        this.tag = tag;
    }

    public SourceCallback(Request request, Source source) {
        this.request = request;
        this.source = source;
    }

    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        onFailure(e.getMessage());
    }

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        String html = null;
        Request req = null;
        if (source != null) {
            html = getHtml(response, source.getCharsetName());
            req = source.buildRequest(request.url().toString(), html, tag);
        } else if (nSource != null) {
            html = getHtml(response, nSource.getCharsetName());
            req = nSource.buildRequest(request.url().toString(), html, tag);
        }
        if (req != null && !request.toString().equals(req.toString())) {
            request = req;
            NetUtil.startLoad(req, this);
        } else {
            onResponse(html);
        }
    }

    public static String getHtml(Response response, String charsetName) {
        String html;
        try {
            byte[] b = response.body().bytes();
            html = new String(b, charsetName);
        } catch (Exception e) {
            html = "";
        }
        return html;
    }

    public abstract void onFailure(String errorMsg);

    public abstract void onResponse(String html);

}
