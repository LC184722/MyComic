package com.qc.mycomic.jsoup;

import com.qc.mycomic.util.StringUtil;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class JsoupStarter<T> {

    private JsoupNode node = new JsoupNode();

    protected boolean isDESC() {
        return true;
    }

    public final void startInfo(String html) {
        node.init(html);
        dealInfo(node);
    }

    public final List<T> startElements(String html, String... cssQuery) {
        node.init(html);
        List<T> list = new ArrayList<>();
        Elements elements = null;
        for (String s : cssQuery) {
            elements = node.getElements(s);
            if (!elements.isEmpty()) {
                break;
            }
        }
        if (elements == null) {
            return list;
        }
        int i = 0;
        int size = elements.size();
        for (Element element : elements) {
            node.init(element);
            int chapterId = getId(i++, size);
            T t = dealElement(node, chapterId);
            if (t != null) {
                list.add(t);
            }
        }
        if (!isDESC()) {
            StringUtil.swapList(list);
        }
        return list;
    }

    protected void dealInfo(JsoupNode node) {
    }

    protected T dealElement(JsoupNode node, int elementId) {
        return null;
    }

    private int getId(int i, int size) {
        if (isDESC()) {
            return size - i - 1;
        } else {
            return i;
        }
    }

}
