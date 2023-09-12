package com.microsoft.kapp.cache;

import java.io.InputStream;
import java.util.List;
/* loaded from: classes.dex */
public interface CacheService {
    void cleanup();

    String getCachedResponse(String str);

    InputStream getCachedResponseAsStream(String str);

    void handleLogout();

    boolean isResponseCached(String str);

    boolean put(String str, InputStream inputStream, List<String> list);

    boolean put(String str, String str2, List<String> list);

    boolean remove(String str);

    void removeAll();

    boolean removeForTag(String str);

    boolean removeForTags(List<String> list);
}
