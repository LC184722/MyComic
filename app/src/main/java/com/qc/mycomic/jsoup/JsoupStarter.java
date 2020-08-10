package com.qc.mycomic.jsoup;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;
import java.util.List;

public abstract class JsoupStarter<T> {

    private JsoupNode node = new JsoupNode();

    public boolean isDESC() {
        return true;
    }

    public void startInfo(String html) {
        node.init(html);
        dealInfo(node);
    }

    public List<T> startElements(String html, String cssQuery) {
        node.init(html);
        Elements elements = node.getElements(cssQuery);
        List<T> list = new LinkedList<>();
        for (Element element : elements) {
            node.init(element);
            T t = dealElement(node);
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

    public abstract T dealElement(JsoupNode node);

}
