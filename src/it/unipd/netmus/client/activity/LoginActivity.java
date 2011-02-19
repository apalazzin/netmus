package it.unipd.netmus.client.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.unipd.netmus.client.ClientFactory;
import it.unipd.netmus.client.place.LoginPlace;
import it.unipd.netmus.client.place.ProfilePlace;
import it.unipd.netmus.client.service.LoginService;
import it.unipd.netmus.client.service.LoginServiceAsync;
import it.unipd.netmus.client.ui.LoginView;
import it.unipd.netmus.client.ui.MyConstants;
import it.unipd.netmus.shared.FieldVerifier;
import it.unipd.netmus.shared.LoginDTO;
import it.unipd.netmus.shared.SongDTO;
import it.unipd.netmus.shared.UserCompleteDTO;
import it.unipd.netmus.shared.exception.LoginException;
import it.unipd.netmus.shared.exception.RegistrationException;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class LoginActivity extends AbstractActivity implements
	LoginView.Presenter {
	// Used to obtain views, eventBus, placeController
	// Alternatively, could be injected via GIN
	private ClientFactory clientFactory;
	private String user;
	private String password;
	private String error;
	private LoginType loginType;
	
	private static Logger logger = Logger.getLogger("LoginActivity");
	
	private LoginServiceAsync loginServiceSvc = GWT.create(LoginService.class);
	MyConstants myConstants = GWT.create(MyConstants.class);
	
	public LoginActivity(LoginPlace place, ClientFactory clientFactory) {
		this.user = place.getLoginName();
		this.password = place.getPassword();
		this.error = place.getError();
		this.loginType = place.getLoginType();
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
                    // user not logged yet - show loginView
                    LoginView loginView = clientFactory.getLoginView();
                    loginView.setError(error);
                    loginView.setLoginType(loginType);
                    loginView.setPassword(password);
                    loginView.setUser(user);
                    loginView.setPresenter(LoginActivity.this);
                    containerWidget.setWidget(loginView.asWidget());
                }
            }

            @Override
            public void onSuccess(String result) {
                logger.info("User '"+result+"' Logged - Redirect to Profile");
                goTo(new ProfilePlace(result));
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
    public void sendLogin(String user, String password) throws LoginException {

        
        
        
        
        
        SongDTO song = new SongDTO();
        song.setArtist("ProvaLowerCase");
        song.setTitle("Titolo");
        song.setAlbum("album");
        
        
        
        
        
        // Set up the callback object.
        AsyncCallback<UserCompleteDTO> callback2 = new AsyncCallback<UserCompleteDTO>() {
            
          public void onFailure(Throwable caught) {
              if (caught instanceof LoginException) {
                  logger.log(Level.INFO, ((LoginException)caught).getMoreInfo());
                  
              }
              else {
                  logger.log(Level.INFO, ("Impossibile connettersi al database"));
                    
              }
          }

          @Override
          public void onSuccess(UserCompleteDTO result) {
              List<SongDTO> list = result.getMusicLibrary().getSongs();
              Window.alert(result.getUser()+" ha "+list.size()+" canzoni");
              for (SongDTO tmp:list) {
                  Window.alert(tmp.getTitle()+" - "+tmp.getArtist()+" - "+tmp.getAlbum()+"\n");
              }
            
          }

        };

        loginServiceSvc.testDatastore(song, callback2);
        
        
        
        
        
        
        
        
        
        
        
       

    }

    @Override
    public void sendRegistration(String user, String password,
            String confirmPassword) throws RegistrationException {
        final String username = user;
        final String pass = password;
        LoginDTO login = new LoginDTO(user,password);;
        
        if (!FieldVerifier.isValidPassword(password))
            goTo( new LoginPlace(username,pass, myConstants.errorPassword() ,LoginType.NETMUSREGISTRATION));
        else if (!FieldVerifier.isValidEmail(username))
            goTo( new LoginPlace(username,pass, myConstants.errorEmail() ,LoginType.NETMUSREGISTRATION));
        else if (!password.equals(confirmPassword))
            goTo( new LoginPlace(username,pass, myConstants.errorCPassword() ,LoginType.NETMUSREGISTRATION));
        else {
            
            // Set up the callback object.
            AsyncCallback<LoginDTO> callback = new AsyncCallback<LoginDTO>() {
            
                public void onFailure(Throwable caught) {
                    logger.log(Level.INFO, "");
                    goTo(new LoginPlace(username,pass, myConstants.infoUserUsato(),LoginType.NETMUSREGISTRATION));
                }

                @Override
                public void onSuccess(LoginDTO result) {
                    logger.log(Level.INFO, myConstants.infoUserInsertDb() + username);
                    try {
                        sendLogin(result.getUser(), result.getPassword());
                    } catch (LoginException e) {
                        e.printStackTrace();
                    }
                }
            };

            // Make the call to send login info.
            try {
                loginServiceSvc.insertRegistration(login, callback);
            } catch (RegistrationException e) {
                //exception alredy cought in method onFailure
                e.printStackTrace();
            }
        }   
    }
}




/*
BUONO PER TESTARE...
// Set up the callback object.
AsyncCallback<ArrayList<UserSummaryDTO>> callback2 = new AsyncCallback<ArrayList<UserSummaryDTO>>() {
  public void onFailure(Throwable caught) {
  }

  @Override
  public void onSuccess(ArrayList<UserSummaryDTO> result) {
	  logger.log(Level.INFO, "LISTA DI TUTTI GLI UTENTI REGISTRATI");
	  for (UserSummaryDTO tmp:result)
		  logger.log(Level.INFO, tmp.getNickName());
  }
};

// Make the call to the stock price service.
loginServiceSvc.getAllUsers(callback2);
*/