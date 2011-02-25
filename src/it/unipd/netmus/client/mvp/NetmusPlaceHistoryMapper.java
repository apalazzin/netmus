package it.unipd.netmus.client.mvp;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;
import it.unipd.netmus.client.place.*;

/**
 * PlaceHistoryMapper interface is used to attach all places which the
 * PlaceHistoryHandler should be aware of. This is done via the @WithTokenizers
 * annotation or by extending PlaceHistoryMapperWithFactory and creating a
 * separate TokenizerFactory.
 */
@WithTokenizers( { LoginPlace.Tokenizer.class, ProfilePlace.Tokenizer.class })
public interface NetmusPlaceHistoryMapper extends PlaceHistoryMapper {
}
