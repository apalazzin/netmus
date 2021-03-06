package it.unipd.netmus.client.ui;

import java.util.List;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * Nome: ProfileView.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 16 Febbraio 2011
 */
public interface ProfileView extends IsWidget {

    public interface Presenter {
        /**
         * Aggiunge una nuova Playlist
         */
        void addPlaylist(String title);

        /**
         * Aggiunge song alla playlist e restituisce true in caso di successo
         */
        void addToPLaylist(String playlist, String autore, String titolo,
                String album);

        /**
         * Imposta la lingua dell'interfaccia su richiesta dell'utente
         * 
         * @param locale
         */
        void changeLanguage(String locale);

        /**
         * Elimina la playlist
         */
        void deletePlaylist(String playlist_name);

        /**
         * Elimina l'account dell'utente
         * 
         * @return
         */
        void deleteProfile();

        /**
         * Elimina la canzone
         */
        void deleteSong(String autore, String titolo, String album);

        /**
         * Invia i dati modficati quando viene premuti il pulsante salva
         */
        void editProfile(String user, String nick_name, String first_name,
                String last_name, String gender, String nationality,
                String aboutMe, String password);

        /**
         * Apre la sezione di visualizzazione/modifica del profilo personale
         */
        void editProfileView(String user);

        /**
         * Salva il nome dell'album che è stato modificato dall'utente
         */
        void editSongAlbum(String new_album, String old_album, String artist,
                String title);

        /**
         * Salva il nome dell'artista che è stato modificato dall'utente
         */
        void editSongArtist(String new_artist, String old_artist, String title,
                String album);

        /**
         * Salva il titolo della canzone che è stato modificato dall'utente
         */
        void editSongTitle(String new_title, String old_title, String artist,
                String album);

        /**
         * Esporta versione PDF del catalogo
         * 
         * @return
         */
        void exportDocLibrary();

        /**
         * Permette di spostarsi in un place differente anche relativo ad
         * un'altra view. Ad esempio per tornare alla pagina di LoginView se
         * l'utente non è autenticato. Verrà quindi chiamato nel metodo start se
         * un utente tenta di accedere al ProfileView senza essere autenticato.
         */
        void goTo(Place place);

        /**
         * Effettua il logout dell'utente, facendo scadere la sessione ed
         * eliminando i cookies relativi ad essa, comunicando con il
         * LoginService implementato nel server.
         */
        void logout();

        /**
         * Sposta in basso la canzone della playlist selezionata
         */
        void moveDownInPLaylist(String playlist, String autore, String titolo,
                String album);

        /**
         * Sposta in alto la canzone della playlist selezionata
         */
        void moveUpInPLaylist(String playlist, String autore, String titolo,
                String album);

        /**
         * Restituisce il link youtube della canzone selezionata
         */
        void playYouTube(String autore, String titolo, String album);

        /**
         * Attribuisce un punteggio compreso tra 1 e 5 alla canzone selezionata
         */
        void rateSong(String artist, String title, String album, int rate);

        /**
         * Rimuovi il brano dalla playlist.
         */
        void removeFromPLaylist(String playlist, String autore, String titolo,
                String album);

        /**
         * Restituisce la lista degli utenti affini su Netmus
         */
        void setFriendList();

        /**
         * Restituisce la lista dei titoli delle singole playlist dell'utente
         */
        void setPlaylistList();

        /**
         * Aggiorna la lista di canzoni della playlist
         */
        void setPlaylistSongs(String titoloPlaylist);

        /**
         * Restituisce il rating della canzone selezionata
         */
        double setRating(String artist, String title, String album);

        /**
         * Imposta la copertina del brano nella modalita' Covers
         * 
         * @return
         */
        void setSongCover(String autore, String titolo, String album,
                HTMLPanel tmp);

        /**
         * Imposta i campi della canzone selezionata.
         * 
         * @return
         */
        void setSongFields(String autore, String titolo, String album);

        /**
         * Restituisce il titolo della canzone in ascolto
         */
        void setSongInfo();

        /**
         * Imposta le statistiche nella view
         * 
         * @return
         */
        void setStats();

        /**
         * Controlla i permessi ed avvia la visualizzazione del profilo
         * dell'utente in input
         */
        void viewOtherLibrary(String user);

    };

    // classe interna che rappresenta un brano
    public abstract class Song {

        @Override
        public abstract boolean equals(Object o);
    }

    /**
     * Aggiungi il brano del catalogo alla playlist
     */
    void addToPLaylist(String autore, String titolo, String album);

    /**
     * Aggiunge una nuova playlist
     */
    void addToPlaylists(String titolo);

    /**
     * Chiude la finestra edit profile
     * 
     */
    void closeEditProfile();

    /**
     * Chiude il gestore della playlist
     */
    void closePlaylist();

    /**
     * Chiude la scheda della canzone
     */
    void closeSong();

    /**
     * Chiude il player youtube
     */
    void closeYouTube();

    /**
     * Elimina la canzone dal catalogo
     */
    void deleteSong(String autore, String titolo, String album);

    /**
     * Inserisce le canzoni date in input al catalogo
     */
    void paintCatalogo(List<String> lista_canzoni);

    /**
     * Riempie la lista dei contatti
     */
    void paintFriendlist(String[] lista);

    /**
     * Imposta la cover principale enlla UI
     * 
     */
    void paintMainCover(String cover);

    /**
     * Riempie la lista delle playlist
     */
    void paintPlaylist(String[] lista);

    /**
     * Riempie la singola playlist delle sue canzoni
     */
    void paintPlaylistSongs(List<String> lista);

    /**
     * Riproduce la canzone successiva a catalogo
     * 
     */
    void playNext();

    /**
     * Ripeoduce la canzone precedente a catalogo
     * 
     */
    void playPrev();

    /**
     * Apre il player youtube sulla relativa canzone
     */
    void playYouTube(String titolo);

    /**
     * Rimuovi il brano dalla playlist
     */
    void removeFromPlaylist(String autore, String titolo, String album);

    /**
     * Resetta l'intera view e i suoi contenuti
     * 
     */
    void resetView();

    /**
     * Imposta il brano selezionato sul catalogo
     */
    void setBranoCatalogo(Song selezione);

    /**
     * Imposta il brano selezionato sulla playlist
     */
    void setBranoPlaylist(Song selezione);

    /**
     * Imposta la finestra informativa
     */
    void setInfo(String testo);

    /**
     * Inizializza il layout
     */
    void setLayout();

    /**
     * Questo metodo viene usato da ProfileActivity per fornire il nome del
     * profilo alla crezione della ProfileView.
     */
    void setName(String profileName);

    /**
     * Questo metodo viene usato da ProfileActivity per fornire il numero di
     * brani alla crezione della ProfileView.
     */
    void setNumeroBrani(int numero);

    /**
     * Questo metodo viene usato da ProfileActivity per impostare una sua
     * istanza come implementazione del presenter di ProfileView.
     */
    void setPresenter(Presenter listener);

    /**
     * Imposta il rating personale dell’utente.
     */
    void setRating(int rating);

    /**
     * Imposta i campi della canzone nella scheda di dettaglio
     */
    void setSongFields(String autore, String titolo, String album,
            String genere, String anno, String compositore, String traccia,
            String cover);

    /**
     * Imposta le statistiche dell'utente
     * 
     */
    void setStats(String string, String string2, String string3, String string4);

    /**
     * Questo metodo viene usato daProfileActivity per fornire l’username
     * dell’utente alla crezione della ProfileView.
     */
    void setUser(String username);

    /**
     * Aggiorna carica dati dell'EditProifle
     */
    void showEditProfile(String nickname, String name, String surname,
            String nationality, String gender, String aboutme);

    /**
     * Segnala un errore/messaggio all'utente con richiesta di conferma
     * 
     */
    void showError(String text);

    /**
     * Segnala un errore/messaggio all'utente che si chiude da solo
     * 
     */
    void showErrorFast(String text);

    /**
     * Aggiorna il global rating
     */
    void showGlobalStar(double d);

    /**
     * Mostra il rating dato come input
     */
    void showStar(int index);

    /**
     * Ordina per artista (ordinamento lessicografico) le canzoni del catalogo
     */
    void sortCatalogo();

    /**
     * Mostra la barra di caricamento
     * 
     */
    void startLoading();

    /**
     * Nasconde la barra di caricamento
     * 
     */
    void stopLoading();

    /**
     * Visualizza la playlist scelta
     */
    void viewPlaylist(String playlist_name);

    /**
     * Visualizza la scheda della canzone
     */
    void viewSong(Song song);

    /**
     * Visualizza il tutorial
     */
    void showHelp();
}
