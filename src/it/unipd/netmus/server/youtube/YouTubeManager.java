package it.unipd.netmus.server.youtube;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import com.google.gdata.client.youtube.YouTubeQuery;
import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.media.mediarss.MediaThumbnail;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.VideoFeed;
import com.google.gdata.data.youtube.YouTubeMediaContent;
import com.google.gdata.data.youtube.YouTubeMediaGroup;

public class YouTubeManager {
 
    private static final String YOUTUBE_URL = "http://gdata.youtube.com/feeds/api/videos";
    private static final String YOUTUBE_EMBEDDED_URL = "http://www.youtube.com/v/";
 
    private String clientID;
 
    public YouTubeManager(String clientID) {
        this.clientID = clientID;
    }
 
     public YouTubeVideo retrieveVideo(String textQuery, boolean filter, int timeout) throws Exception {
  
        YouTubeService service = new YouTubeService(clientID);
        service.setConnectTimeout(timeout); // millis
        YouTubeQuery query = new YouTubeQuery(new URL(YOUTUBE_URL));
  
        query.setOrderBy(YouTubeQuery.OrderBy.RELEVANCE);
        query.setFullTextQuery(textQuery);
        query.setSafeSearch(YouTubeQuery.SafeSearch.MODERATE);
        query.setMaxResults(1);

        VideoFeed videoFeed = service.query(query, VideoFeed.class);  
        List<VideoEntry> videos = videoFeed.getEntries();
  
        return convertVideos(videos);
  
    }
 
    private YouTubeVideo convertVideos(List<VideoEntry> video) {
  
        YouTubeVideo youtubevideo = new YouTubeVideo();
        
        if (video.size()==0)
            youtubevideo = null;
        else {
            YouTubeMediaGroup mediaGroup = video.get(0).getMediaGroup();
            String webPlayerUrl = mediaGroup.getPlayer().getUrl();
            youtubevideo.setWebPlayerUrl(webPlayerUrl);

            String query = "?v=";
            int index = webPlayerUrl.indexOf(query);

            String embeddedWebPlayerUrl = webPlayerUrl.substring(index+query.length());
            embeddedWebPlayerUrl = YOUTUBE_EMBEDDED_URL + embeddedWebPlayerUrl;
            youtubevideo.setEmbeddedWebPlayerUrl(embeddedWebPlayerUrl);

            List<String> thumbnails = new LinkedList<String>();
            for (MediaThumbnail mediaThumbnail : mediaGroup.getThumbnails()) {
                thumbnails.add(mediaThumbnail.getUrl());
            }   
            youtubevideo.setThumbnails(thumbnails);

            List<YouTubeMedia> medias = new LinkedList<YouTubeMedia>();
            for (YouTubeMediaContent mediaContent : mediaGroup.getYouTubeContents()) {
                medias.add(new YouTubeMedia(mediaContent.getUrl(), mediaContent.getType()));
            }
            youtubevideo.setMedias(medias);
        }
  
        return youtubevideo;
  
    }
 
}