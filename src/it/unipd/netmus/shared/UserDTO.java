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
	private String firstName;
	private String lastName;
	private String gender;
	private String nationality;
	private String aboutMe;
	private Date birthDate;
    private Date last_session_id;
    private boolean is_google_user;
	
    public UserDTO() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
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
}
