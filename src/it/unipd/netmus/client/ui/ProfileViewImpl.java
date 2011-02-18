/**
 * 
 */
package it.unipd.netmus.client.ui;

import java.util.Arrays;
import java.util.List;
import it.unipd.netmus.client.place.LoginPlace;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import com.google.gwt.view.client.SingleSelectionModel;
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
   @UiField Label titolo_playlist;
   
   @UiField(provided=true) CellTable<Song> catalogo; 
   @UiField HTMLPanel container;
   @UiField HTMLPanel catalogo_container;
   @UiField HTMLPanel playlist_container;
   @UiField HTMLPanel main_panel;
   @UiField HTMLPanel left_panel;
   @UiField HTMLPanel playlists;
   @UiField HTMLPanel friends;
   @UiField HTMLPanel info;
   @UiField HTMLPanel search;
   
   @UiField Image play;
   @UiField Image play_youtube;
   @UiField Image forward;
   @UiField Image rewind;
   @UiField Image cover;
   @UiField Image edit_button;
   @UiField Image account_button;
   @UiField Image social_button;
   @UiField Image star1;
   @UiField Image star2;
   @UiField Image star3;
   @UiField Image star4;
   @UiField Image star5;
   @UiField Image chiudi_playlist;
   
   
///////////////////////////////////////////////////////
///////////////////////////////////////////////////////

   
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
   
   
  
   
///////////////////////////////////////////////////////
///////////////////////////////////////////////////////

   
   
   public ProfileViewImpl() {

	   //Imposta la dimensione delle componenti della view in base alla dimensione della finestra del browser quando viene ridimensionata
	   Window.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {				
				int catalogo_h = main_panel.getOffsetHeight();
				left_panel.getElement().getStyle().setHeight(event.getHeight()-65, Style.Unit.PX);
				main_panel.getElement().getStyle().setHeight(event.getHeight()-65, Style.Unit.PX);
				catalogo_container.getElement().getStyle().setHeight(event.getHeight()-275, Style.Unit.PX);
				playlist_container.getElement().getStyle().setHeight(event.getHeight()-275, Style.Unit.PX);
                
				if(event.getWidth()<1200) {
					
					search.getElement().getStyle().setOpacity(0);
					social_button.getElement().getStyle().setRight(25, Style.Unit.PX);
					account_button.getElement().getStyle().setRight(95, Style.Unit.PX);
					edit_button.getElement().getStyle().setRight(165, Style.Unit.PX);					
					
				} else {
					
					social_button.getElement().getStyle().setRight(230, Style.Unit.PX);
					account_button.getElement().getStyle().setRight(300, Style.Unit.PX);
					edit_button.getElement().getStyle().setRight(370, Style.Unit.PX);
					
					   Timer timerSearch = new Timer() {
						   public void run() {
							   
							   if(Window.getClientWidth()>1200)
								search.getElement().getStyle().setOpacity(1);
								
						   }
					   };
					   timerSearch.schedule(300);
					
				}
				
				
				if(event.getHeight()<650) {
					
					friends.getElement().getStyle().setOpacity(0);
					
				} else {
					
					friends.getElement().getStyle().setOpacity(1);
					
				}

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
	   
	   //final SingleSelectionModel<Song> selectionModel = new SingleSelectionModel<Song>();
	   //catalogo.setSelectionModel(selectionModel);
	   

	   
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
			   
			   
			  		if(Window.getClientWidth()<1200) {
						
						search.getElement().getStyle().setOpacity(0);
						social_button.getElement().getStyle().setRight(25, Style.Unit.PX);
						account_button.getElement().getStyle().setRight(95, Style.Unit.PX);
						edit_button.getElement().getStyle().setRight(165, Style.Unit.PX);

						
						
					} else {
						
						social_button.getElement().getStyle().setRight(230, Style.Unit.PX);
						account_button.getElement().getStyle().setRight(300, Style.Unit.PX);
						edit_button.getElement().getStyle().setRight(370, Style.Unit.PX);
						
						   Timer timerSearch = new Timer() {
							   public void run() {
								   
								   if(Window.getClientWidth()>1200)
									search.getElement().getStyle().setOpacity(1);
									
							   }
						   };
						   timerSearch.schedule(300);
						
					}
			  		
			  		
					if(Window.getClientHeight()<650) {
						
						friends.getElement().getStyle().setOpacity(0);
						
					} else {
						
						friends.getElement().getStyle().setOpacity(1);
						
					}

			  		
			   	//ridimensiono il layout in base alla dimensione della finestra del browser
				left_panel.getElement().getStyle().setHeight(Window.getClientHeight()-65, Style.Unit.PX);
				main_panel.getElement().getStyle().setHeight(Window.getClientHeight()-65, Style.Unit.PX);
				catalogo_container.getElement().getStyle().setHeight(Window.getClientHeight()-275, Style.Unit.PX);
				playlist_container.getElement().getStyle().setHeight(Window.getClientHeight()-275, Style.Unit.PX);

		   }
	   };
	   timerMain.schedule(10);
	   
	   	   
   }
 
   
   
   
///////////////////////////////////////////////////////
///////////////////////////////////////////////////////
   
   
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
   void handleMouseOutPlay(MouseOutEvent e) {
      play.setUrl("images/play.png");
   }
   
   @UiHandler("play_youtube")
   void handleMouseOverPlayYoutube(MouseOverEvent e) {
      play_youtube.setUrl("images/pause.png");
      play_youtube.getElement().getStyle().setCursor(Style.Cursor.POINTER);
   }
   @UiHandler("play_youtube")
   void handleMouseOutPlayYouTube(MouseOutEvent e) {
      play_youtube.setUrl("images/play.png");
   }

   @UiHandler("forward")
   void handleMouseOverForward(MouseOverEvent e) {
      forward.getElement().getStyle().setCursor(Style.Cursor.POINTER);
   }
   @UiHandler("rewind")
   void handleMouseOverRewind(MouseOverEvent e) {
      rewind.getElement().getStyle().setCursor(Style.Cursor.POINTER);
   }


   @UiHandler("star1")
   void handleMouseOverStar1(MouseOverEvent e) {

	   star1.getElement().getStyle().setCursor(Style.Cursor.POINTER);
	   star1.setUrl("images/star.png");
   }
   @UiHandler("star1")
   void handleMouseOutStar1(MouseOutEvent e) {
	   star1.setUrl("images/starbw.png");
   }
   @UiHandler("star2")
   void handleMouseOverStar2(MouseOverEvent e) {
	   star2.getElement().getStyle().setCursor(Style.Cursor.POINTER);
	   star1.setUrl("images/star.png");
	   star2.setUrl("images/star.png");
   }
   @UiHandler("star2")
   void handleMouseOutStar2(MouseOutEvent e) {
	   star1.setUrl("images/starbw.png");
	   star2.setUrl("images/starbw.png");
   }
   @UiHandler("star3")
   void handleMouseOverStar3(MouseOverEvent e) {
	   star3.getElement().getStyle().setCursor(Style.Cursor.POINTER);
	   star1.setUrl("images/star.png");
	   star2.setUrl("images/star.png");
	   star3.setUrl("images/star.png");
   }
   @UiHandler("star3")
   void handleMouseOutStar3(MouseOutEvent e) {
	   star1.setUrl("images/starbw.png");
	   star2.setUrl("images/starbw.png");
	   star3.setUrl("images/starbw.png");
   }
   @UiHandler("star4")
   void handleMouseOverStar4(MouseOverEvent e) {
	   star4.getElement().getStyle().setCursor(Style.Cursor.POINTER);
	   star1.setUrl("images/star.png");
	   star2.setUrl("images/star.png");
	   star3.setUrl("images/star.png");
	   star4.setUrl("images/star.png");
   }
   @UiHandler("star4")
   void handleMouseOutStar4(MouseOutEvent e) {
	   star1.setUrl("images/starbw.png");
	   star2.setUrl("images/starbw.png");
	   star3.setUrl("images/starbw.png");
	   star4.setUrl("images/starbw.png");
   }
   @UiHandler("star5")
   void handleMouseOverStar5(MouseOverEvent e) {
	   star5.getElement().getStyle().setCursor(Style.Cursor.POINTER);
	   star1.setUrl("images/star.png");
	   star2.setUrl("images/star.png");
	   star3.setUrl("images/star.png");
	   star4.setUrl("images/star.png");
	   star5.setUrl("images/star.png");
   }
   @UiHandler("star5")
   void handleMouseOutStar5(MouseOutEvent e) {
	   star1.setUrl("images/starbw.png");
	   star2.setUrl("images/starbw.png");
	   star3.setUrl("images/starbw.png");
	   star4.setUrl("images/starbw.png");
	   star5.setUrl("images/starbw.png");
   }

   @UiHandler("chiudi_playlist")
   void handleClickChiudiPlaylist(ClickEvent e) {
      closePlaylist();
   }
   
   @UiHandler("chiudi_playlist")
   void handleMouseOverChiudiPlaylist(MouseOverEvent e) {
      chiudi_playlist.getElement().getStyle().setCursor(Style.Cursor.POINTER);
   }
   
///////////////////////////////////////////////////////
///////////////////////////////////////////////////////
   
   
   @Override
   public void setName(String profileName) {
      this.name = profileName;
   }

   @Override
   public void setPresenter(Presenter listener) {
      this.listener = listener;
   }

   @Override
   public void setNumeroBrani(String numero) {
	   numero_brani.setText(numero);
   }
   
   //riempie la lista delle playlists
   @Override
   public void paintPlaylist(String[] lista) {
	   
	   for(int k=0; k< lista.length; k++) {

		   	Label tmpTxt = new Label(lista[k]);
			tmpTxt.getElement().getStyle().setMarginLeft(11, Style.Unit.PX);
	        tmpTxt.getElement().getStyle().setCursor(Style.Cursor.POINTER);
	        
			Image tmpImg = new Image("images/playlist.jpg");
			tmpImg.setWidth("25px");
			HorizontalPanel tmpPnl = new HorizontalPanel();
			tmpPnl.add(tmpImg);
			tmpPnl.add(tmpTxt);
			
			tmpTxt.addClickHandler(new ClickHandler() { 
			    
                @Override
                public void onClick(ClickEvent event) {
                    viewPlaylist(((Label)event.getSource()).getText());
                }
			    
			});
			
			
			playlists.add(tmpPnl);
	   }
   }

   //riempie la lista degli amici netmus
   @Override
   public void paintFriendlist(String[] lista) {
	   
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

   //imposta il testo all'interno della finestra informativa
   @Override
   public void setInfo(String text) {
	   info.getElement().setInnerHTML(text);
   }




   @Override
   public void setUser(String username) {
	   utente.setText(username);
	
   }


   @Override
   public void viewPlaylist(String titolo) {
       
      titolo_playlist.setText(titolo);
      catalogo_container.getElement().getStyle().setWidth(80, Style.Unit.PCT);
      playlist_container.getElement().getStyle().setWidth(20, Style.Unit.PCT);
      playlist_container.getElement().getStyle().setOpacity(1);
      
   }

   @Override
   public void closePlaylist() {
       
      catalogo_container.getElement().getStyle().setWidth(100, Style.Unit.PCT);
      playlist_container.getElement().getStyle().setWidth(0, Style.Unit.PX);
      playlist_container.getElement().getStyle().setOpacity(0);
      
   }

   
}
