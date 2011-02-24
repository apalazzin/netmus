/**
 * Il package contiene le classi di tipo Place.
 * I Place sono indispensabili per far si che la corrispondente
 * Activity sia accessibile via URL.
 */

package it.unipd.netmus.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * EditSongsPlace estende la classe Place messa a disposizione da GWT.
 * La classe e' associata alla classe interna Tokenizer che permette di
 * serializzare lo stato del Place in un simbolo(token) URL. 
 * 
 * @author ValterTexasGroup
 */
public class EditSongsPlace extends Place {
   
   private String editSongsName;
   
   public EditSongsPlace(String token)
   {
      this.editSongsName = token;
   }

   public String getEditSongsName()
   {
      return editSongsName;
   }

   public static class Tokenizer implements PlaceTokenizer<EditSongsPlace>
   {
      @Override
      public String getToken(EditSongsPlace place)
      {
         return place.getEditSongsName();
      }

      @Override
      public EditSongsPlace getPlace(String token)
      {
         return new EditSongsPlace(token);
      }
   }

}
