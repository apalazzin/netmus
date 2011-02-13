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
		this(new String(), new String(), new String(),new String(), new String(), new String(),new String());
	}
	
	public UserDTO(String nick,String mail,String sexType,String city){
		this(nick,mail,sexType,city,new String(), new String(), new String());
		
	}
	
	public UserDTO(String name,String surname,String birthDate){
		this(new String(), new String(), new String(),new String(), name,surname,birthDate);
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
