package it.unipd.netmus.client;

import it.unipd.netmus.client.ui.LoginView;
import it.unipd.netmus.client.ui.LoginViewImpl;
import it.unipd.netmus.client.ui.ProfileView;
import it.unipd.netmus.client.ui.ProfileViewImpl;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.PlaceController;

/**
 * Nome: ClientFactoryImpl.java
 * Autore: VT.G 
 * Licenza: GNU GPL v3 Data 
 * Creazione: 15 Febbraio 2011
 */
public class ClientFactoryImpl implements ClientFactory {
    private static final EventBus event_bus = new SimpleEventBus();
    private static final PlaceController place_controller = new PlaceController(
            event_bus);
    private static final LoginView login_view = new LoginViewImpl();
    private static final ProfileView profile_view = new ProfileViewImpl();

    @Override
    public EventBus getEventBus() {
        return event_bus;
    }

    @Override
    public LoginView getLoginView() {
        return login_view;
    }

    @Override
    public PlaceController getPlaceController() {
        return place_controller;
    }

    @Override
    public ProfileView getProfileView() {
        return profile_view;
    }

}
