package com.qc.mycomic.json;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.HashSet;
import java.util.Set;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:27
 * @ver 1.0
 */
public class JsonNode {

    private JSONObject jsonObject;

    public JsonNode() {
    }

    public JsonNode(JSONObject jsonObject) {
        init(jsonObject);
    }

    public JsonNode(String json) {
        init(json);
    }

    public JsonNode(String json, String... conditions) {
        init(json);
        initConditions(conditions);
    }

    @Override
    public String toString() {
        return "JsonNode{" +
                "jsonObject=" + jsonObject +
                '}';
    }

    private JSONObject parse(String json) {
        return JSONObject.parseObject(json);
    }

    public void init(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public void init(String json) {
        try {
            this.jsonObject = parse(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initConditions(String... conditions) {
        try {
            for (String condition : conditions) {
                init(jsonObject(condition));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Set<String> keySet() {
        try {
            return jsonObject.keySet();
        } catch (Exception e) {
            return new HashSet<>();
        }
    }

    public String arrayToString(String key) {
        try {
            String string = jsonObject.getJSONArray(key).toString();
            string = string.replace("[", "").replace("]", "").replace("\"", "");
            return string;
        } catch (Exception e) {
            return null;
        }
    }

    public String string(String key) {
        try {
            return jsonObject.getString(key);
        } catch (Exception e) {
            return null;
        }
    }

    public JSONObject jsonObject(String key) {
        try {
            return jsonObject.getJSONObject(key);
        } catch (Exception e) {
            return null;
        }
    }

    public JSONArray jsonArray(String key) {
        try {
            return jsonObject.getJSONArray(key);
        } catch (Exception e) {
            return null;
        }
    }

}
