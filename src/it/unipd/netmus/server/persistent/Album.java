package it.unipd.netmus.server.persistent;

import it.unipd.netmus.server.utils.Utils;
import it.unipd.netmus.shared.FieldVerifier;

import com.google.code.twig.annotation.Id;

/**
 * Nome: MusicLibrary.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 10 Marzo 2011
 * 
 */

public class Album {

    public static String getAlbumCover(String name, String artist) {
        Album tmp = ODF.get().load().type(Album.class).id(FieldVerifier.generateAlbumId(name, artist)).now();
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
        Album tmp = ODF.get().load().type(Album.class).id(FieldVerifier.generateAlbumId(name, artist)).now();
        if (tmp != null) {
            if (!tmp.getCover().equals("")) {
                return tmp.getCover();
            }
            else {
                tmp.setCover(Utils.getCoverImage(artist, name));
                ODF.get().update(tmp);
                return tmp.getCover();
            }
        }
        else { 
            return "";
        }
    }
    
    @SuppressWarnings("unused")
    @Id
    private String id;
    
    private String cover;
    
    public Album() {}
    
    public Album(String name, String artist) throws Exception {
        setId(FieldVerifier.generateAlbumId(name, artist));
        setCover("");
        ODF.get().store().instance(this).ensureUniqueKey().now();
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

}
