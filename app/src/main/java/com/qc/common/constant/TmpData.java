package com.qc.common.constant;

import com.qc.common.en.SettingEnum;
import com.qc.common.util.SettingUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/1/27 22:06
 * @ver 1.0
 */
public class TmpData {

    public static int toStatus = Constant.NORMAL;

    public static boolean isLight = true;

    public static boolean isFull = (boolean) SettingUtil.getSettingKey(SettingEnum.IS_FULL_SCREEN);

    public static int contentCode = (int) SettingUtil.getSettingKey(SettingEnum.READ_CONTENT);

    public static String content = SettingUtil.getSettingDesc(SettingEnum.READ_CONTENT);

    public static int videoSpeed = 2;

    public static Map<String, String> map = new HashMap<>();
}
