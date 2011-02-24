/**
 * Il package contiene le classi di tipo Place.
 * I Place sono indispensabili per far si che la corrispondente
 * Activity sia accessibile via URL.
 */

package it.unipd.netmus.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * ProfilePlace estende la classe Place messa a disposizione da GWT.
 * La classe e' associata alla classe interna Tokenizer che permette di
 * serializzare lo stato del Place in un simbolo(token) URL. 
 * 
 * @author ValterTexasGroup
 */
public class ProfilePlace extends Place {
   
   private String profileName;
   
   public ProfilePlace(String token)
   {
      this.profileName = token;
   }

   public String getProfileName()
   {
      return profileName;
   }

   public static class Tokenizer implements PlaceTokenizer<ProfilePlace>
   {
      @Override
      public String getToken(ProfilePlace place)
      {
         return place.getProfileName();
      }

      @Override
      public ProfilePlace getPlace(String token)
      {
         return new ProfilePlace(token);
      }
   }

}
