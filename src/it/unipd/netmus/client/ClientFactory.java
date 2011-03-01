package it.unipd.netmus.client;

import it.unipd.netmus.client.ui.LoginView;
import it.unipd.netmus.client.ui.ProfileView;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
/**
 * Nome: ClientFactory.java
 * Autore:  VT.G
 * Licenza: GNU GPL v3
 * Data Creazione: 15 Febbraio 2011
*/
public interface ClientFactory
{
	/**
	 * Getter dell'attributo event_bus
	 * @return
	 */
	EventBus getEventBus();
	/**
	 * Getter dell'attributo place_controller
	 * @return
	 */
	PlaceController getPlaceController();
	/**
	 * Getter dell'attributo login_view
	 * @return
	 */
	LoginView getLoginView();
	/**
	 * Getter dell'attributo profile_view
	 * @return
	 */
	ProfileView getProfileView();
}
