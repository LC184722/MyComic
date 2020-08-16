package com.qc.mycomic.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:27
 * @ver 1.0
 */
public class JsoupNode {

    private Element element;

    private Elements elements = new Elements();

    public JsoupNode() {
    }

    public JsoupNode(String text) {
        init(text);
    }

    @Override
    public String toString() {
        return "JsoupNode{" +
                "element=" + element.toString() +
                '}';
    }

    public void init(String text) {
        try {
            this.element = Jsoup.parse(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init(Element element) {
        this.element = element;
    }

    public Elements getElements(String cssQuery) {
        try {
            return element.select(cssQuery);
        } catch (Exception e) {
            return new Elements();
        }
    }

    public Element getElement(String cssQuery) {
        try {
            return element.selectFirst(cssQuery);
        } catch (Exception e) {
            return null;
        }
    }

    public void addElement(String cssQuery) {
        addElement(getElement(cssQuery));
    }

    public void addElement(Element element) {
        if (element != null) {
            elements.add(element);
        }
    }

    public String ownText(String cssQuery) {
        try {
            return element.selectFirst(cssQuery).ownText();
        } catch (Exception e) {
            return null;
        }
    }

    public String ownText(String cssQuery1, int index, String cssQuery2) {
        try {
            return element.select(cssQuery1).get(index).selectFirst(cssQuery2).ownText();
        } catch (Exception e) {
            return null;
        }
    }

    public String ownText(String cssQuery, int index) {
        try {
            return element.select(cssQuery).get(index).ownText();
        } catch (Exception e) {
            return null;
        }
    }

    public String ownText(String... cssQuery) {
        for (String s : cssQuery) {
            String result = ownText(s);
            if (result != null && !result.trim().equals("")) {
                return result;
            }
        }
        return null;
    }

    public String text(String cssQuery) {
        try {
            return element.selectFirst(cssQuery).text();
        } catch (Exception e) {
            return null;
        }
    }

    public String text(String cssQuery1, int index, String cssQuery2) {
        try {
            return element.select(cssQuery1).get(index).selectFirst(cssQuery2).text();
        } catch (Exception e) {
            return null;
        }
    }

    public String text(String... cssQuery) {
        for (String s : cssQuery) {
            String result = text(s);
            if (result != null && !result.trim().equals("")) {
                return result;
            }
        }
        return null;
    }

    public String attr(String cssQuery, String attr) {
        try {
            return element.selectFirst(cssQuery).attr(attr);
        } catch (Exception e) {
            return null;
        }
    }

    public String href(String cssQuery) {
        return attr(cssQuery, "href");
    }

    public String src(String cssQuery) {
        return attr(cssQuery, "src");
    }

    public String title(String cssQuery) {
        return attr(cssQuery, "title");
    }

    public Elements getElements() {
        return elements;
    }

    public Element getElement() {
        return element;
    }
}
