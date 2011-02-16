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
	private String moreInfo;
	
	public WrongLoginException() {
		super();
	}
	
	public WrongLoginException(String moreInfo) {
		super();
		this.moreInfo = moreInfo;
	}

	public String getMoreInfo() {
		return moreInfo;
	}
}
