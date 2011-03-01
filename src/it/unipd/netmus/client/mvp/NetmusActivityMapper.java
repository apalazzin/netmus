package it.unipd.netmus.client.mvp;

import it.unipd.netmus.client.ClientFactory;
import it.unipd.netmus.client.activity.LoginActivity;
import it.unipd.netmus.client.activity.ProfileActivity;
import it.unipd.netmus.client.place.LoginPlace;
import it.unipd.netmus.client.place.ProfilePlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;

/**
 * Nome: NetmusActivityMapper.java
 * Autore:  VT.G
 * Licenza: GNU GPL v3
 * Data Creazione: 15 Febbraio 2011
*/
public class NetmusActivityMapper implements ActivityMapper {

	private ClientFactory clientFactory;

	public NetmusActivityMapper(ClientFactory clientFactory) {
		super();
		this.clientFactory = clientFactory;
	}

    /**
     * Activity & Mappa e istanzia l'activity adeguata in base al tipo del place dato in input.
     */
	@Override
	public Activity getActivity(Place place) {
		// This is begging for GIN
		if (place instanceof LoginPlace)
			return new LoginActivity((LoginPlace) place, clientFactory);
		else if (place instanceof ProfilePlace)
			return new ProfileActivity((ProfilePlace) place, clientFactory);

		return null;
	}

}
