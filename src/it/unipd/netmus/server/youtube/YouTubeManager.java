package it.unipd.netmus.server.youtube;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import com.google.gdata.client.youtube.YouTubeQuery;
import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.VideoFeed;
import com.google.gdata.data.youtube.YouTubeMediaContent;
import com.google.gdata.data.youtube.YouTubeMediaGroup;
import com.google.gdata.util.ServiceException;

/**
 * Nome: YouTubeManager.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 17 Febbraio 2011
 */
public class YouTubeManager {

    private static final String YOUTUBE_URL = "http://gdata.youtube.com/feeds/api/videos";

    public static String getSearchResult(String keywords, String ip) throws IOException, ServiceException {

        YouTubeVideo youtubeVideo = null;
        youtubeVideo = retrieveVideo("NetmusProject", keywords, 2000, ip);

        if (youtubeVideo != null) {
            return youtubeVideo.getVideoCode();
        } else {
            return "";
        }
    }

    private static YouTubeVideo convertVideos(List<VideoEntry> video) {

        YouTubeVideo youtubevideo = new YouTubeVideo();

        if (video.size() == 0)
            youtubevideo = null;
        else {
            YouTubeMediaGroup mediaGroup = video.get(0).getMediaGroup();

            String webPlayerUrl = mediaGroup.getPlayer().getUrl();
            String query = "?v=";
            int start = webPlayerUrl.indexOf(query) + query.length();
            int end = webPlayerUrl.indexOf("&");
            String video_code = webPlayerUrl.substring(start, end);

            youtubevideo.setVideoCode(video_code);

            List<YouTubeMedia> medias = new LinkedList<YouTubeMedia>();
            for (YouTubeMediaContent mediaContent : mediaGroup
                    .getYouTubeContents()) {
                medias.add(new YouTubeMedia(mediaContent.getUrl(), mediaContent
                        .getType()));
            }
            youtubevideo.setMedias(medias);
        }

        return youtubevideo;
    }

    private static YouTubeVideo retrieveVideo(String clientID, String keywords,
            int timeout, String ip) throws IOException, ServiceException  {

        YouTubeService service = new YouTubeService(clientID);
        service.setConnectTimeout(timeout); // millis
        YouTubeQuery query = new YouTubeQuery(new URL(YOUTUBE_URL));

        query.setOrderBy(YouTubeQuery.OrderBy.RELEVANCE);
        query.setFullTextQuery(keywords);
        query.setMaxResults(1);
        query.setFormats(5);
        if (!ip.equals("")) query.setIpRestriction(ip);
        query.setSafeSearch(YouTubeQuery.SafeSearch.MODERATE);

        VideoFeed videoFeed = service.query(query, VideoFeed.class);
        List<VideoEntry> videos = videoFeed.getEntries();

        return convertVideos(videos);

    }

    public YouTubeManager() {
    }

}