/**
 * 
 */
package it.unipd.netmus.shared;

import java.io.Serializable;

/**
 * @author ValterTexasGroup
 *
 */
@SuppressWarnings("serial")
public class UserSummaryDTO implements Serializable {
    private String user;
	private String nickName;
	private String avatar;
	
	//default constructor
	public UserSummaryDTO(){
	}

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
	
}
