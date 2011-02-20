/**
 * 
 */
package it.unipd.netmus.shared.exception;

/**
 * @author ValterTexasGroup
 *
 */
@SuppressWarnings("serial")
public class LoginException extends NetmusException {
	
	public LoginException() {
		super();
	}
	
	public LoginException(String moreInfo) {
		super(moreInfo);
	}

}
