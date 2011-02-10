/**
 * 
 */
package it.unipd.netmus.client.ui;

import it.unipd.netmus.client.place.ProfilePlace;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
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

   
   @UiField Button button;

   
   public LoginViewImpl()
   {
      initWidget(uiBinder.createAndBindUi(this));
   }
   
   @Override
   public void setName(String loginName) {
      this.name = loginName;
   }

   @Override
   public void setPresenter(Presenter listener) {
      this.listener = listener;
   }
   
   
   @UiHandler("button")
   void handleClick(ClickEvent e) {
     Window.alert("Hello, AJAX");
     listener.goTo( new ProfilePlace("test"));
   }

}
