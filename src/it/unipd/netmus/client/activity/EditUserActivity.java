package it.unipd.netmus.client.activity;

import it.unipd.netmus.client.ClientFactory;
import it.unipd.netmus.client.place.EditUserPlace;
import it.unipd.netmus.client.ui.EditUserView;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class EditUserActivity extends AbstractActivity implements
		EditUserView.Presenter {
	// Used to obtain views, eventBus, placeController
	// Alternatively, could be injected via GIN
	private ClientFactory clientFactory;
	// Name that will be appended to "Hello,"
	private String name;

	public EditUserActivity(EditUserPlace place, ClientFactory clientFactory) {
		this.name = place.getEditUserName();
		this.clientFactory = clientFactory;
	}

	/**
	 * Invoked by the ActivityManager to start a new Activity
	 */
	@Override
	public void start(AcceptsOneWidget containerWidget, EventBus eventBus) {
		EditUserView editUserView = clientFactory.getEditUserView();
		editUserView.setName(name);
		editUserView.setPresenter(this);
		containerWidget.setWidget(editUserView.asWidget());
	}

	/**
	 * Navigate to a new Place in the browser
	 */
	public void goTo(Place place) {
		clientFactory.getPlaceController().goTo(place);
	}
}
