package it.unipd.netmus.client.activity;

import java.util.ArrayList;
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
	    

                setUser();
                final ProfileView profileView = clientFactory.getProfileView();
                profileView.setName(name);
                profileView.setPresenter(ProfileActivity.this);
                setSongs();
                profileView.paintPlaylist(getPlaylistList());
                setFriendList();
                profileView.setInfo(getSongInfo());
                containerWidget.setWidget(profileView.asWidget());
                profileView.setLayout();
                
                //CHIAMATE TEMPORANEEE DI TEST, DA ELIMINARE
              
                ///////////////////////////////////////////
                
               //load the applet bar, if not active yet
                startApplet();
                
                
                
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

   
   
   public void startApplet() {
       
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
               
               AppletBar.get(user,true).appletBarON();
               
           }
           
       };
       
       try { loginServiceSvc.getLoggedInUser(callback); }
       catch(LoginException e) {
       }
       
       
   }
   
   
   
	@Override
	public void setUser() {
	
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
                
            }
            
        };
	    
        try { loginServiceSvc.getLoggedInUser(callback); }
        catch(LoginException e) {
        }

	}
	
	
	@Override
	public void setPlaylistList() {
	    
	    clientFactory.getProfileView().paintPlaylist(getPlaylistList());
	    
	}
	
	public String[] getPlaylistList() {
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
    public void playYouTube() {
        
        clientFactory.getProfileView().playYouTube(getYouTubeLink());
        
    }
    
    public String getYouTubeLink() {
        // TODO Auto-generated method stub
        return "yNBFkANEd5M";
    }

    
    
    public void setSongs() {
        
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
 
                AsyncCallback<MusicLibrarySummaryDTO> callback = new AsyncCallback<MusicLibrarySummaryDTO>() {
        
                    @Override
                    public void onFailure(Throwable caught) {
                        logger.info("Problema richiesta catalogo");
                    }
        
                    @Override
                    public void onSuccess(MusicLibrarySummaryDTO user_library) {
                        
                        clientFactory.getProfileView().paintCatalogo(getSongs(user_library));
                        clientFactory.getProfileView().setNumeroBrani(user_library.getLibrarySize());
                     
                    }
                };
                
                libraryServiceSvc.getLibrary(user,callback);
            }
        };
        
        try { loginServiceSvc.getLoggedInUser(callback); }
        catch(LoginException e) {
        }
        
    }
 
    
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
    public void addPlaylist(String title) {
        
        // se l'inserimento della playlist nel DB ha successo 
        clientFactory.getProfileView().addToPlaylists(title);
    }

}
