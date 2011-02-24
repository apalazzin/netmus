package it.unipd.netmus.shared.exception;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class SongAlbumMissingException extends NetmusException {
    
    private List<String> possible_albums = new ArrayList<String>();
    private String song_id;

    public SongAlbumMissingException(String song_id) {
        super();
        this.song_id = song_id;
    }
    
    public SongAlbumMissingException(String song_id, String more_info) {
        super(more_info);
        this.song_id = song_id;
    }
    
    public List<String> getPossibleAlbums() {
        return possible_albums;
    }

    public String getSong_id() {
        return song_id;
    }

}
