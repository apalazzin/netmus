/**
 * 
 */
package it.unipd.netmus.client.applet;

/**
 * @author ValterTexasGroup
 *
 */
public final class ABF {
   
	private static AppletBar APPLET_BAR = null;

	private ABF() {
	}

	public static AppletBar get(String user, boolean state) {
		if (APPLET_BAR == null)
			APPLET_BAR = new AppletBar(user, state);
		return APPLET_BAR;
	}
}
