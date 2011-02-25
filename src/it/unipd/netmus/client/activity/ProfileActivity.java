package it.unipd.netmus.client.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import it.unipd.netmus.client.ClientFactory;
import it.unipd.netmus.client.applet.AppletBar;
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

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class ProfileActivity extends AbstractActivity implements
		ProfileView.Presenter {

	private ClientFactory clientFactory;

	private String name;
	
	private boolean isOwner;
	
	private static Logger logger = Logger.getLogger("ProfileActivity");
	
	private LoginServiceAsync loginServiceSvc = GWT.create(LoginService.class);
	private LibraryServiceAsync libraryServiceSvc = GWT.create(LibraryService.class);
	private UserServiceAsync usersServiceSvc = GWT.create(UserService.class);
	private SongServiceAsync songsServiceSvc = GWT.create(SongService.class);
	
	private UserCompleteDTO current_user;
	
	MyConstants myConstants = GWT.create(MyConstants.class);

	public ProfileActivity(ProfilePlace place, ClientFactory clientFactory) {
		this.name = place.getProfileName();
		this.clientFactory = clientFactory;
	}

	/**
	 * Invoked by the ActivityManager to start a new Activity
	 */
	@Override
	public void start(final AcceptsOneWidget containerWidget, EventBus eventBus) {
	    

		AsyncCallback<String> callback = new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof LoginException) {
                    logger.info("User not logged yet - Redirect to Login");
                    goTo(new LoginPlace(""));
                }
            }

            @Override
            public void onSuccess(final String user) {
                
                clientFactory.getProfileView().setUser(user);                
                final ProfileView profileView = clientFactory.getProfileView();
                profileView.setName(name);
                profileView.setPresenter(ProfileActivity.this);
                
                
                //inizializzazione dell'UserCompleteDTO mantenuto nell'activity
                AsyncCallback<UserCompleteDTO> callback2 = new AsyncCallback<UserCompleteDTO>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        // TODO Auto-generated method stub
                        
                    }

                    @Override
                    public void onSuccess(UserCompleteDTO result) {
                        // TODO Auto-generated method stub
                        current_user = result;
                        setSongs();
                        profileView.paintPlaylist(getPlaylistList());
                        setFriendList();
                        profileView.setInfo(getSongInfo());
                        containerWidget.setWidget(profileView.asWidget());
                        profileView.setLayout();

                    }
                
                };
                usersServiceSvc.loadProfile(user, callback2);
                
                //load the applet bar, if not active yet
                AppletBar.get(user,true).appletBarON();
            }
            
        };
	    
        try { loginServiceSvc.getLoggedInUser(callback); }
        catch(LoginException e) {
        }
               
                
	}

	/**
	 * Navigate to a new Place in the browser
	 */
	public void goTo(Place place) {
		clientFactory.getPlaceController().goTo(place);
	}


   @Override
   public void logout() {
      
      AsyncCallback<String> callback = new AsyncCallback<String>() {
         @Override
         public void onFailure(Throwable caught) {
            logger.info("Logout Error");
         }

         @Override
         public void onSuccess(String user) {
            logger.info("Logout user: "+user);
            
            // remove the session cookie
            Cookies.removeCookie("user");
            Cookies.removeCookie("sid");
            
            // hide and disable the applet
            AppletBar.get(user,true).appletBarOFF();
            
            goTo(new LoginPlace(""));
         }
      };
      
      try {
         loginServiceSvc.logout(callback);
      } catch (Exception e) {
      }
   }

	
	@Override
	public void setPlaylistList() {
	    
	    clientFactory.getProfileView().paintPlaylist(getPlaylistList());
	    
	}
	
	public String[] getPlaylistList() {
	    //RESTITUISCNE LA LISTA DI PLAYLIST DELL'UTENTE
		// TODO Auto-generated method stub
		String[] playlists = {"Casa", "Vacanze", "Tokio Hotel", "Rock","Casa", "Vacanze"};
		return playlists;
		
	}

	
	
	@Override
	public void setFriendList() {
	    
	    clientFactory.getProfileView().paintFriendlist(getFriendList());
	    
	}
	
	public String[] getFriendList() {
		// TODO Auto-generated method stub
		String[] friends = {"Alberto Palazzin", "Andrea Mandolo", "Cosimo Caputo", "Daniele Donte", "Federicon Baron", "Simone Daminato","Alberto Palazzin", "Andrea Mandolo", "Cosimo Caputo", "Daniele Donte", "Federicon Baron", "Simone Daminato","Alberto Palazzin", "Andrea Mandolo", "Cosimo Caputo", "Daniele Donte", "Federicon Baron", "Simone Daminato"};
		return friends;
	}

	
	
	@Override
	public void setSongInfo() {
	    
	    clientFactory.getProfileView().setInfo(getSongInfo());
	}
	
	public String getSongInfo() {
		// TODO Auto-generated method stub
		return "Nessun brano in ascolto.";
	}

	
	
	@Override
	public void setPlaylistSongs(String titoloPlaylist) {
	   
	    clientFactory.getProfileView().paintPlaylistSongs(getPlaylistSongs(titoloPlaylist));
	    
	}
    
    public List<String> getPlaylistSongs(String titoloPlaylist) {
        // TODO Auto-generated method stub
        List<String> lista_canzoni = new ArrayList<String>();
        
        //RIEMPIRE LA LISTA CON LA SEQUENZA: autore1, titolo1, album1, autore2, titolo2, album2, autoreN, titoloN, albumN,...
        return lista_canzoni;
        
    }

    
    
    @Override
    public void playYouTube(String autore, String titolo, String album) {
        
        List<SongDTO> songs = current_user.getMusicLibrary().getSongs(); 
        String youTubeCode = "";
        
        for(SongDTO song : songs) {
            if (song.getTitle().equals(titolo) && song.getArtist().equals(autore) && song.getAlbum().equals(album)) {
                youTubeCode = song.getYoutubeCode();
                if (!youTubeCode.equals(""))
                    clientFactory.getProfileView().playYouTube(youTubeCode);
                return;
            }
        }
    }
    
    
    public void setSongs() {
                        
        clientFactory.getProfileView().paintCatalogo(getSongs(current_user.getMusicLibrary()));
        clientFactory.getProfileView().setNumeroBrani(current_user.getMusicLibrary().getSongs().size());
        
    }
 
    
    public List<String> getSongs(MusicLibraryDTO user_library) {

        List<SongDTO> library = user_library.getSongs();
        
        List<String> song_list = new ArrayList<String>();
        
        for(SongSummaryDTO song : library) {

            song_list.add(song.getArtist());
            song_list.add(song.getTitle());
            song_list.add(song.getAlbum());    
        }
        
        return song_list;
    }

    
    
    @Override
    public void addToPLaylist(String playlist, String autore, String titolo, String album) {
        
        //se l'inserimento nella Playlist nel DB ha successo
        clientFactory.getProfileView().addToPLaylist(autore, titolo, album);
        
    }

    @Override
    public void removeFromPLaylist(String playlist, String autore, String titolo, String album) {

      //se la rimozione dalla Playlist nel DB ha successo
       clientFactory.getProfileView().removeFromPlaylist(autore, titolo, album);
        
    }
    
    @Override
    public void moveUpInPLaylist(String playlist, String autore, String titolo, String album) {
        
        //scambia la canzone sleezionata con quella precedente e aggiorna la vista
        
    }
    
    @Override
    public void moveDownInPLaylist(String playlist, String autore, String titolo, String album) {
        
      //scambia la canzone sleezionata con quella successiva e aggiorna la vista
        
    }

    @Override
    public void addPlaylist(String title) {
        
        // se l'inserimento della playlist nel DB ha successo ridisegna la lista PLaylist aggiornata 
        
        clientFactory.getProfileView().paintPlaylist(getPlaylistList());
        //clientFactory.getProfileView().addToPlaylists(title);
    }
    
    @Override
    public void deletePlaylist(String playlist_name) {
        
        // se la rimozione della playlist dall DB ha successo ridisegna la lista PLaylist aggiornata 
        
        clientFactory.getProfileView().paintPlaylist(getPlaylistList());
        //clientFactory.getProfileView().addToPlaylists(title);
        
    }
    
    @Override
    public void rateSong(final String artist, final String title, final String album, final int rate) {
        
        // sistemo subito il rating visivo poi arrivera' la library aggiornata
        clientFactory.getProfileView().setRating(rate);
        clientFactory.getProfileView().showStar(rate);
        
        AsyncCallback<Double> callback = new AsyncCallback<Double>() {

            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(Double result) {
                List<SongDTO> songs_dto = current_user.getMusicLibrary().getSongs();
                for (SongDTO tmp:songs_dto) {
                    if (tmp.getArtist().equalsIgnoreCase(artist) && tmp.getTitle().equalsIgnoreCase(title) && tmp.getAlbum().equalsIgnoreCase(album)) {
                        tmp.setRatingForThisUser(rate);
                        tmp.setRating(result);
                        clientFactory.getProfileView().showGlobalStar(result);
                    }
                }
            }
            
        };
        songsServiceSvc.rateSong(current_user.getUser(), new SongSummaryDTO(artist,title,album), rate, callback);
        
    }

    @Override
    public double setRating(String artist, String title, String album) {
        
        List<SongDTO> songs_dto = this.current_user.getMusicLibrary().getSongs();
        for (SongDTO tmp:songs_dto) {
            if (tmp.getArtist().equalsIgnoreCase(artist) && tmp.getTitle().equalsIgnoreCase(title) && tmp.getAlbum().equalsIgnoreCase(album)) {
                clientFactory.getProfileView().setRating(tmp.getRatingForThisUser());
                clientFactory.getProfileView().showStar(tmp.getRatingForThisUser());
                return tmp.getRating();
            }
        }
        return -1;
    }

	@Override
	public void setSongFields(String autore, String titolo, String album) {
		
	    List<SongDTO> songs = current_user.getMusicLibrary().getSongs();
	    
	    String genere ="----";
        String anno ="----";
        String compositore ="----";
        String traccia ="----";
        String cover = "images/test_cover.jpg";  // valore url cover - defautl (images/test_cover.jpg)
        
        for(SongDTO song : songs) {
            if (song.getTitle().equals(titolo) && song.getArtist().equals(autore) && song.getAlbum().equals(album)) {
                if (!song.getGenre().equals("")) genere = song.getGenre();
                if (!song.getYear().equals("")) anno = song.getYear();
                if (!song.getComposer().equals("")) compositore = song.getComposer();
                if (!song.getTrackNumber().equals("")) traccia = song.getTrackNumber();
                
                if (!song.getAlbumCover().equals("")) cover = song.getAlbumCover();
                clientFactory.getProfileView().setSongFields(autore, titolo, album, genere, anno, compositore, traccia, cover);
                return;
            }
        }
	}

    @Override
    public void viewOtherLibrary(String user) {
        //Controlla che l'utente desiderato abbia un profilo pubblico verso l'utente attualmente autenticato
        //Se ha i permessi accedi al catalogo
        goTo(new ProfilePlace(user));
    }

    @Override
    public void deleteSong(String song_id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void exportPDF(String user) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void editProfileView(String user) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void editProfile(String user, String nick_name, String first_name,
            String last_name, String gender, String nationality,
            String aboutMe, Date bithDate) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void editSongTitle(String new_title, String old_title,
            String artist, String album) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void editSongAlbum(String new_album, String old_album,
            String artist, String title) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void editSongArtist(String new_artist, String old_artist,
            String title, String album) {
        // TODO Auto-generated method stub
        
    }

}
