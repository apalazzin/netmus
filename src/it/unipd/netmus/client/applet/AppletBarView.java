/**
 * 
 */
package it.unipd.netmus.client.applet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Nome: AppletBarView.java
 * Autore:  VT.G
 * Licenza: GNU GPL v3
 * Data Creazione: 18 Febbraio 2011
*/
class AppletBarView {
    
    private static AppletConstants constants = GWT.create(AppletConstants.class);
    private static Label title = new Label(constants.title());
    private static Label on_off = new Label();
    private static Button rescan = new Button(); // VISIBILE FINITA UNA SCANSIONE
    private static HTMLPanel button_container = new HTMLPanel("");
    private static Button chooser = new Button();
    private static TextBox status = new TextBox();
    private static HTML applet = new HTML();
    private static HTMLPanel main = new HTMLPanel("");
    
    /**
     * Inizializza e crea la view.
     */
    static void createView() {
        
        RootPanel.get("applet-bar").setStyleName("applet-bar");

        main.setStyleName("applet_main");
        button_container.setStyleName("applet_button_cnt");
        
        on_off.setSize("50px","14px");
        on_off.setStyleName("applet_switch");
        
        on_off.setText(constants.appletDisable());
        
        rescan.setStyleName("applet_rescan");
        rescan.setText(constants.rescan());
        rescan.getElement().getStyle().setMarginLeft(30, Style.Unit.PX);
        rescan.getElement().getStyle().setWidth(90, Style.Unit.PX);
        rescan.setVisible(false); // diventera' visibile a fine scansione di un dispositivo
                
        chooser.setText(constants.chooser());
        chooser.setStyleName("applet_bottone");
        chooser.getElement().getStyle().setMarginLeft(30, Style.Unit.PX);
        chooser.getElement().getStyle().setWidth(90, Style.Unit.PX);
        
        title.setStyleName("applet_title");
        status.addStyleName("applet_status");

        RootPanel.get("applet-bar").addDomHandler(new MouseOverHandler() {

            @Override
            public void onMouseOver(MouseOverEvent event) {
                if(DOM.getElementById("main_panel").getClientWidth()>805) {
                    DOM.getElementById("main_panel").getStyle().setMarginRight(157, Style.Unit.PX);
                    DOM.getElementById("applet-bar").getStyle().setWidth(140, Style.Unit.PX);
                    status.getElement().getStyle().setProperty("MozTransform", "rotate(0deg)");
                    status.getElement().getStyle().setProperty("WebkitTransform", "rotate(0deg)");
                    status.getElement().getStyle().setProperty("Transform", "rotate(0deg)");
                    status.getElement().getStyle().setMarginLeft(7, Style.Unit.PX);
                    status.getElement().getStyle().setMarginTop(15, Style.Unit.PX);
                    status.getElement().getStyle().setWidth(120, Style.Unit.PX);
                    
                    button_container.getElement().getStyle().setOpacity(1);
                    title.getElement().getStyle().setOpacity(1);
                } else {
                    
                    DOM.getElementById("applet-bar").getStyle().setBackgroundColor("#fabebe");
                }
            }}, MouseOverEvent.getType());
        

        RootPanel.get("applet-bar").addDomHandler(new MouseOutHandler() {

            @Override
            public void onMouseOut(MouseOutEvent event) {
                DOM.getElementById("applet-bar").getStyle().setBackgroundColor("#FFFFFF");
                DOM.getElementById("main_panel").getStyle().setMarginRight(50, Style.Unit.PX);
                DOM.getElementById("applet-bar").getStyle().setWidth(33, Style.Unit.PX);
                status.getElement().getStyle().setProperty("MozTransform", "rotate(90deg)");
                status.getElement().getStyle().setProperty("WebkitTransform", "rotate(90deg)");
                status.getElement().getStyle().setProperty("Transform", "rotate(90deg)");
                status.getElement().getStyle().setMarginLeft(-86, Style.Unit.PX);
                status.getElement().getStyle().setMarginTop(60, Style.Unit.PX);
                status.getElement().getStyle().setWidth(200, Style.Unit.PX);
                button_container.getElement().getStyle().setOpacity(0);
                title.getElement().getStyle().setOpacity(0);
            }}, MouseOutEvent.getType());


        
        VerticalPanel tmp = new VerticalPanel();
        
        tmp.add(rescan);
        tmp.add(chooser);
        
        button_container.add(tmp);
        main.add(title);
        main.add(status);
        
        main.add(button_container);
        main.add(on_off);    
        main.add(applet);
        RootPanel.get("applet-bar").add(main);
        
        
        on_off.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // send changeStatus signal
                if(event.getRelativeElement().getStyle().getColor().equals("red")) {
                    event.getRelativeElement().getStyle().setColor("#38D12F");
                } else {
                    event.getRelativeElement().getStyle().setColor("red");
                }
               
                AppletBar.APPLET_BAR.changeState();
            }
        });
        
        on_off.addMouseOverHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent event) {
                event.getRelativeElement().getStyle().setCursor(Style.Cursor.POINTER);
            }
        });
        
        rescan.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // send rescan signal
                AppletBar.APPLET_BAR.reScanAll();
            }
        });
        
        chooser.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // send  signal
                AppletBar.APPLET_BAR.showChooser();
            }
        });
    }

    /**
     * Rende visibile la view e carica il codice dell'applet.
     */
    static void showBar() {
        RootPanel.get("applet-bar").setVisible(true);
        applet.setHTML("<applet " +
                "style=\"position:relative; left:-99999px;\"" +
                "id='netmus_applet' name='netmus_applet' " +
                "code=\"applet/NetmusApplet.class\" " +
                "archive=\"applet/netmusApplet.jar, applet/jid3lib-0.5.4.jar\" " +
                "width=1 height=1></applet>");
    }

    /**
     * Rende non visibile la view e toglie il codice dell'applet.
     */
    static void hideBar() {
        RootPanel.get("applet-bar").setVisible(false);
        applet.setHTML("");
    }

    /**
     * Cambia lo stato del pulsante on\_off.
     * @param button_state
     */
    static void setOnOffButton(boolean button_state){
        if (button_state)
            on_off.setText(constants.appletEnable());
        else
            on_off.setText(constants.appletDisable());
    }

    /**
     * Nasconde il pulsante rescan.
     */
    static void rescanNotVisible() {
        rescan.setVisible(false);
    }

    /**
     * Visualizza il pulsante rescan.
     */
    static void rescanVisible() {
        rescan.setVisible(true);
    }

    /**
     *  Visualizza lo stato dell'applet.
     * @param info
     */
    static void showStatus(String info){
        // internazionalizzazione
        if (info.equals("appletON")) info = constants.appletON();
        if (info.equals("appletOFF")) info = constants.appletOFF();
        if (info.equals("noNewFiles")) info = constants.noNewFiles();
        if (info.equals("deviceRemoved")) info = constants.deviceRemoved();

        //modifica le informazioni di stato
        status.setText(info);
    }

    /**
     * Visualizza lo stato della scansione: scansionati m su n.
     * @param actual
     * @param total
     */
    static void scanningStatus(int actual, int total){
        //aggiornare la grafica con le nuove info
        showStatus(actual+"/"+total);
    }
}
