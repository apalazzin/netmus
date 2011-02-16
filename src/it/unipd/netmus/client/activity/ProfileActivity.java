package it.unipd.netmus.client.activity;

import java.util.logging.Logger;

import it.unipd.netmus.client.ClientFactory;
import it.unipd.netmus.client.place.LoginPlace;
import it.unipd.netmus.client.place.ProfilePlace;
import it.unipd.netmus.client.service.LoginService;
import it.unipd.netmus.client.service.LoginServiceAsync;
import it.unipd.netmus.client.ui.MyConstants;
import it.unipd.netmus.client.ui.ProfileView;

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
	
	private static Logger logger = Logger.getLogger(LoginActivity.class.getName());
	
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
	public void start(AcceptsOneWidget containerWidget, EventBus eventBus) {
		ProfileView profileView = clientFactory.getProfileView();
		profileView.setName(name);
		profileView.setPresenter(this);
		containerWidget.setWidget(profileView.asWidget());
	}

	/**
	 * Navigate to a new Place in the browser
	 */
	public void goTo(Place place) {
		clientFactory.getPlaceController().goTo(place);
	}

   /* (non-Javadoc)
    * @see it.unipd.netmus.client.ui.ProfileView.Presenter#logout()
    */
   @Override
   public void logout() {
      
      AsyncCallback<Void> callback = new AsyncCallback<Void>() {

         @Override
         public void onFailure(Throwable caught) {
            logger.severe("errore logout");
         }

         @Override
         public void onSuccess(Void result) {
            logger.info("Logout effettuato con successo");
            goTo(new LoginPlace("netmus"));
         }
      };
      
      try {
         loginServiceSvc.logout(callback);
      } catch (Exception e) {
         // che tipo di ecc ??
      }
      
   }
}
