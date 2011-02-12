/**
 * 
 */
package it.unipd.netmus.shared;

/**
 * @author ValterTexasGroup
 *
 */
@SuppressWarnings("serial")
public class SongSummaryDTO implements GenericDTO {
	
	private String author;
	private String title;
	private String album;
	
	//default constructor
	public SongSummaryDTO(){
		this(new String(), new String(), new String());
	}
	
	public SongSummaryDTO(String author, String title, String album){
		this.author = author;
		this.title = title;
		this.album = album;
	}
	
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
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
