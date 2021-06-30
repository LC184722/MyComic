package top.luqichuang.mycomic.source;

import com.alibaba.fastjson.JSONArray;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import top.luqichuang.common.en.SourceEnum;
import top.luqichuang.common.json.JsonNode;
import top.luqichuang.common.json.JsonStarter;
import top.luqichuang.common.model.ChapterInfo;
import top.luqichuang.common.model.Content;
import top.luqichuang.common.util.NetUtil;
import top.luqichuang.common.util.SourceHelper;
import top.luqichuang.common.util.StringUtil;
import top.luqichuang.mycomic.model.BaseComicSource;
import top.luqichuang.mycomic.model.ComicInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class BiliBili extends BaseComicSource {
    @Override
    public SourceEnum getSourceEnum() {
        return SourceEnum.BILI_BILI;
    }

    @Override
    public String getIndex() {
        return "https://manga.bilibili.com";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        String url = "https://manga.bilibili.com/twirp/comic.v1.Comic/Search?device=pc&platform=web";
        Map<String, String> map = new HashMap<>();
        map.put("key_word", searchString);
        map.put("page_num", "1");
        map.put("page_size", "9");
        return NetUtil.postRequest(url, map);
    }

    @Override
    public Request getDetailRequest(String detailUrl) {
        String url = "https://manga.bilibili.com/twirp/comic.v1.Comic/ComicDetail?device=pc&platform=web";
        String id = StringUtil.match("(\\d+)", detailUrl);
        return NetUtil.postRequest(url, "comic_id", id);
    }

    @Override
    public Request getContentRequest(String imageUrl) {
        String url = "https://manga.bilibili.com/twirp/comic.v1.Comic/GetImageIndex?device=pc&platform=web";
        String id = StringUtil.matchLast("(\\d+)", imageUrl);
        return NetUtil.postRequest(url, "ep_id", id);
    }

    @Override
    public Request getRankRequest(String rankUrl) {
        String[] attrs = rankUrl.split("#");
        String url = String.format("https://manga.bilibili.com/twirp/comic.v1.Comic/%s?device=pc&platform=web", attrs[0]);
        JsonNode node = new JsonNode(attrs[1]);
        Map<String, String> map = new HashMap<>();
        for (String key : node.keySet()) {
            map.put(key, node.string(key));
        }
        return NetUtil.postRequest(url, map);
    }

    @Override
    public Request buildRequest(String html, String tag, Map<String, Object> data, Map<String, Object> map) {
        if (CONTENT.equals(tag) && map.isEmpty()) {
            JsonStarter<Object> starter = new JsonStarter<Object>() {
                @Override
                public Object dealDataList(JsonNode node, int dataId) {
                    return node.string("path");
                }
            };
            List<Object> list = starter.startDataList(html, "data", "images");
            com.alibaba.fastjson.JSONArray array = new JSONArray(list);
            String url = "https://manga.bilibili.com/twirp/comic.v1.Comic/ImageToken?device=pc&platform=web";
            map.put("url", url);
            return NetUtil.postRequest(url, "urls", array.toString());
        }
        return super.buildRequest(html, tag, data, map);
    }

    @Override
    public List<ComicInfo> getInfoList(String html) {
        JsonStarter<ComicInfo> starter = new JsonStarter<ComicInfo>() {
            @Override
            protected ComicInfo dealDataList(JsonNode node, int dataId) {
                String title = node.string("org_title");
                String author = node.arrayToString("author_name");
                String updateTime = null;
                String imgUrl = node.string("vertical_cover");
                String detailUrl = "https://manga.bilibili.com/detail/mc" + node.string("id");
                return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startDataList(html, "data", "list");
    }

    @Override
    public void setInfoDetail(ComicInfo info, String html, Map<String, Object> map) {
        String id = StringUtil.match("(\\d+)", info.getDetailUrl());
        JsonStarter<ChapterInfo> starter = new JsonStarter<ChapterInfo>() {
            @Override
            protected void dealData(JsonNode node) {
                String title = node.string("title");
                String imgUrl = node.string("square_cover");
                String author = node.arrayToString("author_name");
                String intro = node.string("classic_lines");
                String updateStatus = node.string("renewal_time");
                String updateTime = null;
                info.setDetail(title, imgUrl, author, updateTime, updateStatus, intro);
            }

            @Override
            protected ChapterInfo dealDataList(JsonNode node, int dataId) {
                String title = node.string("short_title");
                String oTitle = node.string("title");
                if (oTitle != null) {
                    title = title + " " + oTitle;
                }
                String chapterUrl = "https://manga.bilibili.com/mc" + id + "/" + node.string("id");
                return new ChapterInfo(dataId, title, chapterUrl);
            }
        };
        starter.startData(html, "data");
        SourceHelper.initChapterInfoList(info, starter.startDataList(html, "data", "ep_list"));
    }

    @Override
    public List<Content> getContentList(String html, int chapterId, Map<String, Object> map) {
        JsonStarter<Content> starter = new JsonStarter<Content>() {
            @Override
            protected Content dealDataList(JsonNode node, int dataId) {
                String url = node.string("url");
                String token = node.string("token");
                String chapterUrl = url + "?token=" + token;
                chapterUrl = chapterUrl.replace("\\u0026", "&");
                return new Content(chapterId, getCur(), getTotal(), chapterUrl);
            }
        };
        return starter.startDataList(html, "data");
    }

    @Override
    public Map<String, String> getRankMap() {
        Map<String, String> map = new LinkedHashMap<>();
        //https://manga.bilibili.com/twirp/comic.v1.Comic/HomeHot?device=pc&platform=web {"type":3}
        map.put("日漫榜", "HomeHot#{\"type\":3}");
        map.put("国漫榜", "HomeHot#{\"type\":4}");//{"type":4}
        map.put("月票榜", "HomeFans#{\"last_week_offset\":0,\"last_month_offset\":0,\"type\":1}");//{"last_week_offset":0,"last_month_offset":0,"type":1}
        map.put("投喂榜", "HomeFans#{\"last_week_offset\":0,\"last_month_offset\":0,\"type\":0}");//{"last_week_offset":0,"last_month_offset":0,"type":0}
        map.put("飙升榜", "HomeHot#{\"type\":2}");//{"type":2}
        map.put("免费榜", "HomeHot#{\"type\":1}");//{"type":1}
        //https://manga.bilibili.com/twirp/comic.v1.Comic/ClassPage?device=pc&platform=web {"style_id":-1,"area_id":1,"is_finish":-1,"order":0,"page_num":1,"page_size":200,"is_free":-1}
        map.put("免费", "ClassPage#{\"style_id\":-1,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":100,\"is_free\":1}");
        map.put("付费", "ClassPage#{\"style_id\":-1,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":100,\"is_free\":2}");
        map.put("等就免费", "ClassPage#{\"style_id\":-1,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":100,\"is_free\":3}");
        map.put("大陆", "ClassPage#{\"style_id\":-1,\"area_id\":1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":100,\"is_free\":-1}");
        map.put("日本", "ClassPage#{\"style_id\":-1,\"area_id\":2,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":100,\"is_free\":-1}");
        map.put("韩国", "ClassPage#{\"style_id\":-1,\"area_id\":6,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":100,\"is_free\":-1}");
        map.put("连载", "ClassPage#{\"style_id\":-1,\"area_id\":-1,\"is_finish\":0,\"order\":0,\"page_num\":1,\"page_size\":100,\"is_free\":-1}");
        map.put("完结", "ClassPage#{\"style_id\":-1,\"area_id\":-1,\"is_finish\":1,\"order\":0,\"page_num\":1,\"page_size\":100,\"is_free\":-1}");
        map.put("正能量", "ClassPage#{\"style_id\":1028,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":100,\"is_free\":-1}");
        map.put("冒险", "ClassPage#{\"style_id\":1013,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":100,\"is_free\":-1}");
        map.put("热血", "ClassPage#{\"style_id\":999,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":100,\"is_free\":-1}");
        map.put("搞笑", "ClassPage#{\"style_id\":994,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":100,\"is_free\":-1}");
        map.put("恋爱", "ClassPage#{\"style_id\":995,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":100,\"is_free\":-1}");
        map.put("少女", "ClassPage#{\"style_id\":1026,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":100,\"is_free\":-1}");
        map.put("日常", "ClassPage#{\"style_id\":1020,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":100,\"is_free\":-1}");
        map.put("校园", "ClassPage#{\"style_id\":1001,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":100,\"is_free\":-1}");
        map.put("运动", "ClassPage#{\"style_id\":1010,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":100,\"is_free\":-1}");
        map.put("治愈", "ClassPage#{\"style_id\":1007,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":100,\"is_free\":-1}");
        map.put("古风", "ClassPage#{\"style_id\":997,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":100,\"is_free\":-1}");
        map.put("玄幻", "ClassPage#{\"style_id\":1016,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":100,\"is_free\":-1}");
        map.put("奇幻", "ClassPage#{\"style_id\":998,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":100,\"is_free\":-1}");
        map.put("后宫", "ClassPage#{\"style_id\":1017,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":100,\"is_free\":-1}");
        map.put("惊奇", "ClassPage#{\"style_id\":996,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":100,\"is_free\":-1}");
        map.put("悬疑", "ClassPage#{\"style_id\":1023,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":100,\"is_free\":-1}");
        map.put("都市", "ClassPage#{\"style_id\":1002,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":100,\"is_free\":-1}");
        map.put("总裁", "ClassPage#{\"style_id\":1004,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":100,\"is_free\":-1}");
        map.put("剧情", "ClassPage#{\"style_id\":1030,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":100,\"is_free\":-1}");
        return map;
    }

    @Override
    public List<ComicInfo> getRankInfoList(String html) {
        JsonStarter<ComicInfo> starter = new JsonStarter<ComicInfo>() {
            @Override
            protected ComicInfo dealDataList(JsonNode node, int dataId) {
                String title = node.string("title");
                String author = node.arrayToString("author");
                String updateTime = null;
                String imgUrl = node.string("vertical_cover");
                String id = node.string("comic_id");
                if (id == null) {
                    id = node.string("season_id");
                }
                String detailUrl = "https://manga.bilibili.com/detail/mc" + id;
                return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        List<ComicInfo> list = starter.startDataList(html, "data");
        if (list.isEmpty()) {
            list = starter.startDataList(html, "data", "comics");
        }
        return list;
    }
}
