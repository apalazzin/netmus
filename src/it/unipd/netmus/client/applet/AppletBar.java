/**
 * 
 */
package it.unipd.netmus.client.applet;

import it.unipd.netmus.shared.SongDTO;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author ValterTexasGroup
 *
 */
public class AppletBar {
    
    private boolean visible = false;
    private AppletConstants constants = GWT.create(AppletConstants.class);
    private Label title = new Label(constants.title());
    private Anchor onOff = new Anchor();
    private Anchor rescan = new Anchor(); // VISIBILE FINITA LA SCANSIONE
    private TextBox status = new TextBox();
    private HTML applet = new HTML();
    private String user;
    private boolean state;
    private TranslateDTOXML translator = new TranslateDTOXML();
    
    public AppletBar(String user, boolean state) {
    	
    	this.user=user.replaceAll("@\\S*", "");
    	this.state=state;
        
        makeNativeFunction(this);

        title.setSize("150px", "14px");
        
        onOff.setSize("50px","14px");
        if (state) onOff.setText(constants.appletEnabled());
        else onOff.setText(constants.appletDisabled());
        
        rescan.setSize("50px", "10px");
        rescan.setText(constants.rescan());
        
        status.setSize("180px", "10px");

        RootPanel.get("applet-bar").add(title,5,2);
        RootPanel.get("applet-bar").add(onOff,145,2);
        RootPanel.get("applet-bar").add(rescan,195,2);
        RootPanel.get("applet-bar").add(status,285,2);
        RootPanel.get("applet-bar").add(applet);
        
        onOff.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // send changeStatus signal
                changeState();
            }
        });
        
        rescan.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // send rescan signal
                reScanAll();
            }
        });
    }
    
    public void appletBarON() {
        if (!visible) {
            visible = true;
            RootPanel.get("applet-bar").setVisible(true);
            applet.setHTML("<applet id='netmus_applet' name='netmus_applet' " +
                    "code=\"applet/NetmusApplet.class\" " +
                    "archive=\"applet/netmusApplet.jar,applet/jid3lib-0.5.4.jar\" " +
                    "width=0 height=0></applet>");
        }
    }
    
    public void appletBarOFF() {
        if (visible) {
            visible = false;
            RootPanel.get("applet-bar").setVisible(false);
            applet.setHTML("");
        }
    }
    
    // metodi per comunicare con l applet
    
    // per dire all'applet di cambiare stato
    // dopo che ONOFF e' stato premuto
    private void changeState() {
    }
    
    // per dire all'applet di rifare interamente
    // la scansione dopo che RESCAN e' stato premuto
    private void reScanAll() {
    }
    
    /**
     * Metodo che deve chiamare il metodo dell'applet 
     * letsGo(String username, boolean state)
     * per mandare user e stato iniziale e avviare il thread
     */
    private void sendStarts() {
    	sendStartsJSNI(user,state);
    }
    
    private native void sendStartsJSNI( String user, boolean state )/*-{
    	$doc.getElementById('netmus_applet').letsGO(user,state);
    }-*/;
    
    private void scanningStatus(int actual, int total){
    	//aggiornare la grafica con le nuove info
    }
    
    private void status(String status){
    	//modifica le informazioni di stato sulla grafica
    }
    
    private void translateXML(String result) {
        List<SongDTO> temp = translator.XMLToDTO(result);
    }
    
    // metodo per pubblicare le funzioni native di linking
    private native void makeNativeFunction(AppletBar x)/*-{
    $wnd.getStarts = function () {
    x.@it.unipd.netmus.client.applet.AppletBar::sendStarts()();
    };
    $wnd.scanResult = function (result) {
    x.@it.unipd.netmus.client.applet.AppletBar::translateXML(Ljava/lang/String;)(result);
    };
    $wnd.scanStatus = function (actual, total) {
    x.@it.unipd.netmus.client.applet.AppletBar::scanningStatus(II)(actual, total);
    };
    $wnd.status = function (status) {
    x.@it.unipd.netmus.client.applet.AppletBar::status(Ljava/lang/String;)(status);
    };
    }-*/;
    // mancano parametri sopra in ingresso Stringa XML da applet
}
