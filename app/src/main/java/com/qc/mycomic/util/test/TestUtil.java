package com.qc.mycomic.util.test;

import com.qc.mycomic.util.DecryptUtil;

import java.util.Arrays;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class TestUtil {

    public static void main(String[] args) {
        String s = "L3IvTTdsVC1PbmIzbm9BM2d3MEs=";
        s = DecryptUtil.decryptBase64(s);
        System.out.println("s = " + s);

    }

}

