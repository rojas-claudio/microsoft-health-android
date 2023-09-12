package com.microsoft.krestsdk.services;

import com.microsoft.kapp.cache.CacheService;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public interface NetworkProvider {
    String executeHttpDelete(String str, Map<String, String> map) throws URISyntaxException, IOException;

    void executeHttpFileGetAndWriteToCache(String str, Map<String, String> map, CacheService cacheService, List<String> list) throws MalformedURLException, IOException;

    String executeHttpGet(String str, Map<String, String> map) throws URISyntaxException, IOException;

    String executeHttpGet(String str, Map<String, String> map, Map<String, String> map2) throws URISyntaxException, IOException;

    void executeHttpGetAndWriteToCache(String str, Map<String, String> map, CacheService cacheService, List<String> list) throws MalformedURLException, IOException;

    byte[] executeHttpGetBinary(String str, Map<String, String> map) throws URISyntaxException, IOException;

    String executeHttpPatch(String str, Map<String, String> map, String str2) throws URISyntaxException, IOException;

    String executeHttpPost(String str, Map<String, String> map, String str2) throws URISyntaxException, IOException;

    String executeHttpPost(String str, Map<String, String> map, Map<String, String> map2, String str2) throws URISyntaxException, IOException;

    String executeHttpPut(String str, Map<String, String> map, String str2) throws URISyntaxException, IOException;
}
