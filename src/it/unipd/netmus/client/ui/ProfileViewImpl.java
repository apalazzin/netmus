/**
 * 
 */
package it.unipd.netmus.client.ui;

import java.util.Arrays;
import java.util.List;
import it.unipd.netmus.client.place.LoginPlace;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
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
   
   //@UiField Anchor logout;
   //@UiField Anchor back;
   
   
   // elemnti uiBinder
   @UiField(provided=true) final CellTable<Song> catalogo; 
   @UiField HTMLPanel container;
   @UiField HTMLPanel container_catalogo;
   @UiField HTMLPanel main_panel;
   @UiField HTMLPanel left_panel;
   
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

	   Window.addResizeHandler(new ResizeHandler() {

			@Override
			public void onResize(ResizeEvent event) {
				
				int catalogo_h = main_panel.getOffsetHeight();
				
				left_panel.getElement().getStyle().setHeight(event.getHeight()-60, Style.Unit.PX);
				main_panel.getElement().getStyle().setHeight(event.getHeight()-60, Style.Unit.PX);

				container_catalogo.getElement().getStyle().setHeight(event.getHeight()-270, Style.Unit.PX);

			
			}
			
		});
	   
	  
	   
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

		   }
	   };
	   timerMain.schedule(10);
	   
	   	   
   }
 /*  
   @UiHandler("back")
   void handleClickBack(ClickEvent e) {
      listener.goTo(new LoginPlace(name));
   }
   
   @UiHandler("logout")
   void handleClickLogout(ClickEvent e) {
      listener.logout();
   }
*/
   @Override
   public void setName(String profileName) {
      this.name = profileName;
   }

   @Override
   public void setPresenter(Presenter listener) {
      this.listener = listener;
   }

   
}
