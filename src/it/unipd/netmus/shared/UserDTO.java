package it.unipd.netmus.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Nome: UserDTO.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 13 Febbraio 2011
 * 
 * 
 * Tipo, obiettivo e funzione del componente:
 * 
 * Come tutte le altri classi di questo package questa classe permette lo
 * scambio di informazioni tra client e server all'interno delle chiamate RPC.
 * Contiene e rappresenta la maggior parte dei dati di un utente, più
 * precisamente quelli che ci si aspetta che ogni utente abbia inserito.
 * 
 * 
 */

@SuppressWarnings("serial")
public class UserDTO implements Serializable {

    private String user_name;
    private String nick_name;
    private String first_name;
    private String last_name;
    private String gender;
    private String nationality;
    private String about_me;
    private String new_password;
    private boolean is_public_profile;
    private List<String> allowed_users;

    public UserDTO() {
        this.user_name = "";
        this.nick_name = "";
        this.first_name = "";
        this.last_name = "";
        this.gender = "";
        this.nationality = "";
        this.about_me = "";
        this.new_password = "";
        this.is_public_profile = true;
        this.allowed_users = new ArrayList<String>();
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

    public String getNationality() {
        return nationality;
    }

    public String getNewPassword() {
        return new_password;
    }

    public String getNickName() {
        return nick_name;
    }

    public String getUser() {
        return user_name;
    }

    public boolean isPublicProfile() {
        return is_public_profile;
    }

    public void setAboutMe(String about_me) {
        this.about_me = about_me;
    }

    public void setAllowedUsers(List<String> allowed_users) {
        this.allowed_users = allowed_users;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setNewPassword(String new_password) {
        this.new_password = new_password;
    }

    public void setNickName(String nick_name) {
        this.nick_name = nick_name;
    }

    public void setPublicProfile(boolean is_public_profile) {
        this.is_public_profile = is_public_profile;
    }

    public void setUser(String user_name) {
        this.user_name = user_name;
    }
}
