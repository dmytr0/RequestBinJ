package test;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class MainService {

    Map<Object, Object> lruMap;

    public MainService() {

        lruMap = createLRUMap(50);
    }

    public void add(String s, HttpHeaders headers, Map<String, String> params) {
        StringBuilder value = new StringBuilder();
        if (headers != null) {
            value.append("<font size=\"3\" color=\"red\">");
            value.append("<b>HEADERS:</b><br>");
            headers.forEach((h, v) -> value.append(h).append(": ").append(v).append("<br>"));
            lruMap.put(LocalDateTime.now(), value);
            value.append("</font>");
        }

        if (params != null) {
            value.append("<font size=\"3\" color=\"green\">");
            value.append("<br><b>PARAMS:</b><br>");
            params.forEach((h, v) -> value.append(h).append(": ").append(v).append("<br>"));
            lruMap.put(LocalDateTime.now(), value);
            value.append("</font>");
        }

        if (s != null) {
            value.append("<br><b>BODY:</b><br>");
            value.append(s);

        }
    }

    public String getAll() {
        StringBuilder sb = new StringBuilder();
        ArrayList<Map.Entry<Object, Object>> entries = new ArrayList<>(lruMap.entrySet());
        Collections.reverse(entries);
        entries.forEach(e -> sb.append(e.getKey()).append("<br>").append(e.getValue()).append("<br><hr><br>"));

        return sb.toString();
    }

    public static <K, V> Map<K, V> createLRUMap(final int maxEntries) {
        return new LinkedHashMap<K, V>(maxEntries * 10 / 7, 0.7f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > maxEntries;
            }


            //TODO If need define contains like access to node override like it:

            @Override
            public boolean containsKey(Object key) {
                return get(key) != null;
            }
        };
    }

    public void removeAll() {
        lruMap.clear();
    }
}
