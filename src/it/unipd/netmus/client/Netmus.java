package it.unipd.netmus.client;

import java.util.Date;

import it.unipd.netmus.client.mvp.NetmusActivityMapper;
import it.unipd.netmus.client.mvp.NetmusPlaceHistoryMapper;
import it.unipd.netmus.client.place.LoginPlace;
import it.unipd.netmus.client.service.LoginService;
import it.unipd.netmus.client.service.LoginServiceAsync;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Classe Entry point che definisce il metodo <code>onModuleLoad()</code>.
 * @author ValterTexasGroup
 *
 */
public class Netmus implements EntryPoint {

   private Place default_place = new LoginPlace("");
   private SimplePanel app_widget = new SimplePanel();
   private LoginServiceAsync login_service_svc = GWT.create(LoginService.class);

   /**
    * ---
    */
   @Override
   public void onModuleLoad() {
      
      String user = Cookies.getCookie("user");
      String session_id = Cookies.getCookie("sid");
      System.err.println("Cookie user: "+user);
      System.err.println("Cookie sessionID: "+session_id);
      
      
      AsyncCallback<String> callback = new AsyncCallback<String>() {

        @Override
        public void onFailure(Throwable caught) {
            startNetmus();
        }
        @Override
        public void onSuccess(String session_id_new) {
            
          String user = Cookies.getCookie("user"); // stesso utente
            
          // refresh cookies
          Cookies.removeCookie("user"); Cookies.removeCookie("sid");
          final long DURATION = 1000 * 60 * 60 * 24;
          Date expires = new Date(System.currentTimeMillis() + DURATION);
          Cookies.setCookie("user", user, expires);
          Cookies.setCookie("sid", session_id_new, expires);
          
          startNetmus();
        }
      };
      
      try {
          login_service_svc.restartSession(user, session_id, callback);
      } catch (Exception e) {
          e.printStackTrace();
      }
   }
   
   /**
    * ---
    */
   private void startNetmus() {
       
       // Create ClientFactory using deferred binding so we can replace with different impls in gwt.xml
       ClientFactory clientFactory = GWT.create(ClientFactory.class);
       EventBus eventBus = clientFactory.getEventBus();
       PlaceController placeController = clientFactory.getPlaceController();

       // Start ActivityManager for the main widget with our ActivityMapper
       ActivityMapper activityMapper = new NetmusActivityMapper(clientFactory);
       ActivityManager activityManager = new ActivityManager(activityMapper, eventBus);
       activityManager.setDisplay(app_widget);

       // Start PlaceHistoryHandler with our PlaceHistoryMapper
       NetmusPlaceHistoryMapper historyMapper= GWT.create(NetmusPlaceHistoryMapper.class);
       PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
       historyHandler.register(placeController, eventBus, default_place);

       RootPanel.get().setStyleName( "gwt-root" );
       // applet e' la barra applet, resta inizialmente invisibile e vuota
       RootPanel.get("applet-bar").setVisible(false);
       RootPanel.get("application").add(app_widget);
      
       // Goes to place represented on URL or default place
       historyHandler.handleCurrentHistory();
   }
}
