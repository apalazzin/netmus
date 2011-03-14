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
import it.unipd.netmus.shared.SongDTO;
import it.unipd.netmus.shared.SongSummaryDTO;
import it.unipd.netmus.shared.UserCompleteDTO;
import it.unipd.netmus.shared.exception.LoginException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * Nome: ProfileActivity.java Autore: VT.G Licenza: GNU GPL v3 Data Creazione:
 * 15 Febbraio 2011
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

        client_factory.getProfileView().startLoading();
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
                        client_factory.getProfileView().stopLoading();
                    }
                });
        // clientFactory.getProfileView().addToPlaylists(title);
    }

    /**
     * Aggiunge song alla playlist e restituisce true in caso di successo
     */
    @Override
    public void addToPLaylist(String playlist, final String artist,
            final String title, final String album) {

        client_factory.getProfileView().startLoading();

        library_service_svc.addSongToPlaylist(current_user.getUser(), playlist,
                title, artist, album, new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable caught) {
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        if (result)
                            client_factory.getProfileView().addToPLaylist(
                                    artist, title, album);
                        client_factory.getProfileView().stopLoading();
                    }
                });
    }

    /**
     * Rimuove dal DB una playlist, e se ha successo, aggiorna la grafica.
     */
    @Override
    public void deletePlaylist(final String playlist_name) {

        client_factory.getProfileView().startLoading();
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
                        client_factory.getProfileView().stopLoading();
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

        client_factory.getProfileView().startLoading();
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
     * Esporta la lista delle canzoni in pdf
     */
    @Override
    public void exportPDF(String user) {
        // TODO Auto-generated method stub

    }

    /**
     * Restituisce la lista degli utenti affini su Netmus
     */
    public void setFriendList() {
        
        user_service_svc.findRelatedUsers(current_user.getUser(), new AsyncCallback<List<String>>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(List<String> related_users) {
                
                String[] names = new String[related_users.size()];
                
                for (int i=0; i<related_users.size(); i++) {
                    names[i] = related_users.get(i);
                }
                
                client_factory.getProfileView().paintFriendlist(names);
            }
            
        });
        
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

        client_factory.getProfileView().startLoading();
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
                        client_factory.getProfileView().stopLoading();
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
                client_factory.getProfileView().stopLoading();
                goTo(new LoginPlace(""));
            }
        };

        try {
            login_service_svc.logout(callback);
        } catch (Exception e) {
        }
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
    public void playYouTube(final String artist, final String title,
            final String album) {

        client_factory.getProfileView().startLoading();
        final String youTubeCode = "";
        final String cover = "";
        
        //ricerca nella mappa delle info già caricate
        final SongDTO song_dto = info_alredy_loaded.get(FieldVerifier.generateSongId(title, artist, album));


        if (song_dto != null) {
            
            Timer timerPlay = new Timer() {
                public void run() {
                    System.out.println("FAST");
                    if (song_dto.getYoutubeCode().equals("")) {
                        client_factory.getProfileView().closeYouTube();
                        client_factory.getProfileView().playYouTube("00000000000");
                        client_factory.getProfileView().playNext(); 
                        client_factory.getProfileView().showError("Non e' disponibile il video Youtube di: \"" + title + "\""); }
                    else {
                        client_factory.getProfileView().closeYouTube();
                        client_factory.getProfileView().playYouTube(song_dto.getYoutubeCode());
                        client_factory.getProfileView().setInfo(title + " - " + artist + " - " + album);
                    }

                    if (!song_dto.getAlbumCover().equals(""))
                        client_factory.getProfileView().paintMainCover(song_dto.getAlbumCover());
                    else
                        client_factory.getProfileView().paintMainCover(
                                "images/test_cover.jpg");
                                
                    client_factory.getProfileView().stopLoading();
                }     
            };
            timerPlay.schedule(5);

            
            return;
        }
        

        Map<String, SongSummaryDTO> songs = current_user.getMusicLibrary().getSongs();
        final SongSummaryDTO song_summary_dto = songs.get(FieldVerifier.generateSongId(title, artist, album));

        
            System.out.println("RPC");
            song_service_svc.getSongDTO(song_summary_dto, new AsyncCallback<SongDTO>() {
                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(SongDTO song_dto) {
                  
                    
                    info_alredy_loaded.put(FieldVerifier.generateSongId(title, artist, album), song_dto);
                    
                    song_summary_dto.setYoutubeCode(song_dto.getYoutubeCode());
                    
                    if(!song_dto.getAlbumCover().equals("")) {
                        song_summary_dto.setAlbumCover(song_dto.getAlbumCover());
                    }
                    else {
                        song_summary_dto.setAlbumCover("images/test_cover.jpg");
                    }

                    
                    if (song_dto.getYoutubeCode().equals("")) {
                        client_factory.getProfileView().closeYouTube();
                        client_factory.getProfileView().playYouTube("00000000000");
                        client_factory.getProfileView().playNext(); 
                        client_factory.getProfileView().showError("Non e' disponibile il video Youtube di: \"" + title + "\""); }
                    else {
                        client_factory.getProfileView().closeYouTube();
                        client_factory.getProfileView().playYouTube(song_dto.getYoutubeCode());
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
                    return;
                    
                }
            });
        
        
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

        AsyncCallback<Double> callback = new AsyncCallback<Double>() {

            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(Double result) {
                Map<String, SongSummaryDTO> songs = current_user.getMusicLibrary().getSongs();
                SongSummaryDTO song = songs.get(FieldVerifier.generateSongId(title, artist, album));
                
                song.setRatingForThisUser(rate);
                song.setRating(result);
                client_factory.getProfileView().showGlobalStar(result);

                client_factory.getProfileView().stopLoading();
            }

        };
        song_service_svc.rateSong(current_user.getUser(), new SongSummaryDTO(
                artist, title, album), rate, callback);

    }

    /**
     * Rimuovi il brano dalla playlist.
     */
    @Override
    public void removeFromPLaylist(String playlist, final String artist,
            final String title, final String album) {

        client_factory.getProfileView().startLoading();
        
        library_service_svc.removeSongFromPlaylist(current_user.getUser(),
                playlist, title, artist, album , new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable caught) {
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        if (result)
                            client_factory.getProfileView().removeFromPlaylist(
                                    artist, title, album);
                        client_factory.getProfileView().stopLoading();
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
    public void setPlaylistSongs(String titolo_playlist) {
        getPlaylistSongs(titolo_playlist);
    }

    /**
     * Restituisce il rating della canzone selezionata
     */
    @Override
    public double setRating(String artist, String title, String album) {
        
        Map<String, SongSummaryDTO> songs = current_user.getMusicLibrary().getSongs();
        SongSummaryDTO song = songs.get(FieldVerifier.generateSongId(title, artist, album));
        
        client_factory.getProfileView().setRating(song.getRatingForThisUser());
        client_factory.getProfileView().showStar(song.getRatingForThisUser());
        
        return song.getRating();

    }

    /**
     * Imposta i campi della canzone selezionata.
     */
    @Override
    public void setSongCover(final String artist, final String title,
            final String album, final HTMLPanel img) {
        
        client_factory.getProfileView().startLoading();

        Map<String, SongSummaryDTO> songs = current_user.getMusicLibrary().getSongs();
        SongSummaryDTO song = songs.get(FieldVerifier.generateSongId(title, artist, album));

        String cover = song.getAlbumCover();
        
        if (cover.equals("")) {
            
            song_service_svc.getSongDTO(song, new AsyncCallback<SongDTO>() {
                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(SongDTO song_dto) {
                    
                    Map<String, SongSummaryDTO> songs = current_user.getMusicLibrary().getSongs();
                    SongSummaryDTO song = songs.get(FieldVerifier.generateSongId(title, artist, album));
                    
                    song.setAlbumCover("images/test_cover.jpg");
                    
                    song.setYoutubeCode(song_dto.getYoutubeCode());
                    if (!song_dto.getAlbumCover().equals("")) {
                        song.setAlbumCover(song_dto.getAlbumCover());
                    }
                    
                    String cover = "images/test_cover.jpg";
                    if (!song_dto.getAlbumCover().equals("")) {
                        cover = song_dto.getAlbumCover();
                    }
                    
                    img.getElement().getStyle()
                            .setBackgroundImage("url('" + cover + "')");
                    
                    client_factory.getProfileView().stopLoading();
                    return;
                }
            });
        } else {
            
            img.getElement().getStyle()
            .setBackgroundImage("url('" + cover + "')");
    
            client_factory.getProfileView().stopLoading();
            
        }
    }

    /**
     * Imposta i campi della canzone selezionata.
     */
    @Override
    public void setSongFields(final String artist, final String title,
            final String album) {

        client_factory.getProfileView().startLoading();
        final String song_id = FieldVerifier.generateSongId(title, artist, album);
        
        //ricerca nella mappa delle info già caricate
        SongDTO song_dto = info_alredy_loaded.get(song_id);
        
        if (song_dto != null) {
            
            //Riempimento default dei campi
            String genere = "----";
            String anno = "----";
            String compositore = "----";
            String traccia = "----";
            String cover = "images/test_cover.jpg";
            
            //se le info erano già state caricate precedentemente vengono prese ed utilizzate immediatamente
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

            //richiesta di visualizzazione delle info dettagliate nell'interfaccia grafica
            client_factory.getProfileView().setSongFields(artist,
                    title, album, genere, anno, compositore,
                    traccia, cover);
            
            client_factory.getProfileView().stopLoading();
            
            return;
        }
        
        Map<String, SongSummaryDTO> songs = current_user.getMusicLibrary().getSongs();
        final SongSummaryDTO current_song_summary_dto = songs.get(song_id);

        song_service_svc.getSongDTO(current_song_summary_dto, new AsyncCallback<SongDTO>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(SongDTO song_dto) {

                //Imposta la copertina di default in caso non ce ne siano altre
                if (song_dto.getAlbumCover().equals("")) {
                    song_dto.setAlbumCover("images/test_cover.jpg");
                }
                
                //salva le info caricate in modo che siano sempre disponibili nel client
                info_alredy_loaded.put(song_id, song_dto);
                
                //Riempimento default dei campi
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

                //richiesta di visualizzazione delle info dettagliate nell'interfaccia grafica
                client_factory.getProfileView().setSongFields(artist,
                        title, album, genere, anno, compositore,
                        traccia, cover);
                
                client_factory.getProfileView().stopLoading();
                return;
            }
        });
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
                
                //Gestore degli eventi DeviceScannedEvent. Questo metodo viene invocato al termine
                //di ogni inserimento di canzoni (sia da USB che da file system)
                client_factory.getEventBus().addHandler(
                        DeviceScannedEvent.TYPE,
                        new DeviceScannedEventHandler() {
                            
                            //L'evento porta con se la lista delle nuove canzoni inserite, queste
                            //vanno ad aggiornare la libreria dell'utente mantenuta nell'activity
                            @Override
                            public void onScanDevice(DeviceScannedEvent event) {
                                
                                List<String> tmp_list = new ArrayList<String>();
                                
                                for (SongDTO tmp : event.getNewSongs()) {
                                    if (!tmp.getTitle().equals("")) {
                                        
                                        if (current_user.getMusicLibrary().getSongs().put(FieldVerifier.generateSongId(tmp.getTitle(), tmp.getArtist(), tmp.getAlbum()), new SongSummaryDTO(tmp.getArtist(), tmp.getTitle(), tmp.getAlbum())) == null) {
                                            tmp_list.add(tmp.getArtist());
                                            tmp_list.add(tmp.getTitle());
                                            tmp_list.add(tmp.getAlbum());
                                        }
                                    }
                                }
                                
                                if (event.isLastSongs()) {
                                    String tmp = calculatePreferredArtist(current_user.getMusicLibrary().getSongs());
                                    
                                    library_service_svc.storeStatistics(current_user.getUser(), tmp, new AsyncCallback<Void>() {
                                        @Override
                                        public void onSuccess(Void result) {}

                                        @Override
                                        public void onFailure(Throwable caught) {}
                                        }
                                    );
                                }
                                
                                client_factory.getProfileView().paintCatalogo(tmp_list);
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
                        
                        List<String> tmp = new ArrayList<String>();
                        for (SongSummaryDTO dto : result.getMusicLibrary().getSongs().values()) {
                            tmp.add(dto.getArtist());
                            tmp.add(dto.getTitle());
                            tmp.add(dto.getAlbum());
                        }
                        client_factory.getProfileView().repaintLibrary(tmp);
                        
                        profileView.paintPlaylist(getPlaylistList());
                        setFriendList();
                        profileView.setUser(user);
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

	@Override
	public void exportPdf() {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * Ricerca all'interno della libreria data in input il genere musicale più ricorrente tra
	 * tutte le canzoni.
	 */
	public String calculatePreferredArtist(Map<String, SongSummaryDTO> all_songs_map) {
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
	
}
