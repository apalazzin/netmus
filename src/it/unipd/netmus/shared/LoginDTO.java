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
	  private String passwordHash;
	  private String lastSessionId;
	  
	  public LoginDTO(){
		  
	  }
	  
	  public LoginDTO(String user, String passwordHash) {
		  setUser(user);
		  setPassword(passwordHash);
	  }
	  
	  public void setUser(String user) {
		  this.user = user;
	  }
	  
	  public String getUser() {
		  return user;
	  }
	  
	  public void setPassword(String passwordHash) {
		  this.passwordHash = passwordHash;
	  }
	  
	  public String getPassword() {
		  return passwordHash;
	  }

	  public void setLastSessionId(String lastSessionId) {
	      this.lastSessionId = lastSessionId;
	  }

	  public String getLastSessionId() {
	      return lastSessionId;
	  }
}
