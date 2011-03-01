/**
 * 
 */
package it.unipd.netmus.shared;

/**
 * @author ValterTexasGroup
 *
 *
 *Tipo, obiettivo e funzione del componente:
 *
 *Come tutte le altri classi di questo package questa classe permette lo scambio di 
 *informazioni tra client e server all'interno delle chiamate RPC.
 *Contiene e rappresenta tutti i dati di un utente, a partire da quelli visualizzati 
 *sul profilo fino a quelli interni del sistema. In particolare contiene la libreria 
 *musicale dell'utente.
 *
 * 
 */
@SuppressWarnings("serial")
public class UserCompleteDTO extends UserDTO {

	private MusicLibraryDTO music_library;

    public UserCompleteDTO() {
        super();
    }
    
    public MusicLibraryDTO getMusicLibrary() {
        return music_library;
    }

    public void setMusicLibrary(MusicLibraryDTO music_library) {
        this.music_library = music_library;
    }
}
