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
	
	private String albumCover;
	
	private int numOwners;
	
	//costruttore di default
	public SongDTO(){}
	
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

    public void setAlbumCover(String albumCover) {
        this.albumCover = albumCover;
    }

    public String getAlbumCover() {
        return albumCover;
    }

    public void setNumOwners(int numOwners) {
        this.numOwners = numOwners;
    }

    public int getNumOwners() {
        return numOwners;
    }
}
