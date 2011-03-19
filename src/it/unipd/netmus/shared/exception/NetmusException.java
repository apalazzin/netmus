package it.unipd.netmus.shared.exception;

/**
 * Nome: NetmusException.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 13 Febbraio 2011
 * 
 */
@SuppressWarnings("serial")
public class NetmusException extends Exception {
    private String more_info;

    public NetmusException() {
        super();
    }

    public NetmusException(String moreInfo) {
        super();
        this.more_info = moreInfo;
    }

    public String getMoreInfo() {
        return more_info;
    }
}
