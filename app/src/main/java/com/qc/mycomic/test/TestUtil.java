package com.qc.mycomic.test;

import com.qc.mycomic.json.JsonNode;
import com.qc.mycomic.json.JsonStarter;
import com.qc.mycomic.jsoup.JsoupNode;
import com.qc.mycomic.model.ComicInfo;
import com.qc.mycomic.util.Codes;
import com.qc.mycomic.util.NetUtil;
import com.qc.mycomic.util.StringUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author LuQiChuang
 * @description
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class TestUtil {

    public static void main(String[] args) {
        String tagName = removeSuffix("1.0");
        String versionTag = removeSuffix("1.0.0");
        System.out.println("tagName = " + tagName);
        System.out.println("versionTag = " + versionTag);
    }

    public static String removeSuffix(String string) {
        if (string == null) {
            return "";
        }
        while (string.endsWith(".0")) {
            string = string.substring(0, string.lastIndexOf('.'));
        }
        return string;
    }

}

