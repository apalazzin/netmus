package it.unipd.netmus.client;

import it.unipd.netmus.client.ui.LoginView;
import it.unipd.netmus.client.ui.LoginViewImpl;
import it.unipd.netmus.client.ui.ProfileView;
import it.unipd.netmus.client.ui.ProfileViewImpl;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.PlaceController;

public class ClientFactoryImpl implements ClientFactory
{
	private static final EventBus eventBus = new SimpleEventBus();
	private static final PlaceController placeController = new PlaceController(eventBus);
	private static final LoginView loginView = new LoginViewImpl();
	private static final ProfileView profileView = new ProfileViewImpl();

   @Override
   public EventBus getEventBus() {
      return eventBus;
   }

   @Override
   public PlaceController getPlaceController() {
      return placeController;
   }

   @Override
   public LoginView getLoginView() {
      return loginView;
   }

   @Override
   public ProfileView getProfileView() {
      return profileView;
   }

}
