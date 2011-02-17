/**
 * 
 */
package it.unipd.netmus.server.persistent;

import it.unipd.netmus.server.ODF;
import it.unipd.netmus.shared.LoginDTO;
import it.unipd.netmus.shared.UserCompleteDTO;
import it.unipd.netmus.shared.UserDTO;
import it.unipd.netmus.shared.UserSummaryDTO;

import java.util.Date;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Text;
import com.reveregroup.gwt.imagepreloader.FitImage;
import com.vercer.engine.persist.annotation.Child;
import com.vercer.engine.persist.annotation.Key;
import com.vercer.engine.persist.annotation.Type;


/**
 * @author ValterTexasGroup
 *
 */
public class UserAccount {
   
   @Key public String user;
   
   @Child private MusicLibrary MUSICLIBRARY = new MusicLibrary(this);
   
   private String password;
   
   private String firstName;
   
   private String lastName;
   
   private String nickName;
   
   private String gender;
   
   private String nationality;
   
   @Type(Blob.class) private FitImage avatar;

   @Type(Text.class) private String aboutMe;

   private Date birthDate;
   
   private Date registrationDate;
   
   private Date lastLogin;
   
   private Date lastImport;
   
   //private boolean isGoogleUser;
   
   public UserAccount() {
   }
   
   public UserAccount(String user, String password) { 
       this.user = user;
       this.password = password;
   }
   
   public static UserAccount loadUserWithLibrary(String user) {
       ODF.get().setActivationDepth(3);
       return ODF.get().load(UserAccount.class, user);
   }

   public static UserAccount loadUserWithoutLibrary(String user) {
       ODF.get().setActivationDepth(1);
       return ODF.get().load(UserAccount.class, user);
   }
   
   public LoginDTO toLoginDTO() {
       return new LoginDTO(this.user, this.password);
   }
   
   public UserSummaryDTO toUserSummaryDTO() {
       UserSummaryDTO tmp = new UserSummaryDTO();
       tmp.setUser(this.user);
       tmp.setNickName(this.nickName);
       //tmp.setAvatar(this.avatar);
       return tmp;
   }
   
   public UserDTO toUserDTO() {
       UserDTO tmp = (UserDTO) toUserSummaryDTO();
       tmp.setAboutMe(this.aboutMe);
       tmp.setBirthDate(this.birthDate);
       tmp.setFirstName(this.firstName);
       tmp.setGender(this.gender);
       tmp.setLastImport(this.lastImport);
       tmp.setLastLogin(this.lastLogin);
       tmp.setLastName(this.lastName);
       tmp.setNationality(this.nationality);
       tmp.setRegistrationDate(this.registrationDate);
       return tmp;
   }
   
   public UserCompleteDTO toUserCompleteDTO() {
       UserCompleteDTO tmp = (UserCompleteDTO) toUserDTO();
       //tmp.setMusicLibrary(this.MUSICLIBRARY.toMusicLibraryDTO);
       return tmp;
   }
   
   public String getUser() {
       return user;
   }

   public void setUser(String user) {
       this.user = user;
   }

   public MusicLibrary getMUSICLIBRARY() {
    return MUSICLIBRARY;
   }

   public String getPassword() {
       return password;
   }

   public void setPassword(String password) {
       this.password = password;
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

   public String getNickName() {
       return nickName;
   }

   public void setNickName(String nickName) {
       this.nickName = nickName;
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

   public FitImage getAvatar() {
       return avatar;
   }

   public void setAvatar(FitImage avatar) {
       this.avatar = avatar;
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

   public Date getRegistrationDate() {
       return registrationDate;
   }

   public void setRegistrationDate(Date registrationDate) {
       this.registrationDate = registrationDate;
   }

   public Date getLastLogin() {
       return lastLogin;
   }

   public void setLastLogin(Date lastLogin) {
       this.lastLogin = lastLogin;
   }

   public Date getLastImport() {
       return lastImport;
   }

   public void setLastImport(Date lastImport) {
       this.lastImport = lastImport;
   }
   
}
