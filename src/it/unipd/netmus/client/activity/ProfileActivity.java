package it.unipd.netmus.client.activity;

import java.util.logging.Logger;

import it.unipd.netmus.client.ClientFactory;
import it.unipd.netmus.client.applet.ABF;
import it.unipd.netmus.client.place.LoginPlace;
import it.unipd.netmus.client.place.ProfilePlace;
import it.unipd.netmus.client.service.LoginService;
import it.unipd.netmus.client.service.LoginServiceAsync;
import it.unipd.netmus.client.ui.MyConstants;
import it.unipd.netmus.client.ui.ProfileView;
import it.unipd.netmus.shared.exception.LoginException;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class ProfileActivity extends AbstractActivity implements
		ProfileView.Presenter {

	private ClientFactory clientFactory;

	private String name;
	
	private static Logger logger = Logger.getLogger("ProfileActivity");
	
	private LoginServiceAsync loginServiceSvc = GWT.create(LoginService.class);
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
            public void onSuccess(String result) {
                ProfileView profileView = clientFactory.getProfileView();
                profileView.setName(name);
                profileView.setPresenter(ProfileActivity.this);
                
                profileView.setNumeroBrani(getLibrarySize());
                profileView.setUser(getUsername());
                profileView.paintPlaylist(getPlaylistList());
                profileView.paintFriendlist(getFriendList());
                profileView.setInfo(getSongInfo());
                containerWidget.setWidget(profileView.asWidget());
                
               //load the applet bar, if not active yet
                ABF.get().appletBarON();
            }
        };
        
        try { loginServiceSvc.getLoggedInUserDTO(callback); }
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
      
      AsyncCallback<Void> callback = new AsyncCallback<Void>() {
         @Override
         public void onFailure(Throwable caught) {
            logger.info("Logout Error");
         }

         @Override
         public void onSuccess(Void result) {
            logger.info("Logout user");
            
            // hide and disable the applet
            ABF.get().appletBarOFF();
            
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
	public String getLibrarySize() {
		// TODO Auto-generated method stub
		return new String("69");
	}

	@Override
	public String[] getPlaylistList() {
		// TODO Auto-generated method stub
		String[] playlists = {"Casa", "Vacanze", "Tokio Hotel", "Rock" };
		return playlists;
		
	}

	@Override
	public String[] getFriendList() {
		// TODO Auto-generated method stub
		String[] friends = {"Alberto Palazzin", "Andrea Mandolo", "Cosimo Caputo", "Daniele Donte", "Federicon Baron", "Simone Daminato"};
		return friends;
	}

	@Override
	public String getSongInfo() {
		// TODO Auto-generated method stub
		return "Nessun brano in ascolto.";
	}
}
