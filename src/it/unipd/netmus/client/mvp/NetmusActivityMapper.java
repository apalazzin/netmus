package it.unipd.netmus.client.mvp;

import it.unipd.netmus.client.ClientFactory;
import it.unipd.netmus.client.activity.LoginActivity;
import it.unipd.netmus.client.activity.ProfileActivity;
import it.unipd.netmus.client.place.LoginPlace;
import it.unipd.netmus.client.place.ProfilePlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;


public class NetmusActivityMapper implements ActivityMapper {

	private ClientFactory clientFactory;

	/**
	 * AppActivityMapper associates each Place with its corresponding
	 * {@link Activity}
	 * 
	 * @param clientFactory
	 *            Factory to be passed to activities
	 */
	public NetmusActivityMapper(ClientFactory clientFactory) {
		super();
		this.clientFactory = clientFactory;
	}

	/**
	 * Map each Place to its corresponding Activity. This would be a great use
	 * for GIN.
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
