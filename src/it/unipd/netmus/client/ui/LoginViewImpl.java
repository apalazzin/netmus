/**
 * 
 */
package it.unipd.netmus.client.ui;

import it.unipd.netmus.client.place.ProfilePlace;

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
   private String name;

   
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
   public void setName(String loginName) {
      this.name = loginName;
   }

   @Override
   public void setPresenter(Presenter listener) {
      this.listener = listener;
   }
   
   
   @UiHandler("login")
   void handleClick(ClickEvent e) {
     listener.goTo( new ProfilePlace("test"));
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

   
}
