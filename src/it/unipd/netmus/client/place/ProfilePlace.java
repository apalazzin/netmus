package it.unipd.netmus.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * Nome: ProfilePlace.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 15 Febbraio 2011
 */

/**
 * Il package contiene le classi di tipo Place.
 * I Place sono indispensabili per far si che la corrispondente
 * Activity sia accessibile via URL.
 */
public class ProfilePlace extends Place {

    /**
     * Inner class
     */
    public static class Tokenizer implements PlaceTokenizer<ProfilePlace> {
        @Override
        public ProfilePlace getPlace(String token) {
            return new ProfilePlace(token);
        }

        @Override
        public String getToken(ProfilePlace place) {
            return place.getProfileName();
        }
    }

    private String profileName;

    public ProfilePlace(String token) {
        this.profileName = token;
    }

    /**
     * Getter dell'attributo profile_name
     * 
     * @return
     */
    public String getProfileName() {
        return profileName;
    }

}
