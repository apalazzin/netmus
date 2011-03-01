/**
 * 
 */
package it.unipd.netmus.client.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;


/**
 * Nome: ProfileViewImpl.java
 * Autore:  VT.G
 * Licenza: GNU GPL v3
 * Data Creazione: 16 Febbraio 2011
*/
public class ProfileViewImpl extends Composite implements ProfileView {

   private static ProfileViewImplUiBinder uiBinder = GWT.create(ProfileViewImplUiBinder.class);

   interface ProfileViewImplUiBinder extends UiBinder<Widget, ProfileViewImpl>
   {
   }
   
   private Presenter listener;
   private String name;
   private int vertical_offset = 45;
   private int vertical_semioffset = 275;
   private int rating;
   private double global_rating;
   private String password;
   private String cpassword;
   
   private CellTable<Song> lista_canzoni;
   
   
   private HandlerRegistration rm_link;
   private HandlerRegistration rm_fw;
   private HandlerRegistration rm_rw;
   
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
   @UiField Label edit_profile_password;
   @UiField Label edit_profile_cpassword;
   @UiField Label edit_profile_nickname;
   @UiField Label edit_profile_name;
   @UiField Label edit_profile_surname;
   @UiField Label edit_profile_nationality;
   @UiField Label edit_profile_gender;
   @UiField Label edit_profile_labelCpassword;   
   @UiField Label edit_profile_utente;
   
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
   @UiField HTMLPanel edit_profile;
   @UiField HTMLPanel edit_profile_aboutme;

   
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
   @UiField Image starG1;
   @UiField Image starG2;
   @UiField Image starG3;
   @UiField Image starG4;
   @UiField Image starG5;
   @UiField Image chiudi_playlist;
   @UiField Image chiudi_song;
   @UiField Image logo_youtube;
   @UiField Image chiudi_youtube;
   @UiField Image aggiungi_branoplaylist;
   @UiField Image rimuovi_branoplaylist;
   @UiField Image aggiungi_playlist;
   @UiField Image song_cover;
   @UiField Image elimina_playlist;
   @UiField Image elimina_song;
   @UiField Image edit_profile_chiudi;
   @UiField Image edit_profile_checkImg;
   
   @UiField Button edit_profile_check;

   @UiField VerticalPanel edit_profile_vc;

   @UiField TextBox search_box;

   boolean playlist_opened; 
   boolean song_opened;
   
   int youtube_status=0;
   int last_selected = 0;
   int playing = 0;
   
   Song selected_song;
   Song selected_song_playlist;
   Song played_song;
   
   List<Song> canzoni_catalogo = new ArrayList<Song>();
   List<Song> canzoni_playlist = new ArrayList<Song>();
   List<Song> player_playlist = new ArrayList<Song>();
   
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

       youTubeListener();
       youtube_status = 0;
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
	   albumColumn.setSortable(true);
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
	         
	         
	         
	         if(playing==0&&selectionModel.getSelectedObject().equals(played_song)&&(youtube_status==1||youtube_status==2)) {
	             play.setUrl("images/pause.png");
	             youtube_status = 1;
	             last_selected=0;
	         }
	         else if(playing==0&&selectionModel.getSelectedObject().equals(played_song)&&(youtube_status==-1||youtube_status==3)) {
                 play.setUrl("images/play.png");
                 youtube_status = -1;
                 last_selected=0;
             }

	         else {
	             play.setUrl("images/play.png");
	             if(youtube_status!=0&&youtube_status==1)
	                 youtube_status = 2;
	             else if(youtube_status!=0&&youtube_status==-1)
	                 youtube_status = 3;
	             last_selected=0;
	         }
             play_youtube.setUrl("images/play.png");

             global_rating = listener.setRating(selected_song.autore,selected_song.titolo,selected_song.album);
             showStar(rating);
	         
	         listener.setSongFields(selected_song.autore, selected_song.titolo, selected_song.album);
	         
	         
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
	    
	    
	       catalogo.addHandler(new KeyUpHandler() {

            @Override
            public void onKeyUp(KeyUpEvent event) {
                
                if(event.getNativeKeyCode() == KeyCodes.KEY_DELETE) {
                    
                   listener.deleteSong(selected_song.autore, selected_song.titolo, selected_song.album);
                    
                }
                
            }

	            }, KeyUpEvent.getType());
	    
	    
	    List<Song> list = dataProvider_catalogo.getList();
	    
	    ListHandler<Song> columnSortHandler = new ListHandler<Song>(list);
	    
	        columnSortHandler.setComparator(autoreColumn,
	            new Comparator<Song>() {
	              public int compare(Song o1, Song o2) {
	                if (o1 == o2) {
	                  return 0;
	                }

	                // Compare the name columns.
	                if (o1 != null) {
	                  return (o2 != null) ? o1.autore.compareTo(o2.autore) : 1;
	                }
	                return -1;
	              }
	            });
	       
	        
	        
            columnSortHandler.setComparator(titoloColumn,
                    new Comparator<Song>() {
                      public int compare(Song o1, Song o2) {
                        if (o1 == o2) {
                          return 0;
                        }

                        // Compare the name columns.
                        if (o1 != null) {
                          return (o2 != null) ? o1.titolo.compareTo(o2.titolo) : 1;
                        }
                        return -1;
                      }
                    });
            
            
            
            columnSortHandler.setComparator(albumColumn,
                    new Comparator<Song>() {
                      public int compare(Song o1, Song o2) {
                        if (o1 == o2) {
                          return 0;
                        }

                        // Compare the name columns.
                        if (o1 != null) {
                          return (o2 != null) ? o1.album.compareTo(o2.album) : 1;
                        }
                        return -1;
                      }
                    });
            
            catalogo.addColumnSortHandler(columnSortHandler);	        

	        
	        // We know that the data is sorted alphabetically by default.
	        catalogo.getColumnSortList().push(autoreColumn);

	    
	    
	    
	    
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
	           
	           
	             
	             if(playing==1&&selectionModel2.getSelectedObject().equals(played_song)&&(youtube_status==1||youtube_status==2)) {
	                 play.setUrl("images/pause.png");
	                 youtube_status = 1;
	                 last_selected=1;
	             }
	             else if(playing==1&&selectionModel2.getSelectedObject().equals(played_song)&&(youtube_status==-1||youtube_status==3)) {
	                 play.setUrl("images/play.png");
	                 youtube_status = -1;
	                 last_selected=1;
	             }

	             else {
	                 play.setUrl("images/play.png");
	                 if(youtube_status!=0&&youtube_status==1)
	                     youtube_status = 2;
	                 else if(youtube_status!=0&&youtube_status==-1)
	                     youtube_status = 3;
	                 last_selected=1;
	             }
	             play_youtube.setUrl("images/play.png");
	           
	         }
	       });
	       
	       
           List<Song> listPl = dataProvider_playlist.getList();
           
           ListHandler<Song> columnPlSortHandler = new ListHandler<Song>(listPl);
           
               columnPlSortHandler.setComparator(titoloColumn2,
                   new Comparator<Song>() {
                     public int compare(Song o1, Song o2) {
                       if (o1 == o2) {
                         return 0;
                       }

                       // Compare the name columns.
                       if (o1 != null) {
                         return (o2 != null) ? o1.titolo.compareTo(o2.titolo) : 1;
                       }
                       return -1;
                     }
                   });
               
               columnPlSortHandler.setComparator(albumColumn2,
                       new Comparator<Song>() {
                         public int compare(Song o1, Song o2) {
                           if (o1 == o2) {
                             return 0;
                           }

                           // Compare the name columns.
                           if (o1 != null) {
                             return (o2 != null) ? o1.album.compareTo(o2.album) : 1;
                           }
                           return -1;
                         }
                       });
   
               lista_canzoni.addColumnSortHandler(columnPlSortHandler);           
           
               // We know that the data is sorted alphabetically by default.
               lista_canzoni.getColumnSortList().push(titoloColumn2);
               
	       dataProvider_playlist.addDataDisplay(lista_canzoni);
	               
	       
	       playlist_songs.add(lista_canzoni);
	       
	       HTMLPanel off = new HTMLPanel("");
	       off.getElement().getStyle().setHeight(22, Style.Unit.PX);
	       playlist_songs.add(off);	   
	       main_panel.getElement().setId("main_panel");
	       
	       

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
      closeSong();
      closePlaylist();
      listener.logout();      
   }
   

   @UiHandler("play")
   void handleClickPlay(ClickEvent e) {
       
       if(youtube_status==0 && last_selected==0) {
           if(selected_song!=null){ 
               play.setUrl("images/pause.png");
               played_song = selected_song;
               playing=0;
               listener.playYouTube(selected_song.autore, selected_song.titolo, selected_song.album);
               
           }
       }
       
       else if(youtube_status==0 && last_selected==1) {
           if(selected_song_playlist!=null){ 
               play.setUrl("images/pause.png");
               played_song = selected_song_playlist;
               playing=1;
               player_playlist = new ArrayList<Song>(dataProvider_playlist.getList());
               listener.playYouTube(selected_song_playlist.autore, selected_song_playlist.titolo, selected_song_playlist.album);
               
           }
       }
       
       else if(youtube_status==1) {
           pausePlayer();
           youtube_status=-1;
           play.setUrl("images/play.png");
       }
              
       else if(youtube_status==-1) {
           playPlayer();
           youtube_status=1;
           play.setUrl("images/pause.png");
       }

       else if((youtube_status==2||youtube_status==3) && last_selected==0) {
           closeYouTube();
           youtube_status=1;
           play.setUrl("images/pause.png");
           played_song = selected_song;
           playing=0;
           listener.playYouTube(selected_song.autore, selected_song.titolo, selected_song.album);
           setFwRw();
       }

       else if((youtube_status==2||youtube_status==3) && last_selected==1) {
           closeYouTube();
           youtube_status=1;
           play.setUrl("images/pause.png");
           played_song = selected_song_playlist;
           playing=1;
           player_playlist =  new ArrayList<Song>(dataProvider_playlist.getList());
           listener.playYouTube(selected_song_playlist.autore, selected_song_playlist.titolo, selected_song_playlist.album);
           setFwRw();
       }

   }    
    
   private static native void playPlayer() /*-{ 
   
   $doc.getElementById('youtube_player').playVideo();

    }-*/;
   
   private static native void pausePlayer() /*-{ 
        
     $doc.getElementById('youtube_player').pauseVideo();   

      }-*/;

   
   

   @UiHandler("play")
   void handleMouseOverPlay(MouseOverEvent e) {
          play.getElement().getStyle().setCursor(Style.Cursor.POINTER);
   }
   @UiHandler("play")
   void handleMouseOutPlay(MouseOutEvent e) {
       
   }
   
   @UiHandler("play_youtube")
   void handleClickPlayYoutube(ClickEvent e) {
       if(youtube_status==0 && last_selected==0) {
           if(selected_song!=null){ 
               play.setUrl("images/pause.png");
               played_song = selected_song;
               playing=0;
               listener.playYouTube(selected_song.autore, selected_song.titolo, selected_song.album);
               
           }
       }
       
       else if(youtube_status==0 && last_selected==1) {
           if(selected_song_playlist!=null){ 
               play.setUrl("images/pause.png");
               played_song = selected_song_playlist;
               playing=1;
               player_playlist = new ArrayList<Song>(dataProvider_playlist.getList());
               listener.playYouTube(selected_song_playlist.autore, selected_song_playlist.titolo, selected_song_playlist.album);
               
           }
       }
       
       else if(youtube_status==1) {
           pausePlayer();
           youtube_status=-1;
           play.setUrl("images/play.png");
       }
              
       else if(youtube_status==-1) {
           playPlayer();
           youtube_status=1;
           play.setUrl("images/pause.png");
       }

       else if((youtube_status==2||youtube_status==3) && last_selected==0) {
           closeYouTube();
           youtube_status=1;
           play.setUrl("images/pause.png");
           played_song = selected_song;
           playing=0;
           listener.playYouTube(selected_song.autore, selected_song.titolo, selected_song.album);
           setFwRw();
       }

       else if((youtube_status==2||youtube_status==3) && last_selected==1) {
           closeYouTube();
           youtube_status=1;
           play.setUrl("images/pause.png");
           played_song = selected_song_playlist;
           playing=1;
           player_playlist =  new ArrayList<Song>(dataProvider_playlist.getList());
           listener.playYouTube(selected_song_playlist.autore, selected_song_playlist.titolo, selected_song_playlist.album);
           setFwRw();
       }

   }

   @UiHandler("play_youtube")
   void handleMouseOverPlayYoutube(MouseOverEvent e) {
       if(selected_song!=null) {
           play_youtube.getElement().getStyle().setCursor(Style.Cursor.POINTER);
       }
   }

   @UiHandler("play_youtube")
   void handleMouseOutPlayYouTube(MouseOutEvent e) {
       if(selected_song!=null)
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
       star2.getElement().getStyle().setCursor(Style.Cursor.POINTER);
       star3.getElement().getStyle().setCursor(Style.Cursor.POINTER);
       star4.getElement().getStyle().setCursor(Style.Cursor.POINTER);
       star5.getElement().getStyle().setCursor(Style.Cursor.POINTER);
       star1.setUrl("images/star.png");
       star2.setUrl("images/starbw.png");
       star3.setUrl("images/starbw.png");
       star4.setUrl("images/starbw.png");
       star5.setUrl("images/starbw.png");
   }
   @UiHandler("star1")
   void handleMouseOutStar1(MouseOutEvent e) {
	   showStar(this.rating);
   }
   @UiHandler("star1")
   void handleClickStar1(ClickEvent e) {
       if (this.selected_song != null)
           listener.rateSong(this.selected_song.autore,this.selected_song.titolo,this.selected_song.album, 1);
           
   }
   @UiHandler("star2")
   void handleMouseOverStar2(MouseOverEvent e) {
       star1.getElement().getStyle().setCursor(Style.Cursor.POINTER);
       star2.getElement().getStyle().setCursor(Style.Cursor.POINTER);
       star3.getElement().getStyle().setCursor(Style.Cursor.POINTER);
       star4.getElement().getStyle().setCursor(Style.Cursor.POINTER);
       star5.getElement().getStyle().setCursor(Style.Cursor.POINTER);
	   star1.setUrl("images/star.png");
       star2.setUrl("images/star.png");
       star3.setUrl("images/starbw.png");
       star4.setUrl("images/starbw.png");
       star5.setUrl("images/starbw.png");
   }
   @UiHandler("star2")
   void handleMouseOutStar2(MouseOutEvent e) {
	   showStar(this.rating);
   }
   @UiHandler("star2")
   void handleClickStar2(ClickEvent e) {
       if (this.selected_song != null)
           listener.rateSong(this.selected_song.autore,this.selected_song.titolo,this.selected_song.album, 2);
           
   }
   @UiHandler("star3")
   void handleMouseOverStar3(MouseOverEvent e) {
       star1.getElement().getStyle().setCursor(Style.Cursor.POINTER);
       star2.getElement().getStyle().setCursor(Style.Cursor.POINTER);
       star3.getElement().getStyle().setCursor(Style.Cursor.POINTER);
       star4.getElement().getStyle().setCursor(Style.Cursor.POINTER);
       star5.getElement().getStyle().setCursor(Style.Cursor.POINTER);
	   star1.setUrl("images/star.png");
       star2.setUrl("images/star.png");
       star3.setUrl("images/star.png");
       star4.setUrl("images/starbw.png");
       star5.setUrl("images/starbw.png");   }
   @UiHandler("star3")
   void handleMouseOutStar3(MouseOutEvent e) {
	   showStar(this.rating);
   }
   @UiHandler("star3")
   void handleClickStar3(ClickEvent e) {
       if (this.selected_song != null)
           listener.rateSong(this.selected_song.autore,this.selected_song.titolo,this.selected_song.album, 3);
           
   }
   @UiHandler("star4")
   void handleMouseOverStar4(MouseOverEvent e) {
       star1.getElement().getStyle().setCursor(Style.Cursor.POINTER);
       star2.getElement().getStyle().setCursor(Style.Cursor.POINTER);
       star3.getElement().getStyle().setCursor(Style.Cursor.POINTER);
       star4.getElement().getStyle().setCursor(Style.Cursor.POINTER);
       star5.getElement().getStyle().setCursor(Style.Cursor.POINTER);
	   star1.setUrl("images/star.png");
       star2.setUrl("images/star.png");
       star3.setUrl("images/star.png");
       star4.setUrl("images/star.png");
       star5.setUrl("images/starbw.png");   }
   @UiHandler("star4")
   void handleMouseOutStar4(MouseOutEvent e) {
	   showStar(this.rating);
   }
   @UiHandler("star4")
   void handleClickStar4(ClickEvent e) {
       if (this.selected_song != null)
           listener.rateSong(this.selected_song.autore,this.selected_song.titolo,this.selected_song.album, 4);
           
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
	   showStar(this.rating);
   }
   @UiHandler("star5")
   void handleClickStar5(ClickEvent e) {
       if (this.selected_song != null)
           listener.rateSong(this.selected_song.autore,this.selected_song.titolo,this.selected_song.album, 5);
           
   }

   @UiHandler("elimina_playlist")
   void handleClickEliminaPlaylist(ClickEvent e) {
      listener.deletePlaylist(titolo_playlist.getText());
      closePlaylist();
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

   @UiHandler("edit_profile_chiudi")
   void handleMouseEditProfileChiudi(ClickEvent e) {
      edit_profile.getElement().getStyle().setOpacity(0);
      Timer timerHidden = new Timer() {
          public void run() {
              edit_profile.getElement().getStyle().setDisplay(Style.Display.NONE);  
          }     
      };
      timerHidden.schedule(200);
   }

   @UiHandler("edit_profile_chiudi")
   void handleMouseOverEditProfileChiudi(MouseOverEvent e) {
       edit_profile_chiudi.getElement().getStyle().setCursor(Style.Cursor.POINTER);
    }
   
   
   @UiHandler("account_button")
   void handleMouseAccountButton(ClickEvent e) {
      edit_profile.getElement().getStyle().setDisplay(Style.Display.BLOCK);
      edit_profile.getElement().getStyle().setOpacity(1);    
   }

   @UiHandler("account_button")
   void handleMouseOverAccountButton(MouseOverEvent e) {
       account_button.getElement().getStyle().setCursor(Style.Cursor.POINTER);
    }

 

   @UiHandler("edit_profile_nickname")
   void handleMouseOverEditProfileNickname(MouseOverEvent e) {
      edit_profile_nickname.getElement().getStyle().setCursor(Style.Cursor.POINTER);
   }

   @UiHandler("edit_profile_name")
   void handleMouseOverEditProfileName(MouseOverEvent e) {
      edit_profile_name.getElement().getStyle().setCursor(Style.Cursor.POINTER);
   }   
   
   @UiHandler("edit_profile_surname")
   void handleMouseOverEditProfileSurname(MouseOverEvent e) {
      edit_profile_surname.getElement().getStyle().setCursor(Style.Cursor.POINTER);
   }

   @UiHandler("edit_profile_nationality")
   void handleMouseOverEditProfileNationality(MouseOverEvent e) {
      edit_profile_nationality.getElement().getStyle().setCursor(Style.Cursor.POINTER);
   }
   
   @UiHandler("edit_profile_gender")
   void handleMouseOverEditProfileGender(MouseOverEvent e) {
      edit_profile_gender.getElement().getStyle().setCursor(Style.Cursor.POINTER);
   }
   

   @UiHandler("edit_profile_password")
   void handleMouseOverEditProfilePassword(MouseOverEvent e) {
      edit_profile_password.getElement().getStyle().setCursor(Style.Cursor.POINTER);
   }
   
   @UiHandler("edit_profile_cpassword")
   void handleMouseOverEditProfileCPassword(MouseOverEvent e) {
      edit_profile_cpassword.getElement().getStyle().setCursor(Style.Cursor.POINTER);
   }

   @UiHandler("edit_profile_check")
   void handleMouseEditProfileCheck(ClickEvent e) {
        
       listener.editProfile(edit_profile_utente.getText(), edit_profile_nickname.getText(), edit_profile_name.getText(), edit_profile_surname.getText(),
               edit_profile_gender.getText(), edit_profile_nationality.getText(), edit_profile_aboutme.getElement().getInnerHTML(),password);
       
   }
   
   @UiHandler("search_box")
   void handleChangeValueSearchBox(KeyUpEvent e) {
       
       
       dataProvider_catalogo.getList().removeAll(dataProvider_catalogo.getList());
       
       if(!((TextBox)e.getSource()).getValue().equals("")) {
           List<Song> canzoni_ricerca = new ArrayList<Song>();
           for (int j=0; j<canzoni_catalogo.size(); j++) {
           
               if((canzoni_catalogo.get(j).titolo.toLowerCase()).indexOf(((TextBox)e.getSource()).getValue().toLowerCase())>=0 ||
                       (canzoni_catalogo.get(j).autore.toLowerCase()).indexOf(((TextBox)e.getSource()).getValue().toLowerCase())>=0 ||
                           (canzoni_catalogo.get(j).album.toLowerCase()).indexOf(((TextBox)e.getSource()).getValue().toLowerCase())>=0 ) {
                   
                 canzoni_ricerca.add(canzoni_catalogo.get(j));
                   
               } 
               
           }
           
           for (Song song : canzoni_ricerca) {
               dataProvider_catalogo.getList().add(song);
           }
           
       } else {
           
           for (Song song : canzoni_catalogo) {
               dataProvider_catalogo.getList().add(song);
           }
       }

   }
   
   
   @UiHandler("elimina_song")
   void handleMouseClickEliminaSong(ClickEvent e) {
       listener.deleteSong(selected_song.autore, selected_song.titolo, selected_song.album);
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
	          
       Arrays.sort(lista);
       
       playlists.clear();
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

			if(k%2>0)
		         wrapper.getElement().getStyle().setBackgroundColor("#F5F5F5");
			wrapper.getElement().getStyle().setMarginTop(0, Style.Unit.PX);
			wrapper.getElement().getStyle().setProperty("outline", "0");
			wrapper.addMouseOverHandler( new MouseOverHandler(){

                @Override
                public void onMouseOver(MouseOverEvent event) {                    
                      event.getRelativeElement().getStyle().setBackgroundColor("#e0fee1");
                      event.getRelativeElement().getStyle().setCursor(Style.Cursor.POINTER);
                } }); 
			
			if(k%2>0) {
    	         wrapper.addMouseOutHandler( new MouseOutHandler(){
    	                @Override
    	                public void onMouseOut(MouseOutEvent event) {
    	                    event.getRelativeElement().getStyle().setBackgroundColor("#F5F5F5");
    	                } });
			} else {
			    wrapper.addMouseOutHandler( new MouseOutHandler(){
                    @Override
                    public void onMouseOut(MouseOutEvent event) {
                            event.getRelativeElement().getStyle().setBackgroundColor("#FFFFFF");
                    } });
			}

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

       Timer timersd = new Timer() {
           public void run() {
               
               for(int k=12; k>0; k--) {
                   
                   if(utente.getElement().getClientWidth()<=160) {
                       utente.getElement().getStyle().setTop(45, Style.Unit.PX);
                       utente.getElement().getStyle().setLeft(10, Style.Unit.PX);
                       utente.getElement().getStyle().setOpacity(1);
                       utente.getElement().getStyle().setOpacity(1);
                       utente.getElement().getStyle().setOpacity(1);
                       utente.getElement().getStyle().setOpacity(1);
                       break;    
                   }
                       
                   else {
                       utente.getElement().getStyle().setFontSize(k, Style.Unit.PX);
                   }
               }
                               
               
                
           }
       };
       timersd.schedule(600);
	
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
                          song_cover.getElement().getStyle().setOpacity(1);
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
                      song_cover.getElement().getStyle().setOpacity(1);
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
       song_cover.getElement().getStyle().setOpacity(0);
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

   private void setFwRw() {
       
      
       
       if(playing==0){

           if(dataProvider_catalogo.getList().indexOf((played_song))+1<dataProvider_catalogo.getList().size()) {
               if(rm_fw!=null) rm_fw.removeHandler();    
               forward.setVisible(true);
               forward.setUrl("images/fw.png");
               rm_fw = forward.addClickHandler(new ClickHandler(){
    
                @Override
                public void onClick(ClickEvent event) {
                    
                    playNext();
                    
                    
                }});
           } else {
               
               forward.setUrl("images/fwoff.png");
               forward.setVisible(false);
           }
           
           if(dataProvider_catalogo.getList().indexOf(played_song)-1>=0) {
               if(rm_rw!=null) rm_rw.removeHandler();               
               rewind.setVisible(true);
               rewind.setUrl("images/rw.png");
               rm_rw = rewind.addClickHandler(new ClickHandler(){
                   
                   @Override
                   public void onClick(ClickEvent event) {
                       
                       playPrev();
                       
                   }});
           } else {
               rewind.setUrl("images/rwoff.png");
               rewind.setVisible(false);
           }
     
           
       } else if(playing==1) {
                 
           if(player_playlist.indexOf((played_song))+1<player_playlist.size()) {
               if(rm_fw!=null) rm_fw.removeHandler();    
               forward.setVisible(true);
               forward.setUrl("images/fw.png");
               rm_fw = forward.addClickHandler(new ClickHandler(){
    
                @Override
                public void onClick(ClickEvent event) {
                    
                    playNext();
                    
                    
                }});
           } else {
               
               forward.setUrl("images/fwoff.png");
               forward.setVisible(false);
           }
           
           if(player_playlist.indexOf(played_song)-1>=0) {
               if(rm_rw!=null) rm_rw.removeHandler();               
               rewind.setVisible(true);
               rewind.setUrl("images/rw.png");
               rm_rw = rewind.addClickHandler(new ClickHandler(){
                   
                   @Override
                   public void onClick(ClickEvent event) {
                       
                       playPrev();
                       
                   }});
           } else {
               rewind.setUrl("images/rwoff.png");
               rewind.setVisible(false);
           }
     
       }
       
   }
   

   
   private void playNext() {
       
       if(playing==0) {
           
           Song to_play = dataProvider_catalogo.getList().get(dataProvider_catalogo.getList().indexOf((Song)played_song)+1);
                      
           closeYouTube();
           youtube_status=1;
           played_song = to_play;
           playing=0;
           listener.playYouTube(to_play.autore, to_play.titolo, to_play.album);
           setFwRw();
           
       } else {
           
           Song to_play = player_playlist.get(player_playlist.indexOf((Song)played_song)+1);
           
           closeYouTube();
           youtube_status=1;
           played_song = to_play;
           playing=1;
           listener.playYouTube(to_play.autore, to_play.titolo, to_play.album);
           setFwRw();
       }
   }
   
   private void playPrev() {
       
      if(playing==0) {
           
          Song to_play = dataProvider_catalogo.getList().get(dataProvider_catalogo.getList().indexOf((Song)played_song)-1);
                      
           closeYouTube();
           youtube_status=1;
           played_song = to_play;
           playing=0;
           listener.playYouTube(to_play.autore, to_play.titolo, to_play.album);
           setFwRw();
           
       } else {
           
           Song to_play = player_playlist.get(player_playlist.indexOf((Song)played_song)-1);
           
           closeYouTube();
           youtube_status=1;
           played_song = to_play;
           playing=1;
           listener.playYouTube(to_play.autore, to_play.titolo, to_play.album);
           setFwRw();
           
       }
       
   }
   
   public void youTubeChange(int s) {

       if(s==2)
           youtube_status = -1;
       else if(s==1) {
           youtube_status = 1;
           setFwRw();
       }
       else if(s==0)    {
           
           if(playing==0){
               
               Song to_play = dataProvider_catalogo.getList().get(dataProvider_catalogo.getList().indexOf((Song)played_song)+1);
               
               closeYouTube();
               youtube_status=1;
               played_song = to_play;
               playing=0;
               listener.playYouTube(to_play.autore, to_play.titolo, to_play.album);                
               setFwRw();
               
           } else if(playing==1) {

               Song to_play = player_playlist.get(player_playlist.indexOf((Song)played_song)+1);
               
               closeYouTube();
               youtube_status=1;
               played_song = to_play;
               playing=1;
               listener.playYouTube(to_play.autore, to_play.titolo, to_play.album);
               
           }
               
           
       }
           
    }
   
   public native void youTubeListener() /*-{
       
       var _this = this;
       
       $wnd.youTubeChange = function(s) {
          _this.@it.unipd.netmus.client.ui.ProfileViewImpl::youTubeChange(I)(s);
       }
   
   }-*/;


   public void playYouTube(final String link) {
       
       
       if (link.equals("")) {
           
           System.out.println("link mancante");
           closeYouTube();
           
           
       } else {
           
           
           
           youtube_status = 1;
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
           
           player.getElement().getStyle().setZIndex(3);
           
           info_youtube.getElement().setInnerText(played_song.titolo + ". " + played_song.autore);
           
           info_youtube_link.setText("http://www.youtube.com/watch?v=q" + link);
           
           if(rm_link!=null) rm_link.removeHandler();
           
           rm_link = info_youtube_link.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                Window.open("http://www.youtube.com/watch?v=" + link, "_blank", "");
                
            }});
           
           info_youtube.setVisible(true);
           info_youtube_link.setVisible(true);
           youtube_appendice.setVisible(true);
           chiudi_youtube.setVisible(true);
           
           info_youtube.getElement().getStyle().setOpacity(1);
           info_youtube_link.getElement().getStyle().setOpacity(1);
           youtube_appendice.getElement().getStyle().setOpacity(1);
           chiudi_youtube.getElement().getStyle().setOpacity(1);

           
           player.getElement().setInnerHTML("<object width=\"325\" height=\"200\"><param name=\"movie\" value=\"http://www.youtube.com/v/" + link
                   + "&rel=0&ap=%2526fmt%3D18&autoplay=1&iv_load_policy=3&fs=1&autohide=1&enablejsapi=1&showinfo=0&playerapiid=ytplayer\"></param><param name=\"allowFullScreen\" value=\"true\"></param>" +
                   		"<param name=\"allowscriptaccess\" value=\"always\"></param><embed id=\"youtube_player\" src=\"http://www.youtube.com/v/" + link
                   + "&rel=0&ap=%2526fmt%3D18&autoplay=1&iv_load_policy=3&fs=1&autohide=1&enablejsapi=1&showinfo=0&playerapiid=ytplayer\" type=\"application/x-shockwave-flash\" allowscriptaccess=\"always\"" +
                   		"allowfullscreen=\"true\" width=\"325\" height=\"200\"></embed></object>");
           
           
            
       }

       
   }

   public void closeYouTube() {
       
       youtube_status = 0;
       youtube.getElement().getStyle().setHeight(60, Style.Unit.PX);
       youtube.getElement().getStyle().setWidth(200, Style.Unit.PX);
       youtube.getElement().getStyle().setBottom(22, Style.Unit.PX);
       youtube.getElement().getStyle().setLeft(25, Style.Unit.PX);

       vertical_semioffset = 275;

       setLayout();
       /*
       catalogo_container.getElement().getStyle().setHeight(Window.getClientHeight()-(vertical_semioffset), Style.Unit.PX);
       playlist_container.getElement().getStyle().setHeight(Window.getClientHeight()-(vertical_semioffset), Style.Unit.PX);
       song_container.getElement().getStyle().setHeight(Window.getClientHeight()-(vertical_semioffset), Style.Unit.PX);
       */
       
       play_youtube.getElement().getStyle().setOpacity(1);
       logo_youtube.getElement().getStyle().setOpacity(1);

       logo_youtube.getElement().getStyle().setPosition(Style.Position.RELATIVE);
       logo_youtube.getElement().getStyle().setBottom(0, Style.Unit.PX);
       logo_youtube.getElement().getStyle().setLeft(0, Style.Unit.PX);
       
       
       info_youtube.getElement().getStyle().setOpacity(0);
       info_youtube_link.getElement().getStyle().setOpacity(0);
       youtube_appendice.getElement().getStyle().setOpacity(0);
       chiudi_youtube.getElement().getStyle().setOpacity(0);

       info_youtube.setVisible(false);
       info_youtube_link.setVisible(false);
       youtube_appendice.setVisible(false);
       chiudi_youtube.setVisible(false);

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
        
        
            if(Window.getClientWidth()<1270) {
                
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
            DOM.getElementById("applet-bar").getStyle().setHeight(Window.getClientHeight()-vertical_offset, Style.Unit.PX);
            
            catalogo_container.getElement().getStyle().setProperty("minHeight", 515-vertical_semioffset, Style.Unit.PX);
            playlist_container.getElement().getStyle().setProperty("minHeight", 515-vertical_semioffset, Style.Unit.PX);
            
            catalogo_container.getElement().getStyle().setHeight(Window.getClientHeight()-(vertical_semioffset), Style.Unit.PX);
            playlist_container.getElement().getStyle().setHeight(Window.getClientHeight()-(vertical_semioffset), Style.Unit.PX);

            song_container.getElement().getStyle().setProperty("minHeight", 515-vertical_semioffset, Style.Unit.PX);
            song_container.getElement().getStyle().setHeight(Window.getClientHeight()-(vertical_semioffset), Style.Unit.PX);

            playlist_contenuto.getElement().getStyle().setHeight(playlist_container.getElement().getClientHeight()-44, Style.Unit.PX);
            song_contenuto.getElement().getStyle().setHeight(song_container.getElement().getClientHeight()-22, Style.Unit.PX);
            
    
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
        
        
        //Imposta la dimensione delle componenti della view in base alla dimensione della finestra del browser quando viene ridimensionata
        Window.addResizeHandler(new ResizeHandler() {
             @Override
             public void onResize(ResizeEvent event) {               

                 int catalogo_h = main_panel.getOffsetHeight();
                 left_panel.getElement().getStyle().setHeight(event.getHeight()-vertical_offset, Style.Unit.PX);
                 main_panel.getElement().getStyle().setHeight(event.getHeight()-vertical_offset, Style.Unit.PX);
                 DOM.getElementById("applet-bar").getStyle().setHeight(event.getHeight()-vertical_offset, Style.Unit.PX);

                 catalogo_container.getElement().getStyle().setProperty("minHeight", 515-vertical_semioffset, Style.Unit.PX);
                 playlist_container.getElement().getStyle().setProperty("minHeight", 515-vertical_semioffset, Style.Unit.PX);
                 song_container.getElement().getStyle().setProperty("minHeight", 515-vertical_semioffset, Style.Unit.PX);

                 catalogo_container.getElement().getStyle().setHeight(event.getHeight()-(vertical_semioffset), Style.Unit.PX);

                 
            
                 playlist_container.getElement().getStyle().setHeight(event.getHeight()-(vertical_semioffset), Style.Unit.PX);
                 song_container.getElement().getStyle().setHeight(event.getHeight()-(vertical_semioffset), Style.Unit.PX);
                                 
                 playlist_contenuto.getElement().getStyle().setHeight(playlist_container.getElement().getClientHeight()-44, Style.Unit.PX);
                 song_contenuto.getElement().getStyle().setHeight(song_container.getElement().getClientHeight()-22, Style.Unit.PX);
                 
                 
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
                 
                 

                 
                 if(event.getWidth()<1270) {
                     
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
                        tmpTxt.getElement().getStyle().setProperty("fontWeight", "bold");
                        
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
        
        if(!cover.equals("")) {
            song_cover.setUrl(cover);
            this.cover.setUrl(cover);
        } else {
            song_cover.setUrl("images/test_cover.jpg");
            this.cover.setUrl("images/test_cover.jpg");
        }
        
        showGlobalStar(global_rating);

    }




    @Override
    public void showGlobalStar(double index) {
        
                
        if (index > 0) 
            starG1.setUrl("images/star.png");
        else 
            starG1.setUrl("images/starbw.png");
        if (index > 1) 
            starG2.setUrl("images/star.png");
        else 
            starG2.setUrl("images/starbw.png");
        if (index > 2) 
            starG3.setUrl("images/star.png");
        else 
            starG3.setUrl("images/starbw.png");
        if (index > 3) 
            starG4.setUrl("images/star.png");
        else 
            starG4.setUrl("images/starbw.png");
        if (index > 4) 
            starG5.setUrl("images/star.png");
        else 
            starG5.setUrl("images/starbw.png");
        
    }


    ClickHandler MyClickEditProfileHandler = new ClickHandler() {

        @Override
        public void onClick(ClickEvent event) {
            
            final Label label = (Label) event.getSource();
            
            final TextBox nome = new TextBox();
            nome.setValue(label.getText());
            nome.getElement().getStyle().setWidth(50, Style.Unit.PCT);
            
           ((HorizontalPanel) label.getParent()).add(nome);
           nome.setFocus(true);
           label.setVisible(false);
           
           nome.addKeyDownHandler(new KeyDownHandler(){

               @Override
               public void onKeyDown(KeyDownEvent event) {

                   if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    
                       label.setText(nome.getValue());
                       nome.removeFromParent();
                       label.setVisible(true);
                       if(password.equals(cpassword)) {
                           edit_profile_check.getElement().getStyle().setOpacity(1);
                           edit_profile_check.setEnabled(true);
                           edit_profile_checkImg.getElement().getStyle().setOpacity(1);
                       }                   
                   }
               }
             });       
            
           nome.addBlurHandler(new BlurHandler(){

               @Override
               public void onBlur(BlurEvent event) {
                   
                   label.setText(nome.getValue());
                   nome.removeFromParent();
                   label.setVisible(true);
                   if(password.equals(cpassword)) {
                       edit_profile_check.getElement().getStyle().setOpacity(1);
                       edit_profile_check.setEnabled(true);
                       edit_profile_checkImg.getElement().getStyle().setOpacity(1);
                   }
               }
           });
        }
    };
    
    ClickHandler MyClickEditProfilePasswordHandler = new ClickHandler() {

        @Override
        public void onClick(ClickEvent event) {
            
            edit_profile_vc.setVisible(true);
            final Label label = (Label) event.getSource();
            
            final PasswordTextBox nome = new PasswordTextBox(); 
            nome.getElement().getStyle().setWidth(65, Style.Unit.PCT);
            
           ((HorizontalPanel) label.getParent()).add(nome);
           nome.setFocus(true);
           label.setVisible(false);
           
           nome.addKeyDownHandler(new KeyDownHandler(){

               @Override
               public void onKeyDown(KeyDownEvent event) {

                   if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                       label.setText("#######");
                       if(label.getTitle().equals("password"))
                       password = nome.getValue();
                       else if(label.getTitle().equals("cpassword"))
                       cpassword = nome.getValue();    
                       nome.removeFromParent();
                       label.setVisible(true);
                       if(password.equals(cpassword)) {
                           edit_profile_check.getElement().getStyle().setOpacity(1);
                           edit_profile_check.setEnabled(true);
                           edit_profile_checkImg.getElement().getStyle().setOpacity(1);
                           edit_profile_labelCpassword.getElement().getStyle().setColor("#FFFFFF");
                       }
                       else {
                           edit_profile_checkImg.getElement().getStyle().setOpacity(0);
                           edit_profile_check.getElement().getStyle().setOpacity(0.5);
                           edit_profile_check.setEnabled(false);
                           edit_profile_labelCpassword.getElement().getStyle().setColor("#FF0000");
                       }
                   }
               }
             });
           
           nome.addBlurHandler(new BlurHandler(){

            @Override
            public void onBlur(BlurEvent event) {
                label.setText("#######");
                if(label.getTitle().equals("password"))
                password = nome.getValue();
                else if(label.getTitle().equals("cpassword"))
                cpassword = nome.getValue();    
                nome.removeFromParent();
                label.setVisible(true);
                if(password.equals(cpassword)) {
                    edit_profile_check.getElement().getStyle().setOpacity(1);
                    edit_profile_check.setEnabled(true);
                    edit_profile_checkImg.getElement().getStyle().setOpacity(1);
                    edit_profile_labelCpassword.getElement().getStyle().setColor("#FFFFFF");
                }
                else {
                    edit_profile_checkImg.getElement().getStyle().setOpacity(0);
                    edit_profile_check.getElement().getStyle().setOpacity(0.5);
                    edit_profile_check.setEnabled(false);
                    edit_profile_labelCpassword.getElement().getStyle().setColor("#FF0000");
                }
            }

           });
        }
    };
    
    

    @Override
    public void showEditProfile(String nickname, String name, String surname,
            String nationality, String gender, String aboutme) {
        
        password = "";
        cpassword = "";
        
        edit_profile_utente.setText(utente.getText());
        edit_profile_checkImg.getElement().getStyle().setOpacity(0);
        edit_profile_check.getElement().getStyle().setOpacity(0.5);
        edit_profile_check.setEnabled(false);
               
        edit_profile_nickname.setText(nickname);
        edit_profile_name.setText(name);
        edit_profile_surname.setText(surname);
        edit_profile_nationality.setText(nationality);
        edit_profile_gender.setText(gender);
        edit_profile_aboutme.getElement().setInnerHTML(aboutme);
        
        edit_profile_vc.setVisible(false);
        
        edit_profile_password.setTitle("password");
        edit_profile_cpassword.setTitle("cpassword");
        
        edit_profile_password.addClickHandler(MyClickEditProfilePasswordHandler);
        edit_profile_cpassword.addClickHandler(MyClickEditProfilePasswordHandler);
        edit_profile_nickname.addClickHandler(MyClickEditProfileHandler);
        edit_profile_name.addClickHandler(MyClickEditProfileHandler);
        edit_profile_surname.addClickHandler(MyClickEditProfileHandler);
        edit_profile_nationality.addClickHandler(MyClickEditProfileHandler);
        edit_profile_gender.addClickHandler(MyClickEditProfileHandler);
        edit_profile_aboutme.addDomHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                
                final HTMLPanel about = (HTMLPanel) event.getSource();
                
                final TextArea nome = new TextArea(); 
                nome.getElement().getStyle().setWidth(575, Style.Unit.PX);
                nome.getElement().getStyle().setHeight(115, Style.Unit.PX);
                nome.getElement().getStyle().setProperty("resize", "none");
                nome.setValue(about.getElement().getInnerHTML());
               ((VerticalPanel)about.getParent()).add(nome);
               nome.setFocus(true);
               about.setVisible(false);
               
               nome.addKeyDownHandler(new KeyDownHandler(){

                   @Override
                   public void onKeyDown(KeyDownEvent event) {

                       if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                        
                           about.getElement().setInnerHTML(nome.getValue());
                           nome.removeFromParent();
                           about.setVisible(true);
                           if(password.equals(cpassword)) {
                               edit_profile_check.getElement().getStyle().setOpacity(1);
                               edit_profile_check.setEnabled(true);
                               edit_profile_checkImg.getElement().getStyle().setOpacity(1);
                           }
                       }
                   }
                 });       
                
               nome.addBlurHandler(new BlurHandler(){

                   @Override
                   public void onBlur(BlurEvent event) {
                       
                       about.getElement().setInnerHTML(nome.getValue());
                       nome.removeFromParent();
                       about.setVisible(true);
                       if(password.equals(cpassword)) {
                           edit_profile_check.getElement().getStyle().setOpacity(1);
                           edit_profile_check.setEnabled(true);
                           edit_profile_checkImg.getElement().getStyle().setOpacity(1);
                       }
                   }
               });
               
            }   }, ClickEvent.getType());
        //addClickHandler(MyClickEditProfileHandler);
        
    }
	
    public void deleteSong(String autore, String titolo, String album) {
       
        Song canzone = new Song(autore, titolo, album);
        
        dataProvider_catalogo.getList().remove(canzone);
        canzoni_catalogo.remove(canzone);
        closeSong();
    }
	
        
}


