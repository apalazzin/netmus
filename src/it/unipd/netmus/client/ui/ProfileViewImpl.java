/**
 * 
 */
package it.unipd.netmus.client.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import it.unipd.netmus.client.place.LoginPlace;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTable.Resources;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
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
   private int rating;
   
   private CellTable<Song> lista_canzoni;
   
   //elementi uiBinder

   @UiField Anchor logout;
   
   //@UiField Anchor back;
   
   @UiField Label utente;
   @UiField Label numero_brani;
   @UiField Label titolo_playlist;
   @UiField Label titolo_song;
   @UiField Label info_youtube_link;
   @UiField Label brano_aggiungere;
   @UiField Label brano_rimuovere;
   @UiField Label song_titolo;
   @UiField Label song_autore;
   @UiField Label song_album;
   @UiField Label song_genere;
   @UiField Label song_anno;
   @UiField Label song_compositore;
   @UiField Label song_traccia;
   
   @UiField(provided=true) CellTable<Song> catalogo; 
   @UiField HTMLPanel container;
   @UiField HTMLPanel catalogo_container;
   @UiField HTMLPanel playlist_container;
   @UiField HTMLPanel playlist_contenuto;
   @UiField HTMLPanel playlist_songs;
   @UiField HTMLPanel song_container;
   @UiField HTMLPanel song_contenuto;
   @UiField HTMLPanel main_panel;
   @UiField HTMLPanel left_panel;
   @UiField HTMLPanel playlists;
   @UiField HTMLPanel friends;
   @UiField HTMLPanel friends_titolo;
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
   @UiField Image chiudi_song;
   @UiField Image logo_youtube;
   @UiField Image chiudi_youtube;
   @UiField Image aggiungi_branoplaylist;
   @UiField Image rimuovi_branoplaylist;
   @UiField Image aggiungi_playlist;
   @UiField Image song_cover;
   
   boolean playlist_opened; 
   boolean song_opened; 
   
   Song selected_song;
   Song selected_song_playlist;
   
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
   private static class Song extends it.unipd.netmus.client.ui.ProfileView.Song {

	   private final String autore;
	   private final String titolo;
	   private final String album;
	   
	   public Song(String autore, String titolo, String album) {
		   this.autore = autore;
		   this.titolo = titolo;
		   this.album = album;
	   }
	   
	   @Override
	   public boolean equals(Object o) {

	       if (o==null)return false;
	       if (!(o instanceof Song)) return false;
	       
	       Song song = (Song) o;
	       
	       if(this.autore.equals(song.autore) && this.titolo.equals(song.titolo) && this.album.equals(song.album)) {
	           
	           return true;
	       }
	           return false;
	       
        }

   }
 
   
///////////////////////////////////////////////////////
///////////////////////////////////////////////////////

   
   
   public ProfileViewImpl() {

	   //costruisco la componente widget x il catalogo delle canzoni
	   catalogo = new CellTable<Song>(Integer.MAX_VALUE, resource);
	   catalogo.setWidth("100%", true);
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
	          
	         setBranoCatalogo(selectionModel.getSelectedObject());
	         
	         titolo_song.setText(selected_song.titolo);
	         song_titolo.setText(selected_song.titolo);
	         song_autore.setText(selected_song.autore);
	         song_album.setText(selected_song.album);
	         
	         listener.setSongFields(selected_song.autore, selected_song.titolo, selected_song.album);
	         
	         listener.loadRating(selected_song.autore,selected_song.titolo,selected_song.album);
	         showStar(rating);
	      }
	    });
	    
	    catalogo.addDomHandler(new DoubleClickHandler() {

            @Override
            public void onDoubleClick(DoubleClickEvent event) {

                if(playlist_opened) {
                    
                    listener.addToPLaylist(titolo_playlist.getText(), selected_song.autore, selected_song.titolo, selected_song.album);
                    
                } else {
                    
                    if(selected_song!=null)
                        viewSong(selected_song);                    
                }

            }}, DoubleClickEvent.getType());
	    
	    
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
	       lista_canzoni.setColumnWidth(titoloColumn2,"60%");

	       
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
	       lista_canzoni.setColumnWidth(albumColumn2,"60%");
	       
	       // Imposta l'oggetto Song selected_inplaylist in base alla selezione sulla tabella
	       final SingleSelectionModel<Song> selectionModel2 = new SingleSelectionModel<Song>();
	       lista_canzoni.setSelectionModel(selectionModel2);
	       selectionModel2.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	         public void onSelectionChange(SelectionChangeEvent event) {
	             
	           setBranoPlaylist(selectionModel2.getSelectedObject());	           
	         }
	       });
	       
	       dataProvider_playlist.addDataDisplay(lista_canzoni);
	       playlist_songs.add(lista_canzoni);
	       
	       HTMLPanel off = new HTMLPanel("");
	       off.getElement().getStyle().setHeight(22, Style.Unit.PX);
	       playlist_songs.add(off);	   
	   	   
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
       if(selected_song!=null) 
           listener.playYouTube(selected_song.autore, selected_song.titolo, selected_song.album);
   }

   @UiHandler("play_youtube")
   void handleMouseOverPlayYoutube(MouseOverEvent e) {
       if(selected_song!=null) {
           play_youtube.setUrl("images/pause.png");
           play_youtube.getElement().getStyle().setCursor(Style.Cursor.POINTER);
       }
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
	   showStar(this.rating);
   }
   @UiHandler("star1")
   void handleClickStar1(ClickEvent e) {
       if (this.selected_song != null)
           listener.rateSelectedSong(this.selected_song.autore,this.selected_song.titolo,this.selected_song.album, 1);
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
	   showStar(this.rating);
   }
   @UiHandler("star2")
   void handleClickStar2(ClickEvent e) {
       if (this.selected_song != null)
           listener.rateSelectedSong(this.selected_song.autore,this.selected_song.titolo,this.selected_song.album, 2);
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
	   showStar(this.rating);
   }
   @UiHandler("star3")
   void handleClickStar3(ClickEvent e) {
       if (this.selected_song != null)
           listener.rateSelectedSong(this.selected_song.autore,this.selected_song.titolo,this.selected_song.album, 3);
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
	   showStar(this.rating);
   }
   @UiHandler("star4")
   void handleClickStar4(ClickEvent e) {
       if (this.selected_song != null)
           listener.rateSelectedSong(this.selected_song.autore,this.selected_song.titolo,this.selected_song.album, 4);
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
	   showStar(this.rating);
   }
   @UiHandler("star5")
   void handleClickStar5(ClickEvent e) {
       if (this.selected_song != null)
           listener.rateSelectedSong(this.selected_song.autore,this.selected_song.titolo,this.selected_song.album, 5);
   }

   @UiHandler("chiudi_playlist")
   void handleClickChiudiPlaylist(ClickEvent e) {
      closePlaylist();
   }
   
   @UiHandler("chiudi_playlist")
   void handleMouseOverChiudiPlaylist(MouseOverEvent e) {
      chiudi_playlist.getElement().getStyle().setCursor(Style.Cursor.POINTER);
   }

   @UiHandler("edit_button")
   void handleClickEditButton(ClickEvent e) {
	  if(selected_song!=null)
		  viewSong(selected_song);
   }
   
   @UiHandler("edit_button")
   void handleMouseOverEditButton(MouseOverEvent e) {
	   if(selected_song!=null)
		   edit_button.getElement().getStyle().setCursor(Style.Cursor.POINTER);
   }


   @UiHandler("chiudi_song")
   void handleClickChiudiSong(ClickEvent e) {
      closeSong();
   }
   
   @UiHandler("chiudi_song")
   void handleMouseOverChiudiSong(MouseOverEvent e) {
      chiudi_song.getElement().getStyle().setCursor(Style.Cursor.POINTER);
   }

   
   @UiHandler("aggiungi_branoplaylist")
   void handleClickAggiungiBranoPlaylist(ClickEvent e) {
       listener.addToPLaylist(titolo_playlist.getText(), selected_song.autore, selected_song.titolo, selected_song.album);
   }
   
   @UiHandler("aggiungi_branoplaylist")
   void handleMouseOverAggiungiBranoPlaylist(MouseOverEvent e) {
      aggiungi_branoplaylist.getElement().getStyle().setCursor(Style.Cursor.POINTER);
   }

   @UiHandler("rimuovi_branoplaylist")
   void handleClickRimuoviBranoPlaylist(ClickEvent e) {
      listener.removeFromPLaylist(titolo_playlist.getText(), selected_song_playlist.autore, selected_song_playlist.titolo, selected_song_playlist.album);
   }
   
   @UiHandler("rimuovi_branoplaylist")
   void handleMouseOverRimuoviBranoPlaylist(MouseOverEvent e) {
      rimuovi_branoplaylist.getElement().getStyle().setCursor(Style.Cursor.POINTER);
   }

   @UiHandler("aggiungi_playlist")
   void handleClickAggiungiPlaylist(ClickEvent e) {
       
      addPlaylist(); 
      
   }

   @UiHandler("aggiungi_playlist")
   void handleMouseOverAggiungiPlaylist(MouseOverEvent e) {
      aggiungi_playlist.getElement().getStyle().setCursor(Style.Cursor.POINTER);
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
   public void setNumeroBrani(int numero) {
	   numero_brani.setText(String.valueOf(numero));
   }

   @Override
   public void setRating(int rating) {
       this.rating = rating;
   }
   
   @Override
   public void showStar(int index) {
       if (index > 0) 
           star1.setUrl("images/star.png");
       else 
           star1.setUrl("images/starbw.png");
       if (index > 1) 
           star2.setUrl("images/star.png");
       else 
           star2.setUrl("images/starbw.png");
       if (index > 2) 
           star3.setUrl("images/star.png");
       else 
           star3.setUrl("images/starbw.png");
       if (index > 3) 
           star4.setUrl("images/star.png");
       else 
           star4.setUrl("images/starbw.png");
       if (index > 4) 
           star5.setUrl("images/star.png");
       else 
           star5.setUrl("images/starbw.png");
   }
   
   //riempie la lista delle playlists
   @Override
   public void paintPlaylist(String[] lista) {
	          
	   for(int k=0; k< lista.length; k++) {

		   	final Label tmpTxt = new Label(lista[k]);
			tmpTxt.getElement().getStyle().setMarginLeft(11, Style.Unit.PX);
	        tmpTxt.getElement().getStyle().setCursor(Style.Cursor.POINTER);
            tmpTxt.getElement().getStyle().setColor("#37A6EB");
            tmpTxt.getElement().getStyle().setProperty("fontFamily", "Verdana");
            tmpTxt.getElement().getStyle().setMarginLeft(11, Style.Unit.PX);
            tmpTxt.getElement().getStyle().setFontSize(12, Style.Unit.PX);
            tmpTxt.getElement().getStyle().setProperty("fontWeight", "bold");
            
	        
			Image tmpImg = new Image("images/playlistT.png");
			tmpImg.setWidth("25px");
			tmpImg.getElement().getStyle().setMargin(2, Style.Unit.PX);
			tmpImg.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
			tmpImg.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
			
			final FocusPanel wrapper = new FocusPanel();

			HorizontalPanel tmpPnl = new HorizontalPanel();
			tmpPnl.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			tmpPnl.getElement().getStyle().setMarginBottom(0, Style.Unit.PX);
			tmpPnl.add(tmpImg);
			tmpPnl.add(tmpTxt);

			wrapper.getElement().getStyle().setMarginTop(0, Style.Unit.PX);
			wrapper.getElement().getStyle().setProperty("outline", "0");
			wrapper.addMouseOverHandler( new MouseOverHandler(){

                @Override
                public void onMouseOver(MouseOverEvent event) {                    
                      event.getRelativeElement().getStyle().setBackgroundColor("#e0fee1");
                      event.getRelativeElement().getStyle().setCursor(Style.Cursor.POINTER);
                } });
			
	         wrapper.addMouseOutHandler( new MouseOutHandler(){
	                @Override
	                public void onMouseOut(MouseOutEvent event) {
	                      event.getRelativeElement().getStyle().setBackgroundColor("#FFFFFF");
	                } });

	          wrapper.addClickHandler(new ClickHandler() { 
	                @Override
	                public void onClick(ClickEvent event) {
	                    viewPlaylist(tmpTxt.getText());
	                    wrapper.getElement().getStyle().setBackgroundColor("#FFFFFF");
	                    Timer timerButton = new Timer() {
	                        public void run() {
	                            wrapper.getElement().getStyle().setBackgroundColor("#e0fee1");      
	                        }     
	                    };
	                    timerButton.schedule(50);
	                }
	            });

			wrapper.add(tmpPnl);
			
			//playlists.getElement().setPropertyString("width", "100%");
			playlists.add(wrapper);
	   }
   }

   //riempie la lista degli amici netmus
   @Override
   public void paintFriendlist(String[] lista) {
	   
	   for(int k=0; k< lista.length; k++) {

		   	Label tmpTxt = new Label(lista[k]);
		   	Label bull = new Label("\u25FC");
		   	bull.getElement().getStyle().setFontSize(10, Style.Unit.PX);
		   	bull.getElement().getStyle().setColor("#555555");
		   	bull.getElement().getStyle().setMarginRight(10, Style.Unit.PX);
            bull.getElement().getStyle().setMarginBottom(6, Style.Unit.PX);
		   	
		   	
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
   public void viewSong(final it.unipd.netmus.client.ui.ProfileView.Song song) {
       
             
      if(playlist_opened) {
          
          closePlaylist();
          
          Timer timerOpen = new Timer() {
              public void run() {
     
                  
                  song_opened = true;
                  
                  Song s = (Song) song;
                  titolo_song.setText(s.titolo);
                  
                  catalogo_container.getElement().getStyle().setWidth(70, Style.Unit.PCT);
                  song_container.getElement().getStyle().setWidth(30, Style.Unit.PCT);
                  
                  
                  Timer timerPlaylist = new Timer() {
                      public void run() {
                          song_contenuto.getElement().getStyle().setOpacity(1);      
                      }     
                  };
                  
                  timerPlaylist.schedule(400);
                  
              }        
          };
                 
                 timerOpen.schedule(600);

          }  else {
              
              song_opened = true;

              Song s = (Song) song;
              titolo_song.setText(s.titolo);
              
              catalogo_container.getElement().getStyle().setWidth(70, Style.Unit.PCT);
              song_container.getElement().getStyle().setWidth(30, Style.Unit.PCT);
              
              
              Timer timerPlaylist = new Timer() {
                  public void run() {
                      song_contenuto.getElement().getStyle().setOpacity(1);      
                  }     
              };
              
              timerPlaylist.schedule(400);
              
          }
      
      
}  
      

   
   @Override
   public void closeSong() {
       
       song_opened = false;
       
       catalogo_container.getElement().getStyle().setWidth(100, Style.Unit.PCT);
       song_container.getElement().getStyle().setWidth(0, Style.Unit.PX);
       song_contenuto.getElement().getStyle().setOpacity(0);
       
   }

 
   @Override
   public void viewPlaylist(final String titolo) {
      
       if(song_opened)  {
           
           closeSong();
       
           Timer timerOpen = new Timer() {
               public void run() {
      
                   
                  playlist_opened= true;
                   
                  listener.setPlaylistSongs(titolo);
                   
                  titolo_playlist.setText(titolo);
                  catalogo_container.getElement().getStyle().setWidth(70, Style.Unit.PCT);
                  playlist_container.getElement().getStyle().setWidth(30, Style.Unit.PCT);
                  
                  
                  Timer timerPlaylist = new Timer() {
                      public void run() {
                          playlist_contenuto.getElement().getStyle().setOpacity(1);      
                      }     
                  };
                  
                  timerPlaylist.schedule(400);

               }     
           };
           
           timerOpen.schedule(600);
       
       
       } else {
           
           
          playlist_opened= true;
           
          listener.setPlaylistSongs(titolo);
           
          titolo_playlist.setText(titolo);
          catalogo_container.getElement().getStyle().setWidth(70, Style.Unit.PCT);
          playlist_container.getElement().getStyle().setWidth(30, Style.Unit.PCT);
          
          
          Timer timerPlaylist = new Timer() {
              public void run() {
                  playlist_contenuto.getElement().getStyle().setOpacity(1);      
              }     
          };
          
          timerPlaylist.schedule(400);

       }
      
   }

   @Override
   public void closePlaylist() {
       
      playlist_opened= false;
       
      brano_rimuovere.setText("");
      catalogo_container.getElement().getStyle().setWidth(100, Style.Unit.PCT);
      playlist_container.getElement().getStyle().setWidth(0, Style.Unit.PX);
      playlist_contenuto.getElement().getStyle().setOpacity(0);
      titolo_playlist.setText("");

      
   }

   
   public void playYouTube(String link) {
       
       
       youtube.getElement().getStyle().setHeight(215, Style.Unit.PX);
       youtube.getElement().getStyle().setWidth(335, Style.Unit.PX);
       youtube.getElement().getStyle().setBottom(7, Style.Unit.PX);
       youtube.getElement().getStyle().setLeft(5, Style.Unit.PX);

       vertical_semioffset = 400;
       
       setLayout();
       
       /*
       catalogo_container.getElement().getStyle().setProperty("minHeight", 515-vertical_semioffset, Style.Unit.PX);
       playlist_container.getElement().getStyle().setProperty("minHeight", 515-vertical_semioffset, Style.Unit.PX);
       song_container.getElement().getStyle().setProperty("minHeight", 515-vertical_semioffset, Style.Unit.PX);

       catalogo_container.getElement().getStyle().setHeight(Window.getClientHeight()-(vertical_semioffset), Style.Unit.PX);
       playlist_container.getElement().getStyle().setHeight(Window.getClientHeight()-(vertical_semioffset), Style.Unit.PX);
       song_container.getElement().getStyle().setHeight(Window.getClientHeight()-(vertical_semioffset), Style.Unit.PX);
       
       */
       
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

       
       player.getElement().setInnerHTML("<iframe title=\"YouTube video player\" width=\"325\" height=\"200\" src=\"http://www.youtube.com/embed/" + link + "?autoplay=1\" frameborder=\"0\" allowfullscreen></iframe>");

       
   }

   public void closeYouTube() {
       
       youtube.getElement().getStyle().setHeight(60, Style.Unit.PX);
       youtube.getElement().getStyle().setWidth(200, Style.Unit.PX);
       youtube.getElement().getStyle().setBottom(22, Style.Unit.PX);
       youtube.getElement().getStyle().setLeft(25, Style.Unit.PX);

       vertical_semioffset = 275;
       catalogo_container.getElement().getStyle().setHeight(Window.getClientHeight()-(vertical_semioffset), Style.Unit.PX);
       playlist_container.getElement().getStyle().setHeight(Window.getClientHeight()-(vertical_semioffset), Style.Unit.PX);
       song_container.getElement().getStyle().setHeight(Window.getClientHeight()-(vertical_semioffset), Style.Unit.PX);
       
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

   
   //riempie il catalogo/libreria con la lista dei brani
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





    @Override
    public void setBranoCatalogo(
            it.unipd.netmus.client.ui.ProfileView.Song selezione) {
        
        selected_song = (Song) selezione;
        if(!canzoni_playlist.contains(selected_song)) {    
            brano_aggiungere.setText(selected_song.titolo);
        } else {
            
            brano_aggiungere.setText("");
        }
    }
    
    
    
    
    @Override
    public void setBranoPlaylist(
            it.unipd.netmus.client.ui.ProfileView.Song selezione) {
        // TODO Auto-generated method stub
    
        selected_song_playlist = (Song) selezione; 
        brano_rimuovere.setText(selected_song_playlist.titolo);
    
    }
    
    
    
    
    @Override
    public void addToPLaylist(String autore, String titolo, String album) {
         
        Song brano = new Song(autore, titolo, album);
            if(!canzoni_playlist.contains(brano)) {
                List<Song> test = dataProvider_playlist.getList();
                canzoni_playlist.add(new Song(selected_song.autore, selected_song.titolo, selected_song.album));      
                test.add(canzoni_playlist.get(canzoni_playlist.size()-1));
                brano_aggiungere.setText("");
                
                if(((Song)brano).equals(((SingleSelectionModel<Song>)lista_canzoni.getSelectionModel()).getSelectedObject()))
                    brano_rimuovere.setText(((Song)brano).titolo);
            }
        
        
        
    }
    
    
    
    
    @Override
    public void removeFromPlaylist(String autore, String titolo, String album) {

        Song brano = new Song(autore, titolo, album);

            if(canzoni_playlist.contains(brano)) {
                
                // LANCIA UN ECCEZIONE com.google.gwt.event.shared.UmbrellaExceptio- PROBABILE BUG GWT - NESSUN ERRORE IN COMPILAZIONE
                //((SingleSelectionModel<Song>)lista_canzoni.getSelectionModel()).setSelected(((SingleSelectionModel<Song>)lista_canzoni.getSelectionModel()).getSelectedObject(), false);
                // LANCIA UN ECCEZIONE com.google.gwt.event.shared.UmbrellaExceptio- PROBABILE BUG GWT - NESSUN ERRORE IN COMPILAZIONE
                
                List<Song> test = dataProvider_playlist.getList();
                
                canzoni_playlist.remove(brano); 
                if(canzoni_playlist.size()==0) {
                
                    test.remove(0);
                    
                } else {
                    
                    test.remove(brano);
                    
                }
                
                if(((Song)brano).equals(((SingleSelectionModel<Song>)catalogo.getSelectionModel()).getSelectedObject()))
                    brano_aggiungere.setText(((Song)brano).titolo);
                
                brano_rimuovere.setText("");
            }
        
    }
    
    public void setLayout() {
        
        
            if(Window.getClientWidth()<1200) {
                
                search.getElement().getStyle().setOpacity(0);
                social_button.getElement().getStyle().setRight(25, Style.Unit.PX);
                account_button.getElement().getStyle().setRight(95, Style.Unit.PX);
                edit_button.getElement().getStyle().setRight(165, Style.Unit.PX);
    
                
                
            } else {
                
                social_button.getElement().getStyle().setRight(230, Style.Unit.PX);
                account_button.getElement().getStyle().setRight(300, Style.Unit.PX);
                edit_button.getElement().getStyle().setRight(370, Style.Unit.PX);
                
                           if(Window.getClientWidth()>1200)
                            search.getElement().getStyle().setOpacity(1);
                            
                
            }
            
            
            if(Window.getClientHeight()<640) {
                
                friends.getElement().getStyle().setOpacity(0);
                friends_titolo.getElement().getStyle().setOpacity(0);
                
            } else {
                
                friends.getElement().getStyle().setOpacity(1);
                friends_titolo.getElement().getStyle().setOpacity(1);
                
            }
    
            
        //ridimensiono il layout in base alla dimensione della finestra del browser
            left_panel.getElement().getStyle().setHeight(Window.getClientHeight()-vertical_offset, Style.Unit.PX);
            main_panel.getElement().getStyle().setHeight(Window.getClientHeight()-vertical_offset, Style.Unit.PX);
            catalogo_container.getElement().getStyle().setProperty("minHeight", 515-vertical_semioffset, Style.Unit.PX);

            playlist_container.getElement().getStyle().setProperty("minHeight", 515-vertical_semioffset, Style.Unit.PX);
            catalogo_container.getElement().getStyle().setHeight(Window.getClientHeight()-(vertical_semioffset), Style.Unit.PX);
            playlist_container.getElement().getStyle().setHeight(Window.getClientHeight()-(vertical_semioffset), Style.Unit.PX);
            playlist_contenuto.getElement().getStyle().setHeight(playlist_container.getElement().getClientHeight()-44, Style.Unit.PX);
            song_contenuto.getElement().getStyle().setHeight(playlist_container.getElement().getClientHeight()-22, Style.Unit.PX);
            
            song_container.getElement().getStyle().setProperty("minHeight", 515-vertical_semioffset, Style.Unit.PX);
            song_container.getElement().getStyle().setHeight(Window.getClientHeight()-(vertical_semioffset), Style.Unit.PX);
    
        friends.getElement().getStyle().setHeight((Window.getClientHeight()-vertical_offset-338)-playlists.getOffsetHeight(),Style.Unit.PX);
        
        
        if(friends.getElement().getStyle().getOpacity().equals("0")) {
            
            playlists.getElement().getStyle().setHeight((Window.getClientHeight()-vertical_offset-338), Style.Unit.PX);
            
        
        }
        if(friends.getOffsetHeight()>=(Window.getClientHeight()-vertical_offset-338)*0.44) {
            

            playlists.getElement().getStyle().setProperty("height", "auto");
            
            
        }
        if(playlists.getOffsetHeight()>(Window.getClientHeight()-vertical_offset-338)*0.66) {

            playlists.getElement().getStyle().setHeight((Window.getClientHeight()-vertical_offset-338)*0.6,Style.Unit.PX);
            
        }
        
        
        Timer timersd = new Timer() {
            public void run() {
                
                for(int k=12; k>0; k--) {
                    
                    System.out.println("k: " + utente.getElement().getClientWidth());

                    if(utente.getElement().getClientWidth()<=160) {
                        utente.getElement().getStyle().setTop(45, Style.Unit.PX);
                        utente.getElement().getStyle().setLeft(10, Style.Unit.PX);
                        utente.getElement().getStyle().setOpacity(1);
                        break;    
                    }
                        
                    else {
                        utente.getElement().getStyle().setFontSize(k, Style.Unit.PX);
                    }
                }
                                
                
                 
            }
        };
        timersd.schedule(300);
        
        
        
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
                 playlist_contenuto.getElement().getStyle().setHeight(playlist_container.getElement().getClientHeight()-44, Style.Unit.PX);
                 song_contenuto.getElement().getStyle().setHeight(playlist_container.getElement().getClientHeight()-22, Style.Unit.PX);
                 
                 song_container.getElement().getStyle().setProperty("minHeight", 515-vertical_semioffset, Style.Unit.PX);
                 song_container.getElement().getStyle().setHeight(event.getHeight()-(vertical_semioffset), Style.Unit.PX);
                 
                 friends.getElement().getStyle().setHeight((Window.getClientHeight()-vertical_offset-338)-playlists.getOffsetHeight(),Style.Unit.PX);

                 if(friends.getElement().getStyle().getOpacity().equals("0")) {
                     
                     playlists.getElement().getStyle().setHeight((Window.getClientHeight()-vertical_offset-338), Style.Unit.PX);
                     
                 
                 }
                 if(friends.getOffsetHeight()>=(Window.getClientHeight()-vertical_offset-338)*0.44) {
                     

                     playlists.getElement().getStyle().setProperty("height", "auto");
                     
                     
                 }
                 if(playlists.getOffsetHeight()>(Window.getClientHeight()-vertical_offset-338)*0.66) {

                     playlists.getElement().getStyle().setHeight((Window.getClientHeight()-vertical_offset-338)*0.6,Style.Unit.PX);
                     
                 }
                 
                 

                 
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
                 
                 
                 if(event.getHeight()<640) {
                     
                     friends.getElement().getStyle().setOpacity(0);
                     friends_titolo.getElement().getStyle().setOpacity(0);

                     
                 } else {
                     
                     friends.getElement().getStyle().setOpacity(1);
                     friends_titolo.getElement().getStyle().setOpacity(1);
                     
                 }

             }           
         });

        
    }




    @Override
    public void addToPlaylists(String titolo) {
        
        final Label tmpTxt = new Label(titolo);
        tmpTxt.getElement().getStyle().setMarginLeft(11, Style.Unit.PX);
        tmpTxt.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        tmpTxt.getElement().getStyle().setColor("#37A6EB");
        tmpTxt.getElement().getStyle().setProperty("fontFamily", "Verdana");
        tmpTxt.getElement().getStyle().setMarginLeft(11, Style.Unit.PX);
        tmpTxt.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        tmpTxt.getElement().getStyle().setProperty("fontWeight", "600");
        
        
        Image tmpImg = new Image("images/playlistT.png");
        tmpImg.setWidth("25px");
        tmpImg.getElement().getStyle().setMargin(2, Style.Unit.PX);
        tmpImg.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        tmpImg.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        
        final FocusPanel wrapper = new FocusPanel();

        HorizontalPanel tmpPnl = new HorizontalPanel();
        tmpPnl.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        tmpPnl.getElement().getStyle().setMarginBottom(0, Style.Unit.PX);
        tmpPnl.add(tmpImg);
        tmpPnl.add(tmpTxt);

        wrapper.getElement().getStyle().setMarginTop(0, Style.Unit.PX);
        wrapper.getElement().getStyle().setProperty("outline", "0");
        wrapper.addMouseOverHandler( new MouseOverHandler(){

            @Override
            public void onMouseOver(MouseOverEvent event) {                    
                  event.getRelativeElement().getStyle().setBackgroundColor("#e0fee1");
                  event.getRelativeElement().getStyle().setCursor(Style.Cursor.POINTER);
            } });
        
         wrapper.addMouseOutHandler( new MouseOutHandler(){
                @Override
                public void onMouseOut(MouseOutEvent event) {
                      event.getRelativeElement().getStyle().setBackgroundColor("#FFFFFF");
                } });

          wrapper.addClickHandler(new ClickHandler() { 
                @Override
                public void onClick(ClickEvent event) {
                    viewPlaylist(tmpTxt.getText());
                    wrapper.getElement().getStyle().setBackgroundColor("#FFFFFF");
                    Timer timerButton = new Timer() {
                        public void run() {
                            wrapper.getElement().getStyle().setBackgroundColor("#e0fee1");      
                        }     
                    };
                    timerButton.schedule(50);
                }
            });

        wrapper.add(tmpPnl);
        playlists.add(wrapper);
        setLayout();
    }
    
    
    private void addPlaylist() {
        
            final TextBox nome = new TextBox(); 
            nome.getElement().getStyle().setWidth(80, Style.Unit.PCT);
            
            Image tmpImg = new Image("images/playlistT.png");
            tmpImg.setWidth("25px");
            tmpImg.getElement().getStyle().setMargin(2, Style.Unit.PX);
            tmpImg.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
            tmpImg.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
            
            final FocusPanel wrapper = new FocusPanel();

            final HorizontalPanel tmpPnl = new HorizontalPanel();
            tmpPnl.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
            tmpPnl.getElement().getStyle().setMarginBottom(0, Style.Unit.PX);
            tmpPnl.add(tmpImg);
            tmpPnl.add(nome);

            wrapper.getElement().getStyle().setMarginTop(0, Style.Unit.PX);
            wrapper.getElement().getStyle().setProperty("outline", "0");
            wrapper.addMouseOverHandler( new MouseOverHandler(){

                @Override
                public void onMouseOver(MouseOverEvent event) {                    
                      event.getRelativeElement().getStyle().setBackgroundColor("#e0fee1");
                      event.getRelativeElement().getStyle().setCursor(Style.Cursor.POINTER);
                } });
            
             wrapper.addMouseOutHandler( new MouseOutHandler(){
                    @Override
                    public void onMouseOut(MouseOutEvent event) {
                          event.getRelativeElement().getStyle().setBackgroundColor("#FFFFFF");
                    } });

              wrapper.addClickHandler(new ClickHandler() { 
                    @Override
                    public void onClick(ClickEvent event) {
                        
                    }
                });
              
              nome.addKeyDownHandler(new KeyDownHandler(){

                @Override
                public void onKeyDown(KeyDownEvent event) {

                    if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                        
                        Label tmpTxt = new Label(nome.getText());
                        tmpTxt.getElement().getStyle().setMarginLeft(11, Style.Unit.PX);
                        tmpTxt.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                        tmpTxt.getElement().getStyle().setColor("#999999");
                        tmpTxt.getElement().getStyle().setProperty("fontFamily", "Verdana");
                        tmpTxt.getElement().getStyle().setMarginLeft(11, Style.Unit.PX);
                        tmpTxt.getElement().getStyle().setFontSize(12, Style.Unit.PX);
                        tmpTxt.getElement().getStyle().setProperty("fontWeight", "600");
                        
                        wrapper.remove(tmpPnl);
                        listener.addPlaylist(nome.getText());
                        
                        
                    }
                        
                    
                }
                  
                  
              });

            wrapper.add(tmpPnl);
            
            playlists.add(wrapper);
            
            setLayout();
                        
            Timer timerFocus = new Timer() {
                public void run() {
                    nome.setFocus(true);
                }
            };
            timerFocus.schedule(50);
            
            
    }




	@Override
	public void setSongFields(String autore, String titolo, String album,
			String genere, String anno, String compositore, String traccia, String cover) {
		
        song_titolo.setText(titolo);
        song_autore.setText(autore);
        song_album.setText(album);
        song_genere.setText(genere);
        song_anno.setText(anno);
        song_compositore.setText(compositore);
        song_traccia.setText(traccia);
        song_cover.setUrl(cover);
	
	}
        
}


