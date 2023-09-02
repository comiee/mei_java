package com.comiee.mei.communal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;

import java.lang.reflect.Array;
import java.util.*;

// 基于Gson自己实现Json
public class Json extends LinkedHashMap<String, Object> {
    private static final Gson gson = new GsonBuilder()
            .serializeNulls()
            .setNumberToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
            .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
            .create();

    static class JsonList extends ArrayList<Object> {
        JsonList(List<?> list) {
            addAll(list.stream().map(Json::toJsonValue).toList());
        }
    }

    private static Object toJsonValue(Object object) {
        if (object == null) {
            return null;
        } else if (object instanceof Map<?, ?> m) {
            return new Json(m);
        } else if (object instanceof List<?> l) {
            return new Json.JsonList(l);
        } else if (object.getClass().isArray()) {
            int length = Array.getLength(object);
            LinkedList<Object> list = new LinkedList<>();
            for (int i = 0; i < length; i++) {
                list.add(Array.get(object, i));
            }
            return new Json.JsonList(list);
        } else if (object instanceof Number n) {
            if (n.longValue() == n.doubleValue()) {
                return n.longValue();
            } else {
                return n.doubleValue();
            }
        }
        return object;
    }

    public Json() {
    }

    public Json(Map<?, ?> map) {
        for (Object key : map.keySet()) {
            put(key.toString(), toJsonValue(map.get(key)));
        }
    }


    @Override
    public String toString() {
        return dump();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Map<?, ?> obj)) {
            return false;
        }
        Json other = new Json(obj);
        if (!this.keySet().equals(other.keySet())) {
            return false;
        }
        for (String k : this.keySet()) {
            if (this.get(k) == null) {
                return other.get(k) == null;
            } else if (!this.get(k).equals(other.get(k))) {
                return false;
            }
        }
        return true;
    }

    public String dump() {
        return gson.toJson(this);
    }

    public static Json parse(String string) {
        return new Json(gson.fromJson(string, Json.class));
    }

    public static Json of(Object... args) {
        Json json = new Json();
        for (int i = 0; i < args.length; i += 2) {
            String k = args[i].toString();
            Object v = args[i + 1];
            json.put(k, toJsonValue(v));
        }
        return json;
    }
}
