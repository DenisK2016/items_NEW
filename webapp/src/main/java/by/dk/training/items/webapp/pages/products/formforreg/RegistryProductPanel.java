package by.dk.training.items.webapp.pages.products.formforreg;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.RangeValidator;
import org.apache.wicket.validation.validator.StringValidator;

import by.dk.training.items.dataaccess.filters.ProductFilter;
import by.dk.training.items.datamodel.Product;
import by.dk.training.items.datamodel.Type;
import by.dk.training.items.services.ProductService;
import by.dk.training.items.services.TypeService;
import by.dk.training.items.webapp.pages.products.ProductPage;

public class RegistryProductPanel extends Panel {

	private static final long serialVersionUID = 1L;

	@Inject
	private ProductService productService;
	@Inject
	private TypeService typeService;
	private ProductFilter productFilter;
	private Product product;
	private static final List<Boolean> STATUS = Arrays.asList(new Boolean[] { true, false });
	private List<Type> listType = typeService.getAll();
	private List<String> namesType = new ArrayList<>();
	private ArrayList<String> inTypes = new ArrayList<>();
	private List<String> inAddTypes = new ArrayList<>();

	public RegistryProductPanel(String id) {
		super(id);
		product = new Product();
	}

	public RegistryProductPanel(String id, Product product) {
		super(id);
		this.product = product;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		Form<?> form = new Form<>("formreg", new CompoundPropertyModel<Product>(product));
		form.add(new FeedbackPanel("feedback"));

		TextField<String> limit = new TextField<String>("limit");
		limit.setRequired(true);
		limit.add(StringValidator.maximumLength(50));
		limit.add(StringValidator.minimumLength(2));
		limit.add(new PatternValidator("[а-я0-9/ ]+"));
		form.add(limit);

		TextField<String> nameProduct = new TextField<String>("nameProduct");
		nameProduct.setRequired(true);
		nameProduct.add(StringValidator.maximumLength(90));
		nameProduct.add(StringValidator.minimumLength(2));
		form.add(nameProduct);

		TextField<BigDecimal> priceProd = new TextField<BigDecimal>("priceProduct");
		priceProd.setRequired(true);
		priceProd.add(RangeValidator.<BigDecimal> range(new BigDecimal(0), new BigDecimal(1_000_000_000_000_000.00)));
		form.add(priceProd);

		RadioChoice<Boolean> choice = new RadioChoice<>("status", STATUS);
		choice.setRequired(true);
		form.add(choice);

		form.add(new Button("sendButton") {
			@Override
			public void onSubmit() {
				for (String name : inTypes) {
					product.setTypes(listType.get(namesType.indexOf(name.substring(name.lastIndexOf("-") + 1))));

					Type type = listType.get(namesType.indexOf(name.substring(name.lastIndexOf("-") + 1)));
					if (type.getParentType() != null) {
						while (true) {
							type = type.getParentType();
							if (!product.getTypes().contains(type)) {
								product.setTypes(type);
							}

							if (type.getParentType() == null) {
								break;
							}
						}
					}
				}
				if (product.getId() == null) {
					productService.register(product);
				} else {
					productService.update(product);
				}
				setResponsePage(new ProductPage());
			}
		});

		for (int i = 0; i < listType.size(); i++) {
			Type t = listType.get(i);
			String nt = t.getTypeName();
			namesType.add(i, nt);
		}

		List<String> listParentsName = new ArrayList<>();
		List<Type> listTypeCopy = new ArrayList<>(listType);
		while (!listTypeCopy.isEmpty()) {
			for (int i = 0; i < listTypeCopy.size(); i++) {
				Type t = listTypeCopy.get(i);
				Type tPar = t.getParentType();
				String nt = t.getTypeName();
				String pn = "";
				int counter = 0;
				if (tPar != null) {
					pn = tPar.getTypeName();
					if (tPar.getParentType() != null) {
						while (true) {
							tPar = tPar.getParentType();

							if (tPar != null) {
								pn = "-" + pn;
								counter++;
							}

							if (tPar == null) {
								tPar = t.getParentType();
								break;
							}
						}
					}
				}
				if (tPar != null && listParentsName.contains(pn)) {
					for (int k = 0; k < (counter + 1); k++) {
						nt = "-" + nt;
					}
					int indexP = listParentsName.indexOf(pn);
					listParentsName.add(indexP + 1, nt);
					listTypeCopy.remove(t);
				}

				if (tPar == null) {
					listParentsName.add(nt);
					listTypeCopy.remove(t);
				}
			}
		}

		ListMultipleChoice<String> choiceType = new ListMultipleChoice<String>("listType",
				new Model<ArrayList<String>>(inTypes), listParentsName);
		choiceType.setMaxRows(10);
		if (product.getId() == null) {
			choiceType.setRequired(true);
		}
		form.add(choiceType);

		List<Type> existTypesProd = product.getTypes();
		List<String> existNameTypesProd = new ArrayList<String>();
		if (!existTypesProd.isEmpty()) {
			for (Type t : existTypesProd) {
				existNameTypesProd.add(t.getTypeName());
			}
		}
		ListView<String> showsExistType = new ListView<String>("existType", existNameTypesProd) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item) {
				item.add(new Label("existTypes", item.getModel()));
				item.add(new Link<Object>("deleteType", item.getModel()) {
					
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						String selected = (String) getModelObject();
						/*
						 * Попробовать удалять в цикле НЕ сразу с продукта, а со списка(listTypes) те которые удалять не надо из продукта. А потом сразу сделать removeAll(оставшиеся в listTypes)
						 */
//						List<Type> listTypes = product.getTypes();
//						for (Type t : listTypes) {
//							Type tt = t.getParentType();
//							if (tt != null) {
//								while (true) {
//									if (tt != null && tt.getTypeName().equals(selected)) {
//										product.getTypes().remove(t);
//										break;
//									}
//									if (tt == null) {
//										break;
//									}
//									tt = tt.getParentType();
//								}
//							}
//						}
						product.getTypes().remove(existNameTypesProd.indexOf(selected));
						setResponsePage(new ProductRegPage(product));
					}
				});
			}
		};
		form.add(showsExistType);

		form.add(new SubmitLink("saveType") {
			@Override
			public void onSubmit() {
				super.onSubmit();
				for (String s : inTypes) {
					if (!inAddTypes.contains(s)) {
						inAddTypes.add(s.substring(s.lastIndexOf("-") + 1));

						Type type = listType.get(namesType.indexOf(s.substring(s.lastIndexOf("-") + 1)));
						if (type.getParentType() != null) {
							while (true) {
								type = type.getParentType();
								String nm = namesType.get(listType.indexOf(type));
								if (!inAddTypes.contains(nm)) {
									inAddTypes.add(nm);
								} else {
									break;
								}

								if (type.getParentType() == null) {
									break;
								}
							}
						}
					}
				}
			}
		});

		ListView<String> addTypes = new ListView("addTypes", inAddTypes) {
			@Override
			protected void populateItem(ListItem item) {
				item.add(new Label("addType", item.getModel()));
				item.add(new Link("dellType", item.getModel()) {
					@Override
					public void onClick() {
						String selected = (String) getModelObject();
						inTypes.remove(selected);
					}
				});
			}
		};

		form.add(addTypes);

		add(form);

		add(new Link("BackToProducts") {
			@Override
			public void onClick() {
				setResponsePage(new ProductPage());
			}
		});

	}

}
