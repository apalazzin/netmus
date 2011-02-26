/**
 * 
 */
package it.unipd.netmus.client.applet;

import java.util.List;

import it.unipd.netmus.client.ClientFactory;
import it.unipd.netmus.client.event.DeviceScannedEvent;
import it.unipd.netmus.client.service.LibraryService;
import it.unipd.netmus.client.service.LibraryServiceAsync;
import it.unipd.netmus.shared.SongDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author ValterTexasGroup
 *
 */
public class AppletBar {
	
	static AppletBar APPLET_BAR = null; // oggetto istanza statico per SINGLETON
    private boolean visible = false; // stato di visibilita' della barra
    private String user; // identificativo utente loggato
    private boolean state = true; // stato applet alla creazione
    private ClientFactory client_factory = GWT.create(ClientFactory.class);
    private LibraryServiceAsync libraryService = GWT.create(LibraryService.class);
    private static AppletConstants constants = GWT.create(AppletConstants.class);
    
    private TranslateDTOXML translator = new TranslateDTOXML();
    
    // getter per questa classe che utilizza il patter SINGLETON
    public static AppletBar get(String user) {
        if (APPLET_BAR == null)
            APPLET_BAR = new AppletBar(user);
        else
            APPLET_BAR.setUser(user);
        return APPLET_BAR;
    }
    
    // memorizzo username dell'utente loggato
    private void setUser(String user) {
        this.user = user;
    }
    
    // costruttore privato (SINGLETON)
    private AppletBar(String user) {
    	this.user=user;
    	// pubblico i metodi nativi per renderli invocabili dall'applet
        AppletConnector.makeNativeFunction(this);
        // creo la view dell'applet
        AppletBarView.createView();
    }
    
    // carico l'applet e rendo visibile la barra
    public void appletBarON() {
        if (!visible) {
            visible = true;
            AppletBarView.showBar();
        }
    }
    
    // spengo l'applet e rendo invisibile la barra
    public void appletBarOFF() {
        if (visible) {
            visible = false;
            AppletBarView.hideBar();
        }
    }
    
    
    // --- METODI PER LA LOGICA DI COMUNICAZIONE TRA VIEW E APPLET ---
    
    // metodo per far attivare/disattivare la ricerca automatica dei dispositivi
    void changeState() {
        state = !state;
        if (state)
            AppletBarView.setOnOffButton(false);
        else
            AppletBarView.setOnOffButton(true);
        AppletConnector.setState(state); // cambio dello stato dentro l'applet
    }
    
    // avvisa applet di fare una scansione completa del dispositivo inserito
    // ignorando il vecchio file di log che escludeva i file gia' scansionati
    void reScanAll() {
        AppletConnector.sendRescan();
    }
    
    void showChooser() {
        AppletConnector.showChooser();
    }
    
    void sendStarts() {
        AppletConnector.sendStartsJSNI(user,state);
    }

    void translateXML(String result) {
        
        AppletBarView.showStatus(constants.xmlParsing());
        List<SongDTO> new_songs = translator.XMLToDTO(result);
        if (new_songs == null)
            AppletBarView.showStatus(constants.xmlParsingError());
        
        AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                AppletBarView.showStatus(constants.sendingError());
                System.err.println(caught.toString());
            }
            @Override
            public void onSuccess(Void result) {
                AppletBarView.showStatus(constants.sentToServer());
                client_factory.getEventBus().fireEvent(new DeviceScannedEvent());
            }
        };
        
        libraryService.sendUserNewMusic(user, new_songs, callback);
        AppletBarView.showStatus(constants.pleaseWait());
    }

}
