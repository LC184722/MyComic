package com.qc.mycomic.source;

import com.alibaba.fastjson.JSONObject;
import com.qc.mycomic.jsoup.JsoupNode;
import com.qc.mycomic.jsoup.JsoupStarter;
import com.qc.mycomic.model.ChapterInfo;
import com.qc.mycomic.model.ComicInfo;
import com.qc.mycomic.model.ImageInfo;
import com.qc.mycomic.model.MyMap;
import com.qc.mycomic.model.Source;
import com.qc.mycomic.util.Codes;
import com.qc.mycomic.util.ComicUtil;
import com.qc.mycomic.util.DecryptUtil;
import com.qc.mycomic.util.NetUtil;
import com.qc.mycomic.util.StringUtil;

import java.util.List;
import java.util.Locale;

import okhttp3.Request;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/16 23:41
 * @ver 1.0
 */
public class OH extends BaseSource {

    @Override
    public int getSourceId() {
        return Codes.OH;
    }

    @Override
    public String getSourceName() {
        return Codes.OH_STRING;
    }

    @Override
    public String getIndex() {
        return "https://www.cocomanhua.com/";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        searchString = "https://www.cocomanhua.com/search?searchString=" + searchString;
        return NetUtil.getRequest(searchString);
    }

    @Override
    public List<ComicInfo> getComicInfoList(String html) {
        JsoupStarter<ComicInfo> starter = new JsoupStarter<ComicInfo>() {
            @Override
            public ComicInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("h1 a");
                String author = node.ownText("ul li.fed-col-xs6", 1);
                String updateTime = node.ownText("ul li.fed-col-xs6", 2);
                String imgUrl = node.attr("a", "data-original");
                String detailUrl = getIndex() + node.href("a");
                return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "dl.fed-deta-info");
    }

    @Override
    public void setComicDetail(ComicInfo comicInfo, String html) {
        JsoupStarter<ChapterInfo> starter = new JsoupStarter<ChapterInfo>() {

            @Override
            public void dealInfo(JsoupNode node) {
                String author = node.ownText("div.fed-part-layout li.fed-col-md6", 1, "a");
                String intro = node.ownText("p.fed-padding.fed-part-both.fed-text-muted");
                String updateStatus = node.ownText("div.fed-part-layout li.fed-col-md6", 0, "a");
                String updateTime = node.ownText("div.fed-part-layout li.fed-col-md6", 2, "a");
                comicInfo.setDetail(author, updateTime, updateStatus, intro);
            }

            @Override
            public ChapterInfo dealElement(JsoupNode node, int elementId) {
                String title = node.title("a");
                String chapterUrl = getIndex() + node.href("a");
                return new ChapterInfo(elementId, title, chapterUrl);
            }
        };
        starter.startInfo(html);
        comicInfo.initChapterInfoList(starter.startElements(html, "ul.fed-part-rows li.fed-col-lg3"));
    }

    @Override
    public List<ImageInfo> getImageInfoList(String html, int chapterId) {
        String[] urls = null;
        String chapterImagesStr = StringUtil.match("C_DATA='(.*?)'", html);
        String result = DecryptUtil.decryptAES(DecryptUtil.decryptBase64(chapterImagesStr), "fw12558899ertyui");//|SEPARATER|
        if (result != null) {
            try {
                String server = "https://img.cocomanhua.com/comic/";
                result = StringUtil.match("(\\{.*?\\})", result);
                JSONObject jsonObject = JSONObject.parseObject(result);
                String imgPath = jsonObject.getString("enc_code2");
                imgPath = DecryptUtil.decryptAES(DecryptUtil.decryptBase64(imgPath), "fw125gjdi9ertyui");
                imgPath = DecryptUtil.getUrlEncodeStr(imgPath);
                String encCode1 = jsonObject.getString("enc_code1");
                encCode1 = DecryptUtil.decryptAES(DecryptUtil.decryptBase64(encCode1), "fw12558899ertyui");
                int total = encCode1 != null ? Integer.parseInt(encCode1) : 50;
                urls = new String[total];
                for (int i = 0; i < total; i++) {
                    urls[i] = server + imgPath + String.format(Locale.CHINA, "%04d.jpg", i + 1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ComicUtil.getImageInfoList(urls, chapterId);
    }

    @Override
    public MyMap<String, String> getRankMap() {
        MyMap<String, String> map = new MyMap<>();
        map.put("月点击", "https://www.cocomanhua.com/show?orderBy=monthlyCount&page=1");
        map.put("周点击", "https://www.cocomanhua.com/show?orderBy=weeklyCount&page=1");
        map.put("日点击", "https://www.cocomanhua.com/show?orderBy=dailyCount&page=1");
        map.put("收录日", "https://www.cocomanhua.com/show?orderBy=create&page=1");
        map.put("更新日", "https://www.cocomanhua.com/show?orderBy=update&page=1");
        map.put("连载中", "https://www.cocomanhua.com/show?orderBy=weeklyCount&status=1&page=1");
        map.put("已完结", "https://www.cocomanhua.com/show?orderBy=weeklyCount&status=2&page=1");
        map.put("玄幻", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10024&page=1");
        map.put("热血", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10023&page=1");
        map.put("恋爱", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10126&page=1");
        map.put("都市", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10124&page=1");
        map.put("古风", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10143&page=1");
        map.put("冒险", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10210&page=1");
        map.put("穿越", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10129&page=1");
        map.put("爆笑", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10201&page=1");
        map.put("搞笑", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10122&page=1");
        map.put("奇幻", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10242&page=1");
        map.put("校园", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10131&page=1");
        map.put("少年", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10321&page=1");
        map.put("修真", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10133&page=1");
        map.put("霸总", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10127&page=1");
        map.put("其他", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10560&page=1");
        map.put("动作", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10125&page=1");
        map.put("生活", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10142&page=1");
        map.put("少女", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10301&page=1");
        map.put("后宫", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10138&page=1");
        map.put("少男", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10641&page=1");
        map.put("逆转", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10702&page=1");
        map.put("武侠", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10139&page=1");
        map.put("重生", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10461&page=1");
        map.put("科幻", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10181&page=1");
        map.put("总裁", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10306&page=1");
        map.put("剧情", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10480&page=1");
        map.put("大女主", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10706&page=1");
        map.put("悬疑", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10183&page=1");
        map.put("魔幻", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10227&page=1");
        map.put("恐怖", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10185&page=1");
        return map;
    }

    @Override
    public List<ComicInfo> getRankComicInfoList(String html) {//fed-list-item
        JsoupStarter<ComicInfo> starter = new JsoupStarter<ComicInfo>() {
            @Override
            public void dealInfo(JsoupNode node) {

            }

            @Override
            public ComicInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("a.fed-list-title");
                String author = null;
                String updateTime = node.ownText("span.fed-list-desc");
                String imgUrl = node.attr("a", "data-original");
                String detailUrl = getIndex() + node.href("a");
                return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "li.fed-list-item");
    }

}
