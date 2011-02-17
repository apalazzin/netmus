/**
 * 
 */
package it.unipd.netmus.shared;

/**
 * @author ValterTexasGroup
 *
 */
@SuppressWarnings("serial")
public class UserDTO extends UserSummaryDTO {
	
	private String firstName;
	private String lastName;
	private String birth;
	
	//default constructor
	public UserDTO(){
		super();
		firstName = "";
		lastName = "";
		birth = "";
	}
	
	public UserDTO(String nick,String mail,String sexType,String city){
		super(nick,mail,sexType,city);
		firstName = "";
		lastName = "";
		birth = "";		
	}
	
	public UserDTO(String name,String surname,String birthDate){
		super();
		firstName = name;
		lastName = surname;
		birth = birthDate;		
	}
	
	public UserDTO(String nick,String mail,String sexType,String city,String name,String surname,String birthDate){
		super(nick,mail,sexType,city);
		this.firstName = name;
		this.lastName = surname;
		this.birth = birthDate;
	}
	
	
	
	public String getName()
	{
		return firstName;
	}
	
	public void setFirstName(String name)
	{
		this.firstName = name;
	}
	
	public String getLastName()
	{
		return lastName;
	}
	
	public void setLastName(String surname)
	{
		this.lastName = surname;
	}

	public void setBirthDate(String birthDate)
	{
		this.birth = birthDate;
	}
	
	public String getBirthDate()
	{
		return birth;
	}
}
