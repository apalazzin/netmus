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
   void setNumeroBrani(String numero);
   void setUser(String username);
   void paintPlaylist(String[] lista);
   void paintFriendlist(String[] lista);
   void setInfo(String testo);

   
   public interface Presenter
   {
      void logout();
      void goTo(Place place);
      
      //restituisce lo username dell'utente connesso.
      String getUsername();
      
      //restituisce la dimensione del catalogo dell'utente
      String getLibrarySize();
      
      //restituisce la lista dei titoli delle singole playlist dell'utente
      String[] getPlaylistList(); 

      //restituisce la lista degli utenti affini su Netmus
      String[] getFriendList(); 

      //restituisce il titolo della canzone in ascolto
      String getSongInfo(); 

   
   }
}
