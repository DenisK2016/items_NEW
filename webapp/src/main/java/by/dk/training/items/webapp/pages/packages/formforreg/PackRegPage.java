package by.dk.training.items.webapp.pages.packages.formforreg;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;

import com.googlecode.wicket.jquery.ui.form.spinner.AjaxSpinner;

import by.dk.training.items.datamodel.Package;
import by.dk.training.items.webapp.pages.AbstractPage;

public class PackRegPage extends AbstractPage {
	
	private static final long serialVersionUID = 1L;
	private Package pack;

	public PackRegPage() {
		super();

	}
	
	public PackRegPage(Package pack) {
		super();
		this.pack = pack;

	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		if(pack != null){
			add(new RegistryPackPanel("regPanPack", pack));
		}else{
			add(new RegistryPackPanel("regPanPack"));
		}
		
		Form<Integer> formSpin = new Form<Integer>("formSpin", Model.of(0));
		add(formSpin);
		
		final AjaxSpinner<Integer> ajaxSpinner = new AjaxSpinner<Integer>("spinner", formSpin.getModel(), Integer.class) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onSpin(AjaxRequestTarget target, Integer value)
			{
				
			}
			
		};
		
		formSpin.add(ajaxSpinner);
	}
}
