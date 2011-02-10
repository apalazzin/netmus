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
public class EditUserPlace extends Place {
   
   private String editUserName;
   
   public EditUserPlace(String token)
   {
      this.editUserName = token;
   }

   public String getEditUserName()
   {
      return editUserName;
   }

   public static class Tokenizer implements PlaceTokenizer<EditUserPlace>
   {
      @Override
      public String getToken(EditUserPlace place)
      {
         return place.getEditUserName();
      }

      @Override
      public EditUserPlace getPlace(String token)
      {
         return new EditUserPlace(token);
      }
   }

}
