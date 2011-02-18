/**
 * 
 */
package it.unipd.netmus.server.persistent;


import com.vercer.engine.persist.annotation.Key;

import it.unipd.netmus.shared.SongDTO;
import it.unipd.netmus.shared.SongSummaryDTO;

/**
 * @author ValterTexasGroup
 *
 */
public class Song {

    @Key private String id;
    
	private String author;
	
	private String title;
	
	private String album;
	
	private String year;
	
	private String composer;
	
	private String genre;
	
	private String trackNumber;
	
	private String file;

	public Song(String auth,String tit,String alb,String y,String c,String g,String t,String f){

		author = auth;
		title = tit;
		album = alb;
		year = y;
		composer = c;
		genre = g;
		trackNumber = t;
		file = f;
	}

	public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getAuthor(){return author;}
	public String getTitle(){return title;}
	public String getAlbum(){return album;}
	public String getYear(){return year;}
	public String getComposer(){return composer;}
	public String getGenre(){return genre;}
	public String getTrackNumber(){return trackNumber;}
	public String getFile(){return file;}

	public void setAuthor(String auth){author = auth;}
	public void setTitle(String tit){title = tit;}
	public void setAlbum(String alb){album = alb;}
	public void setYear(String y){year = y;}
	public void setComposer(String c){composer = c;}
	public void setGenre(String g){genre = g;}
	public void setTrackNumber(String t){trackNumber = t;}
	public void setFile(String f){file = f;}

	// ritorna il rispettivo SongDTO
	public SongDTO toDTO(){
		return new SongDTO(author,title,album,year,composer,genre,trackNumber,file);
	}
	// ritorna il rispettivo SongSummaryDTO
	public SongSummaryDTO toSummaryDTO(){
		return new SongSummaryDTO(author,title,album);
	}

}
