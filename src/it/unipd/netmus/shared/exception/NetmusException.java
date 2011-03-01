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
	private String more_info;
	
	public NetmusException() {
		super();
	}
	
	public NetmusException(String moreInfo) {
		super();
		this.more_info = moreInfo;
	}

	public String getMoreInfo() {
		return more_info;
	}
}
