package com.qc.mycomic.jsoup;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupUtil {

    private static Element element;

    public static void setElement(Element element) {
        JsoupUtil.element = element;
    }

    public static Elements getElements(String cssQuery) {
        return element.select(cssQuery);
    }

}
