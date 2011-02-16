/**
 * 
 */
package it.unipd.netmus.client.ui;

import it.unipd.netmus.client.place.LoginPlace;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author smile
 *
 */
public class ProfileViewImpl extends Composite implements ProfileView {

   private static ProfileViewImplUiBinder uiBinder = GWT.create(ProfileViewImplUiBinder.class);

   interface ProfileViewImplUiBinder extends UiBinder<Widget, ProfileViewImpl>
   {
   }
   
   private Presenter listener;
   private String name;
   
   @UiField Anchor logout;
   @UiField Anchor back;
   
   public ProfileViewImpl()
   {
      initWidget(uiBinder.createAndBindUi(this));
   }
   
   @UiHandler("back")
   void handleClickBack(ClickEvent e) {
      listener.goTo(new LoginPlace(name));
   }
   
   @UiHandler("logout")
   void handleClickLogout(ClickEvent e) {
      listener.logout();
   }
   
   @Override
   public void setName(String profileName) {
      this.name = profileName;
   }

   @Override
   public void setPresenter(Presenter listener) {
      this.listener = listener;
   }

}
