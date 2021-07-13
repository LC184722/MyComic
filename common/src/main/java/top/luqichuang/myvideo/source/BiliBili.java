package top.luqichuang.myvideo.source;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import top.luqichuang.common.en.VSourceEnum;
import top.luqichuang.common.json.JsonNode;
import top.luqichuang.common.json.JsonStarter;
import top.luqichuang.common.jsoup.JsoupNode;
import top.luqichuang.common.jsoup.JsoupStarter;
import top.luqichuang.common.model.ChapterInfo;
import top.luqichuang.common.model.Content;
import top.luqichuang.common.util.NetUtil;
import top.luqichuang.common.util.SourceHelper;
import top.luqichuang.common.util.StringUtil;
import top.luqichuang.myvideo.model.BaseVideoSource;
import top.luqichuang.myvideo.model.VideoInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/24 22:57
 * @ver 1.0
 */
public class BiliBili extends BaseVideoSource {
    @Override
    public VSourceEnum getVSourceEnum() {
        return VSourceEnum.BILI_BILI;
    }

    @Override
    public String getIndex() {
        return "https://www.bilibili.com";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        String url = "https://search.bilibili.com/bangumi?keyword=%s";
        return NetUtil.getRequest(String.format(url, searchString));
    }

    @Override
    public Request getRankRequest(String rankUrl) {
        String api = "https://api.bilibili.com/pgc/season/index/result";
        Map<String, String> map = new LinkedHashMap<>();
        map.put("season_version", "-1");
        map.put("area", "-1");
        map.put("is_finish", "-1");
        map.put("copyright", "-1");
        map.put("season_status", "-1");
        map.put("season_month", "-1");
        map.put("year", "-1");
        map.put("style_id", "-1");
        map.put("order", "3");
        map.put("st", "1");
        map.put("sort", "0");
        map.put("page", "1");
        map.put("season_type", "1");
        map.put("pagesize", "80");
        map.put("type", "1");
        JsonNode node = new JsonNode(rankUrl);
        for (String key : node.keySet()) {
            map.put(key, node.string(key));
        }
        return NetUtil.getRequest(api, map);
    }

    @Override
    public Request buildRequest(String html, String tag, Map<String, Object> data, Map<String, Object> map) {
        if (CONTENT.equals(tag) && map.isEmpty()) {
            String json = StringUtil.match("__INITIAL_STATE__=(.*?);", html);
            JsonNode node = new JsonNode(json);
            JSONArray array = node.jsonArray("epList");
            Integer chapterId = (Integer) data.get("chapterId");
            if (chapterId == null) {
                chapterId = 0;
            }
            if (array != null && !array.isEmpty()) {
                node.init((JSONObject) array.get(chapterId));
                String api = "https://api.bilibili.com/x/player/playurl?otype=json&fnver=0&fnval=2&player=1&qn=64&bvid=%s&cid=%s";
                String bvid = node.string("bvid");
                String cid = node.string("cid");
                String referer = (String) data.get("url");
                String url = String.format(api, bvid, cid);
                map.put("referer", referer);
                return NetUtil.getRequest(url);
            }
        }
        return null;
    }

    @Override
    public List<VideoInfo> getInfoList(String html) {
        JsoupStarter<VideoInfo> starter = new JsoupStarter<VideoInfo>() {
            @Override
            protected VideoInfo dealElement(JsoupNode node, int elementId) {
                String title = node.title("a");
                String author = null;
                String updateTime = node.text("div.left-block", 1, "span.value");
                String imgUrl = null;
                String detailUrl = "https:" + node.href("a");
                return new VideoInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "div#bangumi-list li.bangumi-item");
    }

    @Override
    public void setInfoDetail(VideoInfo info, String html, Map<String, Object> map) {
        JsoupStarter<ChapterInfo> starter = new JsoupStarter<ChapterInfo>() {
            @Override
            protected void dealInfo(JsoupNode node) {
                String title = node.ownText("div.media-right a");
                String imgUrl = node.src("div#media_module img");
                String author = null;
                String intro = node.ownText("span.absolute");
                String updateStatus = node.ownText("span.pub-info");
                String updateTime = node.remove("div.sinfo span label").text("div.sinfo span");
                info.setDetail(title, imgUrl, author, updateTime, updateStatus, intro);
            }
        };
        starter.startInfo(html);

        JsonStarter<ChapterInfo> jsonStarter = new JsonStarter<ChapterInfo>() {
            @Override
            protected boolean isDESC() {
                return false;
            }

            @Override
            protected ChapterInfo dealDataList(JsonNode node, int dataId) {
                String title = node.string("titleFormat");
                String chapterUrl = String.format("https://www.bilibili.com/bangumi/play/ep%s", node.string("id"));
                return new ChapterInfo(dataId, title, chapterUrl);
            }
        };
        String jsonArr = StringUtil.match("\"epList\":(.*?),\"epInfo\"", html);
        SourceHelper.initChapterInfoList(info, jsonStarter.startDataList(JSON.parseArray(jsonArr)));
    }

    @Override
    public List<Content> getContentList(String html, int chapterId, Map<String, Object> map) {
        JsonNode node = new JsonNode(html);
        node.init(node.string("data"));
        JSONArray array = node.jsonArray("durl");
        String url = null;
        if (array != null && !array.isEmpty()) {
            node.init((JSONObject) array.get(0));
            url = node.string("url");
        }
        Content content = new Content(chapterId);
        content.setUrl(url);
        content.getHeaderMap().put("Referer", (String) map.get("referer"));
        content.getHeaderMap().put("User-Agent", NetUtil.USER_AGENT_WEB);
        return SourceHelper.getContentList(content);
    }

    @Override
    public Map<String, String> getRankMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("全部", "{}");
        map.put("正片", "{\"season_version\":1}");
        map.put("电影", "{\"season_version\":2}");
        map.put("其他类型", "{\"season_version\":3}");
        map.put("日本", "{\"area\":2}");
        map.put("美国", "{\"area\":3}");
        map.put("其他地区", "{\"area\":\"1%2C4%2C5%2C6%2C7%2C8%2C9%2C10%2C11%2C12%2C13%2C14%2C15%2C16%2C17%2C18%2C19%2C20%2C21%2C22%2C23%2C24%2C25%2C26%2C27%2C28%2C29%2C30%2C31%2C32%2C33%2C34%2C35%2C36%2C37%2C38%2C39%2C40%2C41%2C42%2C43%2C44%2C45%2C46%2C47%2C48%2C49%2C50%2C51%2C52%2C53%2C54%2C55\"}");
        map.put("完结", "{\"is_finish\":1}");
        map.put("连载", "{\"is_finish\":0}");
        map.put("独家版权", "{\"copyright\":3}");
        map.put("其他版权", "{\"copyright\":\"1%2C2%2C4\"}");
        map.put("免费", "{\"season_status\":1}");
        map.put("付费", "{\"season_status\":\"2%2C6\"}");
        map.put("大会员", "{\"season_status\":\"4%2C6\"}");
        map.put("1月", "{\"season_month\":1}");
        map.put("4月", "{\"season_month\":4}");
        map.put("7月", "{\"season_month\":7}");
        map.put("10月", "{\"season_month\":10}");
        map.put("2021", "{\"year\":\"%5B2021%2C2022)\"}");
        map.put("2020", "{\"year\":\"%5B2020%2C2021)\"}");
        map.put("2019", "{\"year\":\"%5B2019%2C2020)\"}");
        map.put("2018", "{\"year\":\"%5B2018%2C2019)\"}");
        map.put("2017", "{\"year\":\"%5B2017%2C2018)\"}");
        map.put("2016", "{\"year\":\"%5B2016%2C2017)\"}");
        map.put("2015", "{\"year\":\"%5B2015%2C2016)\"}");
        map.put("2014-2010", "{\"year\":\"%5B2010%2C2015)\"}");
        map.put("2009-2005", "{\"year\":\"%5B2005%2C2010)\"}");
        map.put("2004-2000", "{\"year\":\"%5B2000%2C2005)\"}");
        map.put("90年代", "{\"year\":\"%5B1990%2C2000)\"}");
        map.put("80年代", "{\"year\":\"%5B1980%2C1990)\"}");
        map.put("更早", "{\"year\":\"%5B%2C1980)\"}");
        map.put("原创", "{\"style_id\":10010}");
        map.put("漫画改", "{\"style_id\":10011}");
        map.put("小说改", "{\"style_id\":10012}");
        map.put("游戏改", "{\"style_id\":10013}");
        map.put("特摄", "{\"style_id\":10102}");
        map.put("布袋戏", "{\"style_id\":10015}");
        map.put("热血", "{\"style_id\":10016}");
        map.put("穿越", "{\"style_id\":10017}");
        map.put("奇幻", "{\"style_id\":10018}");
        map.put("战斗", "{\"style_id\":10020}");
        map.put("搞笑", "{\"style_id\":10021}");
        map.put("日常", "{\"style_id\":10022}");
        map.put("科幻", "{\"style_id\":10023}");
        map.put("萌系", "{\"style_id\":10024}");
        map.put("治愈", "{\"style_id\":10025}");
        map.put("校园", "{\"style_id\":10026}");
        map.put("少儿", "{\"style_id\":10027}");
        map.put("泡面", "{\"style_id\":10028}");
        map.put("恋爱", "{\"style_id\":10029}");
        map.put("少女", "{\"style_id\":10030}");
        map.put("魔法", "{\"style_id\":10031}");
        map.put("冒险", "{\"style_id\":10032}");
        map.put("历史", "{\"style_id\":10033}");
        map.put("架空", "{\"style_id\":10034}");
        map.put("机战", "{\"style_id\":10035}");
        map.put("神魔", "{\"style_id\":10036}");
        map.put("声控", "{\"style_id\":10037}");
        map.put("运动", "{\"style_id\":10038}");
        map.put("励志", "{\"style_id\":10039}");
        map.put("音乐", "{\"style_id\":10040}");
        map.put("推理", "{\"style_id\":10041}");
        map.put("社团", "{\"style_id\":10042}");
        map.put("智斗", "{\"style_id\":10043}");
        map.put("催泪", "{\"style_id\":10044}");
        map.put("美食", "{\"style_id\":10045}");
        map.put("偶像", "{\"style_id\":10046}");
        map.put("乙女", "{\"style_id\":10047}");
        map.put("职场", "{\"style_id\":10048}");
        return map;
    }

    @Override
    public List<VideoInfo> getRankInfoList(String html) {
        JsonStarter<VideoInfo> starter = new JsonStarter<VideoInfo>() {
            @Override
            protected VideoInfo dealDataList(JsonNode node, int dataId) {
                String title = node.string("title");
                String author = null;
                String updateTime = null;
                String imgUrl = node.string("cover");
                String detailUrl = node.string("link");
                return new VideoInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startDataList(html, "data", "list");
    }
}
