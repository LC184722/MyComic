package com.qc.common.constant;

import com.qc.common.en.SettingEnum;
import com.qc.common.util.SettingUtil;

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

}
