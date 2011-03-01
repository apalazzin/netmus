/**
 * 
 */
package it.unipd.netmus.server.persistent;

import com.google.code.twig.ObjectDatastore;
import com.google.code.twig.annotation.AnnotationObjectDatastore;

/**
 * 
 * Nome: ODF.java
 * Autore:  VT.G
 * Licenza: GNU GPL v3
 * Data Creazione: 16 Febbraio 2011
 *
 * 
 * Tipo, obiettivo e funzione del componente:
 * 
 * ODF (Object Datastore Factory) è una classe final che segue il pattern Singleton. 
 * Ha la funzione di creare un'unica istanza del ObjectDatastore fornito dal framework 
 * twig-persist e di restituirla a qualunque oggetto la richieda. Quest'ultimo dà la 
 * possibilità di interagire con il DataStore, inviando query, memorizzando oggetti persistent
 *  o richiedendo liste di oggetti tramite semplici metodi. L'oggetto datastore restituito è
 *  di tipo AnnotationObjectDatastore che di default salva gli attributi degli oggetti 
 *  persistenti in modo non indicizzato rendendo il datastore più rapido. 
 *  Questa classe è visibile solamente all'interno del package persistent.
 *
 */
final class ODF {
   
    /**
     * Unica istanza di ObjectDatastore utilizzata all'interno dell'applicazione. 
     * AnnotationObjectDatastore salva in modo non indicizzato gli attributi di ogni entità
     * aumentando l'efficenza del Datastore.
     */
	private static final ObjectDatastore datastore_istance = new AnnotationObjectDatastore(false);

	/**
	 * Costruttore privato
	 */
	private ODF() {
	}

	public static ObjectDatastore get() {
		return datastore_istance;
	}
}
