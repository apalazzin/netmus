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
   //riempie la lista delle playlist
   void paintPlaylist(String[] lista);
   // riempie la singola playlist delle sue canzoni
   void paintPlaylistSongs(String[][] lista);
   void paintFriendlist(String[] lista);
   void setInfo(String testo);
   //visualizza la playlist scelta
   void viewPlaylist(String playlist_name);
   //chiude il gestore della playlist
   void closePlaylist();
   //apre il player youtube sulla relativa canzone
   void playYouTube(String titolo);
 //chiude il player youtube
   void closeYouTube();
   
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
      
      //restituisce le canzoni con i relativi album di una data playlist
      String[][] getPlaylistSongs(String titoloPlaylist);

      //restituisce il link youtube della canzone selezionata
      String getYouTubeLink();

   
   }
}
