/**
 * 
 */
package it.unipd.netmus.client.ui;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * 
 * 
 * @author ValterTexasGroup
 *
 */
public interface EditSongsView extends IsWidget {
   
   void setName(String editSongsName);
   void setPresenter(Presenter listener);

   public interface Presenter
   {
      void goTo(Place place);
      public enum LoginType{
      	   NETMUSLOGIN, NETMUSREGISTRATION
         }
   }
}
