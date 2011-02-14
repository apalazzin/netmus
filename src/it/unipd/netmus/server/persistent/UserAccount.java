/**
 * 
 */
package it.unipd.netmus.server.persistent;

import it.unipd.netmus.shared.UserSummaryDTO;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.code.twig.ObjectDatastore;
import com.google.code.twig.annotation.AnnotationObjectDatastore;

/**
 * @author ValterTexasGroup
 *
 */
public class UserAccount {
   
   private String user;
   
   private String password; // da criptare e decriptare con algoritmi DES o AES
   
   private String firstName;
   
   private String lastName;
   
   private String emailAddress;
   
   private Date birthDate;
   
   private Date registrationDate;
   
   private Date lastLogin;
   
   private Date lastImport;
   
   private int numSongs;
   
   private boolean isGoogleUser;
   
   public UserAccount() {
   }
   
   public UserAccount(String user, String password) {
      this();
      this.user = user;
      this.password = password;
   }
   
   // STATICO
   public static UserAccount findUser(String user) {
      
      ObjectDatastore datastore = new AnnotationObjectDatastore();
      
      UserAccount found = datastore.find()
         .type(UserAccount.class)
         .addFilter("user", FilterOperator.EQUAL, user)
         .returnUnique().now();
      
      return found;
   }
   
   public boolean isGoogleUser() {
      return isGoogleUser;
   }
   
   public UserSummaryDTO toUserSummaryDTO() {
      
      return null;
   }
   
   public UserSummaryDTO toUserDTO() {
      
      return null;
   }
   
   public UserSummaryDTO toUserCompleteDTO() {
      
      return null;
   }

   public String getUser() {
      return user;
   }

   // eventuali cambi password (da decidere come finirlo)
   public void changePassword(String old_pwd, String new_pwd) {
      if(old_pwd == password) {
         this.password = new_pwd;
      }
   }

   public String getPassword() {
      return password;
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

   public String getEmailAddress() {
      return emailAddress;
   }

   public void setEmailAddress(String emailAddress) {
      this.emailAddress = emailAddress;
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

   public int getNumSongs() {
      return numSongs;
   }

   public void setNumSongs(int numSongs) {
      this.numSongs = numSongs;
   }

   public void setGoogleUser(boolean isGoogleUser) {
      this.isGoogleUser = isGoogleUser;
   }
   
   
}
