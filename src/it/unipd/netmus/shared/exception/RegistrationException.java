/**
 * 
 */
package it.unipd.netmus.shared.exception;

/**
 * @author ValterTexasGroup
 *
 */
@SuppressWarnings("serial")
public class RegistrationException extends NetmusException {

	public RegistrationException() {
		super();
	}
	
	public RegistrationException(String moreInfo) {
		super(moreInfo);
	}

}
