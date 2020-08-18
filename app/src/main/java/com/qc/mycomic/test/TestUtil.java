package com.qc.mycomic.test;

import java.util.Arrays;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class TestUtil {

    public static void main(String[] args) {
        String versionTag = "v1.0.12";
        String versionLocal = "v1.0.12";
        System.out.println("existUpdate(versionTag, versionLocal) = " + existUpdate(versionTag, versionLocal));
    }

    public static boolean existUpdate(String updateTag, String localTag) {
        boolean flag = false;
        if (updateTag != null && localTag != null && !updateTag.equals(localTag)) {
            String[] tags = updateTag.replace("v", "").split("\\.");
            String[] locals = localTag.replace("v", "").split("\\.");
            try {
                for (int i = 0; i < tags.length; i++) {
                    int tag = Integer.parseInt(tags[i]);
                    int local = Integer.parseInt(locals[i]);
                    if (tag > local) {
                        flag = true;
                        break;
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

}

