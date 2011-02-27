/**
 * 
 */
package it.unipd.netmus.client.ui;

import java.util.List;

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
   void setNumeroBrani(int numero);
   void setUser(String username);
   void setRating(int rating);

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
   void addToPLaylist(String autore, String titolo, String album);
   //rimuovi il brano dalla playlist
   void removeFromPlaylist(String autore, String titolo, String album);
   //inizializza il layout
   void setLayout();
   //Aggiunge una nuova playlist
   void addToPlaylists(String titolo);
   //Visulaizza la scheda della canzone
   void viewSong(Song song);
   //Chiude la scheda della canzone
   void closeSong();
   //Imposta i campi della canzone nella scheda di dettaglio
   void setSongFields(String autore, String titolo, String album, String genere,
			String anno, String compositore, String traccia, String cover);
   //Mostra il rating dato come input
   void showStar(int index);
   //Aggiorna il global rating
   void showGlobalStar(double d);
   //aggiorna carica dati dell'EditProifle
   void showEditProfile(String nickname, String name, String surname, String nationality, String gender, String aboutme);
   //elimina la canzone dal catalogo
   void deleteSong(String autore, String titolo, String album);

   public interface Presenter
   {
      void logout();
      void goTo(Place place);
      
      //restituisce la lista dei titoli delle singole playlist dell'utente
      void setPlaylistList(); 

      //restituisce la lista degli utenti affini su Netmus
      void setFriendList(); 

      //restituisce il titolo della canzone in ascolto
      void setSongInfo(); 
      
      //restituisce le canzoni con i relativi album data una data playlist
      void setPlaylistSongs(String titoloPlaylist);

      //restituisce il summary delle canzoni
      void setSongs();
      
      //restituisce il rating della canzone selezionata
      double setRating(String artist, String title, String album);

      //restituisce il link youtube della canzone selezionata
      void playYouTube(String autore, String titolo, String album);

      //Aggiunge una nuova Playlist
      void addPlaylist(String title);

      //aggiunge song alla playlist e restituisce true in caso di successo
      void addToPLaylist(String playlist, String autore, String titolo, String album);

      //aggiunge song alla playlist e restituisce true in caso di successo
      void removeFromPLaylist(String playlist, String autore, String titolo, String album);
      
      //Imposta i campi della canzone selezionata.
      void setSongFields(String autore, String titolo, String album);
      
      //attribuisce un punteggio compreso tra 1 e 5 alla canzone selezionata
      void rateSong(String artist, String title, String album, int rate);
      
      //Elimina la playlist
      void deletePlaylist(String playlist_name);

      //Spasta in basso la canzone della playlist selezionata
      void moveDownInPLaylist(String playlist, String autore, String titolo, String album);
      
      //Spasta in basso la canzone della playlist selezionata
      void moveUpInPLaylist(String playlist, String autore, String titolo, String album);
      
      //Controlla i permessi ed avvia la visualizzazione del profilo dell'utente in input
      void viewOtherLibrary(String user);
      
      //Elimina la canzone
      void deleteSong(String autore, String titolo, String album);
      
      //Esporta la lista delle canzoni in pdf
      void exportPDF(String user);
      
      //Apre la sezione di visualizzazione/modifica del profilo personale
      void editProfileView(String user);
      
      //Invia i dati modficati quando viene premuti il pulsante salva
      void editProfile(String user, String nick_name, String first_name, String last_name
              , String gender, String nationality, String aboutMe, String password);
      
      //Salva il titolo della canzone che è stato modificato dall'utente
      void editSongTitle(String new_title, String old_title, String artist, String album);
      
      //Salva il nome dell'album che è stato modificato dall'utente
      void editSongAlbum(String new_album, String old_album, String artist, String title);
      
      //Salva il nome dell'artista che è stato modificato dall'utente
      void editSongArtist(String new_artist, String old_artist, String title, String album);

   }


}
