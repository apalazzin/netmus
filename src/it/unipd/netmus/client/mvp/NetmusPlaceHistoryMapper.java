package it.unipd.netmus.client.mvp;

import it.unipd.netmus.client.place.LoginPlace;
import it.unipd.netmus.client.place.ProfilePlace;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

/**
 * Nome: NetmusPlaceHistoryMapper.java
 * Autore:  VT.G
 * Licenza: GNU GPL v3
 * Data Creazione: 15 Febbraio 2011
 */
//----------------------------------------//
/**
 * Questa annotazione che precede la definizione dell’interfaccia permette di
 * associare ogni place al PlaceHistoryHandler
 */
@WithTokenizers({ LoginPlace.Tokenizer.class, ProfilePlace.Tokenizer.class })
public interface NetmusPlaceHistoryMapper extends PlaceHistoryMapper {
}
