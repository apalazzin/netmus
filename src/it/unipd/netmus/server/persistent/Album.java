package it.unipd.netmus.server.persistent;

import java.io.Serializable;

import it.unipd.netmus.server.utils.Utils;
import it.unipd.netmus.server.utils.cache.CacheSupport;
import it.unipd.netmus.server.utils.cache.Cacheable;
import it.unipd.netmus.shared.FieldVerifier;

import com.google.code.twig.annotation.Id;

/**
 * Nome: MusicLibrary.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 10 Marzo 2011
 * 
 */

/**
 * Questa classe opera come un contenitore per la lista degli album disponibili da 
 * cui gli altri oggetti possono attingere. Fornisce inoltre la businness logic 
 * necessaria alla ricerca esterna di copertine tramite le API di Last.fm ed alla 
 * ricerca interna al Datastore.
 * Implementa le interfacce Serializable e Cacheable poiché viene gli
 * album vengono gestiti anche nella Memcache. 
 * Ogni album è rappresentato da una chiave univoca (id) e dal link alla 
 * copertina ad esso associata. Questa classe ha lo scopo di mantenere nel Datastore 
 * tutte le copertine ricercate su Last.fm in modo ordinato, minimizzando così le 
 * richieste esterne.
 */

@SuppressWarnings("serial")
public class Album implements Serializable, Cacheable {

    /**
     * Legge dalla cache e, se non presente, dal Datastore l'album
     * la cui chiave univoca \`e data in input.
     */
    public static Album load(String id) {
        
        Album album = (Album) CacheSupport.cacheGet(id);
        
        if (album == null) {
            album = ODF.get().load().type(Album.class).id(id).now();
            if (album != null) {
                album.addToCache();
            }
        }
        
        return album;
    }
    
    /**
     * Inserisce nel database per la prima volta l'album. Se esiste già un album con lo 
     * stesso id lancia un'eccezione di tipo IllegalStateException. 
     */
    private void store() {
        ODF.get().store().instance(this).ensureUniqueKey().now();
        
        this.addToCache();
    }
    
    /**
     * Aggiorna o inserisce i dati dell'album nel DataStore.
     */
    public void update() {
        ODF.get().storeOrUpdate(this);
        this.addToCache();
    }
    
    /**
     * Ritorna il link alla copertina dell'album associato al nome e all'artista dati
     * in input. Se l'album non è presente nel Datastore o se la copertina non
     * è ancora stata ricercata su Last.fm viene ritornata la stringa vuota.
     */
    public static String getAlbumCover(String name, String artist) {
        Album tmp = Album.load(FieldVerifier.generateAlbumId(name, artist));
        if (tmp != null) {
            return tmp.getCover();
        }
        else { 
            return "";
        }
    }
    
    /**
     * Salva nel Datastore l'album con il nome e l'artista dati in input. Ritorna
     * false nel caso in cui l'album specificato sia già presente.
     */
    public static boolean storeNewAlbum(String name, String artist) {
        try {
            new Album(name, artist);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Ritorna il link alla copertina dell'album associato al nome e all'artista dati
     * in input. Se l'album non è presente nel Datastore viene effettuata
     * la ricerca esterna su Last.fm e ne viene salvato e ritornato il risultato.
     */
    public static String getAlbumCoverLastFm(String name, String artist) {
        Album tmp = Album.load(FieldVerifier.generateAlbumId(name, artist));
        if (tmp != null) {
            if (!tmp.getCover().equals("")) {
                return tmp.getCover();
            }
            else {
                tmp.setCover(Utils.getCoverImage(artist, name));
                tmp.update();
                return tmp.getCover();
            }
        }
        else { 
            return "";
        }
    }
    
    /**
     * Chiave univoca assegnata ad un album, è generata a
     * partire dal nome dell'album e dall'artista.
     */
    @Id
    private String id;
    
    private String cover;
    
    public Album() {}
    
    public Album(String name, String artist) throws Exception {
        setId(FieldVerifier.generateAlbumId(name, artist));
        setCover("");
        this.store();
    }

    private void setCover(String cover) {
        this.cover = cover;
    }

    private String getCover() {
        return cover;
    }

    private void setId(String id) {
        this.id = id;
    }

    @Override
    public void addToCache() {
        CacheSupport.cachePut(this.id, this);
    }

    @Override
    public void removeFromCache() {
        CacheSupport.cacheRemove(this.id);
    }

}
