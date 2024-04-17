package com.comiee.mei.communal;

import com.google.gson.*;

import java.lang.reflect.Array;
import java.util.*;

public class JsonTool {
    private static final Gson gson = new GsonBuilder()
            .serializeNulls()
            .setNumberToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
            .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
            .create();

    public static JsonElement toJsonElement(Object object) {
        if (object == null) {
            return JsonNull.INSTANCE;
        } else if (object instanceof JsonElement elem) {
            return elem;
        } else if (object instanceof Map<?, ?> map) {
            JsonObject jsonObject = new JsonObject();
            for (Object key : map.keySet()) {
                jsonObject.add(key.toString(), toJsonElement(map.get(key)));
            }
            return jsonObject;
        } else if (object instanceof List<?> list) {
            JsonArray jsonArray = new JsonArray();
            for (Object item : list) {
                jsonArray.add(toJsonElement(item));
            }
            return jsonArray;
        } else if (object.getClass().isArray()) {
            int length = Array.getLength(object);
            JsonArray jsonArray = new JsonArray(length);
            for (int i = 0; i < length; i++) {
                jsonArray.add(toJsonElement(Array.get(object, i)));
            }
            return jsonArray;
        } else if (object instanceof Number num) {
            return new JsonPrimitive(num);
        } else if (object instanceof String str) {
            return new JsonPrimitive(str);
        } else if (object instanceof Boolean bool) {
            return new JsonPrimitive(bool);
        } else if (object instanceof Character chr) {
            return new JsonPrimitive(chr);
        }
        return null;
    }

    public static String dump(JsonElement jsonElement) {
        return gson.toJson(jsonElement);
    }

    public static JsonObject parse(String string) {
        return gson.fromJson(string, JsonObject.class);
    }

    public static <T> T getFromObject(JsonObject jsonObject, String key, Class<T> cls) {
        return getFromObject(jsonObject, key, cls, null);
    }

    public static <T> T getFromObject(JsonObject jsonObject, String key, Class<T> cls, T defaultValue) {
        if (!jsonObject.has(key)) {
            return defaultValue;
        }
        JsonElement jsonElement = jsonObject.get(key);
        if (JsonElement.class.isAssignableFrom(cls)) {
            if (cls.isInstance(jsonElement)) {
                return cls.cast(jsonElement);
            }
        } else {
            if (!jsonElement.isJsonPrimitive()) {
                return defaultValue;
            }
            JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
            return getFromPrimitive(jsonPrimitive, cls);
        }
        return defaultValue;
    }

    public static <T> T getFromPrimitive(JsonPrimitive jsonPrimitive, Class<T> cls) {
        return getFromPrimitive(jsonPrimitive, cls, null);
    }

    public static <T> T getFromPrimitive(JsonPrimitive jsonPrimitive, Class<T> cls, T defaultValue) {
        if (String.class.isAssignableFrom(cls) && jsonPrimitive.isString()) {
            return cls.cast(jsonPrimitive.getAsString());
        } else if (Boolean.class.isAssignableFrom(cls) && jsonPrimitive.isBoolean()) {
            return cls.cast(jsonPrimitive.getAsBoolean());
        } else if (Number.class.isAssignableFrom(cls) && jsonPrimitive.isNumber()) {
            return cls.cast(jsonPrimitive.getAsNumber());
        }
        return defaultValue;
    }

    public static JsonObject createObject(Object... args) {
        JsonObject jsonObject = new JsonObject();
        for (int i = 0; i < args.length; i += 2) {
            String k = args[i].toString();
            Object v = args[i + 1];
            jsonObject.add(k, toJsonElement(v));
        }
        return jsonObject;
    }

    public static JsonArray createArray(Object... args) {
        JsonArray jsonArray = new JsonArray();
        for (Object x : args) {
            jsonArray.add(toJsonElement(x));
        }
        return jsonArray;
    }
}
