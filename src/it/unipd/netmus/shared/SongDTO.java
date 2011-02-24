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
	
	private String youtube_code;
	
	private String playme_code;
	
	private double rating;
	
	private int num_ratings;
	
	private int rating_for_this_user;
	
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

    public void setYoutubeCode(String youtube_code) {
        this.youtube_code = youtube_code;
    }

    public String getYoutubeCode() {
        return youtube_code;
    }

    public void setPlaymeCode(String playme_code) {
        this.playme_code = playme_code;
    }

    public String getPlaymeCode() {
        return playme_code;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getRating() {
        return rating;
    }

    public void setNumRatings(int num_ratings) {
        this.num_ratings = num_ratings;
    }

    public int getNumRatings() {
        return num_ratings;
    }

    public void setRatingForThisUser(int rating_for_this_user) {
        this.rating_for_this_user = rating_for_this_user;
    }

    public int getRatingForThisUser() {
        return rating_for_this_user;
    }
}
