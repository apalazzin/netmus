/**
 * 
 */
package it.unipd.netmus.shared;

/**
 * @author ValterTexasGroup
 *
 */
@SuppressWarnings("serial")
public class SongDTO extends SongSummaryDTO {
	private String year;
	private String composer;
	private String genre;
	private String trackNumber;
	private String file;

	
	//costruttore di default
	public SongDTO(){
		super();
		year = "";
		composer = "";
		genre = "";
		trackNumber = "";
		file = "";		
	}
	
	// costruttore con parametri summary
	public SongDTO(String author, String title, String album){
		super(author,title,album);
		year = "";
		composer = "";
		genre = "";
		trackNumber = "";
		file = "";
	}
	// costruttore senza parametri summary	
	public SongDTO(String y, String c, String g, String t, String f){
		super();
		year = y;
		composer = c;
		genre = g;
		trackNumber = t;
		file = f;
	}
	// costruttore completo
	public SongDTO(String author, String title, String album, String y, String c,String g, String t, String f){
		super(author,title,album);
		year = y;
		composer = c;
		genre = g;
		trackNumber = t;
		file = f;
	}
	
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getComposer() {
		return composer;
	}
	public void setComposer(String composer) {
		this.composer = composer;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getTrackNumber() {
		return trackNumber;
	}
	public void setTrackNumber(String trackNumber) {
		this.trackNumber = trackNumber;
	}

	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
}
