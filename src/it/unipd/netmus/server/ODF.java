/**
 * 
 */
package it.unipd.netmus.server;

import com.vercer.engine.persist.ObjectDatastore;
import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;

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
