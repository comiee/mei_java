package com.comiee.mei.communal;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Json extends LinkedHashMap<String, Object> {
    public static final Object Null = new Object();

    private static String dumpInt(Integer value) {
        return value.toString();
    }

    private static String dumpFloat(Double value) {
        return value.toString();
    }

    private static String dumpString(String value) {
        return "\"" + value + "\"";
    }

    private static String dumpBool(Boolean value) {
        return value.toString();
    }

    private static String dumpList(List<?> value) {
        StringBuilder s = new StringBuilder();
        s.append("[");
        for (Object obj : value) {
            s.append(obj);
            if (obj != value.get(value.size() - 1)) {
                s.append(",");
            }
        }
        s.append("]");
        return s.toString();
    }

    private static String dumpJson(Json value) throws Exception {

        StringBuilder s = new StringBuilder();
        s.append("{");
        for (String k : value.keySet()) {
            s.append(dumpString(k));
            s.append(":");
            Object v = value.get(k);
            s.append(dumpObject(v));
            s.append(",");
        }
        if (s.charAt(s.length() - 1) == ',') {
            s.deleteCharAt(s.length() - 1);
        }
        s.append("}");
        return s.toString();
    }

    private static String dumpObject(Object value) throws Exception {
        return switch (value) {
            case Integer i -> dumpInt(i);
            case Double d -> dumpFloat(d);
            case String s -> dumpString(s);
            case Boolean b -> dumpBool(b);
            case List<?> l -> dumpList(l);
            case Json j -> dumpJson(j);
            default -> {
                if (value == Null) {
                    yield "null";
                }
                throw new Exception("构建Json字符串失败，无法解析的对象：" + value);
            }
        };
    }

    public String dump() throws Exception {
        return dumpJson(this);
    }

    @Override
    public String toString() {
        try {
            return dump();
        } catch (Exception e) {
            e.printStackTrace();
            return super.toString();
        }
    }

    private static void Throw(String text) throws Exception {
        throw new Exception(text);
    }

    private static void Assert(boolean flag, String text) throws Exception {
        if (!flag) {
            Throw(text);
        }
    }

    private static int halfIndex(String string, char c) throws Exception {
        char target = switch (c) {
            case '{' -> '}';
            case '[' -> ']';
            case '"' -> '"';
            default -> throw new Exception("非成对字符");
        };
        int n = 1;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == c) {
                n++;
            } else if (string.charAt(i) == target) {
                n--;
            }
            if (n == 0) {
                return i;
            }
        }
        return string.length();
    } // TODO [}  ["]"]  [{]} "\""

    private static Json parseJson(String string) throws Exception {
        String text = "解析Json字符串失败，Json：" + string;
        Assert(string.charAt(0) == '{' && string.charAt(string.length() - 1) == '}', text);
        Json result = new Json();
        while (!string.isEmpty()) {
            int keyStart = string.indexOf('"');
            int keyEnd = string.indexOf('"', keyStart + 1);
            Assert(keyStart < keyEnd && keyStart != -1, text);
            String key = string.substring(keyStart + 1, keyEnd);
            Assert(string.charAt(keyEnd + 1) == ':', text);
            int valueStart = keyEnd + 2;
            int valueEnd = getValueEnd(string, valueStart);
            Object value = parseValue(string.substring(valueStart, valueEnd));
            result.put(key, value);
            string = string.substring(valueEnd + 1);
        }
        return result;
    }


    private static List<Object> parseList(String string) throws Exception {
        String text = "解析Json字符串失败，List：" + string;
        Assert(string.charAt(0) == '[' && string.charAt(string.length() - 1) == '}', text);
        List<Object> result = new LinkedList<>();
        while (!string.isEmpty()) {

        }
    }

    private static Object parseValue(String string) throws Exception {
        String text = "解析Json字符串失败，Object：" + string;
        if (string.charAt(0) == '{') {
            int n = 1;
            for (int i = 1; i < string.length(); i++) {
                n += switch (string.charAt(i)) {
                    case '{' -> 1;
                    case '}' -> -1;
                    default -> 0;
                };
                if (n == 0) {
                    return parseJson(string.substring(i + 1));
                }
            }
            Throw(text);
        } else if (string.charAt(0) == '[') {
            int n = 1;
            for (int i = 1; i < string.length(); i++) {
                n += switch (string.charAt(i)) {
                    case '[' -> 1;
                    case ']' -> -1;
                    default -> 0;
                };
                if (n == 0) {
                    return parseList(string.substring(i + 1));
                }
            }
            Throw(text);
        } else if (string.charAt(0) == '"') {
            for (int i = 1; i < string.length(); i++) {
                if (string.charAt(i) == '"' && string.charAt(i - 1) != '\\') {
                    return parseString(string.substring(i + 1));
                }
            }
            Throw(text);
        } else if (string.equals("true")) {
            return true;
        } else if (string.startsWith("false")) {
            return false;
        } else if (string.startsWith("null")) {
            return Null;
        }
        try {
            if (string.contains(".")) {
                return Double.valueOf(string);
            } else {
                return Integer.valueOf(string);
            }
        } catch (NumberFormatException e) {
            Throw(text);
        }
        return null;
    }

    public static Json parse(String string) {
        Json result = new Json();
        Json p = result;
        Stack<Object> stack = new Stack<>();
        StringBuilder s = new StringBuilder();
        boolean stringFlag = false;
        for (char c : string.toCharArray()) {
            if (stringFlag) {
                if (c == '"') {
                    stack.push(s.toString());
                    s.delete(0, s.length());
                    stringFlag = false;
                } else {
                    s.append(c);
                }
            } else {
                switch (c) {
                    case '{':
                        stack.push(s.toString());
                        s.delete(0, s.length());
                        break;
                    case '}':
                        Object v = stack.pop();
                        String k = (String) stack.pop();
                        p.put(k, v);

                    case '"':
                        stringFlag = true;
                        break;

                }
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Json other)) {
            return false;
        }
        if (!this.keySet().equals(other.keySet())) {
            return false;
        }
        for (String k : this.keySet()) {
            if (!this.get(k).equals(other.get(k))) {
                return false;
            }
        }
        return true;
    }

    public static Json of(Object... args) {
        Json json = new Json();
        for (int i = 0; i < args.length; i += 2) {
            String k = args[i].toString();
            Object v = args[i + 1];
            json.put(k, v);
        }
        return json;
    }
}
