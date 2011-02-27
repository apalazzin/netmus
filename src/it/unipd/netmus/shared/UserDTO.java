/**
 * 
 */
package it.unipd.netmus.shared;

import java.io.Serializable;
import java.util.Date;

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
	private Date birth_date;
    private Date last_session_id;
    private boolean is_google_user;
    private String new_password;
	
    public UserDTO() {
    }

    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String firstName) {
        this.first_name = firstName;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String lastName) {
        this.last_name = lastName;
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

    public void setAboutMe(String aboutMe) {
        this.about_me = aboutMe;
    }

    public Date getBirthDate() {
        return birth_date;
    }

    public void setBirthDate(Date birthDate) {
        this.birth_date = birthDate;
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

    public Date getLastSessionId() {
        return last_session_id;
    }

    public void setLastSessionId(Date last_session_id) {
        this.last_session_id = last_session_id;
    }

    public boolean isGoogleUser() {
        return is_google_user;
    }

    public void setIsGoogleUser(boolean is_google_user) {
        this.is_google_user = is_google_user;
    }

    public void setNewPassword(String new_password) {
        this.new_password = new_password;
    }

    public String getNewPassword() {
        return new_password;
    }
}
