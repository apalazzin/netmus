package it.unipd.netmus.server.youtube;

public class YouTubeTester {
 
    public static void main(String[] args) throws Exception {
  
        String clientID = "NetmusVTG";
        String textQuery = "Boney m gotta go home";
        boolean filter = true;
        int timeout = 2000;
  
        YouTubeManager ym = new YouTubeManager(clientID);
  
        YouTubeVideo youtubeVideo = ym.retrieveVideo(textQuery, filter, timeout);
        
        if (youtubeVideo != null) {
            System.out.println(youtubeVideo.getWebPlayerUrl());
            System.out.println("Thumbnails");
            for (String thumbnail : youtubeVideo.getThumbnails()) {
                System.out.println("\t" + thumbnail);
            }
            System.out.println(youtubeVideo.getEmbeddedWebPlayerUrl());
            System.out.println("************************************");
        }
        else {
            System.out.println("Nada");
        }
    }

}