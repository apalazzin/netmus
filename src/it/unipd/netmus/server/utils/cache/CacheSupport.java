package it.unipd.netmus.server.utils.cache;

import java.io.Serializable;
import java.util.Collections;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;
import net.sf.jsr107cache.CacheStatistics;

/**
 * Nome: CacheSupport.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 14 Marzo 2011
 */

public class CacheSupport {
    
    /*
     * Restituisce l'istanza della Memcache utilizzata; questo metodo è privato poichè la
     * configurazione della Memcache è interamente gestita in questa classe. 
     */
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

    /*
     * carica dalla mappa della Memcache l'oggetto la cui chiave è data in input
     */
    public static Object cacheGet(String id) {
      Cache cache;
      cache = CacheSupport.cacheInit();
      
      return cache.get(id);
    }

    /*
     * Ripulisce l'intera Memcache
     */
    public static void cacheClear() {
        Cache cache;
        cache = CacheSupport.cacheInit();
        
        cache.clear();
    }
    
    /*
     * Rimuove dalla Memcache l'oggetto relativo alla chiave data in input
     */
    public static void cacheRemove(String id) {
        Cache cache;
        cache = CacheSupport.cacheInit();
        
        cache.remove(id);
    }

    /*
     * Salva nella Memcache l'oggetto e la relativa chiave dati in input
     */
    public static void cachePut(String id, Serializable o) {
        Cache cache;
        cache = CacheSupport.cacheInit();
        
        cache.put(id, o);
    }
    
    public static void printStatistics(Cache cache) {
        CacheStatistics stats = cache.getCacheStatistics();
        
        System.out.println("Objects in cache: "+stats.getObjectCount());
        System.out.println("Cache hits: "+stats.getCacheHits());
        System.out.println("Cache misses: "+stats.getCacheMisses());
    }
}
