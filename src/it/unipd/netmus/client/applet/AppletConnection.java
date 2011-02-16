/**
 * 
 */
package it.unipd.netmus.client.applet;

/**
 * @author ValterTexasGroup
 *
 */
public class AppletConnection {
    
    AppletConnection() {
        
    }

    private static native void makeNativeFunction(AppletConnection x)/*-{
    $wnd.getStarts = function () {
    x.@it.unipd.netmus.client.applet.AppletBar::sendStarts()();
    };
    }-*/;
    
}
