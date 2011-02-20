/**
 * 
 */
package it.unipd.netmus.client.ui;

import java.util.List;

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
   
    //classe interna che rappresenta un brano
    public abstract class Song{
        
        @Override
        public abstract boolean equals(Object o);
    };

    
   void setName(String profileName);
   void setPresenter(Presenter listener);
   void setNumeroBrani(String numero);
   void setUser(String username);

   //riempie il catalogo
   void paintCatalogo(List<String> lista_canzoni);
   //riempie la lista delle playlist
   void paintPlaylist(String[] lista);
   // riempie la singola playlist delle sue canzoni
   void paintPlaylistSongs(List<String> lista);
   //riempie la lista dei contatti
   void paintFriendlist(String[] lista);
   //imposta la finestra informativa
   void setInfo(String testo);
   //visualizza la playlist scelta
   void viewPlaylist(String playlist_name);
   //chiude il gestore della playlist
   void closePlaylist();
   //apre il player youtube sulla relativa canzone
   void playYouTube(String titolo);
   //chiude il player youtube
   void closeYouTube();
   //imposta il brano selezionato sul catalogo
   void setBranoCatalogo(Song selezione);
   //imposta il brano selezionato sulla playlist
   void setBranoPlaylist(Song selezione);
   //aggiungi il brano del catalogo alla playlist 
   void addToPLaylist(Song brano);
   //rimuovi il brano dalla playlist
   void removeFromPlaylist(Song brano);
   //inizializza il layout
   void setLayout();
   
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
      List<String> getPlaylistSongs(String titoloPlaylist);

      
      //restituisce il summary delle canzoni
      List<String> getSongs();

      //restituisce il link youtube della canzone selezionata
      String getYouTubeLink();

      //aggiunge song alla playlist e restituisce true in caso di successo
      boolean addToPLaylist(String playlist, String autore, String titolo, String album);

      //aggiunge song alla playlist e restituisce true in caso di successo
      boolean removeFromPLaylist(String playlist, String autore, String titolo, String album);

   }
}
