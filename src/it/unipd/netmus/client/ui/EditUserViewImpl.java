/**
 * 
 */
package it.unipd.netmus.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author smile
 *
 */
public class EditUserViewImpl extends Composite implements EditUserView {

   private static EditUserViewImplUiBinder uiBinder = GWT.create(EditUserViewImplUiBinder.class);

   interface EditUserViewImplUiBinder extends UiBinder<Widget, EditUserViewImpl>
   {
   }
   
   private Presenter listener;
   private String name;
   
   public EditUserViewImpl()
   {
      initWidget(uiBinder.createAndBindUi(this));
   }
   
   @Override
   public void setName(String editUserName) {
      this.name = editUserName;
   }

   @Override
   public void setPresenter(Presenter listener) {
      this.listener = listener;
   }

}
