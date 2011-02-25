/**
 * 
 */
package it.unipd.netmus.client.applet;

/**
 * @author ValterTexasGroup
 *
 */
final class AppletConnector {
    
    
    static native void sendStartsJSNI( String user, boolean state )/*-{
        var t = $doc.getElementById('netmus_applet');
        t.letsGO(user,state);
    }-*/;

    static native void setState( boolean state )/*-{
        var t = $doc.getElementById('netmus_applet');
        t.setState(state);
    }-*/;

    static native void sendRescan()/*-{
        var t = $doc.getElementById('netmus_applet');
        t.rescanAll();
    }-*/;

    static native void showChooser()/*-{
        var t = $doc.getElementById('netmus_applet');
        t.showChooser();
    }-*/;
    
    // metodo per pubblicare le funzioni native di linking con l'applet
    static native void makeNativeFunction(AppletBar x)/*-{
    $wnd.getStarts = function () {
    x.@it.unipd.netmus.client.applet.AppletBar::sendStarts()();
    };
    $wnd.scanResult = function (result) {
    x.@it.unipd.netmus.client.applet.AppletBar::translateXML(Ljava/lang/String;)(result);
    };
    $wnd.scanStatus = function (actual, total) {
    @it.unipd.netmus.client.applet.AppletBarView::scanningStatus(II)(actual, total);
    };
    $wnd.showStatus = function (s) {
    @it.unipd.netmus.client.applet.AppletBarView::showStatus(Ljava/lang/String;)(s);
    };
    $wnd.rescanVisible = function () {
    @it.unipd.netmus.client.applet.AppletBarView::rescanVisible()();
    };
    $wnd.rescanNotVisible = function () {
    @it.unipd.netmus.client.applet.AppletBarView::rescanNotVisible()();
    };
    }-*/;
    
}
