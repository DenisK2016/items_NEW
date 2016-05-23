package by.dk.training.items.webapp.pages.types.formforreg;

import by.dk.training.items.datamodel.Type;
import by.dk.training.items.webapp.pages.AbstractPage;

public class TypeRegPage extends AbstractPage {
	
	private static final long serialVersionUID = 1L;
	private Type type;

	public TypeRegPage() {
		super();

	}
	
	public TypeRegPage(Type type) {
		super();
		this.type = type;

	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		if(type != null){
			add(new RegistryTypePanel("regPanProd", type));
		}else{
			add(new RegistryTypePanel("regPanProd"));
		}
		
	}
}
