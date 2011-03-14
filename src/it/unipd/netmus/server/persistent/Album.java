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

@SuppressWarnings("serial")
public class Album implements Serializable, Cacheable {

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
    
    public void store() {
        ODF.get().store().instance(this).ensureUniqueKey().now();
        this.addToCache();
    }
    
    public void update() {
        ODF.get().storeOrUpdate(this);
        this.addToCache();
    }
    
    public static String getAlbumCover(String name, String artist) {
        Album tmp = Album.load(FieldVerifier.generateAlbumId(name, artist));
        if (tmp != null) {
            return tmp.getCover();
        }
        else { 
            return "";
        }
    }
    
    public static boolean storeNewAlbum(String name, String artist) {
        try {
            new Album(name, artist);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
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
