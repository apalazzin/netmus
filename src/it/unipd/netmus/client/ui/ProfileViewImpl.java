/**
 * 
 */
package it.unipd.netmus.client.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import it.unipd.netmus.client.place.LoginPlace;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTable.Resources;
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
import com.google.gwt.view.client.SelectionChangeEvent;
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
   private int vertical_offset = 65;
   private int vertical_semioffset = 275;
   
   private CellTable lista_canzoni;
   
   //elementi uiBinder

   @UiField Anchor logout;
   
   //@UiField Anchor back;
   
   @UiField Label utente;
   @UiField Label numero_brani;
   @UiField Label titolo_playlist;
   @UiField Label info_youtube_link;
   
   @UiField(provided=true) CellTable<Song> catalogo; 
   @UiField HTMLPanel container;
   @UiField HTMLPanel catalogo_container;
   @UiField HTMLPanel playlist_container;
   @UiField HTMLPanel playlist_contenuto;
   @UiField HTMLPanel playlist_songs;
   @UiField HTMLPanel main_panel;
   @UiField HTMLPanel left_panel;
   @UiField HTMLPanel playlists;
   @UiField HTMLPanel friends;
   @UiField HTMLPanel info;
   @UiField HTMLPanel search;
   @UiField HTMLPanel youtube;
   @UiField HTMLPanel classifica;
   @UiField HTMLPanel info_youtube;
   @UiField HTMLPanel youtube_appendice;
   
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
   @UiField Image logo_youtube;
   @UiField Image chiudi_youtube;

   Song selected;
   Song selected_inplaylist;
   
   List<Song> canzoni_catalogo = new ArrayList<Song>();
   List<Song> canzoni_playlist = new ArrayList<Song>();
   
   ListDataProvider<Song> dataProvider_playlist = new ListDataProvider<Song>();
   ListDataProvider<Song> dataProvider_catalogo = new ListDataProvider<Song>();

///////////////////////////////////////////////////////
///////////////////////////////////////////////////////

   
   //Estensione necessarria x la configurazione tramite CSS della cellTabel
   public interface MyCellTableResources extends CellTable.Resources {

       public interface CellTableStyle extends CellTable.Style {};

       @Source({"css/CellTable.css"})
       CellTableStyle cellTableStyle();
       
   }
   MyCellTableResources resource = GWT.create( MyCellTableResources.class);
   
   public interface MyCellTableResourcesPlaylist extends CellTable.Resources {

       public interface CellTableStyle extends CellTable.Style {};

       @Source({"css/CellTablePlaylist.css"})
       CellTableStyle cellTableStyle();
       
   }
   MyCellTableResourcesPlaylist resourcePlaylist = GWT.create( MyCellTableResourcesPlaylist.class);
   //Estensione necessarria x la configurazione tramite CSS della cellTabel
   
   
   
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
   /*private static List<Song> songs = Arrays.asList(new Song("Tokio Hotel",
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
   
 
 */ 
   
///////////////////////////////////////////////////////
///////////////////////////////////////////////////////

   
   
   public ProfileViewImpl() {

	   //Imposta la dimensione delle componenti della view in base alla dimensione della finestra del browser quando viene ridimensionata
	   Window.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {				
				int catalogo_h = main_panel.getOffsetHeight();
				left_panel.getElement().getStyle().setHeight(event.getHeight()-vertical_offset, Style.Unit.PX);
				main_panel.getElement().getStyle().setHeight(event.getHeight()-vertical_offset, Style.Unit.PX);
                catalogo_container.getElement().getStyle().setProperty("minHeight", 515-vertical_semioffset, Style.Unit.PX);
                playlist_container.getElement().getStyle().setProperty("minHeight", 515-vertical_semioffset, Style.Unit.PX);
				catalogo_container.getElement().getStyle().setHeight(event.getHeight()-(vertical_semioffset), Style.Unit.PX);
				playlist_container.getElement().getStyle().setHeight(event.getHeight()-(vertical_semioffset), Style.Unit.PX);
                
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
	   catalogo = new CellTable<Song>(Integer.MAX_VALUE, resource);
	   catalogo.setWidth("100%");
//	   catalogo.setPageSize(100000);
	  
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
	   
	   
	    // Imposta l'oggetto Song selected in base alla selezione sulla tabella
	    final SingleSelectionModel<Song> selectionModel = new SingleSelectionModel<Song>();
	    catalogo.setSelectionModel(selectionModel);
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	      public void onSelectionChange(SelectionChangeEvent event) {
	        Song selected = selectionModel.getSelectedObject();
	      }
	    });
	    
	    dataProvider_catalogo.addDataDisplay(catalogo);

	       playlist_songs.getElement().setInnerHTML("");

	       lista_canzoni = new CellTable<Song>(Integer.MAX_VALUE, resourcePlaylist);
	       lista_canzoni.setWidth("100%", true);

	       //crea la colonna titolo
	       TextColumn<Song> titoloColumn2 = new TextColumn<Song>() {
	           @Override
	           public String getValue(Song song) {
	               return song.titolo;
	           }
	       };
	       //la rende ordinabile
	       titoloColumn2.setSortable(true);
	       // la aggiunge al catalogo
	       lista_canzoni.addColumn(titoloColumn2, "Titolo");

	       
	       //crea la colonna Album
	       TextColumn<Song> albumColumn2 = new TextColumn<Song>() {
	           @Override
	           public String getValue(Song song) {
	               return song.album;
	           }
	       };
	       //la rende ordinabile
	       albumColumn2.setSortable(true);
	       // la aggiunge al catalogo
	       lista_canzoni.addColumn(albumColumn2, "Album");

	       
	       // Imposta l'oggetto Song selected_inplaylist in base alla selezione sulla tabella
	       final SingleSelectionModel<Song> selectionModel2 = new SingleSelectionModel<Song>();
	       lista_canzoni.setSelectionModel(selectionModel2);
	       selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	         public void onSelectionChange(SelectionChangeEvent event) {
	           Song selected_inplaylist = selectionModel.getSelectedObject();
	         }
	       });
	       
	       dataProvider_playlist.addDataDisplay(lista_canzoni);
	       playlist_songs.add(lista_canzoni);
	       
	       HTMLPanel off = new HTMLPanel("");
	       off.getElement().getStyle().setHeight(22, Style.Unit.PX);
	       playlist_songs.add(off);
	   
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
				left_panel.getElement().getStyle().setHeight(Window.getClientHeight()-vertical_offset, Style.Unit.PX);
				main_panel.getElement().getStyle().setHeight(Window.getClientHeight()-vertical_offset, Style.Unit.PX);				
				catalogo_container.getElement().getStyle().setHeight(Window.getClientHeight()-(vertical_semioffset), Style.Unit.PX);
				playlist_container.getElement().getStyle().setHeight(Window.getClientHeight()-(vertical_semioffset), Style.Unit.PX);

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
   void handleClickPlayYoutube(ClickEvent e) {
      playYouTube(listener.getYouTubeLink());
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

   @UiHandler("chiudi_youtube")
   void handleClickChiudiYoutube(ClickEvent e) {
      closeYouTube();
   }

   @UiHandler("play_youtube")
   void handleMouseOverChiudiYoutube(MouseOverEvent e) {     
      chiudi_youtube.getElement().getStyle().setCursor(Style.Cursor.POINTER);
   }
   @UiHandler("play_youtube")
   void handleMouseOutChiudiYouTube(MouseOutEvent e) {
      
   }

   @UiHandler("info_youtube_link")
   void handleMouseOverInfoYoutubeLink(MouseOverEvent e) {     
      info_youtube_link.getElement().getStyle().setColor("#38D12F");
      info_youtube_link.getElement().getStyle().setCursor(Style.Cursor.POINTER);
   }
   @UiHandler("info_youtube_link")
   void handleMouseOutInfoYouTubeLink(MouseOutEvent e) {
       info_youtube_link.getElement().getStyle().setColor("#000000");
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
      
      paintPlaylistSongs(listener.getPlaylistSongs(titolo));
       
      titolo_playlist.setText(titolo);
      catalogo_container.getElement().getStyle().setWidth(77, Style.Unit.PCT);
      playlist_container.getElement().getStyle().setWidth(23, Style.Unit.PCT);
      
      
      Timer timerPlaylist = new Timer() {
          public void run() {
              playlist_contenuto.getElement().getStyle().setOpacity(1);      
          }     
      };
      
      timerPlaylist.schedule(400);
      
   }

   @Override
   public void closePlaylist() {
       
      catalogo_container.getElement().getStyle().setWidth(100, Style.Unit.PCT);
      playlist_container.getElement().getStyle().setWidth(0, Style.Unit.PX);
      playlist_contenuto.getElement().getStyle().setOpacity(0);

      
   }

   
   //riempie la lista canzoni della playlist
   @Override
   public void paintPlaylistSongs(List<String> lista) {
       
       List<Song> test = dataProvider_playlist.getList();
       
       test.removeAll(canzoni_playlist);       
       canzoni_playlist.removeAll(canzoni_playlist);
       for (int j=0; j<lista.size(); j+=3) {
           
           canzoni_playlist.add(new Song(lista.get(j), lista.get(j+1), lista.get(j+2)));
       }
       
       
       for (Song song : canzoni_playlist) {
           test.add(song);
       }


   }
   
   
   public void playYouTube(String link) {
       
       
       youtube.getElement().getStyle().setHeight(215, Style.Unit.PX);
       youtube.getElement().getStyle().setWidth(335, Style.Unit.PX);
       youtube.getElement().getStyle().setBottom(7, Style.Unit.PX);
       youtube.getElement().getStyle().setLeft(5, Style.Unit.PX);

       vertical_semioffset = 400;
       catalogo_container.getElement().getStyle().setProperty("minHeight", 515-vertical_semioffset, Style.Unit.PX);
       playlist_container.getElement().getStyle().setProperty("minHeight", 515-vertical_semioffset, Style.Unit.PX);

       catalogo_container.getElement().getStyle().setHeight(Window.getClientHeight()-(vertical_semioffset), Style.Unit.PX);
       playlist_container.getElement().getStyle().setHeight(Window.getClientHeight()-(vertical_semioffset), Style.Unit.PX);
       
       play_youtube.getElement().getStyle().setOpacity(0);
   

       logo_youtube.getElement().getStyle().setPosition(Style.Position.ABSOLUTE);
       logo_youtube.getElement().getStyle().setBottom(173, Style.Unit.PX);
       logo_youtube.getElement().getStyle().setLeft(355, Style.Unit.PX);

       
       classifica.getElement().getStyle().setLeft(365, Style.Unit.PX);
       
       HTMLPanel player = new HTMLPanel("player");
       player.getElement().getStyle().setPosition(Style.Position.ABSOLUTE);
       player.getElement().getStyle().setTop(8, Style.Unit.PX);
       player.getElement().getStyle().setLeft(5, Style.Unit.PX);
       youtube.add(player);
       
       
       info_youtube.getElement().getStyle().setOpacity(1);
       info_youtube_link.getElement().getStyle().setOpacity(1);
       youtube_appendice.getElement().getStyle().setOpacity(1);
       chiudi_youtube.getElement().getStyle().setOpacity(1);

       
       player.getElement().setInnerHTML("<iframe title=\"YouTube video player\" width=\"325\" height=\"200\" src=\"http://www.youtube.com/embed/" + link + "\" frameborder=\"0\" allowfullscreen></iframe>");

       
   }

   public void closeYouTube() {
       
       youtube.getElement().getStyle().setHeight(60, Style.Unit.PX);
       youtube.getElement().getStyle().setWidth(200, Style.Unit.PX);
       youtube.getElement().getStyle().setBottom(22, Style.Unit.PX);
       youtube.getElement().getStyle().setLeft(25, Style.Unit.PX);

       vertical_semioffset = 275;
       catalogo_container.getElement().getStyle().setHeight(Window.getClientHeight()-(vertical_semioffset), Style.Unit.PX);
       playlist_container.getElement().getStyle().setHeight(Window.getClientHeight()-(vertical_semioffset), Style.Unit.PX);
       
       play_youtube.getElement().getStyle().setOpacity(1);
       logo_youtube.getElement().getStyle().setOpacity(1);

       logo_youtube.getElement().getStyle().setPosition(Style.Position.RELATIVE);
       logo_youtube.getElement().getStyle().setBottom(0, Style.Unit.PX);
       logo_youtube.getElement().getStyle().setLeft(0, Style.Unit.PX);
       
       
       info_youtube.getElement().getStyle().setOpacity(0);
       info_youtube_link.getElement().getStyle().setOpacity(0);
       youtube_appendice.getElement().getStyle().setOpacity(0);
       chiudi_youtube.getElement().getStyle().setOpacity(0);
       
       classifica.getElement().getStyle().setLeft(265, Style.Unit.PX);
       
       youtube.getWidget(6).removeFromParent();
       

       
   }




@Override
public void paintCatalogo(List<String> lista_canzoni) {

    List<Song> test = dataProvider_catalogo.getList();
    
    test.removeAll(canzoni_catalogo);       
    canzoni_catalogo.removeAll(canzoni_catalogo);
    
    for (int j=0; j<lista_canzoni.size(); j+=3) {
        
        canzoni_catalogo.add(new Song(lista_canzoni.get(j), lista_canzoni.get(j+1), lista_canzoni.get(j+2)));
    }
    
    for (Song song : canzoni_catalogo) {
        test.add(song);
    }
 
    
}
   
   
}


