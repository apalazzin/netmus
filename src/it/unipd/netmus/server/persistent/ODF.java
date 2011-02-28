/**
 * 
 */
package it.unipd.netmus.server.persistent;

import com.google.code.twig.ObjectDatastore;
import com.google.code.twig.annotation.AnnotationObjectDatastore;

/**
 * @author ValterTexasGroup
 *
 */
final class ODF {
   
	private static final ObjectDatastore datastore_istance = new AnnotationObjectDatastore(false);

	private ODF() {
	}

	public static ObjectDatastore get() {
		return datastore_istance;
	}
}
