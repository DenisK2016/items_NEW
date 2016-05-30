package by.dk.training.items.webapp.pages.products.formforreg;

import java.util.List;

import org.apache.wicket.markup.html.form.ChoiceRenderer;

public class ProductChoiceRenderer extends ChoiceRenderer<String> {

	private static final long serialVersionUID = 1L;
	// public static final ProductChoiceRenderer INSTANCE = new
	// ProductChoiceRenderer();
	private List<String> list;

	public ProductChoiceRenderer(List<String> list) {
		super();
		this.list = list;
	}

	@Override
	public Object getDisplayValue(String object) {

		return object;
	}

	@Override
	public String getIdValue(String object, int index) {
		return String.valueOf(list.indexOf(object));
	}
}
