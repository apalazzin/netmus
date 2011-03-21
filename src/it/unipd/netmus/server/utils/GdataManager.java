package it.unipd.netmus.server.utils;

import it.unipd.netmus.shared.exception.NetmusException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.google.gdata.client.DocumentQuery;
import com.google.gdata.client.docs.DocsService;
import com.google.gdata.data.MediaContent;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.docs.DocumentEntry;
import com.google.gdata.data.docs.DocumentListEntry;
import com.google.gdata.data.docs.DocumentListFeed;
import com.google.gdata.data.media.MediaByteArraySource;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

/**
 * Nome: GdataManager.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 14 Marzo 2011
 */

/**
 * La classe è un singleton che ha lo scopo di fornire i metodi per la gestione
 * (creazione, modifica, inserimento e rimozione) di documenti e di account
 * Google Docs.
 */

public class GdataManager {

    private static final String USERNAME = "netmus.docs@gmail.com";
    private static final String PASSWORD = "lusiaaavtg";

    // Unica istanza della classe GdataManager
    private static GdataManager manager;

    /**
     * Crea l’istanza di GdataManager.
     */
    public static GdataManager manager() {
        if (manager == null)
            manager = new GdataManager();
        return manager;
    }

    private DocsService client = null;

    // Istanza dell’account di autenticazione in Google Docs.
    private GdataManager() {
    }

    /**
     * Inizializza client e verifica i dati di accesso.
     */
    public DocsService client() throws AuthenticationException {
        if (client == null) {
            DocsService newClient = new DocsService("NetMus Library Maker");
            newClient.setUserCredentials(USERNAME, PASSWORD);
            client = newClient;
            client.setConnectTimeout(0);
            client.setReadTimeout(0);
        }
        return client;
    }

    /**
     * Crea un nuovo documento in base hai parametri titolo e contenuto.
     */
    public DocumentListEntry createNewDocument(String title, String content)
            throws ServiceException, NetmusException {
        try {
            String destFolderUrl = ((MediaContent) getFolder().getContent())
                    .getUri();
            URL feedUrl = new URL(destFolderUrl);
            DocumentListEntry newEntry = new DocumentEntry();
            newEntry.setTitle(new PlainTextConstruct(title));
            newEntry = client().insert(feedUrl, newEntry);
            newEntry.setEtag("*");
            newEntry.setMediaSource(new MediaByteArraySource(
                    content.getBytes(), "text/html"));
            newEntry.updateMedia(true);
            return newEntry;

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Restituisce il documento associato al parametro id.
     */
    public DocumentListEntry getDocument(String resourceId) throws Exception {
        try {
            URL feedUri = new URL(
                    "https://docs.google.com/feeds/default/private/full/"
                            + resourceId);
            return client().getEntry(feedUri, DocumentListEntry.class);
        } catch (MalformedURLException e) {
            throw new Exception("Incorrect resource ID: " + resourceId);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Error communicating with Google Docs servers");
        }
    }

    /**
     * Restituisce la cartella nella quale verrà creato il file, così da
     * applicare i permessi pubblici. Solo gli utenti reindirizzati a tale
     * documento avranno accesso al documento.
     */
    public DocumentListEntry getFolder() throws Exception {
        try {
            URL feedUri = new URL(
                    "https://docs.google.com/feeds/default/private/full/-/folder");
            DocumentQuery query = new DocumentQuery(feedUri);
            query.setTitleQuery("netmus-libraries");
            query.setTitleExact(true);
            query.setMaxResults(1);
            DocumentListFeed feed = client().getFeed(query,
                    DocumentListFeed.class);
            List<DocumentListEntry> entry = feed.getEntries();
            return entry.get(0);
        } catch (MalformedURLException e) {
            throw new Exception("Incorrect resource ID");
        } catch (IOException e) {
            throw new RuntimeException(
                    "Error communicating with Google Docs servers");
        }
    }
}