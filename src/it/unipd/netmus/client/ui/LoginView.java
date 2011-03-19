/**
 * 
 */
package it.unipd.netmus.client.ui;

import it.unipd.netmus.client.ui.LoginView.Presenter.LoginType;
import it.unipd.netmus.shared.exception.LoginException;
import it.unipd.netmus.shared.exception.RegistrationException;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * Nome: LoginView.java
 * Autore:  VT.G
 * Licenza: GNU GPL v3
 * Data Creazione: 16 Febbraio 2011
*/
public interface LoginView extends IsWidget {
	/**
	 *Questo metodo viene usato da
	 *LoginActivity per impostare una sua istanza come implementazione
	 *del presenter di LoginView.
	 */ 
   void setPresenter(Presenter listener);
	/**
	 *Questo metodo viene usato da
	 *LoginActivity per impostare l'username inserito nel precedente
	 *tentativo di login o registrazione.
	 */ 
   void setUser(String user);
	/**
	 *Questo metodo viene usato da
	 *LoginActivity per impostare la password inserita nel precedente
	 *tentativo di login o registrazione.
	 */ 
   void setPassword(String passwordHash);
	/**
	 *Questo metodo viene usato da
	 *LoginActivity per impostare il testo di un errore occorso nel
	 *precedente tentativo di login o registrazione.
	 */ 
   void setError(String error);
	/**
	 *Serve ad impostare il tipo di login o
	 *registrazione che si sta effettuando
	 */ 
   void setLoginType(LoginType loginType);
   	/**
   	 *Aggiusta il layout all'avvio
   	 */
   void setLayout();
   
   public interface Presenter
   {
      void goTo(Place place);
      void sendLogin(String login, String password) throws LoginException;
      void sendGoogleLogin(String user, String password) throws LoginException;
      void sendRegistration(String login, String password, String confirmPassword) throws RegistrationException;
      enum LoginType {NETMUSLOGIN, NETMUSREGISTRATION, GOOGLELOGIN}
    
      /**
       *Cambia la lingua
       */
      void changeLanguage(String string);
   }
}
