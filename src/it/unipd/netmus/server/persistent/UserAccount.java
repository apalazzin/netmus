/**
 * 
 */
package it.unipd.netmus.server.persistent;

import it.unipd.netmus.shared.LoginDTO;
import it.unipd.netmus.shared.UserCompleteDTO;
import it.unipd.netmus.shared.UserDTO;
import it.unipd.netmus.shared.UserSummaryDTO;

import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Text;
import com.google.code.twig.annotation.Child;
import com.google.code.twig.annotation.Id;
import com.google.code.twig.annotation.Index;
import com.google.code.twig.annotation.Type;
import com.reveregroup.gwt.imagepreloader.FitImage;


/**
 * @author ValterTexasGroup
 *
 */
public class UserAccount {
   
   @Id private String user;
   
   @Child private MusicLibrary musicLibrary;
   
   private String passwordHash;
   
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
   
   @Index private String lastSessionId;
   
   private boolean isGoogleUser;
   
   public UserAccount() {
       this.musicLibrary = new MusicLibrary(this);
       this.aboutMe = "";
       this.birthDate = new Date(0);
       this.firstName = "";
       this.gender = "";
       this.isGoogleUser = false;
       this.lastImport = new Date(0);
       this.lastLogin = new Date(0);
       this.lastName = "";
       this.lastSessionId = "";
       this.nationality = "";
       this.nickName = "";
       this.passwordHash = "";
       this.registrationDate = new Date(0);
       this.user = "";
   }
   
   public UserAccount(String user, String passwordHash) { 
       musicLibrary = new MusicLibrary(this);
       this.user = user;
       this.passwordHash = passwordHash;
       this.isGoogleUser = false;
       this.aboutMe = "";
       this.birthDate = new Date(0);
       this.firstName = "";
       this.gender = "";
       this.lastImport = new Date(0);
       this.lastLogin = new Date(0);
       this.lastName = "";
       this.lastSessionId = "";
       this.nationality = "";
       this.nickName = "";
       this.registrationDate = new Date(0);
       this.store();
   }
   
   public void store() {
       ODF.get().store().instance(this).ensureUniqueKey().now();
   }
   
   public void update() {
       ODF.get().storeOrUpdate(this);
   }
   
   public static UserAccount load(String user) {
       return ODF.get().load().type(UserAccount.class).id(user).now();
   }
   
   public LoginDTO toLoginDTO() {
       LoginDTO tmp = new LoginDTO(this.user, this.passwordHash);
       tmp.setLastSessionId(this.lastSessionId);
       return tmp;
   }
   
   public UserSummaryDTO toUserSummaryDTO() {
       UserSummaryDTO tmp = new UserSummaryDTO();
       tmp.setUser(this.user);
       tmp.setNickName(this.nickName);
       //tmp.setAvatar(this.avatar);
       return tmp;
   }
   
   public UserDTO toUserDTO() {
       UserDTO tmp = new UserDTO();
       tmp.setUser(this.user);
       tmp.setNickName(this.nickName);
       //tmp.setAvatar(this.avatar);
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
       UserCompleteDTO tmp = new UserCompleteDTO();
       tmp.setUser(this.user);
       tmp.setNickName(this.nickName);
       //tmp.setAvatar(this.avatar);
       tmp.setAboutMe(this.aboutMe);
       tmp.setBirthDate(this.birthDate);
       tmp.setFirstName(this.firstName);
       tmp.setGender(this.gender);
       tmp.setLastImport(this.lastImport);
       tmp.setLastLogin(this.lastLogin);
       tmp.setLastName(this.lastName);
       tmp.setNationality(this.nationality);
       tmp.setRegistrationDate(this.registrationDate);
       tmp.setMusicLibrary(this.musicLibrary.toMusicLibraryDTO());
       return tmp;
   }
   
   public static UserAccount findSessionUser(String sessionId) {
       return ODF.get().find().type(UserAccount.class)
       .addFilter("lastSessionId", FilterOperator.EQUAL, sessionId)
       .returnUnique()
       .now();
   }
   
   public String getUser() {
       return user;
   }

   public MusicLibrary getMusicLibrary() {
       return musicLibrary;
   }

   public String getPassword() {
       return passwordHash;
   }

   public void setPassword(String passwordHash) {
       this.passwordHash = passwordHash;
       this.update();
   }

   public String getFirstName() {
       return firstName;
   }

   public void setFirstName(String firstName) {
       this.firstName = firstName;
       this.update();
   }

   public String getLastName() {
       return lastName;
   }

   public void setLastName(String lastName) {
       this.lastName = lastName;
       this.update();
   }

   public String getNickName() {
       return nickName;
   }

   public void setNickName(String nickName) {
       this.nickName = nickName;
       this.update();
   }

   public String getGender() {
       return gender;
   }

   public void setGender(String gender) {
       this.gender = gender;
       this.update();
   }

   public String getNationality() {
       return nationality;
   }

   public void setNationality(String nationality) {
       this.nationality = nationality;
       this.update();
   }

   public FitImage getAvatar() {
       return avatar;
   }

   public void setAvatar(FitImage avatar) {
       this.avatar = avatar;
       this.update();
   }

   public String getAboutMe() {
       return aboutMe;
   }

   public void setAboutMe(String aboutMe) {
       this.aboutMe = aboutMe;
       this.update();
   }

   public Date getBirthDate() {
       return birthDate;
   }

   public void setBirthDate(Date birthDate) {
       this.birthDate = birthDate;
       this.update();
   }

   public Date getRegistrationDate() {
       return registrationDate;
   }

   public void setRegistrationDate(Date registrationDate) {
       this.registrationDate = registrationDate;
       this.update();
   }

   public Date getLastLogin() {
       return lastLogin;
   }

   public void setLastLogin(Date lastLogin) {
       this.lastLogin = lastLogin;
       this.update();
   }

   public Date getLastImport() {
       return lastImport;
   }

   public void setLastImport(Date lastImport) {
       this.lastImport = lastImport;
       this.update();
   }

   public void setLastSessionId(String lastSessionId) {
       this.lastSessionId = lastSessionId;
       this.update();
   }

   public String getLastSessionId() {
       return lastSessionId;
   }
   
   public void setGoogleUser(boolean isGoogleUser) {
       this.isGoogleUser = isGoogleUser;
       this.update();
   }

   public boolean isGoogleUser() {
       return isGoogleUser;
   }
   
   public static void deleteUser(UserAccount user) {
       ODF.get().storeOrUpdate(user);
       
       List<Song> songsList = user.getMusicLibrary().allSongs();
       for (Song tmp:songsList)
           user.getMusicLibrary().removeSong(tmp, false);
       MusicLibrary.deleteMusicLibrary(user.getMusicLibrary());
       
       ODF.get().storeOrUpdate(user);
       
       ODF.get().delete(user);
   }  
}
