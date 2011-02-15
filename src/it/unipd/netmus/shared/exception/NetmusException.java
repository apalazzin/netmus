/**
 * 
 */
package it.unipd.netmus.shared.exception;

/**
 * @author ValterTexasGroup
 *
 */
@SuppressWarnings("serial")
public class NetmusException extends Exception {
	private String moreInfo;
	
	public NetmusException() {
		super();
	}
	
	public NetmusException(String moreInfo) {
		super();
		this.moreInfo = moreInfo;
	}

	public String getMoreInfo() {
		return moreInfo;
	}
}
