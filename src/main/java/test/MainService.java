package test;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class MainService {

    Map<Object, Object> lruMap;

    public MainService() {

        lruMap = createLRUMap(50);
    }

    public void add(String s, HttpHeaders headers) {
        lruMap.put(LocalDateTime.now(), s + "<br>Headers:<br>" + headers);
    }

    public String getAll() {
        StringBuilder sb = new StringBuilder();
        lruMap.forEach((k, v) -> {
            sb.append(k).append("<br>").append(v).append("<br>------------------<br>");
        });

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
}
