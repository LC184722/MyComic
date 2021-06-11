package top.luqichuang.common.self;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import top.luqichuang.common.model.Source;
import top.luqichuang.common.util.NetUtil;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/10 14:52
 * @ver 1.0
 */
public abstract class CommonCallback implements Callback {

    private Request request;
    private Source source;
    private String tag;
    private Map<String, Object> map = new HashMap<>();

    public CommonCallback(Request request, Source source, String tag) {
        this.request = request;
        this.source = source;
        this.tag = tag;
    }

    public CommonCallback(Request request, Source source) {
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
            req = source.buildRequest(request.url().toString(), html, tag, map);
        }
        if (req != null && !request.toString().equals(req.toString())) {
            request = req;
            NetUtil.startLoad(req, this);
        } else {
            onResponse(html, map);
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

    public abstract void onResponse(String html, Map<String, Object> map);

}
