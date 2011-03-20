package it.unipd.netmus.client.activity;

import it.unipd.netmus.client.ClientFactory;
import it.unipd.netmus.client.applet.AppletBar;
import it.unipd.netmus.client.event.DeviceScannedEvent;
import it.unipd.netmus.client.event.DeviceScannedEventHandler;
import it.unipd.netmus.client.place.LoginPlace;
import it.unipd.netmus.client.place.ProfilePlace;
import it.unipd.netmus.client.service.LibraryService;
import it.unipd.netmus.client.service.LibraryServiceAsync;
import it.unipd.netmus.client.service.LoginService;
import it.unipd.netmus.client.service.LoginServiceAsync;
import it.unipd.netmus.client.service.SongService;
import it.unipd.netmus.client.service.SongServiceAsync;
import it.unipd.netmus.client.service.UserService;
import it.unipd.netmus.client.service.UserServiceAsync;
import it.unipd.netmus.client.ui.MyConstants;
import it.unipd.netmus.client.ui.ProfileView;
import it.unipd.netmus.shared.FieldVerifier;
import it.unipd.netmus.shared.MusicLibraryDTO;
import it.unipd.netmus.shared.MusicLibraryDTO.PlaylistDTO;
import it.unipd.netmus.shared.SongDTO;
import it.unipd.netmus.shared.SongSummaryDTO;
import it.unipd.netmus.shared.UserCompleteDTO;
import it.unipd.netmus.shared.exception.LoginException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * Nome: ProfileActivity.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 15 Febbraio 2011
 */

public class ProfileActivity extends AbstractActivity implements
        ProfileView.Presenter {

    private ClientFactory client_factory;

    private String name;

    @SuppressWarnings("unused")
    private boolean is_owner; // not used yet

    private LoginServiceAsync login_service_svc = GWT
            .create(LoginService.class);
    private LibraryServiceAsync library_service_svc = GWT
            .create(LibraryService.class);
    private UserServiceAsync user_service_svc = GWT.create(UserService.class);
    private SongServiceAsync song_service_svc = GWT.create(SongService.class);

    private UserCompleteDTO current_user;

    private Map<String, SongDTO> info_alredy_loaded = new HashMap<String, SongDTO>();

    private Map<String, String> cover_alredy_loaded = new HashMap<String, String>();

    MyConstants my_constants = GWT.create(MyConstants.class);

    public ProfileActivity(ProfilePlace place, ClientFactory client_factory) {
        this.name = place.getProfileName();
        this.client_factory = client_factory;
    }

    /**
     * Inserisce nel DB la nuova playlist creata dall'utente, se ha un nome non
     * gia' occupato, ed aggiorna la grafica se ha successo
     */
    @Override
    public void addPlaylist(String playlist_name) {

        final String cleared_playlist_name = clearPlaylistName(playlist_name);

        client_factory.getProfileView().startLoading();
        library_service_svc.addPlaylist(current_user.getUser(),
                cleared_playlist_name, new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        client_factory.getProfileView().showError(
                                my_constants.addPlaylistError());
                        client_factory.getProfileView().stopLoading();
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        if (result) {
                            current_user.getMusicLibrary().addPlaylist(
                                    new PlaylistDTO(cleared_playlist_name));
                            setPlaylistList();
                        }
                        client_factory.getProfileView().stopLoading();
                    }
                });
    }

    /**
     * Aggiunge song alla playlist e restituisce true in caso di successo
     */
    @Override
    public void addToPLaylist(String playlist_name, final String artist,
            final String title, final String album) {

        client_factory.getProfileView().startLoading();

        final String cleared_playlist_name = clearPlaylistName(playlist_name);

        library_service_svc.addSongToPlaylist(current_user.getUser(),
                cleared_playlist_name, title, artist, album,
                new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        client_factory.getProfileView().showError(
                                my_constants.addSongToPlaylistError());
                        client_factory.getProfileView().stopLoading();
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        if (result) {
                            client_factory.getProfileView().addToPLaylist(
                                    artist, title, album);
                            current_user.getMusicLibrary().addSongToPlaylist(
                                    cleared_playlist_name,
                                    FieldVerifier.generateSongId(title, artist,
                                            album));
                            setPlaylistList();
                        }
                        client_factory.getProfileView().stopLoading();
                    }
                });
    }

    /**
     * Modifica l'URL del browser per cambiare lingua, utilizzando la gestione
     * automatica dell'intenazionalizzazione offerta da GWT.
     */
    @Override
    public void changeLanguage(String locale) {

        String actual = Window.Location.getHref();

        if (locale.equals("it")) {
            if (!actual.contains("locale=it")) {
                String param = "locale=it";
                if (actual.contains("?")) {
                    param = "&" + param;
                    if (actual.contains("#"))
                        actual = actual.replace("#", param + "#");
                    else
                        actual = actual + param;
                } else {
                    param = "?" + param;
                    if (actual.contains(".html"))
                        actual = actual.replace(".html", ".html" + param);
                    else if (actual.contains("/")) {
                        int i = actual.lastIndexOf("/");
                        actual = actual.substring(0, i + 1) + param
                                + actual.substring(i + 1);
                    }
                }
                Window.Location.assign(actual);
            }
        } else {
            if (actual.contains("locale=it")) {
                String param = "locale=it";
                if (actual.contains("&locale=it"))
                    param = "&locale=it";
                else {
                    if (actual.contains("?locale=it&"))
                        param = "locale=it&";
                    else if (actual.contains("?locale=it"))
                        param = "?locale=it";
                }
                Window.Location.assign(actual.replace(param, ""));
            }
        }
    }

    /**
     * Rimuove dal DB una playlist, e se ha successo, aggiorna la grafica.
     */
    @Override
    public void deletePlaylist(String playlist_name) {

        client_factory.getProfileView().startLoading();

        final String cleared_playlist_name = clearPlaylistName(playlist_name);

        library_service_svc.removePlaylist(current_user.getUser(),
                cleared_playlist_name, new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        client_factory.getProfileView().showError(
                                my_constants.removePlaylistError());
                        client_factory.getProfileView().stopLoading();
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        if (result) {
                            current_user.getMusicLibrary().removePlaylist(
                                    cleared_playlist_name);
                            setPlaylistList();
                        }
                        client_factory.getProfileView().stopLoading();
                    }
                });
    }

    /**
     * Cancella l'utente definitivamente dal sistema e lo fa uscire dalla
     * pagina.
     */
    @Override
    public void deleteProfile() {
        // DEVE ELIMINARE L'UTENTE
        user_service_svc.deleteProfile(current_user.getUser(),
                new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        client_factory.getProfileView().showError(
                                my_constants.deleteProfileError());
                        client_factory.getProfileView().stopLoading();
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        logout();
                        client_factory.getProfileView().closeEditProfile();
                        // refresh della view <- sempre con logout e onStop
                    }
                });
    }

    /**
     * Elimina la canzone
     */
    @Override
    public void deleteSong(final String artist, final String title,
            final String album) {

        client_factory.getProfileView().startLoading();
        song_service_svc.deleteSong(current_user.getUser(), artist, title,
                album, new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        client_factory.getProfileView().showError(
                                my_constants.deleteSongError());
                        client_factory.getProfileView().stopLoading();
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        // una volta eliminata dal database la elimino anche
                        // sulla view
                        if (result) {
                            client_factory.getProfileView().deleteSong(artist,
                                    title, album);
                            String id = FieldVerifier.generateSongId(title,
                                    artist, album);
                            current_user.getMusicLibrary().getSongs()
                                    .remove(id);
                            for (PlaylistDTO tmp : current_user
                                    .getMusicLibrary().getPlaylists()) {
                                current_user.getMusicLibrary()
                                        .removeSongFromPlaylist(tmp.getName(),
                                                id);
                            }
                            setPlaylistList();
                        }
                        client_factory.getProfileView().stopLoading();
                    }
                });
    }

    /**
     * Invia i dati modficati quando viene premuti il pulsante salva
     */
    @Override
    public void editProfile(String user, String nick_name, String first_name,
            String last_name, String gender, String nationality,
            String aboutMe, String password) {

        client_factory.getProfileView().startLoading();
        UserCompleteDTO new_user_data = new UserCompleteDTO();

        new_user_data.setNickName(nick_name);
        new_user_data.setFirstName(first_name);
        new_user_data.setLastName(last_name);
        new_user_data.setGender(gender);
        new_user_data.setNationality(nationality);
        new_user_data.setAboutMe(aboutMe);
        new_user_data.setNewPassword(password);

        current_user.setNickName(nick_name);
        current_user.setFirstName(first_name);
        current_user.setLastName(last_name);
        current_user.setGender(gender);
        current_user.setNationality(nationality);
        current_user.setAboutMe(aboutMe);

        client_factory.getProfileView().showEditProfile(nick_name, first_name,
                last_name, nationality, gender, aboutMe);

        user_service_svc.editProfile(user, new_user_data,
                new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        client_factory.getProfileView().showError(
                                my_constants.editProfileError());
                        client_factory.getProfileView().stopLoading();
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        client_factory.getProfileView().stopLoading();
                    }
                });
    }

    /**
     * Apre la sezione di visualizzazione/modifica del profilo personale
     */
    @Override
    public void editProfileView(String user) {

        String nickname = current_user.getNickName();
        String firstname = current_user.getFirstName();
        String lastname = current_user.getLastName();
        String nationality = current_user.getNationality();
        String gender = current_user.getGender();
        String aboutme = current_user.getAboutMe();

        client_factory.getProfileView().showEditProfile(nickname, firstname,
                lastname, nationality, gender, aboutme);
    }

    /**
     * Salva il nome dell'album che è stato modificato dall'utente
     */
    @Override
    public void editSongAlbum(String new_album, String old_album,
            String artist, String title) {
        // TODO Auto-generated method stub

    }

    /**
     * Salva il nome dell'artista che è stato modificato dall'utente
     */
    @Override
    public void editSongArtist(String new_artist, String old_artist,
            String title, String album) {
        // TODO Auto-generated method stub

    }

    /**
     * Salva il titolo della canzone che è stato modificato dall'utente
     */
    @Override
    public void editSongTitle(String new_title, String old_title,
            String artist, String album) {
        // TODO Auto-generated method stub

    }

    /**
     * Richiede al server l'esportazione in Google Doc del catalogo dell'utente
     * e, una volta generato, lo mostra in una nuova finestra del browser.
     */
    @Override
    public void exportDocLibrary() {

        client_factory.getProfileView().startLoading();

        library_service_svc.generateDoc(current_user.getUser(),
                new AsyncCallback<String>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        client_factory.getProfileView().showError(
                                my_constants.generatePDFError());
                        client_factory.getProfileView().stopLoading();
                    }

                    @Override
                    public void onSuccess(String result) {
                        client_factory.getProfileView().stopLoading();

                        Window.open(result, "_blank", "");
                    }
                });
    }

    /**
     * Crea e restituisce un'array contenente la lista di playlist dell'utente
     * loggato.
     */
    public String[] getPlaylistList() {

        List<String> playlists = new ArrayList<String>();
        for (PlaylistDTO tmp : current_user.getMusicLibrary().getPlaylists()) {
            int real_size = 0;
            for (String tmp2 : tmp.getSongList()) {
                SongSummaryDTO song = current_user.getMusicLibrary().getSongs()
                        .get(tmp2);
                if (song != null) {
                    if (!song.getTitle().equals("")) {
                        real_size++;
                    }
                }
            }
            playlists.add(tmp.getName() + " (" + real_size + ")");
        }
        String[] playlist_array = new String[playlists.size()];
        playlist_array = playlists.toArray(playlist_array);

        return playlist_array;
    }

    /**
     * Fa disegnare alla ProfileView la lista brani di una playlist tramite il
     * metodo paintPlaylistSongs.
     */
    public void getPlaylistSongs(String playlist_name) {

        client_factory.getProfileView().startLoading();

        final String cleared_playlist_name = clearPlaylistName(playlist_name);

        List<String> song_list = new ArrayList<String>();

        for (PlaylistDTO tmp : current_user.getMusicLibrary().getPlaylists()) {
            if (tmp.getName().equals(cleared_playlist_name)) {
                for (String tmp2 : tmp.getSongList()) {
                    SongSummaryDTO song = current_user.getMusicLibrary()
                            .getSongs().get(tmp2);
                    if (song != null) {
                        if (!song.getTitle().equals("")) {
                            song_list.add(song.getArtist());
                            song_list.add(song.getTitle());
                            song_list.add(song.getAlbum());
                        }
                    }
                }
            }
        }

        client_factory.getProfileView().paintPlaylistSongs(song_list);
        client_factory.getProfileView().stopLoading();
    }

    /**
     * Crea e restituisce le informazioni testuali relative al brano in
     * riproduzione.
     */
    public String getSongInfo() {
        return my_constants.info();
    }

    /**
     * Permette di spostarsi in un place differente anche relativo ad un'altra
     * view. Ad esempio per tornare alla pagina di LoginView se l'utente non è
     * autenticato. Verrà quindi chiamato nel metodo start se un utente tenta di
     * accedere al ProfileView senza essere autenticato.
     */
    @Override
    public void goTo(Place place) {
        client_factory.getPlaceController().goTo(place);
    }

    /**
     * Restituisce true se il visitatore è proprietario del profilo
     */
    public void isOwner(boolean is_owner) {
        this.is_owner = is_owner;
    }

    /**
     * Effettua il logout dell'utente, facendo scadere la sessione ed eliminando
     * i cookies relativi ad essa, comunicando con il LoginService implementato
     * nel server.
     */
    @Override
    public void logout() {

        client_factory.getProfileView().startLoading();

        login_service_svc.logout(new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                client_factory.getProfileView().showError(
                        my_constants.loadProfileError());
                client_factory.getProfileView().stopLoading();
            }

            @Override
            public void onSuccess(String user) {

                // remove the session cookie
                Cookies.removeCookie("user");
                Cookies.removeCookie("sid");

                // hide and disable the applet
                AppletBar.get(user).appletBarOFF();
                client_factory.getProfileView().stopLoading();
                goTo(new LoginPlace(""));
            }
        });
    }

    /**
     * Ask user before stopping this activity
     */
    @Override
    public String mayStop() {
        return my_constants.leavingProfilePage();
    }

    /**
     * Sposta in basso la canzone della playlist selezionata
     */
    @Override
    public void moveDownInPLaylist(String playlist_name, String artist,
            String title, String album) {

        client_factory.getProfileView().startLoading();

        final String cleared_playlist_name = clearPlaylistName(playlist_name);

        // Cerca la playlist tra tutte quelle dell'utente corrente
        for (final PlaylistDTO tmp : current_user.getMusicLibrary()
                .getPlaylists()) {
            if (tmp.getName().equals(cleared_playlist_name)) {

                // Cerca la posizione della canzone nella playlist
                final int from = tmp.getSongList().indexOf(
                        FieldVerifier.generateSongId(title, artist, album));

                if (from < tmp.getSongList().size() - 1) {
                    // Sposta la canzone indietro nella playlist
                    library_service_svc.moveSongInPlaylist(
                            current_user.getUser(), cleared_playlist_name,
                            from, from + 1, new AsyncCallback<Boolean>() {

                                @Override
                                public void onFailure(Throwable caught) {
                                    client_factory.getProfileView().showError(
                                            my_constants.moveSongError());
                                    client_factory.getProfileView()
                                            .stopLoading();
                                }

                                @Override
                                public void onSuccess(Boolean result) {

                                    // Aggiorna i dati mantenuti lato client
                                    if (result) {
                                        String rm = tmp.getSongList().remove(
                                                from);
                                        tmp.getSongList().add(from + 1, rm);
                                        getPlaylistSongs(cleared_playlist_name);
                                    }

                                    client_factory.getProfileView()
                                            .stopLoading();
                                }
                            });
                } else {
                    ;
                    client_factory.getProfileView().stopLoading();
                }
                return;
            }
        }
    }

    /**
     * Sposta in alto la canzone della playlist selezionata
     */
    @Override
    public void moveUpInPLaylist(String playlist_name, String artist,
            String title, String album) {

        client_factory.getProfileView().startLoading();

        final String cleared_playlist_name = clearPlaylistName(playlist_name);

        // Cerca la playlist tra tutte quelle dell'utente corrente
        for (final PlaylistDTO tmp : current_user.getMusicLibrary()
                .getPlaylists()) {
            if (tmp.getName().equals(cleared_playlist_name)) {

                // Cerca la posizione della canzone nella playlist
                final int from = tmp.getSongList().indexOf(
                        FieldVerifier.generateSongId(title, artist, album));

                if (from > 0) {
                    // Sposta la canzone avanti nella playlist
                    library_service_svc.moveSongInPlaylist(
                            current_user.getUser(), cleared_playlist_name,
                            from, from - 1, new AsyncCallback<Boolean>() {

                                @Override
                                public void onFailure(Throwable caught) {
                                    client_factory.getProfileView().showError(
                                            my_constants.moveSongError());
                                    client_factory.getProfileView()
                                            .stopLoading();
                                }

                                @Override
                                public void onSuccess(Boolean result) {
                                    // Aggiorna i dati mantenuti lato client
                                    if (result) {
                                        String rm = tmp.getSongList().remove(
                                                from);
                                        tmp.getSongList().add(from - 1, rm);
                                        getPlaylistSongs(cleared_playlist_name);
                                    }

                                    // Visualizza la nuova lista canzoni della
                                    // playlist
                                    client_factory.getProfileView()
                                            .stopLoading();
                                }
                            });
                } else {
                    client_factory.getProfileView().stopLoading();
                }
                return;
            }
        }

    }

    /**
     * Restituisce il link youtube della canzone selezionata
     */
    @Override
    public void playYouTube(final String artist, final String title,
            final String album) {

        client_factory.getProfileView().startLoading();
        final String song_id = FieldVerifier.generateSongId(title, artist,
                album);

        // ricerca nella mappa delle info già caricate
        final SongDTO song_dto = info_alredy_loaded.get(song_id);

        if (song_dto != null) {

            // La canzone è già stata caricata una volta e le informazioni sono
            // disponibili
            // nel client.
            Timer timerPlay = new Timer() {
                @Override
                public void run() {
                    if (song_dto.getYoutubeCode().equals("")) {
                        // client_factory.getProfileView().closeYouTube();
                        client_factory.getProfileView().playYouTube(
                                "00000000000");
                        client_factory.getProfileView().showErrorFast(
                                my_constants.notYoutube() + title + "\"");
                        client_factory.getProfileView().playNext();
                    } else {
                        // client_factory.getProfileView().closeYouTube();
                        client_factory.getProfileView().playYouTube(
                                song_dto.getYoutubeCode());
                        client_factory.getProfileView().setInfo(
                                title + " - " + artist + " - " + album);
                    }

                    if (!song_dto.getAlbumCover().equals(""))
                        client_factory.getProfileView().paintMainCover(
                                song_dto.getAlbumCover());
                    else
                        client_factory.getProfileView().paintMainCover(
                                "images/test_cover.jpg");

                    client_factory.getProfileView().stopLoading();
                }
            };
            timerPlay.schedule(5);

            return;
        } else {

            Map<String, SongSummaryDTO> songs = current_user.getMusicLibrary()
                    .getSongs();

            // caricamento dalla mappa della canzone selezionata
            final SongSummaryDTO current_song_summary_dto = songs
                    .get(FieldVerifier.generateSongId(title, artist, album));

            song_service_svc.getSongDTO(current_song_summary_dto,
                    new AsyncCallback<SongDTO>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            client_factory.getProfileView().showError(
                                    my_constants.getSongDTOError());
                            client_factory.getProfileView().stopLoading();
                        }

                        @Override
                        public void onSuccess(SongDTO song_dto) {

                            // Imposta la copertina di default in caso non ce ne
                            // siano altre
                            if (song_dto.getAlbumCover().equals("")) {
                                song_dto.setAlbumCover("images/test_cover.jpg");
                            }

                            // Mostra la copertina relativa alla canzone
                            client_factory.getProfileView().paintMainCover(
                                    song_dto.getAlbumCover());

                            // Salva le informazioni caricate dal server in una
                            // mappa cache diposnibile nel client
                            info_alredy_loaded.put(song_id, song_dto);
                            cover_alredy_loaded.put(FieldVerifier
                                    .generateAlbumId(album, artist), song_dto
                                    .getAlbumCover());

                            // Se il video non è disponibile passa alla canzone
                            // successiva
                            if (song_dto.getYoutubeCode().equals("")) {
                                // client_factory.getProfileView().closeYouTube();
                                client_factory.getProfileView().playYouTube(
                                        "00000000000");
                                client_factory.getProfileView().showErrorFast(
                                        my_constants.notYoutube() + title
                                                + "\"");
                                client_factory.getProfileView().playNext();
                            } else {
                                // client_factory.getProfileView().closeYouTube();
                                client_factory.getProfileView().playYouTube(
                                        song_dto.getYoutubeCode());
                                client_factory.getProfileView().setInfo(
                                        title + " - " + artist + " - " + album);
                            }

                            client_factory.getProfileView().stopLoading();

                            return;
                        }
                    });
        }
    }

    /**
     * Attribuisce un punteggio compreso tra 1 e 5 alla canzone selezionata
     */
    @Override
    public void rateSong(final String artist, final String title,
            final String album, final int rate) {

        client_factory.getProfileView().startLoading();
        // sistemo subito il rating visivo poi arrivera' la library aggiornata
        client_factory.getProfileView().setRating(rate);
        client_factory.getProfileView().showStar(rate);

        SongSummaryDTO new_song_dto = new SongSummaryDTO(artist, title, album);

        song_service_svc.rateSong(current_user.getUser(), new_song_dto, rate,
                new AsyncCallback<Double>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        client_factory.getProfileView().showError(
                                my_constants.rateSongError());
                        client_factory.getProfileView().stopLoading();
                    }

                    @Override
                    public void onSuccess(Double result) {
                        Map<String, SongSummaryDTO> songs = current_user
                                .getMusicLibrary().getSongs();
                        SongSummaryDTO song = songs.get(FieldVerifier
                                .generateSongId(title, artist, album));

                        song.setRatingForThisUser(rate);
                        song.setRating(result);
                        client_factory.getProfileView().showGlobalStar(result);

                        // Ricalcolo delle statistiche relative al rating delle
                        // canzoni del catalogo
                        List<String> song_statistics = calculateSongStatistics(current_user
                                .getMusicLibrary().getSongs());
                        String most_popular_song = song_statistics.get(0);
                        String most_popular_song_for_this_user = song_statistics
                                .get(1);

                        // Salvataggio delle statistiche nella libreria
                        // mantenuta nel client
                        current_user.getMusicLibrary().setMostPopularSong(
                                most_popular_song);
                        current_user.getMusicLibrary()
                                .setMostPopularSongForThisUser(
                                        most_popular_song_for_this_user);

                        // Salvataggio delle statistiche nel Datasotre
                        library_service_svc.storeStatistics(
                                current_user.getUser(), "", most_popular_song,
                                most_popular_song_for_this_user,
                                new AsyncCallback<Void>() {
                                    @Override
                                    public void onFailure(Throwable caught) {
                                        client_factory
                                                .getProfileView()
                                                .showError(
                                                        my_constants
                                                                .storeStatisticsError());
                                        client_factory.getProfileView()
                                                .stopLoading();
                                    }

                                    @Override
                                    public void onSuccess(Void result) {
                                    }
                                });

                        client_factory.getProfileView().stopLoading();
                    }

                });
    }

    /**
     * Rimuovi il brano dalla playlist.
     */
    @Override
    public void removeFromPLaylist(final String playlist_name,
            final String artist, final String title, final String album) {

        client_factory.getProfileView().startLoading();

        final String cleared_playlist_name = clearPlaylistName(playlist_name);

        library_service_svc.removeSongFromPlaylist(current_user.getUser(),
                cleared_playlist_name, title, artist, album,
                new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        client_factory.getProfileView().showError(
                                my_constants.removeSongFromPlaylistError());
                        client_factory.getProfileView().stopLoading();
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        if (result) {
                            client_factory.getProfileView().removeFromPlaylist(
                                    artist, title, album);

                            for (PlaylistDTO tmp : current_user
                                    .getMusicLibrary().getPlaylists()) {
                                if (tmp.getName().equals(cleared_playlist_name)) {
                                    tmp.getSongList().remove(
                                            FieldVerifier.generateSongId(title,
                                                    artist, album));
                                }
                            }

                            setPlaylistList();
                        }
                        client_factory.getProfileView().stopLoading();
                    }
                });
    }

    /**
     * Restituisce, dopo aver richiesto le informazioni al server, la lista
     * degli utenti che hanno una libreria simile a quella dell'utente.
     */
    @Override
    public void setFriendList() {

        user_service_svc.findRelatedUsers(current_user.getUser(),
                new AsyncCallback<List<String>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        client_factory.getProfileView().showError(
                                my_constants.findRelatedUsersError());
                        client_factory.getProfileView().stopLoading();
                    }

                    @Override
                    public void onSuccess(List<String> related_users) {

                        String[] names = new String[related_users.size()];

                        for (int i = 0; i < related_users.size(); i++) {
                            names[i] = related_users.get(i);
                        }

                        client_factory.getProfileView().paintFriendlist(names);
                    }

                });

    }

    /**
     * Deve andare ad invocare il metodo paintPlaylist di ProfileView per andare
     * a graficare la lista delle playlist generata da getPlaylistList.
     */
    @Override
    public void setPlaylistList() {
        client_factory.getProfileView().paintPlaylist(getPlaylistList());
    }

    /**
     * Aggiorna la lista di canzoni della playlist
     */
    @Override
    public void setPlaylistSongs(String playlist_name) {

        final String cleared_playlist_name = clearPlaylistName(playlist_name);

        getPlaylistSongs(cleared_playlist_name);
    }

    /**
     * Restituisce il rating della canzone selezionata
     */
    @Override
    public double setRating(String artist, String title, String album) {

        Map<String, SongSummaryDTO> songs = current_user.getMusicLibrary()
                .getSongs();
        SongSummaryDTO song = songs.get(FieldVerifier.generateSongId(title,
                artist, album));

        if (song != null) {
            client_factory.getProfileView().setRating(
                    song.getRatingForThisUser());
            client_factory.getProfileView().showStar(
                    song.getRatingForThisUser());
            client_factory.getProfileView().showGlobalStar(song.getRating());
            return song.getRating();
        } else {
            client_factory.getProfileView().showError(
                    my_constants.removeSongFromPlaylistError());
            return -1;
        }

    }

    /**
     * Imposta i campi della canzone selezionata.
     */
    @Override
    public void setSongCover(final String artist, final String title,
            final String album, final HTMLPanel img) {

        client_factory.getProfileView().startLoading();

        String cover = cover_alredy_loaded.get(FieldVerifier.generateAlbumId(
                album, artist));

        if (cover != null) {

            img.getElement().getStyle()
                    .setBackgroundImage("url('" + cover + "')");

            client_factory.getProfileView().stopLoading();

            return;
        }

        Map<String, SongSummaryDTO> songs = current_user.getMusicLibrary()
                .getSongs();
        final SongSummaryDTO song = songs.get(FieldVerifier.generateSongId(
                title, artist, album));

        cover = "";

        song_service_svc.getCoverImage(song, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                client_factory.getProfileView().showError(
                        my_constants.getSongDTOError());
                client_factory.getProfileView().stopLoading();
            }

            @Override
            public void onSuccess(String cover) {

                // Imposta la copertina di default nel caso in cui non sia stata
                // trovata
                if (cover.equals("")) {
                    cover = "images/test_cover.jpg";
                }

                // salva le info caricate in modo che siano sempre disponibili
                // nel client
                cover_alredy_loaded.put(
                        FieldVerifier.generateAlbumId(album, artist), cover);

                // Mostra la copertina
                img.getElement().getStyle()
                        .setBackgroundImage("url('" + cover + "')");

                client_factory.getProfileView().stopLoading();
                return;
            }
        });

        img.getElement().getStyle().setBackgroundImage("url('" + cover + "')");

        client_factory.getProfileView().stopLoading();

    }

    /**
     * Imposta i campi della canzone selezionata.
     */
    @Override
    public void setSongFields(final String artist, final String title,
            final String album) {

        client_factory.getProfileView().startLoading();
        final String song_id = FieldVerifier.generateSongId(title, artist,
                album);

        // ricerca nella mappa delle info già caricate
        SongDTO song_dto = info_alredy_loaded.get(song_id);

        if (song_dto != null) {

            // Riempimento default dei campi
            String genere = "----";
            String anno = "----";
            String compositore = "----";
            String traccia = "----";
            String cover = "images/test_cover.jpg";

            // se le info erano già state caricate precedentemente vengono prese
            // ed utilizzate immediatamente
            if (!song_dto.getGenre().equals(""))
                genere = song_dto.getGenre();
            if (!song_dto.getYear().equals(""))
                anno = song_dto.getYear();
            if (!song_dto.getComposer().equals(""))
                compositore = song_dto.getComposer();
            if (!song_dto.getTrackNumber().equals(""))
                traccia = song_dto.getTrackNumber();
            if (!song_dto.getAlbumCover().equals(""))
                cover = song_dto.getAlbumCover();

            // richiesta di visualizzazione delle info dettagliate
            // nell'interfaccia grafica
            client_factory.getProfileView().setSongFields(artist, title, album,
                    genere, anno, compositore, traccia, cover);

            client_factory.getProfileView().stopLoading();

            return;
        } else {
            Map<String, SongSummaryDTO> songs = current_user.getMusicLibrary()
                    .getSongs();
            final SongSummaryDTO current_song_summary_dto = songs.get(song_id);

            song_service_svc.getSongDTO(current_song_summary_dto,
                    new AsyncCallback<SongDTO>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            client_factory.getProfileView().showError(
                                    my_constants.getSongDTOError());
                            client_factory.getProfileView().stopLoading();
                        }

                        @Override
                        public void onSuccess(SongDTO song_dto) {

                            // Imposta la copertina di default in caso non ce ne
                            // siano altre
                            if (song_dto.getAlbumCover().equals("")) {
                                song_dto.setAlbumCover("images/test_cover.jpg");
                            }

                            // salva le info caricate in modo che siano sempre
                            // disponibili nel client
                            info_alredy_loaded.put(song_id, song_dto);

                            // Riempimento default dei campi
                            String genere = "----";
                            String anno = "----";
                            String compositore = "----";
                            String traccia = "----";
                            String cover = "images/test_cover.jpg";

                            if (!song_dto.getGenre().equals(""))
                                genere = song_dto.getGenre();
                            if (!song_dto.getYear().equals(""))
                                anno = song_dto.getYear();
                            if (!song_dto.getComposer().equals(""))
                                compositore = song_dto.getComposer();
                            if (!song_dto.getTrackNumber().equals(""))
                                traccia = song_dto.getTrackNumber();
                            if (!song_dto.getAlbumCover().equals(""))
                                cover = song_dto.getAlbumCover();

                            // richiesta di visualizzazione delle info
                            // dettagliate nell'interfaccia grafica
                            client_factory.getProfileView().setSongFields(
                                    artist, title, album, genere, anno,
                                    compositore, traccia, cover);

                            client_factory.getProfileView().stopLoading();
                            return;
                        }
                    });
        }
    }

    /**
     * Deve andare ad invocare il metodo setInfo di ProfileView per andare a
     * graficare le informazioni del brano in riproduzione accanto al player,
     * fornite da getSongInfo.
     */
    @Override
    public void setSongInfo() {

        client_factory.getProfileView().setInfo(getSongInfo());
    }

    /**
     * Comunica alla view, tramite il metodo della view setStats, i dati
     * rigurdanti la pagina delle statistiche.
     */
    @Override
    public void setStats() {
        MusicLibraryDTO library = current_user.getMusicLibrary();

        // numero di canzoni del catalogo
        String num_songs = String.valueOf(library.getSongs().size());

        // artista preferito
        String preferred_artist = library.getPreferred_artist();

        // canzone più votata dall'utente
        String most_popular_song_for_this_user = "";
        SongSummaryDTO song = library.getSongs().get(
                library.getMostPopularSongForThisUser());
        if (song != null) {
            most_popular_song_for_this_user = song.getTitle() + " ("
                    + song.getArtist() + ")";
        } else {
            most_popular_song_for_this_user = my_constants
                    .noVoteForThisUserError();
        }

        // canzone più votata del catalogo
        String most_popular_song = "";
        song = library.getSongs().get(library.getMostPopularSong());
        if (song != null) {
            most_popular_song = song.getTitle() + " (" + song.getArtist() + ")";
        } else {
            most_popular_song = my_constants.noVoteError();
        }

        // Visualizzazione del pop-up delle statistiche
        client_factory.getProfileView().setStats(num_songs, preferred_artist,
                most_popular_song_for_this_user, most_popular_song);
    }

    /**
     * Invocato da ActivityManager per avviare effettivamente una nuova
     * ProfileActivity.
     */
    @Override
    public void start(final AcceptsOneWidget container_widget,
            EventBus event_bus) {

        client_factory.getProfileView().resetView();
        login_service_svc.getLoggedInUser(new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof LoginException) {
                    goTo(new LoginPlace(""));
                } else {
                    client_factory.getProfileView().showError(
                            my_constants.getSongDTOError());
                    client_factory.getProfileView().stopLoading();
                }
            }

            @Override
            public void onSuccess(final String user) {

                client_factory.getProfileView().setUser(user);
                final ProfileView profileView = client_factory.getProfileView();

                // Gestore degli eventi DeviceScannedEvent. Questo metodo viene
                // invocato al termine
                // di ogni inserimento di canzoni (sia da USB che da file
                // system)
                client_factory.getEventBus().addHandler(
                        DeviceScannedEvent.TYPE,
                        new DeviceScannedEventHandler() {

                            // L'evento porta con se la lista delle nuove
                            // canzoni inserite, queste
                            // vanno ad aggiornare la libreria dell'utente
                            // mantenuta nell'activity
                            @Override
                            public void onScanDevice(DeviceScannedEvent event) {

                                List<String> tmp_list = new ArrayList<String>();

                                for (SongDTO tmp : event.getNewSongs()) {
                                    if (!tmp.getTitle().equals("")) {

                                        if (current_user
                                                .getMusicLibrary()
                                                .getSongs()
                                                .put(FieldVerifier.generateSongId(
                                                        tmp.getTitle(),
                                                        tmp.getArtist(),
                                                        tmp.getAlbum()),
                                                        new SongSummaryDTO(tmp
                                                                .getArtist(),
                                                                tmp.getTitle(),
                                                                tmp.getAlbum())) == null) {
                                            tmp_list.add(tmp.getArtist());
                                            tmp_list.add(tmp.getTitle());
                                            tmp_list.add(tmp.getAlbum());
                                        }
                                    }
                                }

                                client_factory.getProfileView().paintCatalogo(
                                        tmp_list);

                                if (event.isLastSongs()) {
                                    client_factory.getProfileView()
                                            .sortCatalogo();

                                    String preferred_artist = calculatePreferredArtist(current_user
                                            .getMusicLibrary().getSongs());

                                    // Aggiornamento delle statistiche calcolate
                                    // nel catalogo mantenuto nel client
                                    current_user.getMusicLibrary()
                                            .setPreferred_artist(
                                                    preferred_artist);

                                    // Aggiornamento delle statistiche calcolate
                                    // nel Datastore
                                    library_service_svc.storeStatistics(
                                            current_user.getUser(),
                                            preferred_artist, "", "",
                                            new AsyncCallback<Void>() {
                                                @Override
                                                public void onFailure(
                                                        Throwable caught) {
                                                    client_factory
                                                            .getProfileView()
                                                            .showError(
                                                                    my_constants
                                                                            .storeStatisticsError());

                                                }

                                                @Override
                                                public void onSuccess(
                                                        Void result) {
                                                }
                                            });

                                    // Aggiornamento della lista degli utenti
                                    // affini
                                    setFriendList();

                                }
                            }

                        });

                profileView.setName(name);
                profileView.setPresenter(ProfileActivity.this);

                // inizializzazione dell'UserCompleteDTO mantenuto nell'activity
                user_service_svc.loadProfile(user,
                        new AsyncCallback<UserCompleteDTO>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                client_factory.getProfileView().showError(
                                        my_constants.loadProfileError());
                            }

                            @Override
                            public void onSuccess(UserCompleteDTO result) {
                                current_user = result;

                                List<String> tmp = new ArrayList<String>();
                                for (SongSummaryDTO dto : result
                                        .getMusicLibrary().getSongs().values()) {
                                    tmp.add(dto.getArtist());
                                    tmp.add(dto.getTitle());
                                    tmp.add(dto.getAlbum());
                                }
                                client_factory.getProfileView().paintCatalogo(
                                        tmp);
                                client_factory.getProfileView().sortCatalogo();

                                setPlaylistList();
                                setFriendList();
                                profileView.setUser(user);
                                profileView.setInfo(getSongInfo());
                                editProfileView(user);

                                container_widget.setWidget(profileView
                                        .asWidget());
                                profileView.setLayout();

                            }
                        });

                // load the applet bar, if not active yet
                AppletBar.get(user).appletBarON();
            }

        });

    }

    /**
     * Controlla i permessi ed avvia la visualizzazione del profilo dell'utente
     * in input
     */
    @Override
    public void viewOtherLibrary(String user) {
        // Controlla che l'utente desiderato abbia un profilo pubblico verso
        // l'utente attualmente autenticato
        // Se ha i permessi accedi al catalogo
        goTo(new ProfilePlace(user));
    }

    /**
     * Ricerca all'interno della libreria data in input l'artista più ricorrente
     * tra tutte le canzoni.
     */
    private String calculatePreferredArtist(
            Map<String, SongSummaryDTO> all_songs_map) {
        client_factory.getProfileView().startLoading();

        String preferred_artist = "";
        List<String> all_artists = new ArrayList<String>();

        for (SongSummaryDTO tmp : all_songs_map.values()) {
            all_artists.add(tmp.getArtist());
        }

        int max = 0;
        int count;
        List<String> toBeRemoved = new ArrayList<String>();
        while (all_artists.size() > max) {
            String tmp = all_artists.get(0);
            if (!tmp.equals("")) {
                count = 0;
                toBeRemoved.clear();
                for (String it : all_artists)
                    if (!it.equals("") && it.equals(tmp)) {
                        count++;
                        toBeRemoved.add(it);
                    }
                if (count > max) {
                    max = count;
                    preferred_artist = tmp;
                }
                all_artists.removeAll(toBeRemoved);
            } else
                all_artists.remove(tmp);
        }

        client_factory.getProfileView().stopLoading();

        return preferred_artist;
    }

    /**
     * Ricerca all'interno della libreria data in input la canzone che è stata
     * valutata meglio da tutti gli utenti che la condividono e quella preferita
     * dall'utente che possiede la libreria. Ne ritorna gli id in una lista.
     */
    private List<String> calculateSongStatistics(
            Map<String, SongSummaryDTO> all_songs_map) {
        client_factory.getProfileView().startLoading();

        SongSummaryDTO most_popular_song = null;
        SongSummaryDTO most_popular_song_for_this_user = null;
        double max1 = 0;
        int max2 = 0;

        for (SongSummaryDTO tmp : all_songs_map.values()) {
            if (tmp.getRating() > max1) {
                max1 = tmp.getRating();
                most_popular_song = tmp;
            }
            if (tmp.getRatingForThisUser() > max2) {
                max2 = tmp.getRatingForThisUser();
                most_popular_song_for_this_user = tmp;
            }
        }

        List<String> tmp = new ArrayList<String>(2);
        if (most_popular_song != null) {
            tmp.add(FieldVerifier.generateSongId(most_popular_song.getTitle(),
                    most_popular_song.getArtist(), most_popular_song.getAlbum()));
        } else {
            tmp.add("");
        }
        if (most_popular_song_for_this_user != null) {
            tmp.add(FieldVerifier.generateSongId(
                    most_popular_song_for_this_user.getTitle(),
                    most_popular_song_for_this_user.getArtist(),
                    most_popular_song_for_this_user.getAlbum()));
        } else {
            tmp.add("");
        }

        client_factory.getProfileView().stopLoading();

        return tmp;
    }

    /**
     * Pulisce i nomi delle playlist in entrata dalla view poiche questi
     * contengono anche il numero di brani al loro interno.
     */
    private String clearPlaylistName(String name) {
        if (name.lastIndexOf('(') > 0) {
            name = name.substring(0, name.lastIndexOf('(') - 1);
            name = name.trim();
        }
        return name;
    }
}
