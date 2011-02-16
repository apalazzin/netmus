/**
 * 
 */
package it.unipd.netmus.client.applet;

/**
 * @author ValterTexasGroup
 *
 */
public final class ABF {
   
	private static final AppletBar APPLET_BAR = new AppletBar();

	private ABF() {
	}

	public static AppletBar get() {
		return APPLET_BAR;
	}
}
