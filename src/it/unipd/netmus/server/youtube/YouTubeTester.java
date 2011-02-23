package it.unipd.netmus.server.youtube;

public class YouTubeTester {
    
    public static void main(String[] args) throws Exception {
  
        String[] keys = {"cane","manzo","rana","toro","giglio","rana toro"};
        int i =0;
        while (true) {
            i = (i+1)%5;
            String s = YouTubeManager.getSearchResult(keys[i]);
            System.out.println(s);
        }
    }
}