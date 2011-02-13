package it.unipd.netmus.client.activity;

import it.unipd.netmus.client.ClientFactory;
import it.unipd.netmus.client.place.LoginPlace;
import it.unipd.netmus.client.place.ProfilePlace;
import it.unipd.netmus.client.service.LoginService;
import it.unipd.netmus.client.service.LoginServiceAsync;
import it.unipd.netmus.client.ui.LoginView;
import it.unipd.netmus.shared.LoginDTO;

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
	
	public boolean sendLogin(LoginDTO login)
	{
		final String username = login.getUser();
		final String password = login.getPassword();
		
	    // Initialize the service proxy.
	    if (loginServiceSvc == null) {
	    	loginServiceSvc = GWT.create(LoginService.class);
	    }

	    // Set up the callback object.
	    AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
	      public void onFailure(Throwable caught) {
	      }

	      @Override
	      public void onSuccess(Boolean result) {
	    	  if (result)
	    		  goTo( new ProfilePlace("test"));
	    	  else
	    		  goTo( new LoginPlace(username,password,"login sbagliato",LoginType.NETMUSLOGIN));
	      }
	    };

	    // Make the call to the stock price service.
	    loginServiceSvc.startLogin(login, callback);
		return true;
	}
	
	public boolean sendRegistration(LoginDTO login)
	{//implementation
		return true;
	}
}

