/**
 * 
 */
package it.unipd.netmus.shared;

/**
 * @author ValterTexasGroup
 *
 */
@SuppressWarnings("serial")
public class UserCompleteDTO extends UserDTO {
	private String password;
	private int nSongs;
	
	public UserCompleteDTO(){
		super();
		password = "";
		nSongs = 0;
	}
	
	public UserCompleteDTO(String nick,String mail,String sexType,String city,String name,String surname,String birthDate, String pass){
		super(nick,mail,sexType,city,name,surname,birthDate);
		password = pass;
	}
	
	public UserCompleteDTO(String nick,String mail,String sexType,String city, String pass){
		super(nick,mail,sexType,city);
		password = pass;
	}

	public UserCompleteDTO(String name,String surname,String birthDate, String pass){
		super(name,surname,birthDate);
		password = pass;
	}
	
	
	public void setPassword(String p){
		password = p;
	}
	
	public String getPassword(){
		return password;
	}

	
	public void setNsongs(int n){
		nSongs = n;
	}
	
	public int getNsongs(){
		return nSongs;
	}
}
