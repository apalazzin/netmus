/**
 * 
 */
package it.unipd.netmus.shared;

/**
 * @author ValterTexasGroup
 *
 */
@SuppressWarnings("serial")
public class UserSummaryDTO implements GenericDTO {
	private String nickName;
	private String email;
	private String sex;
	private String location;
	
	//default constructor
	public UserSummaryDTO(){
		this(new String(), new String(), new String(), new String());
	}
	
	public UserSummaryDTO(String nick,String mail,String sexType,String city){
		this.nickName = nick;
		this.email = mail;
		this.sex = sexType;
		this.location = city;
	}
	
	public String getNickName()
	{
		return nickName;
	}
	
	public void setNickName(String nick)
	{
		this.nickName = nick;
	}
	
		
	public String getEmail()
	{
		return email;
	}
	
	public void setEmail(String mail)
	{
		this.email = mail;
	}
	
	public String getSex()
	{
		return sex;
	}
	
	public void setSex(String sexType)
	{
		this.sex = sexType;
	}
	
	public String getLocation()
	{
		return location;
	}
	
	public void setLocation(String city)
	{
		this.location = city;
	}
}
