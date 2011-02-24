package it.unipd.netmus.shared.exception;

/**
 * @author ValterTexasGroup
 *
 */
@SuppressWarnings("serial")
public class DatastoreException extends NetmusException {
    
    public DatastoreException() {
        super();
    }
    
    public DatastoreException(String more_info) {
        super(more_info);
    }
}
