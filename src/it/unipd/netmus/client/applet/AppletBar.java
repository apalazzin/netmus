/**
 * 
 */
package it.unipd.netmus.client.applet;

import java.util.List;

import it.unipd.netmus.client.service.LibraryService;
import it.unipd.netmus.client.service.LibraryServiceAsync;
import it.unipd.netmus.shared.SongDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author ValterTexasGroup
 *
 */
public class AppletBar {
	
	private static AppletBar APPLET_BAR = null;
    private boolean visible = false;
    private AppletConstants constants = GWT.create(AppletConstants.class);
    private Label title = new Label(constants.title());
    private Label onOff = new Label();
    private Button rescan = new Button(); // VISIBILE FINITA LA SCANSIONE
    private HTMLPanel button_container = new HTMLPanel("");
    private Button chooser = new Button();
    private TextBox status = new TextBox();
    private HTML applet = new HTML();
    private String user;
    private String original_user;
    private boolean state;
    private TranslateDTOXML translator = new TranslateDTOXML();
    private HTMLPanel main = new HTMLPanel("");
    
    private LibraryServiceAsync libraryService = GWT.create(LibraryService.class);
    
    public static AppletBar get(String user, boolean state) {
        if (APPLET_BAR == null)
            APPLET_BAR = new AppletBar(user, state);
        APPLET_BAR.setUser(user);
        return APPLET_BAR;
    }
    
    private void setUser(String user) {
        this.user = user;
    }
    
    private AppletBar(String user, boolean state) {
    	
        this.original_user = user;
    	this.user=user.replaceAll("@\\S*", "");
    	this.state=state;
        
        makeNativeFunction(this);


        RootPanel.get("applet-bar").setStyleName("applet-bar");

        main.setStyleName("applet_main");
        button_container.setStyleName("applet_button_cnt");
        
        onOff.setSize("50px","14px");
        onOff.setStyleName("applet_switch");
        
        if (state) onOff.setText(constants.appletDisable());
        else onOff.setText(constants.appletEnable());
        
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
        main.add(onOff);    
        main.add(applet);
        RootPanel.get("applet-bar").add(main);
        
        
        onOff.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // send changeStatus signal
                if(event.getRelativeElement().getStyle().getColor().equals("red")) {
                    event.getRelativeElement().getStyle().setColor("#38D12F");
                } else {
                    event.getRelativeElement().getStyle().setColor("red");
                }
               
                changeState();
            }
        });
        
        onOff.addMouseOverHandler(new MouseOverHandler() {

            @Override
            public void onMouseOver(MouseOverEvent event) {
                event.getRelativeElement().getStyle().setCursor(Style.Cursor.POINTER);
            }
            
        });
        
        rescan.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // send rescan signal
                reScanAll();
            }
        });
        
        chooser.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // send rescan signal
                showChooser();
            }
        });
    }
    
    public void appletBarON() {
        if (!visible) {
            visible = true;
            RootPanel.get("applet-bar").setVisible(true);
            applet.setHTML("<applet " +
            		"style=\"position:relative; left:-99999px;\"" +
            		"id='netmus_applet' name='netmus_applet' " +
                    "code=\"applet/NetmusApplet.class\" " +
                    "archive=\"applet/netmusApplet.jar, applet/jid3lib-0.5.4.jar\" " +
                    "width=1 height=1></applet>");
            
            System.out.println("Applet caricata");
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
        state = !state;
        if (state)
            onOff.setText(constants.appletDisable());
        else
            onOff.setText(constants.appletEnable());
        
        setState(state);
    }
    
    // per dire all'applet di rifare interamente
    // la scansione dopo che RESCAN e' stato premuto
    private void reScanAll() {
        showStatus("RESCAN");
        
        sendRescan();
    }
    
    /**
     * Metodo che deve chiamare il metodo dell'applet 
     * letsGo(String username, boolean state)
     * per mandare user e stato iniziale e avviare il thread
     */
    private void sendStarts() {
    	System.out.println("invio i dati: "+user);
    	sendStartsJSNI(user,state);
    	System.out.println("Inviati!");
    }
    
    private native void sendStartsJSNI( String user, boolean state )/*-{
    	var t = $doc.getElementById('netmus_applet');
    	t.letsGO(user,state);
    }-*/;
    
    private native void setState( boolean state )/*-{
        var t = $doc.getElementById('netmus_applet');
        t.setState(state);
    }-*/;
    
    private native void sendRescan()/*-{
        var t = $doc.getElementById('netmus_applet');
        t.rescanAll();
    }-*/;
    
    private native void showChooser()/*-{
        var t = $doc.getElementById('netmus_applet');
        t.showChooser();
    }-*/;
    
    private void scanningStatus(int actual, int total){
    	//aggiornare la grafica con le nuove info
        showStatus(actual+"/"+total);
    }
    
    private void showStatus(String status){
    	//modifica le informazioni di stato sulla grafica
    	System.out.println("Stato: "+status);
    	this.status.setText("Stato: "+status);
    }
    
    private void translateXML(String result) {
        
        System.out.println("Dati XML arrivati: \n"+result);
        List<SongDTO> new_songs = translator.XMLToDTO(result);
        if (new_songs == null){showStatus("Errore nel parsing XML");}
        System.out.println("parsing riuscito");
        
        this.status.setText("Dati XML arrivati");
        AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                showStatus("Sending Error");
                System.out.println("KO");
                System.err.println(caught.toString());
            }
            @Override
            public void onSuccess(Void result) {
                showStatus("Sent to Server");
                System.out.println("OK");
            }
        };
        
        libraryService.sendUserNewMusic(original_user, new_songs, callback);
        System.out.println("Dati XML spediti");
    }
    
    private void rescanNotVisible() {
        rescan.setVisible(false);
    }
    private void rescanVisible() {
        rescan.setVisible(true);
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
    $wnd.showStatus = function (s) {
    x.@it.unipd.netmus.client.applet.AppletBar::showStatus(Ljava/lang/String;)(s);
    };
    $wnd.rescanVisible = function () {
    x.@it.unipd.netmus.client.applet.AppletBar::rescanVisible()();
    };
    $wnd.rescanNotVisible = function () {
    x.@it.unipd.netmus.client.applet.AppletBar::rescanNotVisible()();
    };
    }-*/;
    // mancano parametri sopra in ingresso Stringa XML da applet
}
