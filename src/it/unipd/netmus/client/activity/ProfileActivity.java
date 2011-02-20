package it.unipd.netmus.client.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import it.unipd.netmus.client.ClientFactory;
import it.unipd.netmus.client.applet.ABF;
import it.unipd.netmus.client.place.LoginPlace;
import it.unipd.netmus.client.place.ProfilePlace;
import it.unipd.netmus.client.service.LibraryService;
import it.unipd.netmus.client.service.LibraryServiceAsync;
import it.unipd.netmus.client.service.LoginService;
import it.unipd.netmus.client.service.LoginServiceAsync;
import it.unipd.netmus.client.ui.MyConstants;
import it.unipd.netmus.client.ui.ProfileView;
import it.unipd.netmus.shared.MusicLibrarySummaryDTO;
import it.unipd.netmus.shared.SongSummaryDTO;
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
	
	private static Logger logger = Logger.getLogger("ProfileActivity");
	
	private LoginServiceAsync loginServiceSvc = GWT.create(LoginService.class);
	private LibraryServiceAsync libraryServiceSvc = GWT.create(LibraryService.class);
	
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
                
                final ProfileView profileView = clientFactory.getProfileView();
                profileView.setName(name);
                profileView.setPresenter(ProfileActivity.this);
                
                
                AsyncCallback<MusicLibrarySummaryDTO> callback = new AsyncCallback<MusicLibrarySummaryDTO>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        logger.info("Problema richiesta catalogo");
                    }

                    @Override
                    public void onSuccess(MusicLibrarySummaryDTO user_library) {
                        
                        profileView.setNumeroBrani(user_library.getLibrarySize());
                        profileView.paintCatalogo(getSongs(user_library));
                        profileView.setUser(user);
                        profileView.paintPlaylist(getPlaylistList());
                        profileView.paintFriendlist(getFriendList());
                        profileView.setInfo(getSongInfo());
                        containerWidget.setWidget(profileView.asWidget());
                        profileView.setLayout();
                    }
                };
                
                libraryServiceSvc.getLibrary(user,callback);
                
                //CHIAMATE TEMPORANEEE DI TEST, DA ELIMINARE
              
                ///////////////////////////////////////////
                
               //load the applet bar, if not active yet
                ABF.get(user,true).appletBarON();
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
            ABF.get(user,true).appletBarOFF();
            
            goTo(new LoginPlace(""));
         }
      };
      
      try {
         loginServiceSvc.logout(callback);
      } catch (Exception e) {
      }
   }

   
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return new String("giovytr@trezzi.net");
	}

	@Override
	public String[] getPlaylistList() {
		// TODO Auto-generated method stub
		String[] playlists = {"Casa", "Vacanze", "Tokio Hotel", "Rock","Casa", "Vacanze"};
		return playlists;
		
	}

	@Override
	public String[] getFriendList() {
		// TODO Auto-generated method stub
		String[] friends = {"Alberto Palazzin", "Andrea Mandolo", "Cosimo Caputo", "Daniele Donte", "Federicon Baron", "Simone Daminato","Alberto Palazzin", "Andrea Mandolo", "Cosimo Caputo", "Daniele Donte", "Federicon Baron", "Simone Daminato","Alberto Palazzin", "Andrea Mandolo", "Cosimo Caputo", "Daniele Donte", "Federicon Baron", "Simone Daminato"};
		return friends;
	}

	@Override
	public String getSongInfo() {
		// TODO Auto-generated method stub
		return "Nessun brano in ascolto.";
	}

    @Override
    public List<String> getPlaylistSongs(String titoloPlaylist) {
        // TODO Auto-generated method stub
        List<String> lista_canzoni = new ArrayList<String>();
        
        //RIEMPIRE LA LISTA CON LA SEQUENZA: autore1, titolo1, album1, autore2, titolo2, album2, autoreN, titoloN, albumN,...
        return lista_canzoni;
        
    }

    @Override
    public String getYouTubeLink() {
        // TODO Auto-generated method stub
        return "yNBFkANEd5M";
    }

    @Override
    public List<String> getSongs(MusicLibrarySummaryDTO user_library) {

        List<SongSummaryDTO> library = user_library.getSongs();
        List<String> song_list = new ArrayList<String>();
        
        for(SongSummaryDTO song : library) {

            song_list.add(song.getArtist());
            song_list.add(song.getTitle());
            song_list.add(song.getAlbum());    
        }
        
        return song_list;
    }

    @Override
    public boolean addToPLaylist(String playlist, String autore, String titolo, String album) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean removeFromPLaylist(String playlist, String autore,
            String titolo, String album) {
        // TODO Auto-generated method stub
        return true;
    }
}
