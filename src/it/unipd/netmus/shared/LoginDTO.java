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
public class LoginDTO implements Serializable {

	  private String user;
	  private String password_hash;
	  private String last_session_id;
	  
	  public LoginDTO(){
		  
	  }
	  
	  public LoginDTO(String user, String password_hash) {
		  setUser(user);
		  setPassword(password_hash);
	  }
	  
	  public void setUser(String user) {
		  this.user = user;
	  }
	  
	  public String getUser() {
		  return user;
	  }
	  
	  public void setPassword(String password_hash) {
		  this.password_hash = password_hash;
	  }
	  
	  public String getPassword() {
		  return password_hash;
	  }

	  public void setLastSessionId(String last_session_id) {
	      this.last_session_id = last_session_id;
	  }

	  public String getLastSessionId() {
	      return last_session_id;
	  }
}
