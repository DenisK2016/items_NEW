package by.dk.training.items.webapp.pages.products.formforreg;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.form.palette.Palette;
import org.apache.wicket.extensions.markup.html.form.palette.theme.DefaultTheme;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.CollectionModel;
import org.apache.wicket.validation.validator.RangeValidator;
import org.apache.wicket.validation.validator.StringValidator;

import by.dk.training.items.dataaccess.filters.ProductFilter;
import by.dk.training.items.datamodel.Product;
import by.dk.training.items.datamodel.Type;
import by.dk.training.items.services.ProductService;
import by.dk.training.items.services.TypeService;
import by.dk.training.items.webapp.app.AuthorizedSession;
import by.dk.training.items.webapp.pages.products.ProductPage;

@AuthorizeAction(roles = { "ADMIN", "COMMANDER", "OFFICER" }, action = Action.RENDER)
public class RegistryProductPanel extends Panel {

	private static final long serialVersionUID = 1L;

	@Inject
	private ProductService productService;
	@Inject
	private TypeService typeService;
	private ProductFilter productFilter;
	private Product product;
	private static final List<String> STATUS = Arrays.asList(new String[] { "Да", "Нет" });
	private List<Type> listType = typeService.getAll();
	private List<String> namesType = new ArrayList<>();
	private ArrayList<String> inTypes = new ArrayList<>();
	private List<String> inAddTypes = new ArrayList<>();
	private String stat;

	List<Type> existTypesProd;
	List<String> existNameTypesProd = new ArrayList<String>();
	private ModalWindow modalWindow;

	public RegistryProductPanel(ModalWindow modalWindow) {
		super(modalWindow.getContentId());
		this.modalWindow = modalWindow;
		this.modalWindow.setInitialHeight(700);
		product = new Product();
		existTypesProd = new ArrayList<>();
	}

	public RegistryProductPanel(ModalWindow modalWindow, Product product) {
		super(modalWindow.getContentId());
		this.modalWindow = modalWindow;
		this.modalWindow.setInitialHeight(700);
		this.product = product;
		existTypesProd = this.product.getTypes();

	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		Form<?> form = new Form<>("formreg", new CompoundPropertyModel<Product>(product));
		form.add(new FeedbackPanel("feedback"));

		TextField<Double> weightProd = new TextField<Double>("weight");
		weightProd.setRequired(true);
		weightProd.add(RangeValidator.<Double> range(new Double(0), Double.MAX_VALUE));
		form.add(weightProd);

		TextField<String> nameProduct = new TextField<String>("nameProduct");
		nameProduct.setRequired(true);
		nameProduct.add(StringValidator.maximumLength(90));
		nameProduct.add(StringValidator.minimumLength(2));
		form.add(nameProduct);

		TextField<BigDecimal> priceProd = new TextField<BigDecimal>("priceProduct");
		priceProd.setRequired(true);
		priceProd.add(RangeValidator.<BigDecimal> range(new BigDecimal(0), new BigDecimal(1_000_000_000_000_000.00)));
		form.add(priceProd);

		RadioChoice<String> choice = new RadioChoice<>("status", new PropertyModel<String>(this, "stat"), STATUS);
		choice.add(new AjaxFormComponentUpdatingBehavior("change") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
			}
		});
		choice.setRequired(true);
		form.add(choice);

		form.add(new Button("sendButton") {
			@Override
			public void onSubmit() {
				product.getTypes().clear();
				if (stat.equals("Да")) {
					product.setStatus(true);
				} else {
					product.setStatus(false);
				}

				for (String name : inTypes) {
					product.setTypes(listType.get(namesType.indexOf(name)));

					Type type = listType.get(namesType.indexOf(name));
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
					product.setIdUser(AuthorizedSession.get().getUser());
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

		if (!existTypesProd.isEmpty()) {
			for (Type t : existTypesProd) {
				existNameTypesProd.add(t.getTypeName());
			}
			inTypes.addAll(existNameTypesProd);
		}

		final Palette<String> palette = new Palette<>("palleteType", Model.ofList(inTypes),
				new CollectionModel<String>(namesType), new ProductChoiceRenderer(namesType), 15, false, true);
		palette.add(new DefaultTheme());
		form.add(palette);
		add(form);

		form.add(new AjaxLink<Void>("BackToProducts") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				modalWindow.close(target);
			}
		});

		if (product.getId() == null) {
			form.add(new Label("regOrUpdate", "Регистрация нового продукта."));
		} else {
			form.add(new Label("regOrUpdate", "Изменение продукта."));
		}

	}

}
