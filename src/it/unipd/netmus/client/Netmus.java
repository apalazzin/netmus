package it.unipd.netmus.client;

import it.unipd.netmus.client.mvp.NetmusActivityMapper;
import it.unipd.netmus.client.mvp.NetmusPlaceHistoryMapper;
import it.unipd.netmus.client.place.LoginPlace;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Classe Entry point che definisce il metodo <code>onModuleLoad()</code>.
 * @author ValterTexasGroup
 *
 */
public class Netmus implements EntryPoint {

   private Place defaultPlace = new LoginPlace("Netmus!");
   private SimplePanel appWidget = new SimplePanel();
   
   @Override
   public void onModuleLoad() {
      
      // Create ClientFactory using deferred binding so we can replace with different
      // impls in gwt.xml
      ClientFactory clientFactory = GWT.create(ClientFactory.class);
      EventBus eventBus = clientFactory.getEventBus();
      PlaceController placeController = clientFactory.getPlaceController();

      // Start ActivityManager for the main widget with our ActivityMapper
      ActivityMapper activityMapper = new NetmusActivityMapper(clientFactory);
      ActivityManager activityManager = new ActivityManager(activityMapper, eventBus);
      activityManager.setDisplay(appWidget);

      // Start PlaceHistoryHandler with our PlaceHistoryMapper
      NetmusPlaceHistoryMapper historyMapper= GWT.create(NetmusPlaceHistoryMapper.class);
      PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
      historyHandler.register(placeController, eventBus, defaultPlace);

      RootPanel.get().setStyleName( "gwt-root" );
      RootPanel.get().add(appWidget);
     
      // Goes to place represented on URL or default place
      historyHandler.handleCurrentHistory();
   }


}
