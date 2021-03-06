package it.unipd.netmus.client.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.dom.client.Style;
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
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * Nome: ProfileViewImpl.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 16 Febbraio 2011
 */
public class ProfileViewImpl extends Composite implements ProfileView {

    // Estensione necessarria x la configurazione tramite CSS della cellTabel
    public interface MyCellTableResources extends CellTable.Resources {

        public interface CellTableStyle extends CellTable.Style {
        };

        @Source({ "css/CellTable.css" })
        CellTableStyle cellTableStyle();

    }
    public interface MyCellTableResourcesPlaylist extends CellTable.Resources {

        public interface CellTableStyle extends CellTable.Style {
        };

        @Source({ "css/CellTablePlaylist.css" })
        CellTableStyle cellTableStyle();

    }

    interface ProfileViewImplUiBinder extends UiBinder<Widget, ProfileViewImpl> {
    }

    // classe interna che rappresenta un brano
    private static class Song extends
            it.unipd.netmus.client.ui.ProfileView.Song implements
            Comparable<Song> {

        private final String album;
        private final String autore;
        private final String titolo;

        public Song(String autore, String titolo, String album) {
            this.autore = autore;
            this.titolo = titolo;
            this.album = album;
        }

        public int compareTo(Song compareObject) {

            if (this == compareObject) {
                return 0;
            }

            if (this != null) {
                return (compareObject != null) ? this.album
                        .compareTo(compareObject.album) : 1;
            }

            return -1;

        }

        @Override
        public boolean equals(Object o) {

            if (o == null)
                return false;
            if (!(o instanceof Song))
                return false;

            Song song = (Song) o;

            if (this.autore.equals(song.autore)
                    && this.titolo.equals(song.titolo)
                    && this.album.equals(song.album)) {

                return true;
            }
            return false;

        }

    }
    private static ProfileViewImplUiBinder uiBinder = GWT
            .create(ProfileViewImplUiBinder.class);
    private static native void pausePlayer() /*-{

		$doc.getElementById('youtube_player').pauseVideo();

    }-*/;
    private static native void playPlayer() /*-{

		$doc.getElementById('youtube_player').playVideo();

    }-*/;
    private static native void playPlayerSong(String t) /*-{

		$doc.getElementById('youtube_player').loadVideoById(t, 1, 'medium');

    }-*/;
    private static native void stopPlayerSong() /*-{

		$doc.getElementById('youtube_player').stopVideo();
		$doc.getElementById('youtube_player').clearVideo();

    }-*/;
    @UiField
    Label about_me;
    @UiField
    Image account_button;

    @UiField
    Image add_playlist;
    @UiField
    Image add_song_to_playlist;

    @UiField
    Label album_song;
    @UiField
    Label artist_song;
    List<Song> canzoni_catalogo = new ArrayList<Song>();

    List<Song> canzoni_cover = new ArrayList<Song>();
    List<Song> canzoni_playlist = new ArrayList<Song>();

    // elementi uiBinder

    @UiField
    Image close_playlist;

    // @UiField Anchor back;

    @UiField
    Image close_song;
    @UiField
    Image close_youtube;
    @UiField
    Label composer_song;
    @UiField
    HTMLPanel container;
    SelectElement country;
    @UiField
    HTMLPanel country_container;
    @UiField
    Image cover;
    Image cover_playing = new Image();
    HTMLPanel cover_selected;
    @UiField
    HTMLPanel covers_container;
    ListDataProvider<Song> dataProvider_album = new ListDataProvider<Song>();
    ListDataProvider<Song> dataProvider_catalogo = new ListDataProvider<Song>();
    ListDataProvider<Song> dataProvider_playlist = new ListDataProvider<Song>();
    @UiField
    Image delete_playlist;
    @UiField
    Label delete_song;
    @UiField
    Image down;
    @UiField
    Image edit_button;
    @UiField
    HTMLPanel edit_profile;
    @UiField
    HTMLPanel edit_profile_aboutme;
    @UiField
    Button edit_profile_check;
    @UiField
    Image edit_profile_check_img;
    @UiField
    Image edit_profile_close;

    @UiField
    Label edit_profile_cpassword;
    @UiField
    Label edit_profile_del;
    @UiField
    Label edit_profile_labelCpassword;
    @UiField
    Label edit_profile_name;
    @UiField
    Label edit_profile_nickname;
    @UiField
    Label edit_profile_password;
    @UiField
    Label edit_profile_surname;
    @UiField
    Label edit_profile_user;
    @UiField
    VerticalPanel edit_profile_vc;
    Label error_text = new Label();
    @UiField
    Label export;
    @UiField
    RadioButton female;
    boolean flag_album;
    @UiField
    Image flag_eng;
    @UiField
    Image flag_ita;
    @UiField
    Image forward;
    @UiField
    HTMLPanel friends;
    @UiField
    HTMLPanel friends_titolo;
    @UiField
    Label friends_titolo_text;
    @UiField
    Label genre_song;
    @UiField
    Label help;
    @UiField
    HTMLPanel help_container;
    @UiField
    Label help_exit;
    @UiField
    Label hlp1;
    @UiField
    Label hlp2;
    @UiField
    Label hlp3;
    @UiField
    Label hlp4;
    @UiField
    Label hlp5;
    @UiField
    Label hlpT;
    @UiField
    HTMLPanel info;
    @UiField
    Label info_text;
    @UiField
    HTMLPanel info_youtube;
    @UiField
    Label info_youtube_link;
    @UiField
    Label insert_song;
    int last_selected = 0;
    @UiField
    HTMLPanel left_panel;

    @UiField(provided = true)
    CellTable<Song> library;

    @UiField
    HTMLPanel library_container;
    @UiField
    Image loading;
    @UiField
    Image logo_youtube;
    @UiField
    Anchor logout;
    @UiField
    HTMLPanel main_panel;
    @UiField
    RadioButton male;
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

            nome.addKeyDownHandler(new KeyDownHandler() {

                @Override
                public void onKeyDown(KeyDownEvent event) {

                    if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {

                        label.setText(nome.getValue());
                        nome.removeFromParent();
                        label.setVisible(true);
                        if (password.equals(cpassword)) {
                            edit_profile_check.getElement().getStyle()
                                    .setOpacity(1);
                            edit_profile_check.setEnabled(true);
                            edit_profile_check_img.getElement().getStyle()
                                    .setOpacity(1);
                        }
                    }
                }
            });

            nome.addBlurHandler(new BlurHandler() {

                @Override
                public void onBlur(BlurEvent event) {

                    label.setText(nome.getValue());
                    nome.removeFromParent();
                    label.setVisible(true);
                    if (password.equals(cpassword)) {
                        edit_profile_check.getElement().getStyle()
                                .setOpacity(1);
                        edit_profile_check.setEnabled(true);
                        edit_profile_check_img.getElement().getStyle()
                                .setOpacity(1);
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

            nome.addKeyDownHandler(new KeyDownHandler() {

                @Override
                public void onKeyDown(KeyDownEvent event) {

                    if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                        label.setText("#######");
                        if (label.getTitle().equals("password"))
                            password = nome.getValue();
                        else if (label.getTitle().equals("cpassword"))
                            cpassword = nome.getValue();
                        nome.removeFromParent();
                        label.setVisible(true);
                        if (password.equals(cpassword)) {
                            edit_profile_check.getElement().getStyle()
                                    .setOpacity(1);
                            edit_profile_check.setEnabled(true);
                            edit_profile_check_img.getElement().getStyle()
                                    .setOpacity(1);
                            edit_profile_labelCpassword.getElement().getStyle()
                                    .setColor("#FFFFFF");
                        } else {
                            edit_profile_check_img.getElement().getStyle()
                                    .setOpacity(0);
                            edit_profile_check.getElement().getStyle()
                                    .setOpacity(0.5);
                            edit_profile_check.setEnabled(false);
                            edit_profile_labelCpassword.getElement().getStyle()
                                    .setColor("#FF0000");
                        }
                    }
                }
            });

            nome.addBlurHandler(new BlurHandler() {

                @Override
                public void onBlur(BlurEvent event) {
                    label.setText("#######");
                    if (label.getTitle().equals("password"))
                        password = nome.getValue();
                    else if (label.getTitle().equals("cpassword"))
                        cpassword = nome.getValue();
                    nome.removeFromParent();
                    label.setVisible(true);
                    if (password.equals(cpassword)) {
                        edit_profile_check.getElement().getStyle()
                                .setOpacity(1);
                        edit_profile_check.setEnabled(true);
                        edit_profile_check_img.getElement().getStyle()
                                .setOpacity(1);
                        edit_profile_labelCpassword.getElement().getStyle()
                                .setColor("#FFFFFF");
                    } else {
                        edit_profile_check_img.getElement().getStyle()
                                .setOpacity(0);
                        edit_profile_check.getElement().getStyle()
                                .setOpacity(0.5);
                        edit_profile_check.setEnabled(false);
                        edit_profile_labelCpassword.getElement().getStyle()
                                .setColor("#FF0000");
                    }
                }

            });
        }
    };
    MyConstants myConstants = GWT.create(MyConstants.class);
    @UiField
    Label n_songs;
    @UiField
    Label name_user;
    @UiField
    Label nation;
    @UiField
    Label num_songs;
    @UiField
    Image pdf;
    @UiField
    Image play;
    @UiField
    Image play_youtube;
    Song played_song;
    List<Song> player_playlist = new ArrayList<Song>();
    int playing = 0;
    @UiField
    HTMLPanel playlist_container;
    @UiField
    HTMLPanel playlist_content;
    boolean playlist_opened;

    @UiField
    HTMLPanel playlist_songs;
    @UiField
    Label playlist_title;
    @UiField
    HTMLPanel playlists;
    @UiField
    HTMLPanel popup;
    @UiField
    HTMLPanel popup_fast;

    @UiField
    Button popup_no;
    @UiField
    Button popup_yes;
    @UiField
    HTMLPanel ranking;
    @UiField
    Label rating_song;
    @UiField
    Label remove_song;
    @UiField
    Image remove_song_from_playlist;
    MyCellTableResources resource = GWT.create(MyCellTableResources.class);
    MyCellTableResourcesPlaylist resourcePlaylist = GWT
            .create(MyCellTableResourcesPlaylist.class);
    @UiField
    Image rewind;
    @UiField
    HTMLPanel search;
    @UiField
    TextBox search_box;
    Timer searcht;
    String selected_album;
    Song selected_song;
    Song selected_song_playlist;
    @UiField
    Label sex;
    @UiField
    Image social_button;
    @UiField
    Label song_album;
    @UiField
    Label song_artist;
    @UiField
    Label song_composer;
    @UiField
    HTMLPanel song_container;
    @UiField
    HTMLPanel song_contenuto;
    @UiField
    Image song_cover;
    @UiField
    Label song_genre;
    boolean song_opened;
    @UiField
    Label song_title;
    @UiField
    Label song_track;
    @UiField
    Label song_year;
    @UiField
    Image star1;

    @UiField
    Image star2;
    @UiField
    Image star3;
    @UiField
    Image star4;
    @UiField
    Image star5;
    @UiField
    Image starG1;
    @UiField
    Image starG2;
    @UiField
    Image starG3;
    @UiField
    Image starG4;
    @UiField
    Image starG5;

    @UiField
    Button stat_close;
    @UiField
    Label stat_netmus;
    @UiField
    Label stat_num;
    @UiField
    Label stat_pref;

    @UiField
    Label stat_pref_netmus;

    @UiField
    Label stat_preferred;

    @UiField
    Label stat_preferredn;
    @UiField
    Label stat_preferreds;

    @UiField
    Label stat_song_pref;

    @UiField
    Label stat_tracks;
    @UiField
    Label statisticL;
    @UiField
    HTMLPanel statistics;
    @UiField
    Image statistics_open;

    @UiField
    Label surname;
    @UiField
    Image switch_cover;
    @UiField
    Image switch_list;

    @UiField
    Label title_song;
    @UiField
    Label track_song;
    @UiField
    Label track_title;
    @UiField
    Image up;

    @UiField
    Label user;

    @UiField
    Label vote;

    @UiField
    Label year_song;

    @UiField
    HTMLPanel youtube;
    @UiField
    HTMLPanel youtube_appendix;
    int youtube_status = 0;
    private CellTable<Song> album_list;
    private String cpassword;
    private double global_rating;
    private Presenter listener;

    // /////////////////////////////////////////////////////
    // /////////////////////////////////////////////////////

    private String name;

    private String password;

    private int rating;

    private HandlerRegistration rm_fw;

    // Estensione necessarria x la configurazione tramite CSS della cellTabel

    private HandlerRegistration rm_link;

    // /////////////////////////////////////////////////////
    // /////////////////////////////////////////////////////

    private HandlerRegistration rm_popno;

    // /////////////////////////////////////////////////////
    // /////////////////////////////////////////////////////

    private HandlerRegistration rm_popyes;

    private HandlerRegistration rm_rw;

    private CellTable<Song> song_list;

    private int vertical_offset = 45;

    private int vertical_semioffset = 275;

    public ProfileViewImpl() {

        youTubeListener();
        youtube_status = 0;
        // costruisco la componente widget x il catalogo delle canzoni
        library = new CellTable<Song>(Integer.MAX_VALUE, resource);
        library.setWidth("100%", true);
        // catalogo.setPageSize(100000);

        initWidget(uiBinder.createAndBindUi(this));

        // crea la colonna autore
        TextColumn<Song> autoreColumn = new TextColumn<Song>() {
            @Override
            public String getValue(Song song) {
                return song.autore;
            }
        };
        // la rende ordinabile
        autoreColumn.setSortable(true);
        // la aggiunge al catalogo
        library.addColumn(autoreColumn, myConstants.artist());

        // crea la colonna titolo
        TextColumn<Song> titoloColumn = new TextColumn<Song>() {
            @Override
            public String getValue(Song song) {
                return song.titolo;
            }
        };
        // la rende ordinabile
        titoloColumn.setSortable(true);
        // la aggiunge al catalogo
        library.addColumn(titoloColumn, myConstants.title());

        // crea la colonna Album
        TextColumn<Song> albumColumn = new TextColumn<Song>() {
            @Override
            public String getValue(Song song) {
                return song.album;
            }
        };
        // la rende ordinabile
        albumColumn.setSortable(true);
        // la aggiunge al catalogo
        library.addColumn(albumColumn, "Album");

        // Imposta l'oggetto Song selected in base alla selezione sulla tabella
        final SingleSelectionModel<Song> selectionModel = new SingleSelectionModel<Song>();
        library.setSelectionModel(selectionModel);
        selectionModel
                .addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
                    public void onSelectionChange(SelectionChangeEvent event) {

                        setBranoCatalogo(selectionModel.getSelectedObject());

                        track_title.setText(selected_song.titolo);
                        song_title.setText(selected_song.titolo);
                        song_artist.setText(selected_song.autore);
                        song_album.setText(selected_song.album);

                        if (playing == 0
                                && selectionModel.getSelectedObject().equals(
                                        played_song)
                                && (youtube_status == 1 || youtube_status == 2)) {
                            play.setUrl("images/pause.png");
                            youtube_status = 1;
                            last_selected = 0;

                        } else if (playing == 0
                                && selectionModel.getSelectedObject().equals(
                                        played_song)
                                && (youtube_status == -1 || youtube_status == 3)) {
                            play.setUrl("images/play.png");
                            youtube_status = -1;
                            last_selected = 0;

                        }

                        else {
                            play.setUrl("images/play.png");
                            if (youtube_status != 0 && youtube_status == 1)
                                youtube_status = 2;
                            else if (youtube_status != 0
                                    && youtube_status == -1)
                                youtube_status = 3;
                            last_selected = 0;
                        }
                        play_youtube.setUrl("images/play.png");

                        global_rating = listener.setRating(
                                selected_song.autore, selected_song.titolo,
                                selected_song.album);
                        showStar(rating);

                        listener.setSongFields(selected_song.autore,
                                selected_song.titolo, selected_song.album);

                        if (youtube_status != 0)
                            setPlaySong(true);

                    }
                });

        library.addDomHandler(new DoubleClickHandler() {

            @Override
            public void onDoubleClick(DoubleClickEvent event) {

                if (playlist_opened && flag_album == false) {

                    listener.addToPLaylist(playlist_title.getText(),
                            selected_song.autore, selected_song.titolo,
                            selected_song.album);

                } else {

                    if (selected_song != null)
                        viewSong(selected_song);
                }

            }
        }, DoubleClickEvent.getType());

        library.addHandler(new KeyUpHandler() {

            @Override
            public void onKeyUp(KeyUpEvent event) {

                if (event.getNativeKeyCode() == KeyCodes.KEY_DELETE) {

                    listener.deleteSong(selected_song.autore,
                            selected_song.titolo, selected_song.album);

                }

            }

        }, KeyUpEvent.getType());

        List<Song> list = dataProvider_catalogo.getList();

        ListHandler<Song> columnSortHandler = new ListHandler<Song>(list);

        columnSortHandler.setComparator(autoreColumn, new Comparator<Song>() {
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

        columnSortHandler.setComparator(titoloColumn, new Comparator<Song>() {
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

        columnSortHandler.setComparator(albumColumn, new Comparator<Song>() {
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

        library.addColumnSortHandler(columnSortHandler);

        // We know that the data is sorted alphabetically by default.
        library.getColumnSortList().push(autoreColumn);

        dataProvider_catalogo.addDataDisplay(library);

        // PLAYLIST TABLE

        playlist_songs.getElement().setInnerHTML("");

        song_list = new CellTable<Song>(Integer.MAX_VALUE, resourcePlaylist);
        song_list.setWidth("100%", true);

        // crea la colonna titolo
        TextColumn<Song> titoloColumn2 = new TextColumn<Song>() {
            @Override
            public String getValue(Song song) {
                return song.titolo;
            }
        };
        // la rende ordinabile
        titoloColumn2.setSortable(true);
        // la aggiunge al catalogo
        song_list.addColumn(titoloColumn2, myConstants.title());
        song_list.setColumnWidth(titoloColumn2, "60%");

        // crea la colonna Album
        TextColumn<Song> albumColumn2 = new TextColumn<Song>() {
            @Override
            public String getValue(Song song) {
                return song.album;
            }
        };
        // la rende ordinabile
        albumColumn2.setSortable(true);
        // la aggiunge al catalogo
        song_list.addColumn(albumColumn2, "Album");
        song_list.setColumnWidth(albumColumn2, "60%");

        // Imposta l'oggetto Song selected_inplaylist in base alla selezione
        // sulla tabella
        final SingleSelectionModel<Song> selectionModel2 = new SingleSelectionModel<Song>();
        song_list.setSelectionModel(selectionModel2);
        selectionModel2
                .addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
                    public void onSelectionChange(SelectionChangeEvent event) {

                        setBranoPlaylist(selectionModel2.getSelectedObject());

                        if (playing == 1
                                && selectionModel2.getSelectedObject().equals(
                                        played_song)
                                && (youtube_status == 1 || youtube_status == 2)) {
                            play.setUrl("images/pause.png");
                            youtube_status = 1;
                            last_selected = 1;
                        } else if (playing == 1
                                && selectionModel2.getSelectedObject().equals(
                                        played_song)
                                && (youtube_status == -1 || youtube_status == 3)) {
                            play.setUrl("images/play.png");
                            youtube_status = -1;
                            last_selected = 1;
                        }

                        else {
                            play.setUrl("images/play.png");
                            if (youtube_status != 0 && youtube_status == 1)
                                youtube_status = 2;
                            else if (youtube_status != 0
                                    && youtube_status == -1)
                                youtube_status = 3;
                            last_selected = 1;
                        }
                        play_youtube.setUrl("images/play.png");

                        if (youtube_status != 0)
                            setPlaySong(true);

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
                            return (o2 != null) ? o1.titolo
                                    .compareTo(o2.titolo) : 1;
                        }
                        return -1;
                    }
                });

        columnPlSortHandler.setComparator(albumColumn2, new Comparator<Song>() {
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

        song_list.addColumnSortHandler(columnPlSortHandler);

        // We know that the data is sorted alphabetically by default.
        song_list.getColumnSortList().push(titoloColumn2);

        dataProvider_playlist.addDataDisplay(song_list);
        playlist_songs.add(song_list);

        // ALBUM

        album_list = new CellTable<Song>(Integer.MAX_VALUE, resource);
        album_list.setWidth("100%", true);

        // crea la colonna titolo
        TextColumn<Song> titoloColumn3 = new TextColumn<Song>() {
            @Override
            public String getValue(Song song) {
                return song.titolo;
            }
        };
        // la rende ordinabile
        titoloColumn3.setSortable(true);
        // la aggiunge al catalogo
        album_list.addColumn(titoloColumn3, myConstants.title());
        album_list.setColumnWidth(titoloColumn3, "60%");

        // crea la colonna Album
        TextColumn<Song> autoreColumn2 = new TextColumn<Song>() {
            @Override
            public String getValue(Song song) {
                return song.autore;
            }
        };
        // la rende ordinabile
        autoreColumn2.setSortable(true);
        // la aggiunge al catalogo
        album_list.addColumn(autoreColumn2, myConstants.artist());
        album_list.setColumnWidth(autoreColumn2, "60%");

        // Imposta l'oggetto Song selected_inplaylist in base alla selezione
        // sulla tabella
        final SingleSelectionModel<Song> selectionModel3 = new SingleSelectionModel<Song>();
        album_list.setSelectionModel(selectionModel3);
        selectionModel3
                .addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
                    public void onSelectionChange(SelectionChangeEvent event) {

                        setBranoPlaylist(selectionModel3.getSelectedObject());

                        if (playing == 1
                                && selectionModel3.getSelectedObject().equals(
                                        played_song)
                                && (youtube_status == 1 || youtube_status == 2)) {
                            play.setUrl("images/pause.png");
                            youtube_status = 1;
                            last_selected = 1;
                        } else if (playing == 1
                                && selectionModel3.getSelectedObject().equals(
                                        played_song)
                                && (youtube_status == -1 || youtube_status == 3)) {
                            play.setUrl("images/play.png");
                            youtube_status = -1;
                            last_selected = 1;
                        }

                        else {
                            play.setUrl("images/play.png");
                            if (youtube_status != 0 && youtube_status == 1)
                                youtube_status = 2;
                            else if (youtube_status != 0
                                    && youtube_status == -1)
                                youtube_status = 3;
                            last_selected = 1;
                        }
                        play_youtube.setUrl("images/play.png");

                        if (youtube_status != 0)
                            setPlaySong(true);

                    }
                });

        List<Song> listAlb = dataProvider_album.getList();

        ListHandler<Song> columnAlbSortHandler = new ListHandler<Song>(listAlb);

        columnAlbSortHandler.setComparator(titoloColumn3,
                new Comparator<Song>() {
                    public int compare(Song o1, Song o2) {
                        if (o1 == o2) {
                            return 0;
                        }

                        // Compare the name columns.
                        if (o1 != null) {
                            return (o2 != null) ? o1.titolo
                                    .compareTo(o2.titolo) : 1;
                        }
                        return -1;
                    }
                });

        columnAlbSortHandler.setComparator(autoreColumn2,
                new Comparator<Song>() {
                    public int compare(Song o1, Song o2) {
                        if (o1 == o2) {
                            return 0;
                        }

                        // Compare the name columns.
                        if (o1 != null) {
                            return (o2 != null) ? o1.autore
                                    .compareTo(o2.autore) : 1;
                        }
                        return -1;
                    }
                });

        album_list.addColumnSortHandler(columnAlbSortHandler);

        // We know that the data is sorted alphabetically by default.
        album_list.getColumnSortList().push(titoloColumn3);

        dataProvider_album.addDataDisplay(album_list);

        // FINE ALBUM

        covers_container.setVisible(false);
        loading.setVisible(false);
        popup.setVisible(false);
        popup_yes.setVisible(false);
        popup_no.setVisible(false);

        HTMLPanel off = new HTMLPanel("");
        off.getElement().getStyle().setHeight(22, Style.Unit.PX);
        playlist_songs.add(off);
        main_panel.getElement().setId("main_panel");
        country = (SelectElement) SelectElement.as(country_container
                .getElement().getChild(0));

        friends_titolo_text.setText(myConstants.friendTitolo());
        info_text.setText(myConstants.info());
        title_song.setText(myConstants.titleSong());
        artist_song.setText(myConstants.artistSong());
        album_song.setText(myConstants.albumSong());
        genre_song.setText(myConstants.genreSong());
        year_song.setText(myConstants.yearSong());
        composer_song.setText(myConstants.composerSong());
        track_song.setText(myConstants.trackSong());
        rating_song.setText(myConstants.ratingSong());
        delete_song.setText(myConstants.delete_song());
        vote.setText(myConstants.vote());
        export.setText(myConstants.export());
        statisticL.setText(myConstants.statisticL());
        edit_profile_check.setText(myConstants.editProfileCheck());
        edit_profile_del.setText(myConstants.edit_profile_del());
        help_exit.setText(myConstants.help_exit());
        name_user.setText(myConstants.name());
        surname.setText(myConstants.surname());
        nation.setText(myConstants.nation());
        sex.setText(myConstants.sex());
        edit_profile_labelCpassword.setText(myConstants
                .editProfileLabelCpassword());
        about_me.setText(myConstants.aboutMe());
        stat_netmus.setText(myConstants.statNetmus());
        stat_num.setText(myConstants.statNum());
        stat_pref.setText(myConstants.statPref());
        stat_song_pref.setText(myConstants.statSongPref());
        stat_pref_netmus.setText(myConstants.statPrefNetmus());
        stat_close.setText(myConstants.statClose());
        num_songs.setText(myConstants.statNum());

        hlpT.setText(myConstants.hlpT());
        hlp1.setText(myConstants.hlp1());
        hlp2.setText(myConstants.hlp2());
        hlp3.setText(myConstants.hlp3());
        hlp4.setText(myConstants.hlp4());
        hlp5.setText(myConstants.hlp5());

        help_container.setVisible(false);

    }

    @SuppressWarnings("unchecked")
    @Override
    public void addToPLaylist(String autore, String titolo, String album) {

        Song brano = new Song(autore, titolo, album);
        if (!canzoni_playlist.contains(brano)) {
            List<Song> test = dataProvider_playlist.getList();
            canzoni_playlist.add(new Song(selected_song.autore,
                    selected_song.titolo, selected_song.album));
            test.add(canzoni_playlist.get(canzoni_playlist.size() - 1));
            insert_song.setText("");

            if (((Song) brano).equals(((SingleSelectionModel<Song>) song_list
                    .getSelectionModel()).getSelectedObject()))
                remove_song.setText(((Song) brano).titolo);
        }

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
        wrapper.addMouseOverHandler(new MouseOverHandler() {

            @Override
            public void onMouseOver(MouseOverEvent event) {
                event.getRelativeElement().getStyle()
                        .setBackgroundColor("#e0fee1");
                event.getRelativeElement().getStyle()
                        .setCursor(Style.Cursor.POINTER);
            }
        });

        wrapper.addMouseOutHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent event) {
                event.getRelativeElement().getStyle()
                        .setBackgroundColor("#FFFFFF");
            }
        });

        wrapper.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                viewPlaylist(tmpTxt.getText());
                wrapper.getElement().getStyle().setBackgroundColor("#FFFFFF");
                Timer timerButton = new Timer() {
                    public void run() {
                        wrapper.getElement().getStyle()
                                .setBackgroundColor("#e0fee1");
                    }
                };
                timerButton.schedule(50);
            }
        });

        wrapper.add(tmpPnl);
        playlists.add(wrapper);
        setLayout();
    }

    @Override
    public void closeEditProfile() {
        edit_profile.getElement().getStyle().setOpacity(0);
        Timer timerHidden = new Timer() {
            public void run() {
                edit_profile.getElement().getStyle()
                        .setDisplay(Style.Display.NONE);
            }
        };
        timerHidden.schedule(200);
    }

    @Override
    public void closePlaylist() {

        playlist_opened = false;

        remove_song.setText("");
        library_container.getElement().getStyle().setWidth(100, Style.Unit.PCT);
        playlist_container.getElement().getStyle().setWidth(0, Style.Unit.PX);
        playlist_content.getElement().getStyle().setOpacity(0);
        playlist_title.setText("");

    }

    @Override
    public void closeSong() {

        song_opened = false;

        library_container.getElement().getStyle().setWidth(100, Style.Unit.PCT);
        song_container.getElement().getStyle().setWidth(0, Style.Unit.PX);
        song_contenuto.getElement().getStyle().setOpacity(0);
        song_cover.getElement().getStyle().setOpacity(0);
    }

    public void closeYouTube() {

        if (youtube_status != 0) {

            youtube_status = 0;
            youtube.getElement().getStyle().setHeight(60, Style.Unit.PX);
            youtube.getElement().getStyle().setWidth(200, Style.Unit.PX);
            youtube.getElement().getStyle().setBottom(22, Style.Unit.PX);
            youtube.getElement().getStyle().setLeft(25, Style.Unit.PX);

            vertical_semioffset = 275;

            Timer timerLayout = new Timer() {
                public void run() {

                    setLayout();

                }
            };
            timerLayout.schedule(500);

            setPlaySong(false);

            /*
             * catalogo_container.getElement().getStyle().setHeight(Window.
             * getClientHeight()-(vertical_semioffset), Style.Unit.PX);
             * playlist_container
             * .getElement().getStyle().setHeight(Window.getClientHeight
             * ()-(vertical_semioffset), Style.Unit.PX);
             * song_container.getElement
             * ().getStyle().setHeight(Window.getClientHeight
             * ()-(vertical_semioffset), Style.Unit.PX);
             */

            play_youtube.getElement().getStyle().setOpacity(1);
            logo_youtube.getElement().getStyle().setOpacity(1);

            logo_youtube.getElement().getStyle()
                    .setPosition(Style.Position.RELATIVE);
            logo_youtube.getElement().getStyle().setBottom(0, Style.Unit.PX);
            logo_youtube.getElement().getStyle().setLeft(0, Style.Unit.PX);

            info_youtube.getElement().getStyle().setOpacity(0);
            info_youtube_link.getElement().getStyle().setOpacity(0);
            youtube_appendix.getElement().getStyle().setOpacity(0);
            close_youtube.getElement().getStyle().setOpacity(0);

            info_youtube.setVisible(false);
            info_youtube_link.setVisible(false);
            youtube_appendix.setVisible(false);
            close_youtube.setVisible(false);

            ranking.getElement().getStyle().setLeft(265, Style.Unit.PX);

            while (youtube.getElementById("player") != null)
                youtube.getElementById("player").removeFromParent();

            youtube.getElementById("player").setId("");
            // youtube.getWidget(6).removeFromParent();
            setInfo(myConstants.info());
            forward.setVisible(false);
            rewind.setVisible(false);
        }

    }

    public void deleteSong(String autore, String titolo, String album) {

        n_songs.setText((new Integer(Integer.parseInt(n_songs.getText()) - 1))
                .toString());

        Song canzone = new Song(autore, titolo, album);

        dataProvider_catalogo.getList().remove(canzone);
        canzoni_catalogo.remove(canzone);
        closeSong();
        if (covers_container.isVisible())
            paintCovers(dataProvider_catalogo.getList());

    }

    // aggiunge al catalogo/libreria la lista dei brani
    @Override
    public void paintCatalogo(List<String> lista_canzoni) {

        List<Song> test = dataProvider_catalogo.getList();

        for (int j = 0; j < lista_canzoni.size(); j += 3) {
            Song song = new Song(lista_canzoni.get(j),
                    lista_canzoni.get(j + 1), lista_canzoni.get(j + 2));
            test.add(song);
            canzoni_catalogo.add(song);
        }

        setNumeroBrani(canzoni_catalogo.size());

        Collections.sort(test, new Comparator<Song>() {
            @Override
            public int compare(Song arg0, Song arg1) {
                String artist_1 = arg0.autore;
                String artist_2 = arg1.autore;
                String album_1 = arg0.album;
                String album_2 = arg1.album;
                int compare_artists = artist_1.compareToIgnoreCase(artist_2);

                if (compare_artists == 0) {
                    return album_1.compareToIgnoreCase(album_2);
                } else {
                    return compare_artists;
                }
            }
        });

    }

    // riempie la lista degli amici netmus
    @Override
    public void paintFriendlist(String[] lista) {

        friends.clear();

        for (int k = 0; k < lista.length; k++) {

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

    @Override
    public void paintMainCover(String cover) {
        this.cover.setUrl(cover);
        cover_playing.setUrl(cover);
    }

    // riempie la lista delle playlists
    @Override
    public void paintPlaylist(String[] lista) {

        Arrays.sort(lista);

        playlists.clear();
        for (int k = 0; k < lista.length; k++) {

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

            if (k % 2 > 0)
                wrapper.getElement().getStyle().setBackgroundColor("#F5F5F5");
            wrapper.getElement().getStyle().setMarginTop(0, Style.Unit.PX);
            wrapper.getElement().getStyle().setProperty("outline", "0");
            wrapper.addMouseOverHandler(new MouseOverHandler() {

                @Override
                public void onMouseOver(MouseOverEvent event) {
                    event.getRelativeElement().getStyle()
                            .setBackgroundColor("#e0fee1");
                    event.getRelativeElement().getStyle()
                            .setCursor(Style.Cursor.POINTER);
                }
            });

            if (k % 2 > 0) {
                wrapper.addMouseOutHandler(new MouseOutHandler() {
                    @Override
                    public void onMouseOut(MouseOutEvent event) {
                        event.getRelativeElement().getStyle()
                                .setBackgroundColor("#F5F5F5");
                    }
                });
            } else {
                wrapper.addMouseOutHandler(new MouseOutHandler() {
                    @Override
                    public void onMouseOut(MouseOutEvent event) {
                        event.getRelativeElement().getStyle()
                                .setBackgroundColor("#FFFFFF");
                    }
                });
            }

            wrapper.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    viewPlaylist(tmpTxt.getText());
                    wrapper.getElement().getStyle()
                            .setBackgroundColor("#FFFFFF");
                    Timer timerButton = new Timer() {
                        public void run() {
                            wrapper.getElement().getStyle()
                                    .setBackgroundColor("#e0fee1");
                        }
                    };
                    timerButton.schedule(50);
                }
            });

            wrapper.add(tmpPnl);

            // playlists.getElement().setPropertyString("width", "100%");
            playlists.add(wrapper);
        }
    }

    // riempie la lista canzoni della playlist
    @Override
    public void paintPlaylistSongs(List<String> lista) {

        flag_album = false;

        List<Song> test = dataProvider_playlist.getList();

        test.removeAll(dataProvider_playlist.getList());
        canzoni_playlist.removeAll(canzoni_playlist);
        for (int j = 0; j < lista.size(); j += 3) {

            canzoni_playlist.add(new Song(lista.get(j), lista.get(j + 1), lista
                    .get(j + 2)));
        }

        for (Song song : canzoni_playlist) {
            test.add(song);
        }

        if (youtube_status != 0 && this.playing == 1)
            setPlaySong(true);

    }

    public void playNext() {

        if (playing == 0
                && dataProvider_catalogo.getList().indexOf((Song) played_song) != dataProvider_catalogo
                        .getList().size() - 1) {

            Song to_play = dataProvider_catalogo.getList()
                    .get(dataProvider_catalogo.getList().indexOf(
                            (Song) played_song) + 1);

            // closeYouTube();
            // youtube_status=1;
            played_song = to_play;
            playing = 0;
            listener.playYouTube(to_play.autore, to_play.titolo, to_play.album);
            setFwRw();

        } else if (player_playlist.indexOf((Song) played_song) != player_playlist
                .size() - 1) {

            Song to_play = player_playlist.get(player_playlist
                    .indexOf((Song) played_song) + 1);

            // closeYouTube();
            // youtube_status=1;
            played_song = to_play;
            playing = 1;
            listener.playYouTube(to_play.autore, to_play.titolo, to_play.album);
            setFwRw();
        }
    }

    public void playPrev() {

        if (playing == 0) {

            Song to_play = dataProvider_catalogo.getList()
                    .get(dataProvider_catalogo.getList().indexOf(
                            (Song) played_song) - 1);

            // closeYouTube();
            // youtube_status=1;
            played_song = to_play;
            playing = 0;
            listener.playYouTube(to_play.autore, to_play.titolo, to_play.album);
            setFwRw();

        } else {

            Song to_play = player_playlist.get(player_playlist
                    .indexOf((Song) played_song) - 1);

            // closeYouTube();
            // youtube_status=1;
            played_song = to_play;
            playing = 1;
            listener.playYouTube(to_play.autore, to_play.titolo, to_play.album);
            setFwRw();

        }

    }

    public void playYouTube(final String link) {

        if (link.equals("")) {

            playNext();

        } else {

            youtube_status = 1;
            youtube.getElement().getStyle().setHeight(215, Style.Unit.PX);
            youtube.getElement().getStyle().setWidth(335, Style.Unit.PX);
            youtube.getElement().getStyle().setBottom(7, Style.Unit.PX);
            youtube.getElement().getStyle().setLeft(5, Style.Unit.PX);

            vertical_semioffset = 400;

            setLayout();

            /*
             * catalogo_container.getElement().getStyle().setProperty("minHeight"
             * , 515-vertical_semioffset, Style.Unit.PX);
             * playlist_container.getElement
             * ().getStyle().setProperty("minHeight", 515-vertical_semioffset,
             * Style.Unit.PX);
             * song_container.getElement().getStyle().setProperty("minHeight",
             * 515-vertical_semioffset, Style.Unit.PX);
             * 
             * catalogo_container.getElement().getStyle().setHeight(Window.
             * getClientHeight()-(vertical_semioffset), Style.Unit.PX);
             * playlist_container
             * .getElement().getStyle().setHeight(Window.getClientHeight
             * ()-(vertical_semioffset), Style.Unit.PX);
             * song_container.getElement
             * ().getStyle().setHeight(Window.getClientHeight
             * ()-(vertical_semioffset), Style.Unit.PX);
             */

            play_youtube.getElement().getStyle().setOpacity(0);

            logo_youtube.getElement().getStyle()
                    .setPosition(Style.Position.ABSOLUTE);
            logo_youtube.getElement().getStyle().setBottom(173, Style.Unit.PX);
            logo_youtube.getElement().getStyle().setLeft(355, Style.Unit.PX);

            ranking.getElement().getStyle().setLeft(365, Style.Unit.PX);

            info_youtube.getElement().setInnerText(
                    played_song.titolo + ". " + played_song.autore);

            info_youtube_link.setText("http://www.youtube.com/watch?v=" + link);

            if (rm_link != null)
                rm_link.removeHandler();

            rm_link = info_youtube_link.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    Window.open("http://www.youtube.com/watch?v=" + link,
                            "_blank", "");

                }
            });

            info_youtube.setVisible(true);
            info_youtube_link.setVisible(true);
            youtube_appendix.setVisible(true);
            close_youtube.setVisible(true);

            info_youtube.getElement().getStyle().setOpacity(1);
            info_youtube_link.getElement().getStyle().setOpacity(1);
            youtube_appendix.getElement().getStyle().setOpacity(1);
            close_youtube.getElement().getStyle().setOpacity(1);

            if (DOM.getElementById("player") == null) {// DOM.getElementById("youtube_player")==null)
                                                       // {

                HTMLPanel player = new HTMLPanel("player");
                player.getElement().setId("player");
                player.getElement().getStyle()
                        .setPosition(Style.Position.ABSOLUTE);
                player.getElement().getStyle().setTop(8, Style.Unit.PX);
                player.getElement().getStyle().setLeft(5, Style.Unit.PX);
                player.getElement().getStyle().setZIndex(3);
                youtube.add(player);

                player.getElement()
                        .setInnerHTML(
                                "<object width=\"325\" height=\"200\"><param name=\"movie\" value=\"http://www.youtube.com/v/"
                                        + link
                                        + "?rel=0&ap=%2526fmt%3D18&autoplay=1&iv_load_policy=3&fs=1&autohide=1&enablejsapi=1&showinfo=0&playerapiid=ytplayer\"></param><param name=\"allowFullScreen\" value=\"true\"></param>"
                                        + "<param name=\"allowscriptaccess\" value=\"always\"></param><param name=\"wmode\" value=\"opaque\"></param><embed id=\"youtube_player\" src=\"http://www.youtube.com/v/"
                                        + link
                                        + "?rel=0&ap=%2526fmt%3D18&autoplay=1&iv_load_policy=3&fs=1&autohide=1&enablejsapi=1&showinfo=0&playerapiid=ytplayer\" type=\"application/x-shockwave-flash\" allowscriptaccess=\"always\""
                                        + "allowfullscreen=\"true\" width=\"325\" height=\"200\" wmode=\"opaque\"></embed></object>");
            } else {

                stopPlayerSong();

                Timer timerPlay = new Timer() {
                    public void run() {

                        playPlayerSong(link);

                    }
                };
                timerPlay.schedule(1300);

            }

            setPlaySong(true);
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public void removeFromPlaylist(String autore, String titolo, String album) {

        Song brano = new Song(autore, titolo, album);

        if (canzoni_playlist.contains(brano)) {

            List<Song> test = dataProvider_playlist.getList();

            canzoni_playlist.remove(brano);
            if (canzoni_playlist.size() == 0) {

                test.remove(0);

            } else {

                test.remove(brano);

            }

            if (((Song) brano).equals(((SingleSelectionModel<Song>) library
                    .getSelectionModel()).getSelectedObject()))
                insert_song.setText(((Song) brano).titolo);

            remove_song.setText("");
        }

    }

    @Override
    public void resetView() {

        canzoni_catalogo.removeAll(canzoni_catalogo);
        dataProvider_catalogo.getList().removeAll(
                dataProvider_catalogo.getList());
        dataProvider_album.getList().removeAll(dataProvider_album.getList());
        dataProvider_playlist.getList().removeAll(
                dataProvider_playlist.getList());

        user.setText("");
        friends.clear();
        playlists.clear();

    }

    @Override
    public void setBranoCatalogo(
            it.unipd.netmus.client.ui.ProfileView.Song selezione) {

        selected_song = (Song) selezione;
        if (!canzoni_playlist.contains(selected_song)) {
            insert_song.setText(selected_song.titolo);
        } else {

            insert_song.setText("");
        }
    }

    @Override
    public void setBranoPlaylist(
            it.unipd.netmus.client.ui.ProfileView.Song selezione) {

        selected_song_playlist = (Song) selezione;
        remove_song.setText(selected_song_playlist.titolo);

    }

    // imposta il testo all'interno della finestra informativa
    @Override
    public void setInfo(String text) {
        info.getElement().setInnerHTML(text);
    }

    public void setLayout() {

        if (Window.getClientWidth() < 1190) {

            search.getElement().getStyle().setOpacity(0);
            social_button.getElement().getStyle().setRight(25, Style.Unit.PX);
            account_button.getElement().getStyle().setRight(95, Style.Unit.PX);
            edit_button.getElement().getStyle().setRight(165, Style.Unit.PX);

        } else {

            social_button.getElement().getStyle().setRight(150, Style.Unit.PX);
            account_button.getElement().getStyle().setRight(220, Style.Unit.PX);
            edit_button.getElement().getStyle().setRight(290, Style.Unit.PX);

            if (Window.getClientWidth() > 1120)
                search.getElement().getStyle().setOpacity(1);

        }

        if (Window.getClientHeight() < 640) {

            friends.getElement().getStyle().setOpacity(0);
            friends_titolo.getElement().getStyle().setOpacity(0);

        } else {

            friends.getElement().getStyle().setOpacity(1);
            friends_titolo.getElement().getStyle().setOpacity(1);

        }

        // ridimensiono il layout in base alla dimensione della finestra del
        // browser
        left_panel
                .getElement()
                .getStyle()
                .setHeight(Window.getClientHeight() - vertical_offset,
                        Style.Unit.PX);
        main_panel
                .getElement()
                .getStyle()
                .setHeight(Window.getClientHeight() - vertical_offset,
                        Style.Unit.PX);
        DOM.getElementById("applet-bar")
                .getStyle()
                .setHeight(Window.getClientHeight() - vertical_offset,
                        Style.Unit.PX);

        library_container
                .getElement()
                .getStyle()
                .setProperty("minHeight", 515 - vertical_semioffset,
                        Style.Unit.PX);
        playlist_container
                .getElement()
                .getStyle()
                .setProperty("minHeight", 515 - vertical_semioffset,
                        Style.Unit.PX);

        library_container
                .getElement()
                .getStyle()
                .setHeight(Window.getClientHeight() - (vertical_semioffset),
                        Style.Unit.PX);
        playlist_container
                .getElement()
                .getStyle()
                .setHeight(Window.getClientHeight() - (vertical_semioffset),
                        Style.Unit.PX);

        song_container
                .getElement()
                .getStyle()
                .setProperty("minHeight", 515 - vertical_semioffset,
                        Style.Unit.PX);
        song_container
                .getElement()
                .getStyle()
                .setHeight(Window.getClientHeight() - (vertical_semioffset),
                        Style.Unit.PX);

        playlist_content
                .getElement()
                .getStyle()
                .setHeight(
                        playlist_container.getElement().getClientHeight() - 44,
                        Style.Unit.PX);
        song_contenuto
                .getElement()
                .getStyle()
                .setHeight(song_container.getElement().getClientHeight() - 22,
                        Style.Unit.PX);

        friends.getElement()
                .getStyle()
                .setHeight(
                        (Window.getClientHeight() - vertical_offset - 338)
                                - playlists.getOffsetHeight(), Style.Unit.PX);

        if (friends.getElement().getStyle().getOpacity().equals("0")) {

            playlists
                    .getElement()
                    .getStyle()
                    .setHeight(
                            (Window.getClientHeight() - vertical_offset - 338),
                            Style.Unit.PX);

        }
        if (friends.getOffsetHeight() >= (Window.getClientHeight()
                - vertical_offset - 338) * 0.44) {

            playlists.getElement().getStyle().setProperty("height", "auto");

        }
        if (playlists.getOffsetHeight() > (Window.getClientHeight()
                - vertical_offset - 338) * 0.66) {

            playlists
                    .getElement()
                    .getStyle()
                    .setHeight(
                            (Window.getClientHeight() - vertical_offset - 338) * 0.6,
                            Style.Unit.PX);

        }

        // Imposta la dimensione delle componenti della view in base alla
        // dimensione della finestra del browser quando viene ridimensionata
        Window.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {

                @SuppressWarnings("unused")
                int catalogo_h = main_panel.getOffsetHeight();
                left_panel
                        .getElement()
                        .getStyle()
                        .setHeight(event.getHeight() - vertical_offset,
                                Style.Unit.PX);
                main_panel
                        .getElement()
                        .getStyle()
                        .setHeight(event.getHeight() - vertical_offset,
                                Style.Unit.PX);
                DOM.getElementById("applet-bar")
                        .getStyle()
                        .setHeight(event.getHeight() - vertical_offset,
                                Style.Unit.PX);

                library_container
                        .getElement()
                        .getStyle()
                        .setProperty("minHeight", 515 - vertical_semioffset,
                                Style.Unit.PX);
                playlist_container
                        .getElement()
                        .getStyle()
                        .setProperty("minHeight", 515 - vertical_semioffset,
                                Style.Unit.PX);
                song_container
                        .getElement()
                        .getStyle()
                        .setProperty("minHeight", 515 - vertical_semioffset,
                                Style.Unit.PX);

                library_container
                        .getElement()
                        .getStyle()
                        .setHeight(event.getHeight() - (vertical_semioffset),
                                Style.Unit.PX);

                playlist_container
                        .getElement()
                        .getStyle()
                        .setHeight(event.getHeight() - (vertical_semioffset),
                                Style.Unit.PX);
                song_container
                        .getElement()
                        .getStyle()
                        .setHeight(event.getHeight() - (vertical_semioffset),
                                Style.Unit.PX);

                playlist_content
                        .getElement()
                        .getStyle()
                        .setHeight(
                                playlist_container.getElement()
                                        .getClientHeight() - 44, Style.Unit.PX);
                song_contenuto
                        .getElement()
                        .getStyle()
                        .setHeight(
                                song_container.getElement().getClientHeight() - 22,
                                Style.Unit.PX);

                friends.getElement()
                        .getStyle()
                        .setHeight(
                                (Window.getClientHeight() - vertical_offset - 338)
                                        - playlists.getOffsetHeight(),
                                Style.Unit.PX);

                if (friends.getElement().getStyle().getOpacity().equals("0")) {

                    playlists
                            .getElement()
                            .getStyle()
                            .setHeight(
                                    (Window.getClientHeight() - vertical_offset - 338),
                                    Style.Unit.PX);

                }
                if (friends.getOffsetHeight() >= (Window.getClientHeight()
                        - vertical_offset - 338) * 0.44) {

                    playlists.getElement().getStyle()
                            .setProperty("height", "auto");

                }
                if (playlists.getOffsetHeight() > (Window.getClientHeight()
                        - vertical_offset - 338) * 0.66) {

                    playlists
                            .getElement()
                            .getStyle()
                            .setHeight(
                                    (Window.getClientHeight() - vertical_offset - 338) * 0.6,
                                    Style.Unit.PX);

                }

                if (event.getWidth() < 1190) {

                    search.getElement().getStyle().setOpacity(0);
                    social_button.getElement().getStyle()
                            .setRight(25, Style.Unit.PX);
                    account_button.getElement().getStyle()
                            .setRight(95, Style.Unit.PX);
                    edit_button.getElement().getStyle()
                            .setRight(165, Style.Unit.PX);

                } else {

                    social_button.getElement().getStyle()
                            .setRight(150, Style.Unit.PX);
                    account_button.getElement().getStyle()
                            .setRight(220, Style.Unit.PX);
                    edit_button.getElement().getStyle()
                            .setRight(290, Style.Unit.PX);

                    Timer timerSearch = new Timer() {
                        public void run() {

                            if (Window.getClientWidth() > 1120)
                                search.getElement().getStyle().setOpacity(1);

                        }
                    };
                    timerSearch.schedule(300);

                }

                if (event.getHeight() < 640) {

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
    public void setName(String profileName) {
        this.name = profileName;
    }

    @Override
    public void setNumeroBrani(int numero) {
        n_songs.setText(String.valueOf(numero));
    }

    @Override
    public void setPresenter(Presenter listener) {
        this.listener = listener;
    }

    @Override
    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public void setSongFields(String autore, String titolo, String album,
            String genere, String anno, String compositore, String traccia,
            String cover) {

        song_title.setText(titolo);
        song_artist.setText(autore);
        song_album.setText(album);
        song_genre.setText(genere);
        song_year.setText(anno);
        song_composer.setText(compositore);
        song_track.setText(traccia);

        if (!cover.equals("")) {
            song_cover.setUrl(cover);
            if (youtube_status != 1 && youtube_status != 2)
                this.cover.setUrl(cover);
        } else {
            song_cover.setUrl("images/test_cover.jpg");
            if (youtube_status != 1 && youtube_status != 2)
                this.cover.setUrl("images/test_cover.jpg");
        }

    }

    @Override
    public void setStats(String num, String pref, String prefs, String prefn) {

        stat_tracks.setText(num);
        stat_preferred.setText(pref);
        stat_preferreds.setText(prefs);
        stat_preferredn.setText(prefn);

        statistics.getElement().getStyle().setDisplay(Style.Display.BLOCK);

        stat_close.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {

                statistics.getElement().getStyle()
                        .setDisplay(Style.Display.NONE);

            }
        });

    }

    @Override
    public void setUser(String username) {
        user.setText(username);

        Timer timersd = new Timer() {
            public void run() {

                for (int k = 12; k > 0; k--) {

                    if (user.getElement().getClientWidth() <= 160) {
                        user.getElement().getStyle().setTop(45, Style.Unit.PX);
                        user.getElement().getStyle().setLeft(10, Style.Unit.PX);
                        user.getElement().getStyle().setOpacity(1);
                        user.getElement().getStyle().setOpacity(1);
                        user.getElement().getStyle().setOpacity(1);
                        user.getElement().getStyle().setOpacity(1);
                        break;
                    }

                    else {
                        user.getElement().getStyle()
                                .setFontSize(k, Style.Unit.PX);
                    }
                }

            }
        };
        timersd.schedule(600);

    }

    public void showCatalogo() {

        library.setVisible(true);
        covers_container.setVisible(false);

    }

    @Override
    public void showEditProfile(String nickname, String name, String surname,
            String nationality, String gender, String aboutme) {

        password = "";
        cpassword = "";

        edit_profile_user.setText(user.getText());
        edit_profile_check_img.getElement().getStyle().setOpacity(0);
        edit_profile_check.getElement().getStyle().setOpacity(0.5);
        edit_profile_check.setEnabled(false);

        edit_profile_nickname.setText(nickname);
        edit_profile_name.setText(name);
        edit_profile_surname.setText(surname);
        country.setValue(nationality);
        // edit_profile_nationality.setText(nationality);
        // edit_profile_gender.setText(gender);
        if (gender.equals("m"))
            male.setValue(true);
        else
            female.setValue(true);

        edit_profile_aboutme.getElement().setInnerHTML(aboutme);

        edit_profile_vc.setVisible(false);

        edit_profile_password.setTitle("password");
        edit_profile_cpassword.setTitle("cpassword");

        edit_profile_password
                .addClickHandler(MyClickEditProfilePasswordHandler);
        edit_profile_cpassword
                .addClickHandler(MyClickEditProfilePasswordHandler);
        edit_profile_nickname.addClickHandler(MyClickEditProfileHandler);
        edit_profile_name.addClickHandler(MyClickEditProfileHandler);
        edit_profile_surname.addClickHandler(MyClickEditProfileHandler);
        // edit_profile_nationality.addClickHandler(MyClickEditProfileHandler);
        // edit_profile_gender.addClickHandler(MyClickEditProfileHandler);
        edit_profile_aboutme.addDomHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {

                final HTMLPanel about = (HTMLPanel) event.getSource();

                final TextArea nome = new TextArea();
                nome.getElement().getStyle().setWidth(575, Style.Unit.PX);
                nome.getElement().getStyle().setHeight(115, Style.Unit.PX);
                nome.getElement().getStyle().setProperty("resize", "none");
                nome.setValue(about.getElement().getInnerHTML());
                ((VerticalPanel) about.getParent()).add(nome);
                nome.setFocus(true);
                about.setVisible(false);

                nome.addKeyDownHandler(new KeyDownHandler() {

                    @Override
                    public void onKeyDown(KeyDownEvent event) {

                        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {

                            about.getElement().setInnerHTML(nome.getValue());
                            nome.removeFromParent();
                            about.setVisible(true);
                            if (password.equals(cpassword)) {
                                edit_profile_check.getElement().getStyle()
                                        .setOpacity(1);
                                edit_profile_check.setEnabled(true);
                                edit_profile_check_img.getElement().getStyle()
                                        .setOpacity(1);
                            }
                        }
                    }
                });

                nome.addBlurHandler(new BlurHandler() {

                    @Override
                    public void onBlur(BlurEvent event) {

                        about.getElement().setInnerHTML(nome.getValue());
                        nome.removeFromParent();
                        about.setVisible(true);
                        if (password.equals(cpassword)) {
                            edit_profile_check.getElement().getStyle()
                                    .setOpacity(1);
                            edit_profile_check.setEnabled(true);
                            edit_profile_check_img.getElement().getStyle()
                                    .setOpacity(1);
                        }
                    }
                });

            }
        }, ClickEvent.getType());
        // addClickHandler(MyClickEditProfileHandler);

        Element countr = (Element) country_container.getElement().getChild(0);

        DOM.setEventListener(countr, new EventListener() {

            @Override
            public void onBrowserEvent(Event event) {

                if (password.equals(cpassword)) {
                    edit_profile_check.getElement().getStyle().setOpacity(1);
                    edit_profile_check.setEnabled(true);
                    edit_profile_check_img.getElement().getStyle()
                            .setOpacity(1);
                }

            }
        });

        DOM.sinkEvents(countr, Event.ONCLICK);

        male.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {

                if (password.equals(cpassword)) {
                    edit_profile_check.getElement().getStyle().setOpacity(1);
                    edit_profile_check.setEnabled(true);
                    edit_profile_check_img.getElement().getStyle()
                            .setOpacity(1);
                }
            }
        });

        female.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {

                if (password.equals(cpassword)) {
                    edit_profile_check.getElement().getStyle().setOpacity(1);
                    edit_profile_check.setEnabled(true);
                    edit_profile_check_img.getElement().getStyle()
                            .setOpacity(1);
                }
            }
        });

    }

    @Override
    public void showError(String text_t) {

        final HorizontalPanel popup_text = new HorizontalPanel();
        popup_text.getElement().getStyle().setWidth(240, Style.Unit.PX);

        final Label text = error_text;
        text.setText(text_t);
        text.getElement().getStyle().setWidth(240, Style.Unit.PX);
        text.getElement().getStyle().setProperty("textAlign", "left");

        popup_text.add(text);
        popup_text.getElement().getStyle().setProperty("fontFamily", "Arial");
        popup_text.getElement().getStyle().setFontSize(10, Style.Unit.PX);
        popup_text.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        popup_text.getElement().getStyle().setColor("#FF0000");
        popup_text.getElement().getStyle().setProperty("textAlign", "left");

        popup.add(popup_text);

        popup_yes.setText("ok");

        if (rm_popyes != null)
            rm_popyes.removeHandler();

        rm_popyes = popup_yes.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                popup.remove(popup_text);
                popup.setVisible(false);
                popup_yes.setVisible(false);
                popup_no.setVisible(false);

            }
        });

        popup.setVisible(true);
        popup_yes.setVisible(true);

    }

    @Override
    public void showErrorFast(String text_t) {

        popup_fast.getElement().getStyle().setDisplay(Style.Display.BLOCK);

        final HorizontalPanel popup_text = new HorizontalPanel();
        popup_text.getElement().getStyle().setWidth(240, Style.Unit.PX);
        popup_text.getElement().getStyle().setMarginTop(20, Style.Unit.PX);
        popup_text.getElement().getStyle().setProperty("textAlign", "center");

        final Label text = error_text;
        text.setText(text_t);
        text.getElement().getStyle().setWidth(240, Style.Unit.PX);
        text.getElement().getStyle().setProperty("textAlign", "center");

        popup_text.add(text);
        popup_text.getElement().getStyle().setProperty("fontFamily", "Arial");
        popup_text.getElement().getStyle().setFontSize(10, Style.Unit.PX);
        popup_text.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        popup_text.getElement().getStyle().setColor("#FF0000");
        popup_text.getElement().getStyle().setProperty("textAlign", "left");

        popup_fast.add(popup_text);

        Timer timerPopup1 = new Timer() {
            public void run() {

                Timer timerPopup2 = new Timer() {
                    public void run() {

                        Timer timerPopup3 = new Timer() {
                            public void run() {

                                popup_fast.remove(popup_text);
                                popup_fast.getElement().getStyle()
                                        .setDisplay(Style.Display.NONE);

                            }
                        };
                        timerPopup3.schedule(300);

                        popup_fast.getElement().getStyle().setOpacity(0);

                    }
                };
                timerPopup2.schedule(1800);
                popup_fast.getElement().getStyle().setOpacity(1);
            }
        };
        timerPopup1.schedule(10);

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

    // Ordina per artista (ordinamento lessicografico) le canzoni del catalogo
    @Override
    public void sortCatalogo() {

        List<Song> test = dataProvider_catalogo.getList();

        Collections.sort(test, new Comparator<Song>() {
            @Override
            public int compare(Song arg0, Song arg1) {
                String artist_1 = arg0.autore;
                String artist_2 = arg1.autore;
                String album_1 = arg0.album;
                String album_2 = arg1.album;
                int compare_artists = artist_1.compareToIgnoreCase(artist_2);

                if (compare_artists == 0) {
                    return album_1.compareToIgnoreCase(album_2);
                } else {
                    return compare_artists;
                }
            }
        });

    }

    @Override
    public void startLoading() {
        loading.setVisible(true);

    }

    @Override
    public void stopLoading() {
        loading.setVisible(false);

    }

    @Override
    public void viewPlaylist(String titolo) {

        if (titolo.lastIndexOf('(') > 0) {
            titolo = titolo.substring(0, titolo.lastIndexOf('(') - 1);
            titolo = titolo.trim();
        }
        final String titolo_pulito = titolo;

        flag_album = false;
        delete_playlist.setVisible(true);
        remove_song_from_playlist.setVisible(true);
        remove_song_from_playlist.setVisible(true);
        add_song_to_playlist.setVisible(true);
        remove_song.setVisible(true);
        insert_song.setVisible(true);
        delete_playlist.setVisible(true);
        up.setVisible(true);
        down.setVisible(true);

        playlist_songs.clear();
        playlist_songs.add(song_list);

        if (song_opened) {

            closeSong();

            Timer timerOpen = new Timer() {
                public void run() {

                    playlist_opened = true;

                    listener.setPlaylistSongs(titolo_pulito);

                    playlist_title.setText(titolo_pulito);
                    library_container.getElement().getStyle()
                            .setWidth(70, Style.Unit.PCT);
                    playlist_container.getElement().getStyle()
                            .setWidth(30, Style.Unit.PCT);

                    Timer timerPlaylist = new Timer() {
                        public void run() {
                            playlist_content.getElement().getStyle()
                                    .setOpacity(1);
                        }
                    };

                    timerPlaylist.schedule(400);

                }
            };

            timerOpen.schedule(600);

        } else {

            playlist_opened = true;

            listener.setPlaylistSongs(titolo_pulito);

            playlist_title.setText(titolo_pulito);
            library_container.getElement().getStyle()
                    .setWidth(70, Style.Unit.PCT);
            playlist_container.getElement().getStyle()
                    .setWidth(30, Style.Unit.PCT);

            Timer timerPlaylist = new Timer() {
                public void run() {
                    playlist_content.getElement().getStyle().setOpacity(1);
                }
            };

            timerPlaylist.schedule(400);

        }

    }

    @Override
    public void viewSong(final it.unipd.netmus.client.ui.ProfileView.Song song) {

        if (playlist_opened) {

            closePlaylist();

            Timer timerOpen = new Timer() {
                public void run() {

                    song_opened = true;

                    Song s = (Song) song;
                    track_title.setText(s.titolo);

                    library_container.getElement().getStyle()
                            .setWidth(70, Style.Unit.PCT);
                    song_container.getElement().getStyle()
                            .setWidth(30, Style.Unit.PCT);

                    Timer timerPlaylist = new Timer() {
                        public void run() {
                            song_contenuto.getElement().getStyle()
                                    .setOpacity(1);
                            song_cover.getElement().getStyle().setOpacity(1);
                        }
                    };

                    timerPlaylist.schedule(400);

                }
            };

            timerOpen.schedule(600);

        } else {

            song_opened = true;

            Song s = (Song) song;
            track_title.setText(s.titolo);

            library_container.getElement().getStyle()
                    .setWidth(70, Style.Unit.PCT);
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

    public void youTubeChange(int s) {

        if (s == 2) {
            youtube_status = -1;
            play.setUrl("images/play.png");
        }

        else if (s == 1) {
            if (cover_playing.getUrl() != "")
                paintMainCover(cover_playing.getUrl());
            youtube_status = 1;
            if (this.selected_song.equals(this.played_song))
                play.setUrl("images/pause.png");
            setFwRw();

        } else if (s == 0) {

            if (playing == 0) {

                Song to_play = dataProvider_catalogo.getList().get(
                        dataProvider_catalogo.getList().indexOf(
                                (Song) played_song) + 1);

                // closeYouTube();
                youtube_status = 1;
                played_song = to_play;
                playing = 0;
                listener.playYouTube(to_play.autore, to_play.titolo,
                        to_play.album);
                setFwRw();

            } else if (playing == 1) {

                Song to_play = player_playlist.get(player_playlist
                        .indexOf((Song) played_song) + 1);

                // closeYouTube();
                youtube_status = 1;
                played_song = to_play;
                playing = 1;
                listener.playYouTube(to_play.autore, to_play.titolo,
                        to_play.album);

            }

        }

    }

    public void youTubeError(int s) {

        Window.alert("" + s);
        playNext();

    }

    public native void youTubeErrorListener() /*-{

		var _this = this;

		$wnd.youTubeError = function(s) {
			_this.@it.unipd.netmus.client.ui.ProfileViewImpl::youTubeError(I)(s);
		}

    }-*/;

    public native void youTubeListener() /*-{

		var _this = this;

		$wnd.youTubeChange = function(s) {
			_this.@it.unipd.netmus.client.ui.ProfileViewImpl::youTubeChange(I)(s);
		}

    }-*/;

    @UiHandler("search_box")
    void handleChangeValueSearchBox(KeyUpEvent e) {

        if (searcht != null)
            searcht.cancel();

        dataProvider_catalogo.getList().removeAll(
                dataProvider_catalogo.getList());

        if (!((TextBox) e.getSource()).getValue().equals("")) {
            List<Song> canzoni_ricerca = new ArrayList<Song>();
            for (int j = 0; j < canzoni_catalogo.size(); j++) {

                if ((canzoni_catalogo.get(j).titolo.toLowerCase())
                        .indexOf(((TextBox) e.getSource()).getValue()
                                .toLowerCase()) >= 0
                        || (canzoni_catalogo.get(j).autore.toLowerCase())
                                .indexOf(((TextBox) e.getSource()).getValue()
                                        .toLowerCase()) >= 0
                        || (canzoni_catalogo.get(j).album.toLowerCase())
                                .indexOf(((TextBox) e.getSource()).getValue()
                                        .toLowerCase()) >= 0) {

                    canzoni_ricerca.add(canzoni_catalogo.get(j));

                }

            }

            for (Song song : canzoni_ricerca) {
                dataProvider_catalogo.getList().add(song);
            }

            searcht = new Timer() {
                public void run() {
                    if (covers_container.isVisible()
                            && !canzoni_cover.equals(dataProvider_catalogo
                                    .getList())) {
                        List<Song> lista = new ArrayList<Song>();
                        List<String> generi = new ArrayList<String>();
                        for (Song song : dataProvider_catalogo.getList()) {

                            if (generi.indexOf(song.album) == -1) {

                                lista.add(song);
                                generi.add(song.album);

                            }

                        }

                        Collections.sort(lista);

                        paintCovers(lista);
                        // paintCovers(dataProvider_catalogo.getList());
                    }
                }
            };
            searcht.schedule(800);

        } else {

            for (Song song : canzoni_catalogo) {
                dataProvider_catalogo.getList().add(song);
            }

            searcht = new Timer() {
                public void run() {
                    if (covers_container.isVisible()
                            && !canzoni_cover.equals(dataProvider_catalogo
                                    .getList())) {
                        List<Song> lista = new ArrayList<Song>();
                        List<String> generi = new ArrayList<String>();
                        for (Song song : dataProvider_catalogo.getList()) {

                            if (generi.indexOf(song.album) == -1) {

                                lista.add(song);
                                generi.add(song.album);

                            }

                        }

                        Collections.sort(lista);

                        paintCovers(lista);
                        // paintCovers(dataProvider_catalogo.getList());
                    }
                }
            };
            searcht.schedule(800);
        }

    }

    @UiHandler("add_song_to_playlist")
    void handleClickAggiungiBranoPlaylist(ClickEvent e) {
        listener.addToPLaylist(playlist_title.getText(), selected_song.autore,
                selected_song.titolo, selected_song.album);
    }

    @UiHandler("add_playlist")
    void handleClickAggiungiPlaylist(ClickEvent e) {

        addPlaylist();

    }

    @UiHandler("close_playlist")
    void handleClickChiudiPlaylist(ClickEvent e) {
        closePlaylist();

        Timer playclose = new Timer() {
            public void run() {

                flag_album = false;
                delete_playlist.setVisible(true);
                remove_song_from_playlist.setVisible(true);
                remove_song_from_playlist.setVisible(true);
                add_song_to_playlist.setVisible(true);
                remove_song.setVisible(true);
                insert_song.setVisible(true);
                delete_playlist.setVisible(true);
                up.setVisible(true);
                down.setVisible(true);

                playlist_songs.clear();
                playlist_songs.add(song_list);

            }
        };
        playclose.schedule(400);
    }

    @UiHandler("close_song")
    void handleClickChiudiSong(ClickEvent e) {
        closeSong();
    }

    @UiHandler("close_youtube")
    void handleClickChiudiYoutube(ClickEvent e) {
        closeYouTube();
    }

    @UiHandler("edit_button")
    void handleClickEditButton(ClickEvent e) {
        if (selected_song != null && !covers_container.isVisible())
            viewSong(selected_song);
    }

    @UiHandler("delete_playlist")
    void handleClickEliminaPlaylist(ClickEvent e) {

        final HorizontalPanel popup_text = new HorizontalPanel();
        popup_text.getElement().getStyle().setWidth(240, Style.Unit.PX);

        final Label text = new Label();
        text.setText(myConstants.confirmDelete() + "\n\""
                + playlist_title.getText() + " \"?");
        text.getElement().getStyle().setWidth(240, Style.Unit.PX);
        text.getElement().getStyle().setProperty("textAlign", "left");

        popup_text.add(text);
        popup_text.getElement().getStyle().setProperty("fontFamily", "Arial");
        popup_text.getElement().getStyle().setFontSize(10, Style.Unit.PX);
        popup_text.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        popup_text.getElement().getStyle().setColor("#FF0000");
        popup_text.getElement().getStyle().setProperty("textAlign", "left");

        popup.add(popup_text);

        popup_yes.setText(myConstants.yes());
        popup_no.setText(myConstants.no());

        if (rm_popyes != null)
            rm_popyes.removeHandler();
        if (rm_popno != null)
            rm_popno.removeHandler();

        rm_popyes = popup_yes.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                listener.deletePlaylist(playlist_title.getText());
                closePlaylist();
                popup.remove(popup_text);
                popup.setVisible(false);
                popup_yes.setVisible(false);
                popup_no.setVisible(false);

            }
        });

        rm_popno = popup_no.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                popup.remove(popup_text);
                popup.setVisible(false);
                popup_yes.setVisible(false);
                popup_no.setVisible(false);

            }
        });

        popup.setVisible(true);
        popup_yes.setVisible(true);
        popup_no.setVisible(true);

    }

    /*
     * @UiHandler("back") void handleClickBack(ClickEvent e) { listener.goTo(new
     * LoginPlace(name)); }
     */
    @UiHandler("logout")
    void handleClickLogout(ClickEvent e) {
        closeSong();
        closePlaylist();
        listener.logout();
    }

    @UiHandler("play")
    void handleClickPlay(ClickEvent e) {

        if (youtube_status == 0 && last_selected == 0) {
            if (selected_song != null) {
                play.setUrl("images/pause.png");
                played_song = selected_song;
                playing = 0;
                listener.playYouTube(selected_song.autore,
                        selected_song.titolo, selected_song.album);
                setFwRw();
            }
        }

        else if (youtube_status == 0 && last_selected == 1) {
            if (selected_song_playlist != null) {
                play.setUrl("images/pause.png");
                played_song = selected_song_playlist;
                playing = 1;
                player_playlist = new ArrayList<Song>(canzoni_playlist);
                listener.playYouTube(selected_song_playlist.autore,
                        selected_song_playlist.titolo,
                        selected_song_playlist.album);
                setFwRw();
            }
        }

        else if (youtube_status == 1) {
            pausePlayer();
            youtube_status = -1;
            play.setUrl("images/play.png");
        }

        else if (youtube_status == -1) {
            playPlayer();
            youtube_status = 1;
            play.setUrl("images/pause.png");
            paintMainCover(cover_playing.getUrl());
        }

        else if ((youtube_status == 2 || youtube_status == 3)
                && last_selected == 0) {
            // closeYouTube();
            youtube_status = 1;
            play.setUrl("images/pause.png");
            played_song = selected_song;
            playing = 0;
            listener.playYouTube(selected_song.autore, selected_song.titolo,
                    selected_song.album);
            setFwRw();
        }

        else if ((youtube_status == 2 || youtube_status == 3)
                && last_selected == 1) {
            // closeYouTube();
            youtube_status = 1;
            play.setUrl("images/pause.png");
            played_song = selected_song_playlist;
            playing = 1;
            player_playlist = new ArrayList<Song>(canzoni_playlist);
            listener.playYouTube(selected_song_playlist.autore,
                    selected_song_playlist.titolo, selected_song_playlist.album);
            setFwRw();
        }

    }

    @UiHandler("play_youtube")
    void handleClickPlayYoutube(ClickEvent e) {
        if (youtube_status == 0 && last_selected == 0) {
            if (selected_song != null) {
                play.setUrl("images/pause.png");
                played_song = selected_song;
                playing = 0;
                listener.playYouTube(selected_song.autore,
                        selected_song.titolo, selected_song.album);
                setFwRw();
            }
        }

        else if (youtube_status == 0 && last_selected == 1) {
            if (selected_song_playlist != null) {
                play.setUrl("images/pause.png");
                played_song = selected_song_playlist;
                playing = 1;
                player_playlist = new ArrayList<Song>(canzoni_playlist);
                listener.playYouTube(selected_song_playlist.autore,
                        selected_song_playlist.titolo,
                        selected_song_playlist.album);
                setFwRw();
            }
        }

        else if (youtube_status == 1) {
            pausePlayer();
            youtube_status = -1;
            play.setUrl("images/play.png");
        }

        else if (youtube_status == -1) {
            playPlayer();
            youtube_status = 1;
            play.setUrl("images/pause.png");
        }

        else if ((youtube_status == 2 || youtube_status == 3)
                && last_selected == 0) {
            // closeYouTube();
            youtube_status = 1;
            play.setUrl("images/pause.png");
            played_song = selected_song;
            playing = 0;
            listener.playYouTube(selected_song.autore, selected_song.titolo,
                    selected_song.album);
            setFwRw();
        }

        else if ((youtube_status == 2 || youtube_status == 3)
                && last_selected == 1) {
            // closeYouTube();
            youtube_status = 1;
            play.setUrl("images/pause.png");
            played_song = selected_song_playlist;
            playing = 1;
            player_playlist = new ArrayList<Song>(canzoni_playlist);
            listener.playYouTube(selected_song_playlist.autore,
                    selected_song_playlist.titolo, selected_song_playlist.album);
            setFwRw();
        }

    }

    @UiHandler("remove_song_from_playlist")
    void handleClickRimuoviBranoPlaylist(ClickEvent e) {
        listener.removeFromPLaylist(playlist_title.getText(),
                selected_song_playlist.autore, selected_song_playlist.titolo,
                selected_song_playlist.album);
    }

    @UiHandler("social_button")
    void handleClickSocialButton(ClickEvent e) {
        if (selected_song != null || selected_album != null) {
            closeSong();

            playlist_opened = true;

            // listener.setPlaylistSongs(canzone.album);
            if (covers_container.isVisible() && selected_album != null) {

                fillAlbum(selected_album);
                playlist_title.setText(selected_album);

            } else {

                fillAlbum(selected_song.album);
                playlist_title.setText(selected_song.album);

            }

            library_container.getElement().getStyle()
                    .setWidth(70, Style.Unit.PCT);
            playlist_container.getElement().getStyle()
                    .setWidth(30, Style.Unit.PCT);

            Timer timerAlbum = new Timer() {
                public void run() {
                    playlist_content.getElement().getStyle().setOpacity(1);
                }
            };
            timerAlbum.schedule(400);
        }
    }

    @UiHandler("star1")
    void handleClickStar1(ClickEvent e) {
        if (this.selected_song != null)
            listener.rateSong(this.selected_song.autore,
                    this.selected_song.titolo, this.selected_song.album, 1);

    }

    @UiHandler("star2")
    void handleClickStar2(ClickEvent e) {
        if (this.selected_song != null)
            listener.rateSong(this.selected_song.autore,
                    this.selected_song.titolo, this.selected_song.album, 2);

    }

    @UiHandler("star3")
    void handleClickStar3(ClickEvent e) {
        if (this.selected_song != null)
            listener.rateSong(this.selected_song.autore,
                    this.selected_song.titolo, this.selected_song.album, 3);

    }

    @UiHandler("star4")
    void handleClickStar4(ClickEvent e) {
        if (this.selected_song != null)
            listener.rateSong(this.selected_song.autore,
                    this.selected_song.titolo, this.selected_song.album, 4);

    }

    @UiHandler("star5")
    void handleClickStar5(ClickEvent e) {
        if (this.selected_song != null)
            listener.rateSong(this.selected_song.autore,
                    this.selected_song.titolo, this.selected_song.album, 5);

    }

    @UiHandler("account_button")
    void handleMouseAccountButton(ClickEvent e) {
        edit_profile.getElement().getStyle().setDisplay(Style.Display.BLOCK);
        edit_profile.getElement().getStyle().setOpacity(1);
    }

    @UiHandler("down")
    void handleMouseClickDown(ClickEvent e) {
        listener.moveDownInPLaylist(playlist_title.getText(),
                selected_song_playlist.autore, selected_song_playlist.titolo,
                selected_song_playlist.album);
    }

    @UiHandler("delete_song")
    void handleMouseClickEliminaSong(ClickEvent e) {

        final HorizontalPanel popup_text = new HorizontalPanel();
        popup_text.getElement().getStyle().setWidth(240, Style.Unit.PX);

        final Label text = new Label();
        text.setText(myConstants.confirmDelete() + "\n\""
                + selected_song.titolo + " \"?");
        text.getElement().getStyle().setWidth(240, Style.Unit.PX);
        text.getElement().getStyle().setProperty("textAlign", "left");

        popup_text.add(text);
        popup_text.getElement().getStyle().setProperty("fontFamily", "Arial");
        popup_text.getElement().getStyle().setFontSize(10, Style.Unit.PX);
        popup_text.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        popup_text.getElement().getStyle().setColor("#FF0000");
        popup_text.getElement().getStyle().setProperty("textAlign", "left");

        popup.add(popup_text);

        popup_yes.setText(myConstants.yes());
        popup_no.setText(myConstants.no());

        if (rm_popyes != null)
            rm_popyes.removeHandler();
        if (rm_popno != null)
            rm_popno.removeHandler();

        rm_popyes = popup_yes.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                listener.deleteSong(selected_song.autore, selected_song.titolo,
                        selected_song.album);
                popup.remove(popup_text);
                popup.setVisible(false);
                popup_yes.setVisible(false);
                popup_no.setVisible(false);

            }
        });

        rm_popno = popup_no.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                popup.remove(popup_text);
                popup.setVisible(false);
                popup_yes.setVisible(false);
                popup_no.setVisible(false);

            }
        });

        popup.setVisible(true);
        popup_yes.setVisible(true);
        popup_no.setVisible(true);

    }

    @UiHandler("flag_eng")
    void handleMouseClickFlagEngOpen(ClickEvent e) {
        listener.changeLanguage("en");
    }

    @UiHandler("flag_ita")
    void handleMouseClickFlagItaOpen(ClickEvent e) {
        listener.changeLanguage("it");
    }

    @UiHandler("help")
    void handleMouseClickHelp(ClickEvent e) {
        showHelp();
    }

    @UiHandler("help_exit")
    void handleMouseClickHelpExit(ClickEvent e) {
        hideHelp();
    }

    @UiHandler("pdf")
    void handleMouseClickPdf(ClickEvent e) {
        listener.exportDocLibrary();
    }

    @UiHandler("statistics_open")
    void handleMouseClickStatisticsOpen(ClickEvent e) {
        if (!n_songs.getText().equals("0"))
            listener.setStats();
    }

    @UiHandler("switch_cover")
    void handleMouseClickSwitchCover(ClickEvent e) {
        switch_list.setUrl("images/switch_sx_off.png");
        switch_cover.setUrl("images/switch_dx_on.png");
        if (!covers_container.isVisible()) {

            if (song_opened)
                closeSong();

            List<Song> lista = new ArrayList<Song>();
            List<String> generi = new ArrayList<String>();
            for (Song song : dataProvider_catalogo.getList()) {

                if (generi.indexOf(song.album) == -1) {

                    lista.add(song);
                    generi.add(song.album);

                }

            }

            Collections.sort(lista);

            paintCovers(lista);

        }

    }

    @UiHandler("switch_list")
    void handleMouseClickSwitchList(ClickEvent e) {
        switch_list.setUrl("images/switch_sx_on.png");
        switch_cover.setUrl("images/switch_dx_off.png");
        showCatalogo();

    }

    @UiHandler("up")
    void handleMouseClickUp(ClickEvent e) {
        listener.moveUpInPLaylist(playlist_title.getText(),
                selected_song_playlist.autore, selected_song_playlist.titolo,
                selected_song_playlist.album);
    }

    @UiHandler("edit_profile_check")
    void handleMouseEditProfileCheck(ClickEvent e) {

        String gender = "f";

        if (male.getValue())
            gender = "m";

        listener.editProfile(edit_profile_user.getText(),
                edit_profile_nickname.getText(), edit_profile_name.getText(),
                edit_profile_surname.getText(), gender, country.getValue(),
                edit_profile_aboutme.getElement().getInnerHTML(), password);

    }

    // /////////////////////////////////////////////////////
    // /////////////////////////////////////////////////////

    @UiHandler("edit_profile_close")
    void handleMouseEditProfileChiudi(ClickEvent e) {
        closeEditProfile();
    }

    @UiHandler("edit_profile_del")
    void handleMouseEditProfileClear(ClickEvent e) {

        final HorizontalPanel popup_text = new HorizontalPanel();
        popup_text.getElement().getStyle().setWidth(240, Style.Unit.PX);

        final Label text = new Label();
        text.setText(myConstants.confirmDeleteProfile());
        text.getElement().getStyle().setWidth(240, Style.Unit.PX);
        text.getElement().getStyle().setProperty("textAlign", "left");

        popup_text.add(text);
        popup_text.getElement().getStyle().setProperty("fontFamily", "Arial");
        popup_text.getElement().getStyle().setFontSize(10, Style.Unit.PX);
        popup_text.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        popup_text.getElement().getStyle().setColor("#FF0000");
        popup_text.getElement().getStyle().setProperty("textAlign", "left");

        popup.add(popup_text);

        popup_yes.setText(myConstants.yes());
        popup_no.setText(myConstants.no());

        if (rm_popyes != null)
            rm_popyes.removeHandler();
        if (rm_popno != null)
            rm_popno.removeHandler();

        rm_popyes = popup_yes.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {

                listener.deleteProfile();
                popup.remove(popup_text);
                popup.setVisible(false);
                popup_yes.setVisible(false);
                popup_no.setVisible(false);

            }
        });

        rm_popno = popup_no.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                popup.remove(popup_text);
                popup.setVisible(false);
                popup_yes.setVisible(false);
                popup_no.setVisible(false);

            }
        });

        popup.setVisible(true);
        popup_yes.setVisible(true);
        popup_no.setVisible(true);

    }

    @UiHandler("account_button")
    void handleMouseOutAccountButton(MouseOutEvent e) {
        account_button.setUrl("images/account_button.png");
    }

    @UiHandler("play_youtube")
    void handleMouseOutChiudiYouTube(MouseOutEvent e) {

    }

    @UiHandler("edit_button")
    void handleMouseOutEditButton(MouseOutEvent e) {
        edit_button.setUrl("images/edit_button.png");
    }

    @UiHandler("info_youtube_link")
    void handleMouseOutInfoYouTubeLink(MouseOutEvent e) {
        info_youtube_link.getElement().getStyle().setColor("#000000");
    }

    @UiHandler("play")
    void handleMouseOutPlay(MouseOutEvent e) {

    }

    @UiHandler("play_youtube")
    void handleMouseOutPlayYouTube(MouseOutEvent e) {
        if (selected_song != null)
            play_youtube.setUrl("images/play.png");
    }

    @UiHandler("social_button")
    void handleMouseOutSocialButton(MouseOutEvent e) {
        social_button.setUrl("images/social_button.png");
    }

    @UiHandler("star1")
    void handleMouseOutStar1(MouseOutEvent e) {
        showStar(this.rating);
    }

    @UiHandler("star2")
    void handleMouseOutStar2(MouseOutEvent e) {
        showStar(this.rating);
    }

    @UiHandler("star3")
    void handleMouseOutStar3(MouseOutEvent e) {
        showStar(this.rating);
    }

    @UiHandler("star4")
    void handleMouseOutStar4(MouseOutEvent e) {
        showStar(this.rating);
    }

    @UiHandler("star5")
    void handleMouseOutStar5(MouseOutEvent e) {
        showStar(this.rating);
    }

    @UiHandler("account_button")
    void handleMouseOverAccountButton(MouseOverEvent e) {
        account_button.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        account_button.setUrl("images/account_button_o.png");
    }

    @UiHandler("add_song_to_playlist")
    void handleMouseOverAggiungiBranoPlaylist(MouseOverEvent e) {
        add_song_to_playlist.getElement().getStyle()
                .setCursor(Style.Cursor.POINTER);
    }

    @UiHandler("add_playlist")
    void handleMouseOverAggiungiPlaylist(MouseOverEvent e) {
        add_playlist.getElement().getStyle().setCursor(Style.Cursor.POINTER);
    }

    @UiHandler("close_playlist")
    void handleMouseOverChiudiPlaylist(MouseOverEvent e) {
        close_playlist.getElement().getStyle().setCursor(Style.Cursor.POINTER);
    }

    @UiHandler("close_song")
    void handleMouseOverChiudiSong(MouseOverEvent e) {
        close_song.getElement().getStyle().setCursor(Style.Cursor.POINTER);
    }

    @UiHandler("play_youtube")
    void handleMouseOverChiudiYoutube(MouseOverEvent e) {
        close_youtube.getElement().getStyle().setCursor(Style.Cursor.POINTER);
    }

    @UiHandler("delete_song")
    void handleMouseOverDeleteSong(MouseOverEvent e) {
        delete_song.getElement().getStyle().setCursor(Style.Cursor.POINTER);
    }

    @UiHandler("edit_button")
    void handleMouseOverEditButton(MouseOverEvent e) {
        if (selected_song != null && !covers_container.isVisible()) {
            edit_button.getElement().getStyle().setCursor(Style.Cursor.POINTER);
            edit_button.setUrl("images/edit_button_o.png");
        } else
            edit_button.getElement().getStyle().setCursor(Style.Cursor.AUTO);
    }

    @UiHandler("edit_profile_close")
    void handleMouseOverEditProfileChiudi(MouseOverEvent e) {
        edit_profile_close.getElement().getStyle()
                .setCursor(Style.Cursor.POINTER);
    }

    @UiHandler("edit_profile_del")
    void handleMouseOverEditProfileClear(MouseOverEvent e) {
        edit_profile_del.getElement().getStyle()
                .setCursor(Style.Cursor.POINTER);
    }

    @UiHandler("edit_profile_cpassword")
    void handleMouseOverEditProfileCPassword(MouseOverEvent e) {
        edit_profile_cpassword.getElement().getStyle()
                .setCursor(Style.Cursor.POINTER);
    }

    @UiHandler("edit_profile_name")
    void handleMouseOverEditProfileName(MouseOverEvent e) {
        edit_profile_name.getElement().getStyle()
                .setCursor(Style.Cursor.POINTER);
    }

    @UiHandler("edit_profile_nickname")
    void handleMouseOverEditProfileNickname(MouseOverEvent e) {
        edit_profile_nickname.getElement().getStyle()
                .setCursor(Style.Cursor.POINTER);
    }

    @UiHandler("edit_profile_password")
    void handleMouseOverEditProfilePassword(MouseOverEvent e) {
        edit_profile_password.getElement().getStyle()
                .setCursor(Style.Cursor.POINTER);
    }

    @UiHandler("edit_profile_surname")
    void handleMouseOverEditProfileSurname(MouseOverEvent e) {
        edit_profile_surname.getElement().getStyle()
                .setCursor(Style.Cursor.POINTER);
    }

    @UiHandler("flag_eng")
    void handleMouseOverFlagEngOver(MouseOverEvent e) {
        flag_eng.getElement().getStyle().setCursor(Style.Cursor.POINTER);
    }

    @UiHandler("flag_ita")
    void handleMouseOverFlagItaOver(MouseOverEvent e) {
        flag_ita.getElement().getStyle().setCursor(Style.Cursor.POINTER);
    }

    @UiHandler("forward")
    void handleMouseOverForward(MouseOverEvent e) {
        forward.getElement().getStyle().setCursor(Style.Cursor.POINTER);
    }

    @UiHandler("info_youtube_link")
    void handleMouseOverInfoYoutubeLink(MouseOverEvent e) {
        info_youtube_link.getElement().getStyle().setColor("#38D12F");
        info_youtube_link.getElement().getStyle()
                .setCursor(Style.Cursor.POINTER);
    }

    @UiHandler("pdf")
    void handleMouseOverPdf(MouseOverEvent e) {
        pdf.getElement().getStyle().setCursor(Style.Cursor.POINTER);
    }

    @UiHandler("play")
    void handleMouseOverPlay(MouseOverEvent e) {
        play.getElement().getStyle().setCursor(Style.Cursor.POINTER);
    }

    @UiHandler("play_youtube")
    void handleMouseOverPlayYoutube(MouseOverEvent e) {
        if (selected_song != null) {
            play_youtube.getElement().getStyle()
                    .setCursor(Style.Cursor.POINTER);
        }
    }

    @UiHandler("rewind")
    void handleMouseOverRewind(MouseOverEvent e) {
        rewind.getElement().getStyle().setCursor(Style.Cursor.POINTER);
    }

    @UiHandler("remove_song_from_playlist")
    void handleMouseOverRimuoviBranoPlaylist(MouseOverEvent e) {
        remove_song_from_playlist.getElement().getStyle()
                .setCursor(Style.Cursor.POINTER);
    }

    @UiHandler("social_button")
    void handleMouseOverSocialButton(MouseOverEvent e) {
        if (selected_song != null || selected_album != null) {
            social_button.getElement().getStyle()
                    .setCursor(Style.Cursor.POINTER);
            social_button.setUrl("images/social_button_o.png");
        }
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
        star5.setUrl("images/starbw.png");
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
        star5.setUrl("images/starbw.png");
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

    @UiHandler("statistics_open")
    void handleMouseOverStatisticsOver(MouseOverEvent e) {
        if (!n_songs.getText().equals("0"))
            statistics_open.getElement().getStyle()
                    .setCursor(Style.Cursor.POINTER);
    }

    @UiHandler("switch_cover")
    void handleMouseOverSwitchCover(MouseOverEvent e) {
        switch_cover.getElement().getStyle().setCursor(Style.Cursor.POINTER);
    }

    @UiHandler("switch_list")
    void handleMouseOverSwitchList(MouseOverEvent e) {
        switch_list.getElement().getStyle().setCursor(Style.Cursor.POINTER);
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
        wrapper.addMouseOverHandler(new MouseOverHandler() {

            @Override
            public void onMouseOver(MouseOverEvent event) {
                event.getRelativeElement().getStyle()
                        .setBackgroundColor("#e0fee1");
                event.getRelativeElement().getStyle()
                        .setCursor(Style.Cursor.POINTER);
            }
        });

        wrapper.addMouseOutHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent event) {
                event.getRelativeElement().getStyle()
                        .setBackgroundColor("#FFFFFF");
            }
        });

        wrapper.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {

            }
        });

        nome.addKeyDownHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {

                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER
                        && !nome.getText().equals("")
                        && !nome.getText().equals(" ")
                        && !nome.getText().equals("  ")) {

                    Label tmpTxt = new Label(nome.getText());
                    tmpTxt.getElement().getStyle()
                            .setMarginLeft(11, Style.Unit.PX);
                    tmpTxt.getElement().getStyle()
                            .setCursor(Style.Cursor.POINTER);
                    tmpTxt.getElement().getStyle().setColor("#999999");
                    tmpTxt.getElement().getStyle()
                            .setProperty("fontFamily", "Verdana");
                    tmpTxt.getElement().getStyle()
                            .setMarginLeft(11, Style.Unit.PX);
                    tmpTxt.getElement().getStyle()
                            .setFontSize(12, Style.Unit.PX);
                    tmpTxt.getElement().getStyle()
                            .setProperty("fontWeight", "bold");

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

    private void fillAlbum(String t) {

        flag_album = true;

        delete_playlist.setVisible(false);
        remove_song_from_playlist.setVisible(false);
        remove_song_from_playlist.setVisible(false);
        add_song_to_playlist.setVisible(false);
        remove_song.setVisible(false);
        insert_song.setVisible(false);
        delete_playlist.setVisible(false);
        up.setVisible(false);
        down.setVisible(false);

        playlist_songs.clear();
        playlist_songs.add(album_list);

        List<Song> test = dataProvider_album.getList();

        test.removeAll(dataProvider_album.getList());

        canzoni_playlist.removeAll(canzoni_playlist);

        for (int j = 0; j < dataProvider_catalogo.getList().size(); j++) {

            if (dataProvider_catalogo.getList().get(j).album.equals(t))
                canzoni_playlist.add(dataProvider_catalogo.getList().get(j));

        }

        Collections.sort(canzoni_playlist, new Comparator<Song>() {
            @Override
            public int compare(Song arg0, Song arg1) {
                String artist_1 = arg0.titolo;
                String artist_2 = arg1.titolo;
                return artist_1.compareToIgnoreCase(artist_2);
            }
        });

        for (Song song : canzoni_playlist) {
            test.add(song);
        }

        if (youtube_status != 0 && this.playing == 1)
            setPlaySong(true);

    }

    private void hideHelp() {

        help_container.setVisible(false);
        DOM.getElementById("s1").getStyle().setDisplay(Style.Display.NONE);
        DOM.getElementById("s2").getStyle().setDisplay(Style.Display.NONE);
        DOM.getElementById("s3").getStyle().setDisplay(Style.Display.NONE);
        DOM.getElementById("s4").getStyle().setDisplay(Style.Display.NONE);
        DOM.getElementById("s5").getStyle().setDisplay(Style.Display.NONE);

    }

    // riempie il catalogo/libreria con la visuale Covers
    private void paintCovers(final List<Song> list) {

        if (list.size() > 0)
            startLoading();

        library.setVisible(false);
        covers_container.setVisible(true);
        covers_container.clear();

        canzoni_cover.remove(canzoni_cover);
        canzoni_cover = new ArrayList<Song>(list);

        Timer timerCovers = new Timer() {
            public void run() {
                paintInsideCover(list);
            }
        };

        timerCovers.schedule(5);

    }

    private void paintInsideCover(List<Song> list) {

        for (final it.unipd.netmus.client.ui.ProfileView.Song song : list) {

            Timer timerCovers = new Timer() {
                public void run() {

                    final Song canzone = (Song) song;

                    HTMLPanel cover_container = new HTMLPanel("");

                    cover_container.getElement().getStyle()
                            .setWidth(108, Style.Unit.PX);
                    cover_container.getElement().getStyle()
                            .setHeight(147, Style.Unit.PX);
                    cover_container.getElement().getStyle()
                            .setOverflow(Style.Overflow.VISIBLE);
                    cover_container.getElement().getStyle()
                            .setMarginLeft(40, Style.Unit.PX);
                    cover_container.getElement().getStyle()
                            .setMarginTop(20, Style.Unit.PX);
                    cover_container.getElement().getStyle()
                            .setFloat(Style.Float.LEFT);
                    cover_container.getElement().getStyle()
                            .setProperty("textAlign", "center");

                    final HTMLPanel tmp = new HTMLPanel("");
                    tmp.getElement().getStyle()
                            .setProperty("backgroundSize", "contain");
                    listener.setSongCover(canzone.autore, canzone.titolo,
                            canzone.album, tmp);

                    tmp.getElement().getStyle()
                            .setMarginBottom(5, Style.Unit.PX);
                    tmp.getElement().getStyle().setWidth(100, Style.Unit.PX);
                    tmp.getElement().getStyle().setHeight(100, Style.Unit.PX);
                    tmp.getElement().getStyle()
                            .setProperty("borderRadius", "10px");
                    tmp.getElement().getStyle()
                            .setProperty("MozBorderRadius", "10px");
                    tmp.getElement()
                            .getStyle()
                            .setProperty("WebkitBoxShadow",
                                    "2px 2px 4px #888888");
                    tmp.getElement().getStyle()
                            .setProperty("MozBoxShadow", "2px 2px 4px #888888");

                    if (canzone.equals(selected_song)) {
                        cover_selected = tmp;
                        cover_selected.getElement().getStyle()
                                .setProperty("border", "2px solid #37A6EB");
                    }

                    tmp.addDomHandler(new MouseOverHandler() {

                        @Override
                        public void onMouseOver(MouseOverEvent event) {
                            ((Widget) event.getSource()).getElement()
                                    .getStyle().setCursor(Style.Cursor.POINTER);

                        }
                    }, MouseOverEvent.getType());

                    tmp.addDomHandler(new ClickHandler() {

                        @Override
                        public void onClick(ClickEvent event) {

                            selected_album = canzone.album;
                            if (cover_selected != null)
                                cover_selected
                                        .getElement()
                                        .getStyle()
                                        .setProperty("border",
                                                "0px solid #37A6EB");

                            cover_selected = tmp;
                            cover_selected.getElement().getStyle()
                                    .setProperty("border", "2px solid #37A6EB");

                            if (playlist_opened == true) {

                                playlist_title.setText(canzone.album);
                                fillAlbum(canzone.album);

                            } /*
                               * setBranoCatalogo(canzone);
                               * 
                               * track_title.setText(selected_song.titolo);
                               * song_title.setText(selected_song.titolo);
                               * song_artist.setText(selected_song.autore);
                               * song_album.setText(selected_song.album);
                               * 
                               * 
                               * 
                               * if(playing==0&&canzone.equals(played_song)&&(
                               * youtube_status==1||youtube_status==2)) {
                               * play.setUrl("images/pause.png"); youtube_status
                               * = 1; last_selected=0; } else
                               * if(playing==0&&canzone
                               * .equals(played_song)&&(youtube_status
                               * ==-1||youtube_status==3)) {
                               * play.setUrl("images/play.png"); youtube_status
                               * = -1; last_selected=0; }
                               * 
                               * else { play.setUrl("images/play.png");
                               * if(youtube_status!=0&&youtube_status==1)
                               * youtube_status = 2; else
                               * if(youtube_status!=0&&youtube_status==-1)
                               * youtube_status = 3; last_selected=0; }
                               * play_youtube.setUrl("images/play.png");
                               * 
                               * global_rating =
                               * listener.setRating(selected_song
                               * .autore,selected_song
                               * .titolo,selected_song.album); showStar(rating);
                               * 
                               * listener.setSongFields(selected_song.autore,
                               * selected_song.titolo, selected_song.album);
                               */
                        }
                    }, ClickEvent.getType());

                    tmp.addDomHandler(new DoubleClickHandler() {

                        @Override
                        public void onDoubleClick(DoubleClickEvent event) {

                            closeSong();

                            playlist_opened = true;

                            // listener.setPlaylistSongs(canzone.album);
                            fillAlbum(canzone.album);

                            playlist_title.setText(canzone.album);
                            library_container.getElement().getStyle()
                                    .setWidth(70, Style.Unit.PCT);
                            playlist_container.getElement().getStyle()
                                    .setWidth(30, Style.Unit.PCT);

                            Timer timerAlbum = new Timer() {
                                public void run() {
                                    playlist_content.getElement().getStyle()
                                            .setOpacity(1);
                                }
                            };
                            timerAlbum.schedule(400);

                        }

                    }, DoubleClickEvent.getType());

                    // Inizializzazione etichette
                    Label titolo = new Label();
                    titolo.setText(canzone.album);
                    Label autore = new Label();
                    autore.setText(canzone.autore);

                    // Ridefinizione etichette per album mancante
                    if (titolo.getText().equals("")) {
                        titolo.setText(myConstants.unknownAlbum());
                        autore.setText("");
                    }

                    // Definizione stili etichette
                    titolo.getElement().getStyle()
                            .setProperty("fontFamily", "Verdana");
                    titolo.getElement().getStyle()
                            .setFontSize(11, Style.Unit.PX);
                    titolo.getElement().getStyle()
                            .setProperty("maxHeight", "29px");
                    titolo.getElement().getStyle()
                            .setOverflow(Style.Overflow.HIDDEN);
                    autore.getElement().getStyle()
                            .setProperty("fontFamily", "Verdana");
                    autore.getElement().getStyle()
                            .setFontSize(10, Style.Unit.PX);
                    autore.getElement().getStyle().setColor("#999999");

                    cover_container.add(tmp);
                    cover_container.add(titolo);
                    cover_container.add(autore);

                    covers_container.add(cover_container);

                }
            };

            timerCovers.schedule(5);

        }
    }

    private void setFwRw() {

        if (playing == 0) {

            if (dataProvider_catalogo.getList().indexOf((played_song)) + 1 < dataProvider_catalogo
                    .getList().size()) {
                if (rm_fw != null)
                    rm_fw.removeHandler();
                forward.setVisible(true);
                forward.setUrl("images/fw.png");
                rm_fw = forward.addClickHandler(new ClickHandler() {

                    @Override
                    public void onClick(ClickEvent event) {

                        playNext();

                    }
                });
            } else {

                forward.setUrl("images/fwoff.png");
                forward.setVisible(false);
            }

            if (dataProvider_catalogo.getList().indexOf(played_song) - 1 >= 0) {
                if (rm_rw != null)
                    rm_rw.removeHandler();
                rewind.setVisible(true);
                rewind.setUrl("images/rw.png");
                rm_rw = rewind.addClickHandler(new ClickHandler() {

                    @Override
                    public void onClick(ClickEvent event) {

                        playPrev();

                    }
                });
            } else {
                rewind.setUrl("images/rwoff.png");
                rewind.setVisible(false);
            }

        } else if (playing == 1) {

            if (player_playlist.indexOf((played_song)) + 1 < player_playlist
                    .size()) {
                if (rm_fw != null)
                    rm_fw.removeHandler();
                forward.setVisible(true);
                forward.setUrl("images/fw.png");
                rm_fw = forward.addClickHandler(new ClickHandler() {

                    @Override
                    public void onClick(ClickEvent event) {

                        playNext();

                    }
                });
            } else {

                forward.setUrl("images/fwoff.png");
                forward.setVisible(false);
            }

            if (player_playlist.indexOf(played_song) - 1 >= 0) {
                if (rm_rw != null)
                    rm_rw.removeHandler();
                rewind.setVisible(true);
                rewind.setUrl("images/rw.png");
                rm_rw = rewind.addClickHandler(new ClickHandler() {

                    @Override
                    public void onClick(ClickEvent event) {

                        playPrev();

                    }
                });
            } else {
                rewind.setUrl("images/rwoff.png");
                rewind.setVisible(false);
            }

        }

    }

    private void setPlaySong(boolean flag) {

        if (played_song != null && playing == 0) {
            for (int j = 0; j < dataProvider_catalogo.getList().size(); j++) {

                if (played_song.equals(dataProvider_catalogo.getList().get(j))
                        && flag) {
                    final int l = j;
                    Timer timerButton = new Timer() {
                        public void run() {
                            library.getRowElement(l).getStyle()
                                    .setColor("#37A6EB");
                            library.getRowElement(l).getStyle()
                                    .setFontWeight(Style.FontWeight.BOLD);

                        }
                    };
                    timerButton.schedule(40);

                } else {

                    library.getRowElement(j).getStyle().setColor("#000000");
                    library.getRowElement(j).getStyle()
                            .setFontWeight(Style.FontWeight.NORMAL);
                }
            }

            for (int l = 0; l < dataProvider_playlist.getList().size(); l++) {

                song_list.getRowElement(l).getStyle().setColor("#000000");
                song_list.getRowElement(l).getStyle()
                        .setFontWeight(Style.FontWeight.NORMAL);

            }

            if (flag_album) {

                for (int l = 0; l < dataProvider_album.getList().size(); l++) {

                    album_list.getRowElement(l).getStyle().setColor("#000000");
                    album_list.getRowElement(l).getStyle()
                            .setFontWeight(Style.FontWeight.NORMAL);

                }

            }

        } else if (played_song != null && playing == 1 && !flag_album) {

            for (int j = 0; j < dataProvider_playlist.getList().size(); j++) {

                if (played_song.equals(dataProvider_playlist.getList().get(j))
                        && flag) {
                    final int l = j;
                    Timer timerButton = new Timer() {
                        public void run() {
                            song_list.getRowElement(l).getStyle()
                                    .setColor("#37A6EB");
                            song_list.getRowElement(l).getStyle()
                                    .setFontWeight(Style.FontWeight.BOLD);
                        }
                    };
                    timerButton.schedule(40);

                } else {

                    song_list.getRowElement(j).getStyle().setColor("#000000");
                    song_list.getRowElement(j).getStyle()
                            .setFontWeight(Style.FontWeight.NORMAL);

                }
            }

            for (int l = 0; l < dataProvider_catalogo.getList().size(); l++) {

                library.getRowElement(l).getStyle().setColor("#000000");
                library.getRowElement(l).getStyle()
                        .setFontWeight(Style.FontWeight.NORMAL);

            }
        } else if (played_song != null && playing == 1 && flag_album) {

            for (int j = 0; j < dataProvider_album.getList().size(); j++) {

                if (played_song.equals(dataProvider_album.getList().get(j))
                        && flag) {
                    final int l = j;
                    Timer timerButton = new Timer() {
                        public void run() {
                            album_list.getRowElement(l).getStyle()
                                    .setColor("#37A6EB");
                            album_list.getRowElement(l).getStyle()
                                    .setFontWeight(Style.FontWeight.BOLD);
                        }
                    };
                    timerButton.schedule(40);

                } else {

                    album_list.getRowElement(j).getStyle().setColor("#000000");
                    album_list.getRowElement(j).getStyle()
                            .setFontWeight(Style.FontWeight.NORMAL);

                }
            }

            for (int l = 0; l < dataProvider_catalogo.getList().size(); l++) {

                library.getRowElement(l).getStyle().setColor("#000000");
                library.getRowElement(l).getStyle()
                        .setFontWeight(Style.FontWeight.NORMAL);

            }
        }

    }

    @Override
    public void showHelp() {

        help_container.setVisible(true);
        DOM.getElementById("s1").getStyle().setDisplay(Style.Display.BLOCK);
        DOM.getElementById("s2").getStyle().setDisplay(Style.Display.BLOCK);
        DOM.getElementById("s3").getStyle().setDisplay(Style.Display.BLOCK);
        DOM.getElementById("s4").getStyle().setDisplay(Style.Display.BLOCK);
        DOM.getElementById("s5").getStyle().setDisplay(Style.Display.BLOCK);

    }

}
