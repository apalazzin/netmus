/**
 * 
 */
package it.unipd.netmus.client.ui;

import it.unipd.netmus.client.ui.LoginView.Presenter.LoginType;
import it.unipd.netmus.shared.exception.LoginException;
import it.unipd.netmus.shared.exception.RegistrationException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;


/**
 * @author smile
 *
 */
public class LoginViewImpl extends Composite implements LoginView {

   private static LoginViewImplUiBinder uiBinder = GWT.create(LoginViewImplUiBinder.class);
   MyConstants myConstants = GWT.create(MyConstants.class);
   
   interface LoginViewImplUiBinder extends UiBinder<Widget, LoginViewImpl>
   {
   }
   
   private Presenter listener;
   private LoginType type;
   
   @UiField Label login;
   @UiField Label account;
   @UiField Label register;
   @UiField Label error;
   
   @UiField HTMLPanel container;
   
   @UiField TextBox user;
   @UiField PasswordTextBox password;
   @UiField PasswordTextBox c_password;
   @UiField RadioButton check_google;
   @UiField RadioButton check_netmus;
   
   public LoginViewImpl()
   {
      initWidget(uiBinder.createAndBindUi(this));
            
      //localizzazione
      login.setText(myConstants.loginLabel());
      register.setText(myConstants.registerSwitchLabel());
      account.setText(myConstants.accountGoogle());
      check_google.setValue(true);
      
      Timer timerMain = new Timer() {
		  public void run() {
		      container.getElement().getStyle().setProperty("opacity", "1");
		  }
	  };
	  timerMain.schedule(200);
      
   }

   @Override
   public void setPresenter(Presenter listener) {
      this.listener = listener;
   }
   
   @UiHandler("login")
   void handleClickLogin(ClickEvent e) {
	 try {
		 if(type != LoginType.NETMUSREGISTRATION)
			 listener.sendLogin(user.getText(), password.getText());
		 else
			 listener.sendRegistration(user.getText(), password.getText(), c_password.getText());
	 } catch (LoginException e1) {
		 e1.printStackTrace();
	 } catch (RegistrationException e2) {
		 e2.printStackTrace();
	 }
   }
   
   @UiHandler(value={"password", "c_password"})
   void handlePressEnterPassword(KeyPressEvent e) {
       if (e.getNativeEvent().getKeyCode()==KeyCodes.KEY_ENTER) {
           try {
               if(type != LoginType.NETMUSREGISTRATION)
                   listener.sendLogin(user.getText(), password.getText());
               else
                   listener.sendRegistration(user.getText(), password.getText(), c_password.getText());
           } catch (LoginException e1) {
               e1.printStackTrace();
           } catch (RegistrationException e2) {
               e2.printStackTrace();
           }
       }
   }

   @UiHandler("register")
   void handleClickRegister(ClickEvent e) {
	   
	  if(type != LoginType.NETMUSREGISTRATION) {
		  
		  type = LoginType.NETMUSREGISTRATION;
		  
	      DOM.getElementById("password_check").setInnerHTML(myConstants.passwordCheck());

		  
		  DOM.getElementById("registrazione").getStyle().setProperty("opacity", "1");
		  DOM.getElementById("registrazione").getStyle().setProperty("height", "40px");
	
		  	  
		  register.getElement().getStyle().setProperty("opacity", "0");
		  Timer timerRegisterLabel = new Timer() {
			  public void run() {
				  register.setText(myConstants.loginSwitchLabel());
				  register.getElement().getStyle().setProperty("opacity", "1");
			  }
		  };
		  timerRegisterLabel.schedule(200);
	
		  
		  login.getElement().getStyle().setProperty("opacity", "0");
		  Timer timerLoginLabel = new Timer() {
			  public void run() {
				  login.setText(myConstants.registerLabel());
				  login.getElement().getStyle().setProperty("opacity", "1");
			  }
		  };
		  timerLoginLabel.schedule(200);
		  
		   account.setText(myConstants.accountNetmus());
		   check_google.setValue(false);
		   check_netmus.setValue(true);
		   check_google.setEnabled(false);
			   

	  } else {
		  
		  type = LoginType.NETMUSLOGIN;
		  
		  DOM.getElementById("registrazione").getStyle().setProperty("opacity", "0");
		  DOM.getElementById("registrazione").getStyle().setProperty("height", "0");
		  
		  	  
		  register.getElement().getStyle().setProperty("opacity", "0");
		  Timer timerRegisterLabel = new Timer() {
			  public void run() {
				  register.setText(myConstants.registerSwitchLabel());
				  register.getElement().getStyle().setProperty("opacity", "1");
			  }
		  };
		  timerRegisterLabel.schedule(200);
	
		  
		  login.getElement().getStyle().setProperty("opacity", "0");
		  Timer timerLoginLabel = new Timer() {
			  public void run() {
				  login.setText(myConstants.loginLabel());
				  login.getElement().getStyle().setProperty("opacity", "1");
			  }
		  };
		  timerLoginLabel.schedule(200);
		  
		  check_google.setValue(false);
		  check_netmus.setValue(true);
		  check_google.setEnabled(true);

	  }
   }

   
   @UiHandler("check_google")
   void handleClickGoogle(ClickEvent e) {

	   account.setText(myConstants.accountGoogle());
	   type = LoginType.GOOGLELOGIN;
	   
	   if(check_netmus.getValue()) {
		   
		   check_netmus.setValue(false);
		   
	   }
     
   }
   
   @UiHandler("check_netmus")
   void handleClickNetmus(ClickEvent e) {
	   
	   account.setText(myConstants.accountNetmus());
	   type = LoginType.NETMUSLOGIN;
	   
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
    	this.error.setText(error);	
    }
    
    @Override
    public void setLoginType(LoginType loginType) {
    	this.type = loginType;
    }
   
    
    @Override
    public void setLayout() {
        
        c_password.setText("");
        
        DOM.getElementById("registrazione").getStyle().setProperty("opacity", "0");
        DOM.getElementById("registrazione").getStyle().setProperty("height", "0");
            
        register.getElement().getStyle().setProperty("opacity", "0");
    
        register.setText(myConstants.registerSwitchLabel());
        register.getElement().getStyle().setProperty("opacity", "1");

        login.getElement().getStyle().setProperty("opacity", "0");
  
        login.setText(myConstants.loginLabel());
        login.getElement().getStyle().setProperty("opacity", "1");
        check_google.setValue(false);
        check_netmus.setValue(true);
        check_google.setEnabled(true);

    }
    
}
