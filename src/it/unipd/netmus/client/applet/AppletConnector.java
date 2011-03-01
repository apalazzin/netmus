/**
 * 
 */
package it.unipd.netmus.client.applet;

/**
 * Nome: AppletConnector.java
 * Autore:  VT.G
 * Licenza: GNU GPL v3
 * Data Creazione: 18 Febbraio 2011
*/
final class AppletConnector {
    
    /**
     * Inizializza l'applet.
     * @param user
     * @param state
     */
    static native void sendStartsJSNI( String user, boolean state )/*-{
        var t = $doc.getElementById('netmus_applet');
        t.letsGO(user,state);
    }-*/;
    /**
     * Attiva/disattiva la scansione automatica 
     * @param state
     */
    static native void setState( boolean state )/*-{
        var t = $doc.getElementById('netmus_applet');
        t.setState(state);
    }-*/;
    /**
     * Indica all'applet di fare una scansione completa dell'ultimo device utilizato.
     */
    static native void sendRescan()/*-{
        var t = $doc.getElementById('netmus_applet');
        t.rescanAll();
    }-*/;
    /**
     * Indica all'applet di fare una scansione manuale di una cartella indicata dall'utente.
     * Si occupa l'applet di chiedere quale.
     */
    static native void showChooser()/*-{
        var t = $doc.getElementById('netmus_applet');
        t.showChooser();
    }-*/;
    
    /**
     * Crea delle funzioni JavaScript utilizzabili 
     * dall'applet, che richiamano i metodi relativi di AppletBar.
     * @param x
     */
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
