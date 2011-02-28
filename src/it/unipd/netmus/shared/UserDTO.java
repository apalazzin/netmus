/**
 * 
 */
package it.unipd.netmus.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ValterTexasGroup
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

    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getAboutMe() {
        return about_me;
    }

    public void setAboutMe(String about_me) {
        this.about_me = about_me;
    }

    public String getUser() {
        return user_name;
    }

    public void setUser(String user_name) {
        this.user_name = user_name;
    }

    public String getNickName() {
        return nick_name;
    }

    public void setNickName(String nick_name) {
        this.nick_name = nick_name;
    }

    public void setNewPassword(String new_password) {
        this.new_password = new_password;
    }

    public String getNewPassword() {
        return new_password;
    }

    public void setPublicProfile(boolean is_public_profile) {
        this.is_public_profile = is_public_profile;
    }

    public boolean isPublicProfile() {
        return is_public_profile;
    }

    public void setAllowedUsers(List<String> allowed_users) {
        this.allowed_users = allowed_users;
    }

    public List<String> getAllowedUsers() {
        return allowed_users;
    }
}
