/**
 * 
 */
package it.unipd.netmus.client.applet;

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
    
    private Label title = new Label("Device Scanner BAR");
    private Anchor onOff = new Anchor();
    private Anchor rescan = new Anchor();
    private TextBox status = new TextBox();
    private HTML applet = new HTML();
    
    public AppletBar() {

        title.setSize("150", "14");
        
        onOff.setSize("50","14");
        onOff.setText("Attiva");
        
        rescan.setSize("50", "14");
        rescan.setText("Riscansiona");
        
        status.setSize("180", "14");
        
        applet.setHTML("<applet id='netmus_applet' name='netmus_applet' " +
        		"code=\"applet/NetmusApplet.class\" " +
        		"archive=\"applet/netmusApplet.jar,applet/jid3lib-0.5.4.jar\" " +
        		"width=0 height=0></applet>");

        RootPanel.get("applet-bar").add(title,0,3);
        RootPanel.get("applet-bar").add(onOff,140,3);
        RootPanel.get("applet-bar").add(rescan,190,3);
        RootPanel.get("applet-bar").add(status,280,-2);
        RootPanel.get("applet-bar").add(applet);
        
        onOff.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // send changeStatus signal
            }
        });
        
        rescan.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // send rescan signal
            }
        });
    }
    
    public void appletBarON() {
        if (!visible) {
            visible = true;
            RootPanel.get("applet-bar").setVisible(true);
        }
    }
    
    public void appletBarOFF() {
        if (visible) {
            visible = false;
            RootPanel.get("applet-bar").setVisible(false);
        }
    }
    
    
    // metodi per comunicare con l applet
    
    void sendStarts() {
    }
    
    
}
