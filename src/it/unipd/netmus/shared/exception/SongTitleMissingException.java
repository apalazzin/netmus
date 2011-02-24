package it.unipd.netmus.shared.exception;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class SongTitleMissingException extends DatastoreException {

    private List<String> possible_titles = new ArrayList<String>();
    private String song_id;

    public SongTitleMissingException(String song_id) {
        super();
        this.song_id = song_id;
    }
    
    public SongTitleMissingException(String song_id, String more_info) {
        super(more_info);
        this.song_id = song_id;
    }
    
    public List<String> getPossibleTitles() {
        return possible_titles;
    }

    public String getSong_id() {
        return song_id;
    }
}
