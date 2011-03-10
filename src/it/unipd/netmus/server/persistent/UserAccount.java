package it.unipd.netmus.server.persistent;

import it.unipd.netmus.shared.LoginDTO;
import it.unipd.netmus.shared.UserCompleteDTO;
import it.unipd.netmus.shared.UserDTO;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Text;
import com.google.code.twig.annotation.Child;
import com.google.code.twig.annotation.Id;
import com.google.code.twig.annotation.Index;
import com.google.code.twig.annotation.Type;

/**
 * 
 * Nome: UserAccount.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 15 Febbraio 2011
 * 
 * 
 * Tipo, obiettivo e funzione del componente:
 * 
 * UserAccount utilizza il design pattern DAO poichè contiene tutte le
 * informazioni relative ad un utente così come viene salvato all'interno del
 * Datastore. La classe è implementata come POJO poichè twig-persist lo supporta
 * senza bisogno di configurazioni aggiuntive. Vengono usate solamente le
 * annotazioni @Key e @Index per gestire l'indicizzazione deigli oggetti nel
 * datastore.
 * 
 */

public class UserAccount {

    /**
     * 
     * Rimuove completamente dal database la canzone. Viene utilizzato solo nei
     * casi di provata incompletezza o scorrettezza di una canzone.
     * 
     */
    public static void deleteUser(UserAccount user) {
        ODF.get().storeOrUpdate(user);

        MusicLibrary.deleteMusicLibrary(user.getMusicLibrary());

        ODF.get().storeOrUpdate(user);

        ODF.get().delete(user);
    }

    /**
     * 
     * Cerca nel database e ritorna i dati realivi ad un utente che come
     * last_session_id ha l'id di sessione HTTP dato in input.
     * 
     */
    public static UserAccount findSessionUser(String session_id) {
        return ODF.get().find().type(UserAccount.class)
                .addFilter("lastSessionId", FilterOperator.EQUAL, session_id)
                .returnUnique().now();
    }
    
    /**
     * 
     * 
     */
    public List<String> findRelatedUsers() {
        
        //Artista e genere preferiti dell'utente, valori aggiornati all'ultimo caricamento/rimozione di canzoni
        String preferred_artist = this.getMusicLibrary().getPreferredArtist();
        String preferred_genre = this.getMusicLibrary().getPreferredGenre();
        
        //inizializzazione liste necessarie all'algoritmo
        List<String> related_users = new ArrayList<String>();
        List<String> related_users_for_genre = new ArrayList<String>();
        List<String> related_users_for_artist = new ArrayList<String>();
        List<MusicLibrary> library = new ArrayList<MusicLibrary>();
        
        //query nel datastore per cercare gli utenti con lo stesso preferred_artist
        library = ODF.get().find().type(MusicLibrary.class).addFilter("preferred_artist", FilterOperator.EQUAL, preferred_artist).fetchFirst(20).returnAll().now();
        for (MusicLibrary tmp : library) {
            String tmp2 = tmp.getOwner().getUser();
            if (!tmp2.equals(this.getUser())) {
                related_users_for_artist.add(tmp.getOwner().getUser());
            }
        }
        
        //query nel datastore per cercare gli utenti con lo stesso preferred_genre
        library = ODF.get().find().type(MusicLibrary.class).addFilter("preferred_genre", FilterOperator.EQUAL, preferred_genre).fetchFirst(20).returnAll().now();
        for (MusicLibrary tmp : library) {
            String tmp2 = tmp.getOwner().getUser();
            if (!tmp2.equals(this.getUser())) {
                related_users_for_genre.add(tmp.getOwner().getUser());
            }
        }
        
        //se gli utenti affini lo sono tutti per genere restituisce solo quella lista
        if (related_users_for_artist.size() == 0) {
            related_users.addAll(related_users_for_genre);
            return related_users;
        }
        
        //se gli utenti affini lo sono tutti per genere restituisce 
        if (related_users_for_genre.size() == 0) {
            related_users.addAll(related_users_for_artist);
            return related_users;
        }
        
        //altimenti mescola le due liste uno-per-tipo
        for (int i = 0; i<related_users_for_artist.size() || i<related_users_for_genre.size(); i++) {
            if (related_users.indexOf(related_users_for_artist.get(i)) < 0) {
                related_users.add(related_users_for_artist.get(i));
            }
            if (related_users.indexOf(related_users_for_genre.get(i)) < 0) {
                related_users.add(related_users_for_genre.get(i));
            }
            if (i+1 == related_users_for_artist.size()) {
                if (i+1 < related_users_for_genre.size()) {
                    related_users.addAll(related_users_for_genre.subList(i+1, related_users_for_genre.size()-1));
                }
            }
            if (i+1 == related_users_for_genre.size()) {
                if (i+1 < related_users_for_artist.size()) {
                    related_users.addAll(related_users_for_artist.subList(i+1, related_users_for_artist.size()-1));
            
                }
            }
        }
        
        return related_users;
    }
    
    public static UserAccount load(String user) {
        return ODF.get().load().type(UserAccount.class).id(user).now();
    }

    @Id
    private String user;

    @Child
    private MusicLibrary music_library;

    private String password_hash;

    private String first_name;

    private String last_name;

    private String nick_name;

    private String gender;

    private String nationality;

    private boolean is_public_profile;

    private List<String> allowed_users;

    @Type(Text.class)
    private String about_me;

    @Index
    private String last_session_id;

    private boolean is_google_user;

    public UserAccount() {
        this.music_library = new MusicLibrary(this);
        this.about_me = "";
        this.first_name = "";
        this.gender = "";
        this.is_google_user = false;
        this.last_name = "";
        this.last_session_id = "";
        this.nationality = "";
        this.nick_name = "";
        this.password_hash = "";
        this.user = "";
        this.is_public_profile = true;
        this.allowed_users = new ArrayList<String>();
    }

    public UserAccount(String user, String password_hash) {
        music_library = new MusicLibrary(this);
        this.user = user;
        this.password_hash = password_hash;
        this.is_google_user = false;
        this.about_me = "";
        this.first_name = "";
        this.gender = "";
        this.last_name = "";
        this.last_session_id = "";
        this.nationality = "";
        this.nick_name = "";
        this.is_public_profile = true;
        this.allowed_users = new ArrayList<String>();
        this.store();
    }

    public String getAboutMe() {
        return about_me;
    }

    public List<String> getAllowedUsers() {
        return allowed_users;
    }

    public String getFirstName() {
        return first_name;
    }

    public String getGender() {
        return gender;
    }

    public String getLastName() {
        return last_name;
    }

    public String getLastSessionId() {
        return last_session_id;
    }

    public MusicLibrary getMusicLibrary() {
        return music_library;
    }

    public String getNationality() {
        return nationality;
    }

    public String getNickName() {
        return nick_name;
    }

    public String getPassword() {
        return password_hash;
    }

    public String getUser() {
        return user;
    }

    public boolean isGoogleUser() {
        return is_google_user;
    }

    public boolean isPublicProfile() {
        return is_public_profile;
    }

    public void setAboutMe(String about_me) {
        this.about_me = about_me;
        this.update();
    }

    public void setAllowedUsers(List<String> allowed_users) {
        this.allowed_users = allowed_users;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
        this.update();
    }

    public void setGender(String gender) {
        this.gender = gender;
        this.update();
    }

    public void setGoogleUser(boolean is_uoogle_user) {
        this.is_google_user = is_uoogle_user;
        this.update();
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
        this.update();
    }

    public void setLastSessionId(String last_session_id) {
        this.last_session_id = last_session_id;
        this.update();
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
        this.update();
    }

    public void setNickName(String nick_name) {
        this.nick_name = nick_name;
        this.update();
    }

    public void setPassword(String password_hash) {
        this.password_hash = password_hash;
        this.update();
    }

    public void setPublicProfile(boolean is_public_profile) {
        this.is_public_profile = is_public_profile;
    }

    public void store() {
        ODF.get().store().instance(this).ensureUniqueKey().now();
    }

    public LoginDTO toLoginDTO() {
        LoginDTO tmp = new LoginDTO(this.user, this.password_hash);
        tmp.setLastSessionId(this.last_session_id);
        return tmp;
    }

    public UserCompleteDTO toUserCompleteDTO() {
        UserCompleteDTO tmp = new UserCompleteDTO();
        tmp.setUser(this.user);
        tmp.setNickName(this.nick_name);
        tmp.setAboutMe(this.about_me);
        tmp.setFirstName(this.first_name);
        tmp.setGender(this.gender);
        tmp.setLastName(this.last_name);
        tmp.setNationality(this.nationality);
        tmp.setPublicProfile(this.is_public_profile);
        tmp.setAllowedUsers(this.allowed_users);
        tmp.setMusicLibrary(this.music_library.toMusicLibrarySummaryDTO());
        return tmp;
    }

    public UserDTO toUserDTO() {
        UserDTO tmp = new UserDTO();
        tmp.setUser(this.user);
        tmp.setNickName(this.nick_name);
        tmp.setAboutMe(this.about_me);
        tmp.setFirstName(this.first_name);
        tmp.setGender(this.gender);
        tmp.setLastName(this.last_name);
        tmp.setNationality(this.nationality);
        tmp.setPublicProfile(this.is_public_profile);
        tmp.setAllowedUsers(this.allowed_users);
        return tmp;
    }

    public void update() {
        ODF.get().storeOrUpdate(this);
    }
}
