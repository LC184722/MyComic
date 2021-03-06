package top.luqichuang.common.util;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

import java.net.URLEncoder;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author LuQiChuang
 * @desc 解密工具
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class DecryptUtil {

    /**
     * base64解密，返回解密字符串
     *
     * @param encodeStr 加密字符串
     * @return String
     */
    public static String decryptBase64(String encodeStr) {
        byte[] codes = decryptBase64ToBytes(encodeStr);
        return codes != null ? new String(codes) : null;
    }

    /**
     * base64解密，返回解密字节数组
     *
     * @param encodeStr 加密字符串
     * @return byte[]
     */
    public static byte[] decryptBase64ToBytes(String encodeStr) {
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            return decoder.decode(encodeStr);
        } catch (Exception e) {
            try {
                return android.util.Base64.decode(encodeStr, android.util.Base64.DEFAULT);
            } catch (Exception ex) {
                return null;
            }
        }
    }

    /**
     * AES解密，返回解密字符串
     *
     * @param value 加密字符串
     * @param key   密钥
     * @param iv    偏移
     * @return String
     */
    public static String decryptAES(String value, String key, String iv) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
//            Base64.Decoder decoder = Base64.getDecoder();
//            byte[] code = decoder.decode(value);
            byte[] codes = decryptBase64ToBytes(value);
            return new String(cipher.doFinal(codes));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES解密，返回解密字符串
     *
     * @param value 加密字符串
     * @param key   密钥
     * @return String
     */
    public static String decryptAES(String value, String key) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] code = decryptBase64ToBytes(value);
            return new String(cipher.doFinal(code));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getUtf8EncodeStr(String string) {
        String resUrl = null;
        try {
            resUrl = URLEncoder.encode(string, "UTF-8");
            resUrl = resUrl.replace("%2F", "/").replace("+", "%20");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resUrl;
    }

    public static String getGBKEncodeStr(String string) {
        String resUrl = null;
        try {
            resUrl = URLEncoder.encode(string, "GBK");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resUrl;
    }

    /**
     * 执行js代码，返回结果值
     *
     * @param code js代码
     * @return String
     */
    public static String exeJsCode(String code) {
        try {
            Context ctx = Context.enter();
            ctx.setOptimizationLevel(-1);
            Scriptable scope = ctx.initStandardObjects();
            Object object = ctx.evaluateString(scope, code, null, 0, null);
            return Context.toString(object);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 执行js函数代码，返回结果值
     *
     * @param jsCode  js代码
     * @param funName 函数名
     * @param values  函数数据
     * @return String
     */
    public static String exeJsFunction(String jsCode, String funName, Object... values) {
        Context ctx = Context.enter();
        ctx.setOptimizationLevel(-1);
        Scriptable scope = ctx.initStandardObjects();
        ctx.evaluateString(scope, jsCode, "<cmd>", 1, null);
        Object fun = scope.get(funName, scope);
        try {
            Object data = ((Function) fun).call(ctx, scope, scope, values);
            return Context.toString(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * packedJs解密 输入eval(function(p,a,c,k,e,d)...)字符串，返回计算结果
     *
     * @param jsCode jsCode
     * @return String
     */
    public static String decryptPackedJsCode(String jsCode) {
        jsCode = jsCode.substring(5, jsCode.length() - 1);
        jsCode = "function fun(){return " + jsCode + "}";
        return exeJsFunction(jsCode, "fun");
    }

    /**
     * unescape解密
     *
     * @param src src
     * @return String
     */
    public static String unescape(String src) {
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length());
        int lastPos = 0, pos = 0;
        char ch;
        while (lastPos < src.length()) {
            pos = src.indexOf("%", lastPos);
            if (pos == lastPos) {
                if (src.charAt(pos + 1) == 'u') {
                    ch = (char) Integer.parseInt(src
                            .substring(pos + 2, pos + 6), 16);
                    tmp.append(ch);
                    lastPos = pos + 6;
                } else {
                    ch = (char) Integer.parseInt(src
                            .substring(pos + 1, pos + 3), 16);
                    tmp.append(ch);
                    lastPos = pos + 3;
                }
            } else {
                if (pos == -1) {
                    tmp.append(src.substring(lastPos));
                    lastPos = src.length();
                } else {
                    tmp.append(src.substring(lastPos, pos));
                    lastPos = pos;
                }
            }
        }
        return tmp.toString();
    }

}
