/**
 * Il package contiene le classi di tipo Place.
 * I Place sono indispensabili per far si che la corrispondente
 * Activity sia accessibile via URL.
 */

package it.unipd.netmus.client.place;

import it.unipd.netmus.client.ui.LoginView.Presenter.LoginType;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * Nome: LoginPlace.java
 * Autore:  VT.G
 * Licenza: GNU GPL v3
 * Data Creazione: 15 Febbraio 2011
*/
public class LoginPlace extends Place {
	   
	   private String user;
	   private String passwordHash;
	   private String error;
	   private LoginType loginType;
	   
	   public LoginPlace(String token)
	   {
	      this.user = token;
	   }

	   public LoginPlace(String user, String passwordHash, String error, LoginType loginType)
	   {
	      this.user = user;
	      this.passwordHash = passwordHash;
	      this.error = error;
	      this.loginType = loginType;
	   }
	   /**
	    * Getter dell'attributo user
	    * @return
	    */
	   public String getLoginName()
	   {
	      return user;
	   }
	   /**
	    * Getter dell'attributo passwordHash
	    * @return
	    */
	   public String getPassword()
	   {
		   return passwordHash;
	   }
	   /**
	    * Getter dell'attributo error
	    * @return
	    */
	   public String getError()
	   {
		   return error;
	   }
       /**
        * Getter dell'attributo loginType
        * @return
        */
	   public LoginType getLoginType()
	   {
		   return loginType;
	   }
	   /**
	    * Inner class
	    */
	   public static class Tokenizer implements PlaceTokenizer<LoginPlace>
	   {
	      @Override
	      public String getToken(LoginPlace place)
	      {
	         return place.getLoginName();
	      }

	      @Override
	      public LoginPlace getPlace(String loginName)
	      {
	         return new LoginPlace(loginName);
	      }
	   }

	}
