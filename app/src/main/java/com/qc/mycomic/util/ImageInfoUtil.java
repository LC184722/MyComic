package com.qc.mycomic.util;

import com.qc.mycomic.model.ImageInfo;

import java.util.LinkedList;
import java.util.List;

/**
 * @author LuQiChuang
 * @description 图片信息工具
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class ImageInfoUtil {

    public static List<ImageInfo> getImageInfoList(String[] urls, int chapterId) {
        return getImageInfoList(urls, chapterId, "");
    }

    public static List<ImageInfo> getImageInfoList(String[] urls, int chapterId, String urlPrev) {
        List<ImageInfo> list = new LinkedList<>();
        if (urls != null) {
            int i = 0;
            for (String url : urls) {
                ImageInfo imageInfo = new ImageInfo(chapterId, i++, urls.length, urlPrev + url);
                list.add(imageInfo);
            }
        }
        return list;
    }

}
