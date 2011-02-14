/**
 * 
 */
package it.unipd.netmus.shared;

/**
 * @author ValterTexasGroup
 *
 */
@SuppressWarnings("serial")
public class LoginDTO implements GenericDTO {

	  private String user;
	  private String password;
	  
	  public LoginDTO(){
		  
	  }
	  
	  public LoginDTO(String user, String password) {
		  setUser(user);
		  setPassword(password);
	  }
	  
	  public void setUser(String user) {
		  this.user = user;
	  }
	  public String getUser() {
		  return user;
	  }
	  public void setPassword(String password) {
		  this.password = password;
	  }
	  public String getPassword() {
		  return password;
	  }
}
