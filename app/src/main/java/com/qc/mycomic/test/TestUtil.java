package com.qc.mycomic.test;

import com.qc.mycomic.util.DecryptUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class TestUtil {

    public static void main(String[] args) {

        String[] ss = {"111", "222", "333"};

        for (int i = 0; i < ss.length; i++) {
            ss[i] = ss[i] + "0";
        }

        System.out.println("ss = " + Arrays.toString(ss));


    }

}

