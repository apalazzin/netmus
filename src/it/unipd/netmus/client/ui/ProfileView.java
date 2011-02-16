/**
 * 
 */
package it.unipd.netmus.client.ui;

import it.unipd.netmus.shared.UserSummaryDTO;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * 
 * 
 * @author ValterTexasGroup
 *
 */
public interface ProfileView extends IsWidget {
   
   void setName(String profileName);
   void setPresenter(Presenter listener);

   public interface Presenter
   {
      void logout();
      void goTo(Place place);
   }
}
