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
public final class ODF {
   
	private static final ObjectDatastore datastoreIstance = new AnnotationObjectDatastore(false);

	private ODF() {
	}

	public static ObjectDatastore get() {
		return datastoreIstance;
	}
}
