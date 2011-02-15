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
public class EditSongsViewImpl extends Composite implements EditSongsView {

   private static EditSongsViewImplUiBinder uiBinder = GWT.create(EditSongsViewImplUiBinder.class);

   interface EditSongsViewImplUiBinder extends UiBinder<Widget, EditSongsViewImpl>
   {
   }
   
   private Presenter listener;
   private String name;
   
   public EditSongsViewImpl()
   {
      initWidget(uiBinder.createAndBindUi(this));
   }
   
   @Override
   public void setName(String editSongsName) {
      this.name = editSongsName;
   }

   @Override
   public void setPresenter(Presenter listener) {
      this.listener = listener;
   }

}
