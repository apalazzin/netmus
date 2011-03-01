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
import it.unipd.netmus.shared.MusicLibraryDTO;
import it.unipd.netmus.shared.SongDTO;
import it.unipd.netmus.shared.SongSummaryDTO;
import it.unipd.netmus.shared.UserCompleteDTO;
import it.unipd.netmus.shared.exception.LoginException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

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
    private boolean is_owner;

    private LoginServiceAsync login_service_svc = GWT
            .create(LoginService.class);
    private LibraryServiceAsync library_service_svc = GWT
            .create(LibraryService.class);
    private UserServiceAsync user_service_svc = GWT.create(UserService.class);
    private SongServiceAsync song_service_svc = GWT.create(SongService.class);

    private UserCompleteDTO current_user;

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
    public void addPlaylist(final String playlist_name) {

        library_service_svc.addPlaylist(current_user.getUser(), playlist_name,
                new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable caught) {
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        if (result) {
                            current_user.getMusicLibrary().addPlaylist(
                                    playlist_name);
                            client_factory.getProfileView().paintPlaylist(
                                    getPlaylistList());
                        }
                    }
                });
        // clientFactory.getProfileView().addToPlaylists(title);
    }

    /**
     * Aggiunge song alla playlist e restituisce true in caso di successo
     */
    @Override
    public void addToPLaylist(String playlist, final String autore,
            final String titolo, final String album) {

        String song_id = titolo + "-vt.g-" + autore + "-vt.g-" + album;
        song_id = song_id.toLowerCase();
        library_service_svc.addSongToPlaylist(current_user.getUser(), playlist,
                song_id, new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable caught) {
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        if (result)
                            client_factory.getProfileView().addToPLaylist(
                                    autore, titolo, album);
                    }
                });
    }

    /**
     * Rimuove dal DB una playlist, e se ha successo, aggiorna la grafica.
     */
    @Override
    public void deletePlaylist(final String playlist_name) {

        library_service_svc.removePlaylist(current_user.getUser(),
                playlist_name, new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable caught) {
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        if (result) {
                            current_user.getMusicLibrary().removePlaylist(
                                    playlist_name);
                            client_factory.getProfileView().paintPlaylist(
                                    getPlaylistList());
                        }
                    }
                });
        // clientFactory.getProfileView().addToPlaylists(title);
    }

    /**
     * Elimina la canzone
     */
    @Override
    public void deleteSong(final String autore, final String titolo,
            final String album) {

        song_service_svc.deleteSong(current_user.getUser(), autore, titolo,
                album, new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable caught) {
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        // una volta eliminata dal database la elimino anche
                        // sulla view
                        if (result)
                            client_factory.getProfileView().deleteSong(autore,
                                    titolo, album);
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
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        System.out.println("Profilo utente aggiornato");
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
     * Esporta la lista delle canzoni in pdf
     */
    @Override
    public void exportPDF(String user) {
        // TODO Auto-generated method stub

    }

    /**
     * Crea e restituisce un'array contenente la lista di utenti affini
     * all'utente loggato.
     */
    public String[] getFriendList() {
        // TODO Auto-generated method stub
        String[] friends = { "Alberto Palazzin", "Andrea Mandolo",
                "Cosimo Caputo", "Daniele Donte", "Federicon Baron",
                "Simone Daminato", "Alberto Palazzin", "Andrea Mandolo",
                "Cosimo Caputo", "Daniele Donte", "Federicon Baron",
                "Simone Daminato", "Alberto Palazzin", "Andrea Mandolo",
                "Cosimo Caputo", "Daniele Donte", "Federicon Baron",
                "Simone Daminato" };
        return friends;
    }

    /**
     * Crea e restituisce un'array contenente la lista di playlist dell'utente
     * loggato.
     */
    public String[] getPlaylistList() {

        List<String> playlists = current_user.getMusicLibrary().getPlaylists();
        String[] playlist_array = new String[playlists.size()];
        playlist_array = playlists.toArray(playlist_array);

        return playlist_array;
    }

    /**
     * Fa disegnare alla \co{ProfileView} la lista brani di una playlist tramite
     * il metodo \emph{paintPlaylistSongs}.
     */
    public List<String> getPlaylistSongs(String playlist_name) {

        library_service_svc.getPlaylist(current_user.getUser(), playlist_name,
                new AsyncCallback<List<SongSummaryDTO>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                    }

                    @Override
                    public void onSuccess(List<SongSummaryDTO> playlist_songs) {

                        List<String> song_list = new ArrayList<String>();
                        Iterator<SongSummaryDTO> it = playlist_songs.iterator();
                        while (it.hasNext()) {
                            SongSummaryDTO song = it.next();
                            song_list.add(song.getArtist());
                            song_list.add(song.getTitle());
                            song_list.add(song.getAlbum());
                        }
                        client_factory.getProfileView().paintPlaylistSongs(
                                song_list);
                    }
                });

        List<String> lista_canzoni = new ArrayList<String>();

        // RIEMPIRE LA LISTA CON LA SEQUENZA: autore1, titolo1, album1, autore2,
        // titolo2, album2, autoreN, titoloN, albumN,...
        return lista_canzoni;

    }

    /**
     * Crea e restituisce le informazioni testuali relative al brano in
     * riproduzione.
     */
    public String getSongInfo() {
        // TODO Auto-generated method stub
        return "Nessun brano in ascolto.";
    }

    public List<String> getSongs(MusicLibraryDTO user_library) {

        List<SongDTO> library = user_library.getSongs();

        List<String> song_list = new ArrayList<String>();

        for (SongSummaryDTO song : library) {

            song_list.add(song.getArtist());
            song_list.add(song.getTitle());
            song_list.add(song.getAlbum());
        }

        return song_list;
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

        AsyncCallback<String> callback = new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(String user) {

                // remove the session cookie
                Cookies.removeCookie("user");
                Cookies.removeCookie("sid");

                // hide and disable the applet
                AppletBar.get(user).appletBarOFF();

                goTo(new LoginPlace(""));
            }
        };

        try {
            login_service_svc.logout(callback);
        } catch (Exception e) {
        }
    }

    /**
     * Sposta in basso la canzone della playlist selezionata
     */
    @Override
    public void moveDownInPLaylist(String playlist, String autore,
            String titolo, String album) {

        // scambia la canzone sleezionata con quella successiva e aggiorna la
        // vista

    }

    /**
     * Sposta in alto la canzone della playlist selezionata
     */
    @Override
    public void moveUpInPLaylist(String playlist, String autore, String titolo,
            String album) {

        // scambia la canzone sleezionata con quella precedente e aggiorna la
        // vista

    }

    /**
     * Restituisce il link youtube della canzone selezionata
     */
    @Override
    public void playYouTube(String autore, String titolo, String album) {

        List<SongDTO> songs = current_user.getMusicLibrary().getSongs();
        String youTubeCode = "";

        for (SongDTO song : songs) {
            if (song.getTitle().equals(titolo)
                    && song.getArtist().equals(autore)
                    && song.getAlbum().equals(album)) {
                youTubeCode = song.getYoutubeCode();
                if (!youTubeCode.equals(""))
                    client_factory.getProfileView().playYouTube(youTubeCode);
                client_factory.getProfileView().setInfo(
                        titolo + " - " + autore + " -");
                return;
            }
        }
    }

    /**
     * Attribuisce un punteggio compreso tra 1 e 5 alla canzone selezionata
     */
    @Override
    public void rateSong(final String artist, final String title,
            final String album, final int rate) {

        // sistemo subito il rating visivo poi arrivera' la library aggiornata
        client_factory.getProfileView().setRating(rate);
        client_factory.getProfileView().showStar(rate);

        AsyncCallback<Double> callback = new AsyncCallback<Double>() {

            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(Double result) {
                List<SongDTO> songs_dto = current_user.getMusicLibrary()
                        .getSongs();
                for (SongDTO tmp : songs_dto) {
                    if (tmp.getArtist().equalsIgnoreCase(artist)
                            && tmp.getTitle().equalsIgnoreCase(title)
                            && tmp.getAlbum().equalsIgnoreCase(album)) {
                        tmp.setRatingForThisUser(rate);
                        tmp.setRating(result);
                        client_factory.getProfileView().showGlobalStar(result);
                    }
                }
            }

        };
        song_service_svc.rateSong(current_user.getUser(), new SongSummaryDTO(
                artist, title, album), rate, callback);

    }

    /**
     * Rimuovi il brano dalla playlist.
     */
    @Override
    public void removeFromPLaylist(String playlist, final String autore,
            final String titolo, final String album) {

        String song_id = titolo + "-vt.g-" + autore + "-vt.g-" + album;
        song_id = song_id.toLowerCase();
        library_service_svc.removeSongFromPlaylist(current_user.getUser(),
                playlist, song_id, new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable caught) {
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        if (result)
                            client_factory.getProfileView().removeFromPlaylist(
                                    autore, titolo, album);
                    }
                });
    }

    /**
     * Restituisce la lista degli utenti affini su Netmus
     */
    @Override
    public void setFriendList() {

        client_factory.getProfileView().paintFriendlist(getFriendList());

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
    public void setPlaylistSongs(String titolo_playlist) {
        getPlaylistSongs(titolo_playlist);
    }

    /**
     * Restituisce il rating della canzone selezionata
     */
    @Override
    public double setRating(String artist, String title, String album) {

        List<SongDTO> songs_dto = this.current_user.getMusicLibrary()
                .getSongs();
        for (SongDTO tmp : songs_dto) {
            if (tmp.getArtist().equalsIgnoreCase(artist)
                    && tmp.getTitle().equalsIgnoreCase(title)
                    && tmp.getAlbum().equalsIgnoreCase(album)) {
                client_factory.getProfileView().setRating(
                        tmp.getRatingForThisUser());
                client_factory.getProfileView().showStar(
                        tmp.getRatingForThisUser());
                return tmp.getRating();
            }
        }
        return -1;
    }

    /**
     * Imposta i campi della canzone selezionata.
     */
    @Override
    public void setSongFields(String autore, String titolo, String album) {

        List<SongDTO> songs = current_user.getMusicLibrary().getSongs();

        String genere = "----";
        String anno = "----";
        String compositore = "----";
        String traccia = "----";
        String cover = "images/test_cover.jpg"; // valore url cover - defautl
                                                // (images/test_cover.jpg)

        for (SongDTO song : songs) {
            if (song.getTitle().equals(titolo)
                    && song.getArtist().equals(autore)
                    && song.getAlbum().equals(album)) {
                if (!song.getGenre().equals(""))
                    genere = song.getGenre();
                if (!song.getYear().equals(""))
                    anno = song.getYear();
                if (!song.getComposer().equals(""))
                    compositore = song.getComposer();
                if (!song.getTrackNumber().equals(""))
                    traccia = song.getTrackNumber();

                if (!song.getAlbumCover().equals(""))
                    cover = song.getAlbumCover();
                client_factory.getProfileView().setSongFields(autore, titolo,
                        album, genere, anno, compositore, traccia, cover);
                return;
            }
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
     * Restituisce il summary delle canzoni
     */
    @Override
    public void setSongs() {

        client_factory.getProfileView().paintCatalogo(
                getSongs(current_user.getMusicLibrary()));
        client_factory.getProfileView().setNumeroBrani(
                current_user.getMusicLibrary().getSongs().size());

    }

    /**
     * Invocato da ActivityManager per avviare effettivamente una nuova
     * ProfileActivity.
     */
    @Override
    public void start(final AcceptsOneWidget container_widget,
            EventBus event_bus) {

        AsyncCallback<String> callback = new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof LoginException) {
                    goTo(new LoginPlace(""));
                }
            }

            @Override
            public void onSuccess(final String user) {

                client_factory.getProfileView().setUser(user);
                final ProfileView profileView = client_factory.getProfileView();

                client_factory.getEventBus().addHandler(
                        DeviceScannedEvent.TYPE,
                        new DeviceScannedEventHandler() {
                            @Override
                            public void onScanDevice(DeviceScannedEvent event) {
                                AsyncCallback<UserCompleteDTO> callbackUpdateUser = new AsyncCallback<UserCompleteDTO>() {
                                    @Override
                                    public void onFailure(Throwable caught) {
                                    }

                                    @Override
                                    public void onSuccess(UserCompleteDTO result) {
                                        current_user = result;
                                        setSongs();
                                        profileView
                                                .paintPlaylist(getPlaylistList());
                                    }
                                };
                                user_service_svc.loadProfile(user,
                                        callbackUpdateUser);
                            }
                        });

                profileView.setName(name);
                profileView.setPresenter(ProfileActivity.this);

                // inizializzazione dell'UserCompleteDTO mantenuto nell'activity
                AsyncCallback<UserCompleteDTO> callback2 = new AsyncCallback<UserCompleteDTO>() {
                    @Override
                    public void onFailure(Throwable caught) {
                    }

                    @Override
                    public void onSuccess(UserCompleteDTO result) {
                        current_user = result;
                        setSongs();
                        profileView.paintPlaylist(getPlaylistList());
                        setFriendList();
                        profileView.setInfo(getSongInfo());
                        editProfileView(user);

                        container_widget.setWidget(profileView.asWidget());
                        profileView.setLayout();

                    }
                };
                user_service_svc.loadProfile(user, callback2);

                // load the applet bar, if not active yet
                AppletBar.get(user).appletBarON();
            }

        };

        try {
            login_service_svc.getLoggedInUser(callback);
        } catch (LoginException e) {
        }

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

}
