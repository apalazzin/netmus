package it.unipd.netmus.client.activity;

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
import it.unipd.netmus.shared.UserSummaryDTO;
import it.unipd.netmus.shared.exception.LoginException;
import it.unipd.netmus.shared.exception.RegistrationException;
import it.unipd.netmus.shared.exception.WrongLoginException;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
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
	public void start(AcceptsOneWidget containerWidget, EventBus eventBus) {
	    
	    AsyncCallback<String> callback = new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof LoginException)
                    logger.severe("Utente non ancora loggato");
            }

            @Override
            public void onSuccess(String result) {
                logger.info("Utente gia' loggato");
                goTo(new ProfilePlace(result));
            }
        };
        
        try { loginServiceSvc.getLoggedInUserDTO(callback); }
        catch(LoginException e) {
        }
	    
		LoginView loginView = clientFactory.getLoginView();
		loginView.setError(error);
		loginView.setLoginType(loginType);
		loginView.setPassword(password);
		loginView.setUser(user);
		loginView.setPresenter(this);
		containerWidget.setWidget(loginView.asWidget());
	}
	
	/**
	* Navigate to a new Place in the browser
	*/
	public void goTo(Place place) {
		clientFactory.getPlaceController().goTo(place);
	}
	
	/**
	* Send and verify login informations to server datastore.
	*/
	public void sendLogin(LoginDTO login)
	{
		final String username = login.getUser();
		final String password = login.getPassword();

	    // Set up the callback object.
	    AsyncCallback<Void> callback = new AsyncCallback<Void>() {
	    	
	      public void onFailure(Throwable caught) {
	    	  if (caught instanceof LoginException) {
	    		  logger.log(Level.INFO, ((LoginException)caught).getMoreInfo());
	    		  goTo( new LoginPlace(username,password, myConstants.infoLoginIncorrect(),LoginType.NETMUSLOGIN));
	    	  }
	    	  else {
	    		  logger.log(Level.INFO, ("Impossibile connettersi al database"));
	    		  goTo(new LoginPlace(username,password, myConstants.databaseErrorGeneric(),LoginType.NETMUSLOGIN));  
	    	  }
	      }
	      
	      @Override
	      public void onSuccess(Void result) {
	    	  logger.log(Level.INFO, username+" "+ myConstants.infoCorrectLogin());
	    	  goTo( new ProfilePlace(username));
	      }
	    	  goTo(new ProfilePlace(username));
	    	  
	    	  
	    	  	/*
	    	  	// Initialize the service proxy.
				if (loginServiceSvc == null) {
					loginServiceSvc = GWT.create(LoginService.class);
				}

				// Set up the callback object.
				AsyncCallback<UserSummaryDTO> callback2 = new AsyncCallback<UserSummaryDTO>() {
		    	
				public void onFailure(Throwable caught) {
					logger.log(Level.INFO, "GET LOGIN SESSION FAILED");
					}

					@Override
					public void onSuccess(UserSummaryDTO result) {
						logger.log(Level.INFO, "GET LOGIN SESSION SUCCESS:" + result.getNickName());
					}
				};

				// Make the call to send login info.
				try {
					loginServiceSvc.getLoggedInUserDTO(callback2);
				} catch (Exception e) {}

	    */  }
	    };

	    // Make the call to send login info.
	    try {
			loginServiceSvc.startLogin(login, callback);
		} catch (Exception e) {}

	}
	
	
	/**
	* Send and verify registration informations to server datastore.
	*/
	public void sendRegistration(LoginDTO login, String confirmPassword)
	{
		final String username = login.getUser();
		final String password = login.getPassword();
		
		if (!FieldVerifier.isValidPassword(password))
			goTo( new LoginPlace(username,password, myConstants.errorPassword() ,LoginType.NETMUSREGISTRATION));
		else if (!FieldVerifier.isValidEmail(username))
			goTo( new LoginPlace(username,password, myConstants.errorEmail() ,LoginType.NETMUSREGISTRATION));
		else if (!password.equals(confirmPassword))
			goTo( new LoginPlace(username,password, myConstants.errorCPassword() ,LoginType.NETMUSREGISTRATION));
		else {
		    
			// Set up the callback object.
			AsyncCallback<LoginDTO> callback = new AsyncCallback<LoginDTO>() {
	    	
				public void onFailure(Throwable caught) {
					logger.log(Level.INFO, "");
					goTo(new LoginPlace(username,password, myConstants.infoUserUsato(),LoginType.NETMUSREGISTRATION));
				}

				@Override
				public void onSuccess(LoginDTO result) {
					logger.log(Level.INFO, myConstants.infoUserInsertDb() + username);
					goTo( new ProfilePlace(username));
					sendLogin(result);
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