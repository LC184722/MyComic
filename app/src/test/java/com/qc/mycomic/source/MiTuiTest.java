package com.qc.mycomic.source;

import com.qc.mycomic.model.Source;
import com.qc.mycomic.util.test.HtmlUtil;
import com.qc.mycomic.util.DecryptUtil;

import org.junit.Test;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/10/15 14:06
 * @ver 1.0
 */
public class MiTuiTest {

    private Source source = new MiTui();

    @Test
    public void getComicInfoList() {
    }

    @Test
    public void setComicDetail() {
        HtmlUtil.setComicDetailTest(source, "mt/mt_detail.html");
    }

    @Test
    public void getImageInfoList() {
//        HtmlUtil.getImageInfoListTest(source, "mt/mt-test.html");
        //https://res0818.imitui.com/r/bFiyRCJs62BZrGMuFw90RzQ4NmEwY2JkYTE4ZDBiMzk5MWIxZmFkYzEzY2NkMDgyY2VlNjQxZTRlMTY2ZDg0ZTc5YmZkYjIwNmZiNDIyOWENlnPLNktg4kjjiXTOZ--xwpSYlCpqX9El6f-DDvRnYTSCtaUhZABenf-fLJP45SDYfGRf5xZpHtVUFTGLpoeCjsBpA6NMSewnECz8rTF9_wJ-B9fQPg1K4FfaqxAyluiucbtoPqX5NowtRauSMWE-/0
        //https://res0818.imitui.com/r/L3IvYkZpeVJDSnM2MkJackdNdUY=
        String js = "eval(function(p,a,c,k,e,d){e=function(c){return(c<a?'':e(parseInt(c/a)))+((c=c%a)>35?String.fromCharCode(c+29):c.toString(36))};if(!''.replace(/^/,String)){while(c--){d[e(c)]=k[c]||e(c)}k=[function(e){return d[e]}];e=function(){return'\\\\w+'};c=1};while(c--){if(k[c]){p=p.replace(new RegExp('\\\\b'+e(c)+'\\\\b','g'),k[c])}}return p}('b L=\\'//19.U.V/W?C=X&Y=1\\';b M=\"n://10.Z.m\";b O=\"n://8.12.m\";d e(5){g(5.f(/^(\\\\/r\\\\/?)/i)){9 M+5}Q g(5.f(/^(\\\\/|j?)/i)){9 O+5}9 5}d D(5){g(5.f(/^(\\\\/r\\\\/?)/i)){9\\'\\'}Q g(5.f(/^(\\\\/|j?)/i)){9\\'\\'}9 5}d I(){b h=15 17();h[\\'s\\'+\\'T\\']=e(\"/r/B--A-E-J-R-/0\");h.13=d(){k.q=\\'<3><a v=\"/u/t/l-2.o\" 4=\"6:7;\"><3><8 4=\"y-w: 7;6:z;\"  c=\"\\'+D(\"/r/11-/0\")+\\'\"></3></a><a v=\"/u/t/l-2.o\"><3><8 4=\"y-w: 7;6:z;P: 16%;\" c=\"\\'+e(\"/r/B--A-E-J-R-/0\")+\\'\" /><p 4=\"F-G: H;\">1/14</p></3></a><8 4=\"y-w: 7;6:7;\"  c=\"\\'+e(\"/r/N-S/1\")+\\'\"></3>\\'};h.1s=d(){k.q=\\'<a v=\"/u/t/l-2.o\" 4=\"1q-1p: 18; 1o: 1n;F-G: H;\"><3 1m=\"1l\"  4=\"6:z;\" ><8 4=\"6:7;\" c=\"\\'+e(\"/r/N-S/1\")+\\'\" K=\"\"><8 c=\"n://1h.1g.m/j/1e/1d.1c\" K=\"\" 4=\"1b-P: 1t;\"> <p>图片加载失败，点击刷新页面试试</p><p>刷新几次都无法加载就翻下一页吧</p></3></a>\\';b 3=x.1k(\\'3\\');3.q=\\'<3 4=\"6: 7;\"><8 c=\"\\'+L+\\'\"></3>\\';x.1f.1i(3)}}1a.1j();b k=x.1r(\\'j\\');k.C=\"\";I();',62,92,'|||div|style|image|display|none|img|return||var|src|function|getI6mageUrl|match|if|v28e82||images|vcc219|898331|com|https|html||innerHTML|||shisedaluzhichushendansheng|manhua|href|events|document|pointer|block|xwpSYlCpqX9El6f|bFiyRCJs62BZrGMuFw90RzQ4NmEwY2JkYTE4ZDBiMzk5MWIxZmFkYzEzY2NkMDgyY2VlNjQxZTRlMTY2ZDg0ZTc5YmZkYjIwNmZiNDIyOWENlnPLNktg4kjjiXTOZ|id|getI5mageUrl|DDvRnYTSCtaUhZABenf|text|align|center|loadImage|fLJP45SDYfGRf5xZpHtVUFTGLpoeCjsBpA6NMSewnECz8rTF9_wJ|alt|esi|cirh|haSmR2dDrU8kp39dXhAIPzI1Y2NkMzVmZmRiMDNjZTUzZTg5YTI4MDkzMzViYTcyODc0MGFmOWEwZDc0MDJmZjUzNWYzMmQ4ZWMxMGEwNjfWFInspdxsUXdqXp0xMnHEKv_A7ATSBPMXHdsAbytq|cih|width|else|B9fQPg1K4FfaqxAyluiucbtoPqX5NowtRauSMWE|KAstWHjgb7gt1TSQAlbPuYyNiIHd8VMXjAaYZqfm0AVOKCYgBnvsAGzKdpEjXoBK8_mrAwcoJ9OvK1GBGhSGaUNffM2tZ58FcKcSGH3bDPt|rc|51|la|go1|4187563|pvFlag|imitui|res0818|iyRCJs62BZrGMuFw90RzQ4NmEwY2TzGpzLxzeSB6MRz6luiucbtoPqX5NowtRauSMWE|zuimh|onload||new|100|Image|16px|ia|sinChapter|max|png|error|default|body|duoduomh|img1|append|initBan|createElement|fail|class|red|color|size|font|getElementById|onerror|15em'.split('|'),0,{}))";
        js = "eval(function(p,a,c,k,e,d){e=function(c){return(c<a?'':e(parseInt(c/a)))+((c=c%a)>35?String.fromCharCode(c+29):c.toString(36))};if(!''.replace(/^/,String)){while(c--){d[e(c)]=k[c]||e(c)}k=[function(e){return d[e]}];e=function(){return'\\\\w+'};c=1};while(c--){if(k[c]){p=p.replace(new RegExp('\\\\b'+e(c)+'\\\\b','g'),k[c])}}return p}('c R=\\'//T.A.1c/1j?H=1k&X=1\\';c G=\"m://U.14.u\";c F=\"m://8.1r.u\";e d(4){f(4.g(/^(\\\\/r\\\\/?)/i)){b G+4}E f(4.g(/^(\\\\/|h?)/i)){b F+4}b 4}e D(4){f(4.g(/^(\\\\/r\\\\/?)/i)){b\\'\\'}E f(4.g(/^(\\\\/|h?)/i)){b\\'\\'}b 4}e K(){c k=12 11();k[\\'s\\'+\\'Z\\']=d(\"/r/C-B=/0\");k.Y=e(){j.y=\\'<3><a v=\"/t/l/o-2.q\" 5=\"6:7;\"><3><8 5=\"x-n: 7;6:w;\"  9=\"\\'+D(\"/r/W-V=/0\")+\\'\"></3></a><a v=\"/t/l/o-2.q\"><3><8 5=\"x-n: 7;6:w;M: 19%;\" 9=\"\\'+d(\"/r/C-B=/0\")+\\'\" /><p 5=\"L-I: J;\">1/A</p></3></a><8 5=\"x-n: 7;6:7;\"  9=\"\\'+d(\"/r/N-O-P-S=/1\")+\\'\"></3>\\'};k.1p=e(){j.y=\\'<a v=\"/t/l/o-2.q\" 5=\"1i-1h: 1g; 1f: 1e;L-I: J;\"><3 1d=\"13\"  5=\"6:w;\" ><8 5=\"6:7;\" 9=\"\\'+d(\"/r/N-O-P-S=/1\")+\\'\" Q=\"\"><8 9=\"m://1l.1m.u/h/1n/1o.1q\" Q=\"\" 5=\"1a-M: 1b;\"> <p>图片加载失败，点击刷新页面试试</p><p>刷新几次都无法加载就翻下一页吧</p></3></a>\\';c 3=z.10(\\'3\\');3.y=\\'<3 5=\"6: 7;\"><8 9=\"\\'+R+\\'\"></3>\\';z.15.16(3)}}17.18();c j=z.1s(\\'h\\');j.H=\"\";K();',62,91,'|||div|image|style|display|none|img|src||return|var|getI6mageUrl|function|if|match|images||v9bca5|v37f65|fangkainagenvwu|https|events|91231||html|||manhua|com|href|block|pointer|innerHTML|document|51|Onb3noA3gw0KGEX8zNkYzEyMzhiNDliOTE2NmZhMjM4N2QxMzQxZTAyNjAwOTQ4MjYzYzI5YTZkYzM1NmI1ZjIyMWNjOWYxNmYyZTeomTjd1UellkN1V33Gbq0ry8cAgpuj8rgztZutL5XFySYBBGkyVk5r7kYQDaeJkyUHhvdSiUZZkgDaJU2yc0GQeRb93AotxNcPR1qHx3bhIRxOZPg0eSKlPJk7U3IYYjDQ6xE6iiansB4lcXW3kzOLTfSqRB9lHneRfPrWkQZXeCvzW4WmpMN7zKJGz2p84So|M7lT|getI5mageUrl|else|cih|cirh|id|align|center|loadImage|text|width|2iq6Y6pkUJXhoSv1p44mgjA5YWZlZThjMzc2NmJmYjllZWQ3ZThjMmFhYTEzODFkOWIzZDU4Y2ZhZjRkZDEwMGU3NDkzMDkxNDZiMzhkOTUqLQLRV_QjlC6UOlAfIT_uqw1TKJTQg_bWUmVFE_hYwwrfykkPGC_5Tx16VY2cVCEnIGdUa|fhdcuR26ISSYtEsB9t5EbJnioklkg6bEWip6QEW8tOiv|Hqilx|alt|esi|ZWQ5D2vrhQdNuaF_W1hOVVn7FDu8IBn73h3_rVdXaugzL8C1juZcEEfsAODIdKfSHRA4yk|ia|res0818|Onb3noA3gwTzGpzLxzeSB6MRz6pMN7zKJGz2p84So|lT|pvFlag|onload|rc|createElement|Image|new|fail|imitui|body|append|sinChapter|initBan|100|max|15em|la|class|red|color|16px|size|font|go1|4187563|img1|duoduomh|default|error|onerror|png|zuimh|getElementById'.split('|'),0,{}))";


        js = js.substring(5, js.length() - 1);
        js = "function fun(){return " + js + "}";
        System.out.println("js = " + js);
//        String s = DecryptUtil.exeJsCode(js);
        String s = DecryptUtil.exeJsFunction(js, "fun");
        System.out.println("s = " + s);
    }
}