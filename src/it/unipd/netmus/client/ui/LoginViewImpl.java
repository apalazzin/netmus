/**
 * 
 */
package it.unipd.netmus.client.ui;

import it.unipd.netmus.client.place.LoginPlace;
import it.unipd.netmus.client.place.ProfilePlace;
import it.unipd.netmus.client.ui.LoginView.Presenter.LoginType;
import it.unipd.netmus.shared.LoginDTO;
import it.unipd.netmus.shared.exception.LoginException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;


/**
 * @author smile
 *
 */
public class LoginViewImpl extends Composite implements LoginView {

   private static LoginViewImplUiBinder uiBinder = GWT.create(LoginViewImplUiBinder.class);
   
   interface LoginViewImplUiBinder extends UiBinder<Widget, LoginViewImpl>
   {
   }
   
   private Presenter listener;
   private LoginType type;
   
   @UiField Label login;
   @UiField Label account;
   @UiField TextBox user;
   @UiField TextBox password;
   @UiField RadioButton check_google;
   @UiField RadioButton check_netmus;

   
   public LoginViewImpl()
   {
      initWidget(uiBinder.createAndBindUi(this));
      
      check_google.setValue(true);
   }

   @Override
   public void setPresenter(Presenter listener) {
      this.listener = listener;
   }
   
   
   @UiHandler("login")
   void handleClick(ClickEvent e) {
	 try {
		 listener.sendLogin(new LoginDTO(user.getText(),password.getText()));
	} catch (LoginException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
   }

   @UiHandler("check_google")
   void handleClickGoogle(ClickEvent e) {

	   account.setText("Account Google");
	   
	   if(check_netmus.getValue()) {
		   
		   check_netmus.setValue(false);
		   
	   }
     
   }
   
   @UiHandler("check_netmus")
   void handleClickNetmus(ClickEvent e) {
	   
	   account.setText("Account Netmus");
	   
	   if(check_google.getValue()) {
		   
		   check_google.setValue(false);
		   
	   }
     
   }

@Override
public void setUser(String user) {
	this.user.setText(user);
}

@Override
public void setPassword(String password) {
	this.password.setText(password);
}

@Override
public void setError(String error) {
	// TODO Auto-generated method stub
	
}

@Override
public void setLoginType(LoginType loginType) {
	this.type = loginType;
}

@Override
public void goRegisterView() {
	// TODO Auto-generated method stub
	
}

   
}
