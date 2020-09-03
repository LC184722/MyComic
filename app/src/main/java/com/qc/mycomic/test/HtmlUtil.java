package com.qc.mycomic.test;

import com.qc.mycomic.model.MyMap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class HtmlUtil {

    public static String getSearchHtml() {
        return "";
    }

    public static String getDetailHtml() {
        return "";
    }

    public static String getImageHtml() {
        return "";
    }

    public static String getTestHtml() {
        return "";
    }

    public static String getHtmlByFile() {
        try {
            String filePath = "D:\\Programming\\Files\\MH\\text.html";
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

    public static void writeFile(String[] ss, String var, String fileName) {
        MyMap<String, String> myMap = getMyMap(ss);
        String html = HtmlUtil.getHtmlByFile();
        for (String s : myMap.keySet()) {
            String str = var + "[" + s + "]";
            html = html.replace(str, myMap.get(s));
        }
        HtmlUtil.writeFile(html, fileName);
    }

    public static MyMap<String, String> getMyMap(String[] ss) {
        MyMap<String, String> myMap = new MyMap<>();
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
            System.out.println("s = " + s);
            String key = String.format("0x%x", i++);
            String value = "\"" + s + "\"";
            myMap.put(key, value);
        }
        System.out.println("myMap = " + myMap);
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

}
