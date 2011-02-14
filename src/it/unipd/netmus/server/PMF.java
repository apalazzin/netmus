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
public final class PMF {
   
	private static final ObjectDatastore datastoreIstance = new AnnotationObjectDatastore(false);

	private PMF() {
	}

	public static ObjectDatastore get() {
		return datastoreIstance;
	}
}
