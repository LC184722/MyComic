package com.qc.mycomic.json;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.LinkedList;
import java.util.List;

public abstract class JsonStarter<T> {

    private JsonNode node = new JsonNode();

    private int cur;

    private int total;

    public boolean isDESC() {
        return true;
    }

    public int getCur() {
        return cur;
    }

    public int getTotal() {
        return total;
    }

    public List<T> startDataList(String json, String... conditions) {
        node.init(json);
        JSONArray jsonArray = null;
        int i = 0;
        for (String condition : conditions) {
            if (++i != conditions.length) {
                node.init(node.jsonObject(condition));
            } else {
                jsonArray = node.jsonArray(condition);
            }
        }
        List<T> list = new LinkedList<>();
        if (jsonArray != null) {
            total = jsonArray.size();
            cur = 0;
            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                node.init(jsonObject);
                T t = dealDataList(node);
                cur++;
                if (t != null) {
                    if (isDESC()) {
                        list.add(t);
                    } else {
                        list.add(0, t);
                    }
                }
            }
        }
        return list;
    }

    public void startData(String json, String... conditions) {
        node.init(json);
        node.initConditions(conditions);
        dealData(node);
    }

    public abstract void dealData(JsonNode node);

    public abstract T dealDataList(JsonNode node);

}