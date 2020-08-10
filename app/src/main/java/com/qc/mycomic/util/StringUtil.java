package com.qc.mycomic.util;

import android.util.Log;

import com.qc.mycomic.model.Comic;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class StringUtil {

    public static final String TAG = "StringUtil";

    public static boolean find(String regex, String input, int group) {
        try {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(input);
            return matcher.find();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean find(String regex, String input) {
        return find(regex, input, 1);
    }

    public static String match(String regex, String input, int group) {
        try {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(input);
            if (matcher.find()) {
                return Objects.requireNonNull(matcher.group(group)).trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String match(String regex, String input) {
        return match(regex, input, 1);
    }

    public static String matchLast(String regex, String input, int group) {
        try {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(input);
            String result = null;
            while (matcher.find()) {
                result = Objects.requireNonNull(matcher.group(group)).trim();
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String matchLast(String regex, String input) {
        return matchLast(regex, input, 1);
    }

    public static String[] matchArray(String regex, String input, int group) {
        try {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(input);
            List<String> list = new LinkedList<>();
            while (matcher.find()) {
                list.add(Objects.requireNonNull(matcher.group(group)).trim());
            }
            String[] result = new String[list.size()];
            list.toArray(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String[] matchArray(String regex, String input) {
        return matchArray(regex, input, 1);
    }

    public static String getGBKDecodedStr(String str) {
        byte[] helloBytes = str.getBytes(Charset.forName("GBK"));
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[helloBytes.length * 2];
        for (int j = 0; j < helloBytes.length; j++) {
            int v = helloBytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        String helloHex = new String(hexChars);
        StringBuilder decodedStr = new StringBuilder();
        int count = 0;
        for (int i = 0; i < helloHex.length(); i++) {
            if (count++ % 2 == 0) {
                decodedStr.append("%").append(helloHex.charAt(i));
            } else {
                decodedStr.append(helloHex.charAt(i));
            }
        }
        return decodedStr.toString();
    }

    public static <T> void swapList(List<T> list) {
        List<T> nList = new LinkedList<>();
        for (T t : list) {
            nList.add(0, t);
        }
        list.clear();
        list.addAll(nList);
    }

//    public static void printHashCode(Object... objects) {
//        printHashCode(TAG, objects);
//    }
//
//    public static void printHashCode(String TAG, Object... objects) {
//        StringBuilder code = new StringBuilder();
//        for (Object object : objects) {
//            code.append(object.hashCode()).append(" ");
//        }
//        Log.i(TAG, "printHashCode: " + code);
//    }
//
//    public static void printHashCode(Comic comic) {
//        if (comic != null) {
//            Log.i(TAG, "printHashCode: comic = " + comic.hashCode());
//            if (comic.getComicInfo() != null) {
//                Log.i(TAG, "printHashCode: comicInfo = " + comic.getComicInfo().hashCode());
//                if (comic.getComicInfo().getChapterInfoList() != null) {
//                    Log.i(TAG, "printHashCode: chapterInfoList = " + comic.getComicInfo().getChapterInfoList().hashCode());
//                }
//            }
//        }
//    }
}
