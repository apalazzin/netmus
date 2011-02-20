/**
 * 
 */
package it.unipd.netmus.shared.exception;

/**
 * @author ValterTexasGroup
 *
 */
@SuppressWarnings("serial")
public class NickNotFreeException extends RegistrationException {

	public NickNotFreeException() {
		super();
	}
	
	public NickNotFreeException(String moreInfo) {
		super(moreInfo);
	}
	
}
