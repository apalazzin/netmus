package it.unipd.netmus.client.applet;

import it.unipd.netmus.client.ClientFactory;
import it.unipd.netmus.client.event.DeviceScannedEvent;
import it.unipd.netmus.client.service.LibraryService;
import it.unipd.netmus.client.service.LibraryServiceAsync;
import it.unipd.netmus.shared.SongDTO;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Nome: AppletBar.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 18 Febbraio 2011
 */
public class AppletBar {

    static AppletBar APPLET_BAR = null; // oggetto istanza statico per SINGLETON
    private boolean visible = false; // stato di visibilita' della barra
    private String user; // identificativo utente loggato
    private boolean state = true; // stato applet alla creazione
    private ClientFactory client_factory = GWT.create(ClientFactory.class);
    private LibraryServiceAsync libraryService = GWT
            .create(LibraryService.class);
    private static AppletConstants constants = GWT
            .create(AppletConstants.class);

    /**
     * Se non esiste già, crea una nuova istanza di AppletBar e la salva nel
     * relativo campo statico. Restituisce l'istanza dopo aver aggiornato
     * l'utente.
     */
    public static AppletBar get(String user) {
        if (APPLET_BAR == null)
            APPLET_BAR = new AppletBar(user);
        else
            APPLET_BAR.setUser(user);
        return APPLET_BAR;
    }

    private TranslateDTOXML translator = new TranslateDTOXML();

    /**
     * costruttore privato
     */
    private AppletBar(String user) {
        this.user = user;
        // pubblico i metodi nativi per renderli invocabili dall'applet
        AppletConnector.makeNativeFunction(this);
        // creo la view dell'applet
        AppletBarView.createView();
    }

    /**
     * Elimina il Menù laterale
     */
    public void appletBarOFF() {
        if (visible) {
            visible = false;
            AppletBarView.hideBar();
        }
    }

    /**
     * Crea il menù laterale.
     */
    public void appletBarON() {
        if (!visible) {
            visible = true;
            AppletBarView.showBar();
        }
    }

    /**
     * Attiva/disattiva la scansione automatica, e aggiorna view e applet di
     * conseguenza.
     */
    void changeState() {
        state = !state;
        if (state)
            AppletBarView.setOnOffButton(false);
        else
            AppletBarView.setOnOffButton(true);
        AppletConnector.setState(state); // cambio dello stato dentro l'applet
    }

    // --- METODI PER LA LOGICA DI COMUNICAZIONE TRA VIEW E APPLET ---

    /**
     * Indica all'applet di riscansionare l'ultimo device.
     */
    void reScanAll() {
        AppletConnector.sendRescan();
    }

    /**
     * Inizializza l'applet.
     */
    void sendStarts() {
        AppletConnector.sendStartsJSNI(user, state);
    }

    /**
     * Indica all'applet di effettuare una scansione manuale.
     */
    void showChooser() {
        AppletConnector.showChooser();
    }

    /**
     * Dall'XML ricevuto dall'applet estrae le canzoni e le invia al server.
     * 
     * @param result
     */
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
                client_factory.getEventBus()
                        .fireEvent(new DeviceScannedEvent());
            }
        };

        libraryService.sendUserNewMusic(user, new_songs, callback);
        AppletBarView.showStatus(constants.pleaseWait());
    }

    /**
     * Aggiorna l'attributo user.
     */
    private void setUser(String user) {
        this.user = user;
    }

}
