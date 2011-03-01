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
