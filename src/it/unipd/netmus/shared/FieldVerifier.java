package it.unipd.netmus.shared;

/**
 * 
 * Nome: FieldVerifier.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 13 Febbraio 2011
 * 
 * 
 * Tipo, obiettivo e funzione del componente:
 * 
 * Questa semplice classe contiene i metodi statici necessari a validare con
 * livello di sicurezza accetabile i dati inseriti dall'utente, si rende
 * fondamentale nelle operazioni di autenticazione e registrazione.
 * 
 * 
 */

public class FieldVerifier {

    private static final String SEPARATOR = "-vtg-";

    /**
     * Converte la string in input in una string pulita da ogni carattere
     * diverso da lettere non accentate e numeri Ritrona stringa vuota in caso
     * di valore null.
     */
    public static String cleanString(String s) {
        if (s == null)
            return "";
        String t = s.toLowerCase();
        t = t.replaceAll("é|è|ê|ë|æ|ē|ĕ|ė|ę|ě|ẹ|ẻ|ẽ|ế|ề|ể|ễ|ệ", "e");
        t = t.replaceAll("á|à|â|ã|ä|å|ā|ă|ą|ằ|ạ|ả|ấ|ầ|ẩ|ẫ|ậ|ắ|ằ|ẳ|ẵ|ặ", "a");
        t = t.replaceAll("í|ì|î|ï|ĩ|ī|ĭ|į|ı|ỉ|ị", "i");
        t = t.replaceAll("ó|ò|ô|õ|ö|ō|ŏ|ő|ọ|ỏ|ố|ồ|ổ|ỗ|ộ|ớ|ờ|ở|ỡ|ợ", "o");
        t = t.replaceAll("ù|ù|û|ü|ũ|ū|ŭ|ů|ű|ų|ụ|ủ|ứ|ừ|ử|ữ|ự", "u");
        t = t.replaceAll(
                " |'|`|\"|^|\\?|!|%|&|@|,|\\.|#|\\{|\\}|\\[|\\]|\\||-|_|/|;|:|<|>|\\\\|\\*|\\+|=|\\(|\\)",
                "");
        return t;
    }

    public static String generateAlbumId(String name, String artist) {
        String song_id = (FieldVerifier.cleanString(name)
                + FieldVerifier.SEPARATOR + FieldVerifier.cleanString(artist));
        return song_id;
    }

    public static String generatePlaylistId(String name, String owner) {
        String song_id = (FieldVerifier.cleanString(name)
                + FieldVerifier.SEPARATOR + FieldVerifier.cleanString(owner));
        return song_id;
    }

    public static String generateSongId(String title, String artist,
            String album) {
        String song_id = (FieldVerifier.cleanString(title)
                + FieldVerifier.SEPARATOR + FieldVerifier.cleanString(artist)
                + FieldVerifier.SEPARATOR + FieldVerifier.cleanString(album));
        return song_id;
    }

    public static boolean isValidEmail(String email) {

        return email
                .matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    }

    public static boolean isValidNickName(String nickname) {

        if (nickname == null)
            return false;
        else
            return nickname.length() > 4;

    }

    public static boolean isValidPassword(String password) {

        if (password == null)
            return false;
        else
            return password.length() > 4;
    }

}
