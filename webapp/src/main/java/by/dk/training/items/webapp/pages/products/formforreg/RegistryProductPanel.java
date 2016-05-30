package by.dk.training.items.webapp.pages.products.formforreg;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.extensions.markup.html.form.palette.Palette;
import org.apache.wicket.extensions.markup.html.form.palette.theme.DefaultTheme;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.CollectionModel;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.RangeValidator;
import org.apache.wicket.validation.validator.StringValidator;

import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.ui.widget.tooltip.CustomTooltipBehavior;

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
	private static final List<Boolean> STATUS = Arrays.asList(new Boolean[] { true, false });
	private List<Type> listType = typeService.getAll();
	private List<String> namesType = new ArrayList<>();
	private ArrayList<String> inTypes = new ArrayList<>();
	private List<String> inAddTypes = new ArrayList<>();

	List<Type> existTypesProd;
	List<String> existNameTypesProd = new ArrayList<String>();

	private ProductRegPage page;
	private String descLimit = "Здесь вводится какое количество продуктов в год можно ввести в страну и еденицы измерения.";
	private String descName = "Здесь вводится конкретное название(марка) продукта. Например \"Lenovo Z510\"";
	private String descPrice = "Цена растаможки за каждую еденицу продукта, при привышении указанного лимита ввоза.";
	private String descStatus = "Статус продукта определяет разрешение на ввоз в страну.";
	private String descTypes = "Типы продукта. Типов может быть несколько.Паррент типы добавляются автоматически.";
	private String descDellType = "Удалить добавленный тип";
	private String descDellExType = "Удалить существующий тип";
	private String saveProd = "Сохранить продукт";
	private String addType = "Добавить тип";
	private String descLink = "На страницу с продуктами.";

	public RegistryProductPanel(String id, ProductRegPage page) {
		super(id);
		product = new Product();
		existTypesProd = new ArrayList<>();
		this.page = page;
	}

	public RegistryProductPanel(String id, Product product, ProductRegPage page) {
		super(id);
		this.product = product;
		existTypesProd = this.product.getTypes();
		this.page = page;
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
		limit.add(new PatternValidator("[0-9]+ [а-я]+/[а-я]+"));
		limit.add(new CoverTooltipBehavior(descLimit, null));
		form.add(limit);

		TextField<String> nameProduct = new TextField<String>("nameProduct");
		nameProduct.setRequired(true);
		nameProduct.add(StringValidator.maximumLength(90));
		nameProduct.add(StringValidator.minimumLength(2));
		nameProduct.add(new CoverTooltipBehavior(descName, null));
		form.add(nameProduct);

		TextField<BigDecimal> priceProd = new TextField<BigDecimal>("priceProduct");
		priceProd.setRequired(true);
		priceProd.add(RangeValidator.<BigDecimal> range(new BigDecimal(0), new BigDecimal(1_000_000_000_000_000.00)));
		priceProd.add(new CoverTooltipBehavior(descPrice, null));
		form.add(priceProd);

		RadioChoice<Boolean> choice = new RadioChoice<>("status", STATUS);
		choice.setRequired(true);
		choice.add(new CoverTooltipBehavior(descStatus, null));
		form.add(choice);

		form.add(new Button("sendButton") {
			@Override
			public void onSubmit() {
				product.getTypes().clear();

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
		}.add(new CoverTooltipBehavior(saveProd, null)));

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

		form.add(new Link<ProductPage>("BackToProducts") {
			@Override
			public void onClick() {
				setResponsePage(new ProductPage());
			}
		}.add(new CoverTooltipBehavior(descLink, null)));

	}

	private static Options newOptions() {
		Options options = new Options();
		options.set("track", true);
		options.set("hide", "{ effect: 'drop', delay: 100 }");

		return options;
	}

	class CoverTooltipBehavior extends CustomTooltipBehavior {
		private static final long serialVersionUID = 1L;

		private final String name;
		private final String url;

		public CoverTooltipBehavior(String name, String url) {
			super(newOptions());

			this.name = name;
			this.url = url;
		}

		@Override
		protected WebMarkupContainer newContent(String markupId) {
			Fragment fragment = new Fragment(markupId, "tooltip-fragment", RegistryProductPanel.this);
			fragment.add(new Label("name", Model.of(this.name)));
			// fragment.add(new ContextImage("cover", Model.of(this.url)));

			return fragment;
		}

	}

}
