package it.unipd.netmus.client.activity;

import java.util.logging.Level;
import java.util.logging.Logger;

import it.unipd.netmus.client.ClientFactory;
import it.unipd.netmus.client.place.LoginPlace;
import it.unipd.netmus.client.place.ProfilePlace;
import it.unipd.netmus.client.service.LoginService;
import it.unipd.netmus.client.service.LoginServiceAsync;
import it.unipd.netmus.client.ui.LoginView;
import it.unipd.netmus.shared.LoginDTO;
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
	
	private static Logger logger = Logger.getLogger(LoginActivity.class.getName());
	
	private LoginServiceAsync loginServiceSvc = GWT.create(LoginService.class);

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
	
	public void sendLogin(LoginDTO login)
	{
		final String username = login.getUser();
		final String password = login.getPassword();
		
	    // Initialize the service proxy.
	    if (loginServiceSvc == null) {
	    	loginServiceSvc = GWT.create(LoginService.class);
	    }

	    // Set up the callback object.
	    AsyncCallback<Void> callback = new AsyncCallback<Void>() {
	    	
	      public void onFailure(Throwable caught) {
	    	  logger.log(Level.INFO, "I dati utente inseriti non sono corretti");
	    	  goTo( new LoginPlace(username,password,"ERRORE: login sbagliato",LoginType.NETMUSLOGIN));
	      }

	      @Override
	      public void onSuccess(Void result) {
	    	  logger.log(Level.INFO, "L'utente "+ username+" è loggato con successo");
	    	  goTo( new ProfilePlace("test"));
	      }
	    };

	    // Make the call to send login info.
	    try {
			loginServiceSvc.verifyLogin(login, callback);
		} catch (WrongLoginException e) {
			e.printStackTrace();
		}
	}
	
	public void sendRegistration(LoginDTO login)
	{
		final String username = login.getUser();
		final String password = login.getPassword();
		
	    // Initialize the service proxy.
	    if (loginServiceSvc == null) {
	    	loginServiceSvc = GWT.create(LoginService.class);
	    }

	    // Set up the callback object.
	    AsyncCallback<Void> callback = new AsyncCallback<Void>() {
	    	
	      public void onFailure(Throwable caught) {
	    	  logger.log(Level.INFO, "Utente "+username+" già presente in database");
	    	  goTo( new LoginPlace(username,password,"ERRORE: login sbagliato",LoginType.NETMUSREGISTRATION));
	      }

	      @Override
	      public void onSuccess(Void result) {
	    	  logger.log(Level.INFO, "Inserito nel database l'utente: "+ username);
	    	  goTo( new ProfilePlace("test"));
	      }
	    };

	    // Make the call to send login info.
	    try {
	    	loginServiceSvc.insertRegistration(login, callback);
		} catch (IllegalStateException e) {
			//exception alredy cought in method onFailure
			e.printStackTrace();
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
