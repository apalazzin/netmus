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
	EventBus getEventBus();
	PlaceController getPlaceController();
	LoginView getLoginView();
	ProfileView getProfileView();
}
