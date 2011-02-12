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
	private String id;
	private String file;
	
	//default constructor
	public SongDTO(){
		this(new String(), new String(), new String(), new String(), new String(), new String(), new String(), new String(), new String());
	}
	
	public SongDTO(String author, String title, String album){
		this(new String(), author, title, album, new String(), new String(), new String(), new String(), new String());
	}
	
	public SongDTO(String year, String composer, String genre, String trackNumber, String file){
		this(new String(), new String(), new String(), new String(), year, composer, genre, trackNumber, file);
	}
	
	public SongDTO(String author, String title, String album, String year, String composer,
			String genre, String trackNumber, String file){
		this(new String(), author, title, album, year, composer, genre, trackNumber, file);
	}
	
	public SongDTO(String id, String author, String title, String album, String year, String composer,
			String genre, String trackNumber, String file){
		super(author,title,album);
		this.id = id;
		this.year = year;
		this.composer = composer;
		this.genre = genre;
		this.trackNumber = trackNumber;
		this.file = file;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
}
