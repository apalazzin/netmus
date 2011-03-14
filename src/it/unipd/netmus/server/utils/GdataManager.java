package it.unipd.netmus.server.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gdata.client.docs.DocsService;
import com.google.gdata.data.MediaContent;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.docs.DocumentEntry;
import com.google.gdata.data.docs.DocumentListEntry;
import com.google.gdata.data.media.MediaByteArraySource;
import com.google.gdata.data.media.MediaSource;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

/**
 * Nome: GdataManager.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 14 Marzo 2011
 * 
 */

public class GdataManager {

    private static final String USERNAME = "fede.baron.89@gmail.com";
    private static final String PASSWORD = "jiPEHQ12";

    private static GdataManager manager;
    
    private DocsService client = null;

    private GdataManager() {
    }
    
    public static GdataManager manager() {
        if(manager == null)
            manager = new GdataManager();
        return manager;
    }
    
    public DocsService client() throws AuthenticationException {
        if(client == null) {
            DocsService newClient = new DocsService("Document List Demo");
            newClient.setUserCredentials(USERNAME, PASSWORD);
            client = newClient;
        }
        return client;
    }
    
    public DocumentListEntry getDocument(String resourceId) throws Exception {
        try {
            URL feedUri = new URL("https://docs.google.com/feeds/default/private/full/" + resourceId);
            return client().getEntry(feedUri, DocumentListEntry.class);
        } catch (MalformedURLException e) {
            throw new Exception("Incorrect resource ID: " + resourceId);
        } catch (IOException e) {
            throw new RuntimeException("Error communicating with Google Docs servers");
        }
    }
    
    public InputStream getPdfInputStream(DocumentListEntry document) throws ServiceException {
        String exportUrl = ((MediaContent)document.getContent()).getUri() + "&exportFormat=pdf";
        MediaContent mc = new MediaContent();
        mc.setUri(exportUrl);
        try {
            MediaSource ms = client().getMedia(mc);
            return ms.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public InputStream getPdfInputStream(String resourceId) throws Exception {
        return getPdfInputStream(getDocument(resourceId));
    }
    
    public DocumentListEntry createNewDocument(String title, String content) throws ServiceException {
        try {
             URL feedUrl = new URL("https://docs.google.com/feeds/default/private/full/");
                DocumentListEntry newEntry = new DocumentEntry();
                newEntry.setTitle(new PlainTextConstruct(title));
                newEntry = client().insert(feedUrl, newEntry);
                newEntry.setEtag("*");
                newEntry.setMediaSource(new MediaByteArraySource(content.getBytes(), "text/html"));
                newEntry.updateMedia(true);
                return newEntry;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}