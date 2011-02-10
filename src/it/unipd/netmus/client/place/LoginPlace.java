/**
 * Il package contiene le classi di tipo Place.
 * I Place sono indispensabili per far si che la corrispondente
 * Activity sia accessibile via URL.
 */

package it.unipd.netmus.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * LoginPlace estende la classe Place messa a disposizione da GWT.
 * La classe e' associata alla classe interna Tokenizer che permette di
 * serializzare lo stato del Place in un simbolo(token) URL. 
 * 
 * @author ValterTexasGroup
 */
public class LoginPlace extends Place {
   
   private String loginName;
   
   public LoginPlace(String token)
   {
      this.loginName = token;
   }

   public String getLoginName()
   {
      return loginName;
   }

   public static class Tokenizer implements PlaceTokenizer<LoginPlace>
   {
      @Override
      public String getToken(LoginPlace place)
      {
         return place.getLoginName();
      }

      @Override
      public LoginPlace getPlace(String token)
      {
         return new LoginPlace(token);
      }
   }

}
