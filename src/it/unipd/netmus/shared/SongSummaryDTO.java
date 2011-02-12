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
	
	public String getauthor() {
		return author;
	}
	public void setauthor(String author) {
		this.author = author;
	}
	public String gettitle() {
		return title;
	}
	public void settitle(String title) {
		this.title = title;
	}
	public String getalbum() {
		return album;
	}
	public void setalbum(String album) {
		this.album = album;
	}
}
