/**
 * 
 */
package it.unipd.netmus.shared.exception;

/**
 * @author ValterTexasGroup
 *
 */
@SuppressWarnings("serial")
public class DuplicateEmailException extends RegistrationException {

	public DuplicateEmailException() {
		super();
	}
	
	public DuplicateEmailException(String moreInfo) {
		super(moreInfo);
	}
}
