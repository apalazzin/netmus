/**
 * 
 */
package it.unipd.netmus.shared.exception;

/**
 * @author ValterTexasGroup
 *
 */
@SuppressWarnings("serial")
public class WrongLoginException extends LoginException {
	
	public WrongLoginException() {
		super();
	}
	
	public WrongLoginException(String moreInfo) {
		super(moreInfo);
	}
	
}
