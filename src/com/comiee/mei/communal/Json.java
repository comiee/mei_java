package com.comiee.mei.communal;

import java.util.Hashtable;
import java.util.List;

public class Json extends Hashtable<String, Object> {
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("{");
        for (String k : this.keySet()) {
            s.append("\"");
            s.append(k);
            s.append("\":");
            Object v = this.get(k);
            if (v instanceof Json) {
                s.append(v.toString());
            } else if (v instanceof String) {
                s.append("\"");
                s.append(v);
                s.append("\"");
            } else if (v instanceof List) {
                List list = (List) v;
                s.append("[");
                for (Object obj : list) {
                    s.append(obj);
                    if (obj != list.get(list.size() - 1)) {
                        s.append(",");
                    }
                }
                s.append("]");
            } else {
                s.append(v);
            }
            s.append(",");
        }
        if (s.charAt(s.length() - 1) == ',') {
            s.deleteCharAt(s.length() - 1);
        }
        s.append("}");
        return s.toString();
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
