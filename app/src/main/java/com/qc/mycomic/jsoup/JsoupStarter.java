package com.qc.mycomic.jsoup;

import android.util.Log;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;
import java.util.List;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public abstract class JsoupStarter<T> {

    private JsoupNode node = new JsoupNode();

    public boolean isDESC() {
        return true;
    }

    private int getId(int i, int size) {
        if (isDESC()) {
            return size - i - 1;
        } else {
            return i;
        }
    }

    public void startInfo(String html) {
        node.init(html);
        dealInfo(node);
    }

    public List<T> startElements(String html, String cssQuery) {
        node.init(html);
        Elements elements = node.getElements(cssQuery);
        List<T> list = new LinkedList<>();
        int i = 0;
        int size = elements.size();
        for (Element element : elements) {
            node.init(element);
            int chapterId = getId(i++, size);
            T t = dealElement(node, chapterId);
            if (t != null) {
                if (isDESC()) {
                    list.add(t);
                } else {
                    list.add(0, t);
                }
            }
        }
        return list;
    }

    public abstract void dealInfo(JsoupNode node);

    public abstract T dealElement(JsoupNode node, int elementId);

}
