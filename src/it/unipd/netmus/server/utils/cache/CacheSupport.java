package it.unipd.netmus.server.utils.cache;

import java.io.Serializable;
import java.util.Collections;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;
import net.sf.jsr107cache.CacheStatistics;

public class CacheSupport {
    
    private static Cache cacheInit() {
        
        Cache cache;
        
        try {
            CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
            cache = cacheFactory.createCache(Collections.emptyMap());
            return cache;
        } catch (CacheException e) {
            return null;
        }
        
    }

    public static Object cacheGet(String id) {
      Cache cache;
      cache = CacheSupport.cacheInit();
      
      CacheSupport.printStatistics(cache);
      
      return cache.get(id);
    }

    public static void cacheClear() {
        Cache cache;
        cache = CacheSupport.cacheInit();
        
        CacheSupport.printStatistics(cache);
        
        cache.clear();
    }
    
    public static void cacheRemove(String id) {
        Cache cache;
        cache = CacheSupport.cacheInit();
        
        CacheSupport.printStatistics(cache);
        
        cache.remove(id);
    }

    public static void cachePut(String id, Serializable o) {
        Cache cache;
        cache = CacheSupport.cacheInit();
        
        CacheSupport.printStatistics(cache);
        
        cache.put(id, o);
    }
    
    public static void printStatistics(Cache cache) {
        CacheStatistics stats = cache.getCacheStatistics();
        
        System.out.println("Objects in cache: "+stats.getObjectCount());
        System.out.println("Cache hits: "+stats.getCacheHits());
        System.out.println("Cache misses: "+stats.getCacheMisses());
    }
}
