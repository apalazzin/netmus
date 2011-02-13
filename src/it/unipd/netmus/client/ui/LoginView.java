/**
 * 
 */
package it.unipd.netmus.client.ui;

import it.unipd.netmus.client.ui.LoginView.Presenter.LoginType;
import it.unipd.netmus.shared.LoginDTO;
import it.unipd.netmus.shared.exception.LoginException;
import it.unipd.netmus.shared.exception.RegistrationException;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * 
 * 
 * @author ValterTexasGroup
 *
 */
public interface LoginView extends IsWidget {
   
   void setPresenter(Presenter listener);
   void setUser(String user);
   void setPassword(String password);
   void setError(String error);
   void setLoginType(LoginType loginType);
   void goRegisterView();


   public interface Presenter
   {
      void goTo(Place place);
      public boolean sendLogin(LoginDTO login) throws LoginException;
      public boolean sendRegistration(LoginDTO login) throws RegistrationException;
      public enum LoginType {NETMUSLOGIN, NETMUSREGISTRATION};
   }
}
