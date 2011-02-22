package it.unipd.netmus.server.youtube;

public class YouTubeMedia {

    private String location;
    private String type;

    public YouTubeMedia(String location, String type) {
        super();
        this.location = location;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
     
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}