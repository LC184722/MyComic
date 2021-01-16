package com.qc.mycomic.util.test;

import com.qc.mycomic.model.ComicInfo;
import com.qc.mycomic.model.ImageInfo;
import com.qc.mycomic.model.Source;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class HtmlUtil {

    public static String getHtmlByFile() {
        return getHtmlByFile("text.html");
    }

    public static String getHtmlByFile(String filename) {
        try {
            String filePath = "D:\\Programming\\Files\\MH\\" + filename;
            File file = new File(filePath);
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedReader in = new BufferedReader(new FileReader(new File(filePath)));
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = in.readLine()) != null) {
                builder.append(line).append("\n");
            }
            in.close();
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getTextByFile() {
        try {
            String filePath = "D:\\Programming\\Files\\MH\\text.txt";
            BufferedReader in = new BufferedReader(new FileReader(new File(filePath)));
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = in.readLine()) != null) {
                builder.append(line);
            }
            in.close();
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void writeFile(String html) {
        writeFile(html, "result.html");
    }

    public static void writeFile(String html, String fileName) {
        try {
            String filePath = "D:\\Programming\\Files\\MH\\" + fileName;
            BufferedWriter in = new BufferedWriter(new FileWriter(new File(filePath)));
            in.write(html);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeFile(String[] ss, String var, String fromFileName, String toFileName) {
        Map<String, String> myMap = getMyMap(ss);
        String html = HtmlUtil.getHtmlByFile(fromFileName);
        for (String s : myMap.keySet()) {
            String str = var + "[" + s + "]";
            System.out.println(str + " --> " + myMap.get(s));
            html = html.replace(str, myMap.get(s));
        }
        HtmlUtil.writeFile(html, toFileName);
    }

    public static Map<String, String> getMyMap(String[] ss) {
        Map<String, String> myMap = new LinkedHashMap<>();
        int i = 0;
        for (String s : ss) {
            if (s.contains("\\x")) {
                s = s.replace("\\x", "%");
            }
            try {
                s = URLDecoder.decode(s, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (s.contains("\\u")) {
                s = UnicodeToCN(s);
            }
            String key = String.format("0x%x", i++);
            String value = "\"" + s + "\"";
            myMap.put(key, value);
        }
        return myMap;
    }

    /**
     * unicode编码转换为汉字
     *
     * @param unicodeStr 待转化的编码
     * @return 返回转化后的汉子
     */
    public static String UnicodeToCN(String unicodeStr) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(unicodeStr);
        char ch;
        while (matcher.find()) {
            //group
            String group = matcher.group(2);
            //ch:'李四'
            ch = (char) Integer.parseInt(group, 16);
            //group1
            String group1 = matcher.group(1);
            unicodeStr = unicodeStr.replace(group1, ch + "");
        }
        return unicodeStr.replace("\\", "").trim();
    }

    public static void getComicInfoListTest(Source source, String fileName) {
        String html = HtmlUtil.getHtmlByFile(fileName);
        List<ComicInfo> comicInfoList;
        comicInfoList = source.getComicInfoList(html);
        if (comicInfoList != null) {
            System.out.println("comicInfoList.size() = " + comicInfoList.size());
            for (ComicInfo info : comicInfoList) {
                System.out.println("info.getTitle() = " + info.getTitle());
                System.out.println("info.getAuthor() = " + info.getAuthor());
                System.out.println("info.getUpdateTime() = " + info.getUpdateTime());
                System.out.println("info.getImgUrl() = " + info.getImgUrl());
                System.out.println("info.getDetailUrl() = " + info.getDetailUrl());
                System.out.println();
            }
        } else {
            System.out.println("comicInfoList = null");
        }
    }

    public static void setComicDetailTest(Source source, String fileName) {
        String html = HtmlUtil.getHtmlByFile(fileName);
        ComicInfo info = new ComicInfo();
        source.setComicDetail(info, html);
        System.out.println("info.getTitle() = " + info.getTitle());
        System.out.println("info.getImgUrl() = " + info.getImgUrl());
        System.out.println("info.getAuthor() = " + info.getAuthor());
        System.out.println("info.getUpdateTime() = " + info.getUpdateTime());
        System.out.println("info.getUpdateChapter() = " + info.getUpdateChapter());
        System.out.println("info.getUpdateStatus() = " + info.getUpdateStatus());
        System.out.println("info.getIntro() = " + info.getIntro());
        System.out.println("info.getChapterInfoList().size() = " + info.getChapterInfoList().size());
        int size = info.getChapterInfoList().size();
        if (size > 0) {
            int first = 0;
            int last = size - 1;
            System.out.println("last  chapter = " + info.getChapterInfoList().get(first));
            System.out.println(".......................................");
            System.out.println("first chapter = " + info.getChapterInfoList().get(last));
        }
    }

    public static void getImageInfoListTest(Source source, String fileName) {
        String html = HtmlUtil.getHtmlByFile(fileName);
        List<ImageInfo> imageInfoList = source.getImageInfoList(html, 100);
        System.out.println("imageInfoList.size() = " + imageInfoList.size());
        for (ImageInfo imageInfo : imageInfoList) {
            System.out.println("imageInfo = " + imageInfo.getUrl());
        }
    }

    public static void getRankComicInfoListTest(Source source, String fileName) {
        String html = HtmlUtil.getHtmlByFile(fileName);
        List<ComicInfo> comicInfoList = source.getRankComicInfoList(html);
        System.out.println("comicInfoList.size() = " + comicInfoList.size());
        int i = 0;
        for (ComicInfo info : comicInfoList) {
            System.out.println("--------------------  " + (++i) + "  ----------------------");
            System.out.println("info.getTitle() = " + info.getTitle());
            System.out.println("info.getAuthor() = " + info.getAuthor());
            System.out.println("info.getUpdateTime() = " + info.getUpdateTime());
            System.out.println("info.getImgUrl() = " + info.getImgUrl());
            System.out.println("info.getDetailUrl() = " + info.getDetailUrl());
            System.out.println("----------------------------------------------");
            System.out.println();
        }
    }

}
