package it.unipd.netmus.server.utils.cache;

/**
 * Nome: Cacheable.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 14 Marzo 2011
 */

/*
 * Questa interfaccia è implementata da tutte le classi di persistenza di cui si voglia
 * anche una memoria cache. I metodi definiti sono necessari e sufficenti alla gestione
 * della Memcache.
 */
public interface Cacheable  {
    
    public void addToCache();
    
    public void removeFromCache();
    
}
