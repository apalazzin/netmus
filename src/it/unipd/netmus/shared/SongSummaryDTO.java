/**
 * 
 */
package it.unipd.netmus.shared;

import java.io.Serializable;

/**
 * @author ValterTexasGroup
 *
 */
@SuppressWarnings("serial")
public class SongSummaryDTO implements Serializable {
	
	private String artist;
	private String title;
	private String album;
	
	public SongSummaryDTO(){
	}
	
	public SongSummaryDTO(String artist, String title, String album){
		this.artist = artist;
		this.title = title;
		this.album = album;
	}
	
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
}
