package it.unipd.netmus.client.activity;

import it.unipd.netmus.client.ClientFactory;
import it.unipd.netmus.client.place.EditSongsPlace;
import it.unipd.netmus.client.ui.EditSongsView;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class EditSongsActivity extends AbstractActivity implements
		EditSongsView.Presenter {
	// Used to obtain views, eventBus, placeController
	// Alternatively, could be injected via GIN
	private ClientFactory clientFactory;
	// Name that will be appended to "Hello,"
	private String name;

	public EditSongsActivity(EditSongsPlace place, ClientFactory clientFactory) {
		this.name = place.getEditSongsName();
		this.clientFactory = clientFactory;
	}

	/**
	 * Invoked by the ActivityManager to start a new Activity
	 */
	@Override
	public void start(AcceptsOneWidget containerWidget, EventBus eventBus) {
		EditSongsView editSongsView = clientFactory.getEditSongsView();
		editSongsView.setName(name);
		editSongsView.setPresenter(this);
		containerWidget.setWidget(editSongsView.asWidget());
	}

	/**
	 * Navigate to a new Place in the browser
	 */
	public void goTo(Place place) {
		clientFactory.getPlaceController().goTo(place);
	}
}
