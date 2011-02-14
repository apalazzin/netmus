/**
 * 
 */
package it.unipd.netmus.server;

import com.google.code.twig.ObjectDatastore;
import com.google.code.twig.annotation.AnnotationObjectDatastore;

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
