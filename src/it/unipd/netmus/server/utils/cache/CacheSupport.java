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

/**
 * CacheSupport è composta da soli metodi statici che forniscono le principali
 * funzioni disponibili sulla Memcache. Questi servizi sono disponibili a tutte
 * le componenti del lato server. Rende disponibili all'esterno le funzioni
 * basilari di lettura e scrittura sulla memoria cache in modo completamente
 * trasparente rispetto all'implmentazione tramite JCache e soprattutto
 * all'utilizzo della CacheFactory.
 */

public class CacheSupport {

    /**
     * Ripulisce l'intera Memcache
     */
    public static void cacheClear() {
        Cache cache;
        cache = CacheSupport.cacheInit();

        cache.clear();
    }

    /**
     * carica dalla mappa della Memcache l'oggetto la cui chiave è data in input
     */
    public static Object cacheGet(String id) {
        Cache cache;
        cache = CacheSupport.cacheInit();

        return cache.get(id);
    }

    /**
     * Salva nella Memcache l'oggetto e la relativa chiave dati in input
     */
    public static void cachePut(String id, Serializable o) {
        Cache cache;
        cache = CacheSupport.cacheInit();

        cache.put(id, o);
    }

    /**
     * Rimuove dalla Memcache l'oggetto relativo alla chiave data in input
     */
    public static void cacheRemove(String id) {
        Cache cache;
        cache = CacheSupport.cacheInit();

        cache.remove(id);
    }

    public static void printStatistics(Cache cache) {
        CacheStatistics stats = cache.getCacheStatistics();

        System.out.println("Objects in cache: " + stats.getObjectCount());
        System.out.println("Cache hits: " + stats.getCacheHits());
        System.out.println("Cache misses: " + stats.getCacheMisses());
    }

    /**
     * Restituisce l'istanza della Memcache utilizzata; questo metodo è privato
     * poichè la configurazione della Memcache è interamente gestita in questa
     * classe.
     */
    private static Cache cacheInit() {

        Cache cache;

        try {
            CacheFactory cacheFactory = CacheManager.getInstance()
                    .getCacheFactory();
            cache = cacheFactory.createCache(Collections.emptyMap());
            return cache;
        } catch (CacheException e) {
            return null;
        }

    }
}
