package it.unipd.netmus.client.activity;

import it.unipd.netmus.client.ClientFactory;
import it.unipd.netmus.client.place.LoginPlace;
import it.unipd.netmus.client.service.LoginService;
import it.unipd.netmus.client.ui.EditSongsView.Presenter.LoginType;
import it.unipd.netmus.client.ui.LoginView;
import it.unipd.netmus.shared.LoginDTO;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class LoginActivity extends AbstractActivity implements
	LoginView.Presenter {
	// Used to obtain views, eventBus, placeController
	// Alternatively, could be injected via GIN
	private ClientFactory clientFactory;
	// Name that will be appended to "Hello,"
	private String user;
	private String password;
	private String error;
	private LoginType loginType;
	private LoginService loginService;


	public LoginActivity(LoginPlace place, ClientFactory clientFactory)/*, LoginService service)*/ {
		this.user = place.getLoginName();
		this.password = place.getPassword();
		this.error = place.getError();
		this.loginType = place.getLoginType();
		//this.loginService = service;
		this.clientFactory = clientFactory;
	}

	/**
	* Invoked by the ActivityManager to start a new Activity
	*/
	@Override
	public void start(AcceptsOneWidget containerWidget, EventBus eventBus) {
		LoginView loginView = clientFactory.getLoginView();
		loginView.setName(user);
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
	{//implementation
		return true;
	}
	
	public boolean sendRegistration(LoginDTO login)
	{//implementation
		return true;
	}
}

