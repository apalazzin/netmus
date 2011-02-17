/**
 * 
 */
package it.unipd.netmus.client.ui;

import java.util.Arrays;
import java.util.List;
import it.unipd.netmus.client.place.LoginPlace;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author smile
 *
 */
public class ProfileViewImpl extends Composite implements ProfileView {

   private static ProfileViewImplUiBinder uiBinder = GWT.create(ProfileViewImplUiBinder.class);

   interface ProfileViewImplUiBinder extends UiBinder<Widget, ProfileViewImpl>
   {
   }
   
   private Presenter listener;
   private String name;
   
   //elementi uiBinder

   @UiField Anchor logout;
   //@UiField Anchor back;
   
   @UiField Label utente;
   @UiField Label numero_brani;
   
   @UiField(provided=true) final CellTable<Song> catalogo; 
   @UiField HTMLPanel container;
   @UiField HTMLPanel container_catalogo;
   @UiField HTMLPanel main_panel;
   @UiField HTMLPanel left_panel;
   @UiField HTMLPanel playlists;
   @UiField HTMLPanel friends;
   
   @UiField Image play;
   @UiField Image forward;
   @UiField Image rewind;
   
   //classe interna che rappresenta un brano
   private static class Song {

	   private final String autore;
	   private final String titolo;
	   private final String album;
	   
	   public Song(String autore, String titolo, String album) {
		   this.autore = autore;
		   this.titolo = titolo;
		   this.album = album;
	   }
   }
   
   
   // Lista fittizia di brani da popolare in teoria via database
   private static List<Song> SONGS = Arrays.asList(new Song("Tokio Hotel",
	       "Monsoon", "Scream"), new Song("Tokio Hotel","Forever Now", "Humanoid"), new Song("Tokio Hotel","Der Letzte Tag", "Schrei so laut du kannst"),new Song("Tokio Hotel",
	    	       "Monsoon", "Scream"), new Song("Tokio Hotel","Forever Now", "Humanoid"), new Song("Tokio Hotel","Der Letzte Tag", "Schrei so laut du kannst"),new Song("Tokio Hotel",
	    	    	       "Monsoon", "Scream"), new Song("Tokio Hotel","Forever Now", "Humanoid"), new Song("Tokio Hotel","Der Letzte Tag", "Schrei so laut du kannst"),new Song("Tokio Hotel",
	    	    	    	       "Monsoon", "Scream"), new Song("Tokio Hotel","Forever Now", "Humanoid"), new Song("Tokio Hotel","Der Letzte Tag", "Schrei so laut du kannst"),new Song("Tokio Hotel",
	    	    	    	    	       "Monsoon", "Scream"), new Song("Tokio Hotel","Forever Now", "Humanoid"), new Song("Tokio Hotel","Alien", "Humanoid"), new Song("Tokio Hotel","Zoom Into Me", "Humanoid"),new Song("Tokio Hotel",
	    	    	    	    	    	       "Monsoon", "Scream"), new Song("Tokio Hotel","Forever Now", "Humanoid"), new Song("Tokio Hotel","Der Letzte Tag", "Schrei so laut du kannst"),new Song("Tokio Hotel",
	    	    	    	    	    	    	       "Monsoon", "Scream"), new Song("Tokio Hotel","Forever Now", "Humanoid"), new Song("Tokio Hotel","Der Letzte Tag", "Schrei so laut du kannst"),new Song("Tokio Hotel",
	    	    	    	    	    	    	    	       "Monsoon", "Scream"), new Song("Tokio Hotel","Forever Now", "Humanoid"), new Song("Tokio Hotel","Der Letzte Tag", "Schrei so laut du kannst"),new Song("Tokio Hotel",
	    	    	    	    	    	    	    	    	       "Monsoon", "Scream"), new Song("Tokio Hotel","Forever Now", "Humanoid"), new Song("Tokio Hotel","Der Letzte Tag", "Schrei so laut du kannst"),new Song("Tokio Hotel",
	    	    	    	    	    	    	    	    	    	       "Monsoon", "Scream"), new Song("Tokio Hotel","Forever Now", "Humanoid"), new Song("Tokio Hotel","Alien", "Humanoid"), new Song("Tokio Hotel","Zoom Into Me", "Humanoid"));
   
   
  
   public ProfileViewImpl() {

	   //Imposta la dimensione delle componenti della view in base alla dimensione della finestra del browser quando viene ridimensionata
	   Window.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {				
				int catalogo_h = main_panel.getOffsetHeight();
				left_panel.getElement().getStyle().setHeight(event.getHeight()-60, Style.Unit.PX);
				main_panel.getElement().getStyle().setHeight(event.getHeight()-60, Style.Unit.PX);
				container_catalogo.getElement().getStyle().setHeight(event.getHeight()-270, Style.Unit.PX);
			}			
		});
	   
	  
	   
	   //costruisco la componente widget x il catalogo delle canzoni
	   catalogo = new CellTable<Song>();
	   catalogo.setWidth("100%");
	   catalogo.setPageSize(100000);
	  
	   initWidget(uiBinder.createAndBindUi(this));

	   //crea la colonna autore
	   TextColumn<Song> autoreColumn = new TextColumn<Song>() {
		   @Override
	       public String getValue(Song song) {
			   return song.autore;
		   }
	   };
	   //la rende ordinabile
	   autoreColumn.setSortable(true);
	   // la aggiunge al catalogo
	   catalogo.addColumn(autoreColumn, "Autore");

	   //crea la colonna titolo
	   TextColumn<Song> titoloColumn = new TextColumn<Song>() {
		   @Override
	       public String getValue(Song song) {
			   return song.titolo;
		   }
	   };
	   //la rende ordinabile
	   titoloColumn.setSortable(true);
	   // la aggiunge al catalogo
	   catalogo.addColumn(titoloColumn, "Titolo");

	   
	   //crea la colonna Album
	   TextColumn<Song> albumColumn = new TextColumn<Song>() {
		   @Override
	       public String getValue(Song song) {
			   return song.album;
		   }
	   };
	   //la rende ordinabile
	   autoreColumn.setSortable(true);
	   // la aggiunge al catalogo
	   catalogo.addColumn(albumColumn, "Album");

	   
	   // Create a data provider.
	   ListDataProvider<Song> dataProvider = new ListDataProvider<Song>();
	   // Connect the table to the data provider.
	   dataProvider.addDataDisplay(catalogo);
	   // Add the data to the data provider, which automatically pushes it to the
	   // widget.
	   List<Song> list = dataProvider.getList();
	   for (Song song : SONGS) {
		   list.add(song);
	   }
	   
	   
	   
	   Timer timerMain = new Timer() {
		   public void run() {
			   
				left_panel.getElement().getStyle().setHeight(Window.getClientHeight()-60, Style.Unit.PX);
				main_panel.getElement().getStyle().setHeight(Window.getClientHeight()-60, Style.Unit.PX);
				container_catalogo.getElement().getStyle().setHeight(Window.getClientHeight()-270, Style.Unit.PX);

				//recupero lo username e lo imposto
				utente.setText(listener.getUsername());
				numero_brani.setText(listener.getLibrarySize());
				
				paintPlaylist(listener.getPlaylistList());
				paintFriendlist(listener.getFriendList());

		   }
	   };
	   timerMain.schedule(10);
	   
	   	   
   }
 /*  
   @UiHandler("back")
   void handleClickBack(ClickEvent e) {
      listener.goTo(new LoginPlace(name));
   }
*/   
   @UiHandler("logout")
   void handleClickLogout(ClickEvent e) {
      listener.logout();
   }
   
   @UiHandler("play")
   void handleMouseOverPlay(MouseOverEvent e) {
      play.setUrl("images/pause.png");
      play.getElement().getStyle().setCursor(Style.Cursor.POINTER);
   }
   @UiHandler("play")
   void handleMouseOut(MouseOutEvent e) {
      play.setUrl("images/play.png");
   }

   @UiHandler("forward")
   void handleMouseOverForward(MouseOverEvent e) {
      forward.getElement().getStyle().setCursor(Style.Cursor.POINTER);
   }
   @UiHandler("rewind")
   void handleMouseOverRewind(MouseOverEvent e) {
      rewind.getElement().getStyle().setCursor(Style.Cursor.POINTER);
   }

   @Override
   public void setName(String profileName) {
      this.name = profileName;
   }

   @Override
   public void setPresenter(Presenter listener) {
      this.listener = listener;
   }

   private void paintPlaylist(String[] lista) {
	   
	   for(int k=0; k< lista.length; k++) {

		   	Label tmpTxt = new Label(lista[k]);
			tmpTxt.getElement().getStyle().setMarginLeft(11, Style.Unit.PX);
			Image tmpImg = new Image("images/playlist.jpg");
			tmpImg.setWidth("25px");
			HorizontalPanel tmpPnl = new HorizontalPanel();
			tmpPnl.add(tmpImg);
			tmpPnl.add(tmpTxt);
			
			playlists.add(tmpPnl);
	   }
   }

   private void paintFriendlist(String[] lista) {
	   
	   for(int k=0; k< lista.length; k++) {

		   	Label tmpTxt = new Label(lista[k]);
		   	Label bull = new Label("\u2022");
		   	bull.getElement().getStyle().setColor("#000000");
		   	bull.getElement().getStyle().setMarginRight(11, Style.Unit.PX);
			HorizontalPanel tmpPnl = new HorizontalPanel();
			tmpPnl.add(bull);
			tmpPnl.add(tmpTxt);
			
			friends.add(tmpPnl);
	   }
   }

   
}
