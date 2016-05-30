package by.dk.training.items.webapp.pages.packages.formforreg;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.RangeValidator;
import org.apache.wicket.validation.validator.StringValidator;

import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.ui.form.spinner.AjaxSpinner;
import com.googlecode.wicket.jquery.ui.widget.tooltip.CustomTooltipBehavior;

import by.dk.training.items.dataaccess.filters.ProductFilter;
import by.dk.training.items.dataaccess.filters.UserFilter;
import by.dk.training.items.datamodel.Package;
import by.dk.training.items.datamodel.Product;
import by.dk.training.items.datamodel.Recipient;
import by.dk.training.items.datamodel.UserProfile;
import by.dk.training.items.services.PackageService;
import by.dk.training.items.services.ProductService;
import by.dk.training.items.services.RecipientService;
import by.dk.training.items.services.UserProfileService;
import by.dk.training.items.webapp.app.AuthorizedSession;
import by.dk.training.items.webapp.pages.packages.PackagesPage;

@AuthorizeAction(roles = { "ADMIN", "COMMANDER", "OFFICER" }, action = Action.RENDER)
public class RegistryPackPanel extends Panel {

	private static final long serialVersionUID = 1L;
	boolean officer = AuthorizedSession.get().getRoles().contains("OFFICER");

	private Package pack;
	@Inject
	private PackageService packageService;
	@Inject
	private RecipientService recipientService;
	@Inject
	private UserProfileService userProfileService;
	@Inject
	private ProductService productService;

	private List<Product> allProducts = productService.getAll();
	private List<String> allNameProducts = new ArrayList<>();
	private String inProducts;
	private List<String> listProd = new ArrayList<>();
	private List<String> existProducts = new ArrayList<>();
	private Integer spin = 1;

	private List<Recipient> allRecipients = recipientService.getAll();
	private List<String> allIdRecipients = new ArrayList<>();
	private String idRecipient;
	private Recipient recipient;

	private List<UserProfile> allUsers = userProfileService.getAll();
	private List<String> allIdUsers = new ArrayList<>();
	private String idUser;

	private ProductFilter productFilter;
	private UserFilter userFilter;
	private UserProfile userProfile;

	private String descTrack = "Уникальный трекинг-код посылкки";
	private String descRecipient = "Выберите получателя";
	private String descPrice = "Цена растаможки все посылки. Может определяться автоматически.";
	private String descWeight = "Вес всей посылки";
	private String descUser = "Выберите инспектора который принял посылку";
	private String descDate = "Дата прихода посылки";
	private String descDescript = "Дополнительное описание посылки";
	private String descCountry = "Страна отправитель посылки";
	private String descPayment = "Срок оплаты посылки в днях.";
	private String descFine = "Штраф за просроченную оплату";
	private String descPaid = "Оплачена или нет посылка";
	private String descSubmit = "Сохранить посылку";
	private String descProduct = "Выберите продукты которые содержатся в посылке";
	private String descSelectProd = "Добавить продукт в пакет.";
	private String descLink = "К списку пакетов";
	private String descSpinner = "Выберите количество продукта";

	private static final List<Boolean> STATUS = Arrays.asList(new Boolean[] { true, false });

	public RegistryPackPanel(String id) {
		super(id);
		pack = new Package();

	}

	public RegistryPackPanel(String id, Package pack) {
		super(id);
		setPack(pack);
		idRecipient = String.format("%d %s %s %s", pack.getIdRecipient().getId(), pack.getIdRecipient().getName(),
				pack.getIdRecipient().getCity(), pack.getIdRecipient().getAddress());
		idUser = String.format("%d %s", pack.getIdUser().getId(), pack.getIdUser().getLogin());

	}

	public Integer getSpin() {
		return spin;
	}

	public void setSpin(Integer spin) {
		this.spin = spin;
	}

	public Package getPack() {
		return pack;
	}

	public void setPack(Package pack) {
		this.pack = pack;
	}

	public String getInProducts() {
		return inProducts;
	}

	public void setInProducts(String inProducts) {
		this.inProducts = inProducts;
	}

	public String getIdRecipient() {
		return idRecipient;
	}

	public void setIdRecipient(String idRecipient) {
		this.idRecipient = idRecipient;
	}

	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		userFilter = new UserFilter();
		userFilter.setFetchCredentials(true);
		userFilter.setLogin(AuthorizedSession.get().getUser().getLogin());
		userProfile = userProfileService.find(userFilter).get(0);
		Form<Package> form = new Form<Package>("formRegPack", new CompoundPropertyModel<Package>(pack));
		form.add(new FeedbackPanel("feedback"));

		TextField<Long> idField = new TextField<Long>("id");
		idField.setRequired(true);
		idField.add(RangeValidator.<Long> range(new Long(0), new Long(1_000_000_000_000_000_000l)));
		idField.add(new CoverTooltipBehavior(descTrack, null));
		form.add(idField);
		if (pack.getId() != null) {
			idField.setEnabled(false);
		}

		for (Recipient rec : allRecipients) {
			String infoRecipient = String.format("%d %s %s %s", rec.getId(), rec.getName(), rec.getCity(),
					rec.getAddress());
			allIdRecipients.add(allRecipients.indexOf(rec), infoRecipient);
		}
		DropDownChoice<String> recipients = new DropDownChoice<String>("idRecipient",
				new PropertyModel<String>(this, "idRecipient"), allIdRecipients);
		recipients.setNullValid(true);

		recipients.setRequired(true);
		recipients.add(new CoverTooltipBehavior(descRecipient, null));
		form.add(recipients);

		TextField<BigDecimal> price = new TextField<BigDecimal>("price");
		price.setRequired(true);
		price.add(RangeValidator.<BigDecimal> range(new BigDecimal(0), new BigDecimal(1_000_000_000)));
		price.add(new CoverTooltipBehavior(descPrice, null));
		form.add(price);
		if (userProfile.getUserCredentials().getStatus().name().equals("OFFICER")) {
			price.setEnabled(false);
		}

		TextField<Double> weight = new TextField<Double>("weight");
		weight.setRequired(true);
		weight.add(RangeValidator.<Double> range(0d, 1_000_000_000d));
		weight.add(new CoverTooltipBehavior(descWeight, null));
		form.add(weight);

		for (UserProfile user : allUsers) {
			String infoUser = String.format("%d %s", user.getId(), user.getLogin());
			allIdUsers.add(allUsers.indexOf(user), infoUser);
		}
		DropDownChoice<String> choiceUser = new DropDownChoice<String>("idUser",
				new PropertyModel<String>(this, "idUser"), allIdUsers);
		choiceUser.setNullValid(true);
		choiceUser.setRequired(true);
		choiceUser.add(new CoverTooltipBehavior(descUser, null));
		form.add(choiceUser);
		if (officer) {
			choiceUser.setVisible(false);
		}

		DateTextField receivedField = new DateTextField("date");
		pack.setDate(new Date());
		receivedField.add(new DatePicker());
		receivedField.setRequired(true);
		receivedField.add(new CoverTooltipBehavior(descDate, null));
		form.add(receivedField);

		TextField<String> description = new TextField<String>("description");
		description.add(new CoverTooltipBehavior(descDescript, null));
		form.add(description);

		TextField<String> countrySender = new TextField<String>("countrySender");
		countrySender.setRequired(true);
		countrySender.add(StringValidator.maximumLength(100));
		countrySender.add(StringValidator.minimumLength(2));
		countrySender.add(new CoverTooltipBehavior(descCountry, null));
		form.add(countrySender);

		TextField<String> paymentDeadline = new TextField<String>("paymentDeadline");
		paymentDeadline.setRequired(true);
		paymentDeadline.add(new PatternValidator("[0-9]+"));
		paymentDeadline.add(new CoverTooltipBehavior(descPayment, null));
		form.add(paymentDeadline);

		TextField<BigDecimal> fine = new TextField<BigDecimal>("fine");
		fine.setRequired(true);
		fine.add(RangeValidator.<BigDecimal> range(new BigDecimal(0), new BigDecimal(1_000_000_000)));
		fine.add(new CoverTooltipBehavior(descFine, null));
		form.add(fine);

		RadioChoice<Boolean> choice = new RadioChoice<Boolean>("paid", STATUS);
		choice.setRequired(true);
		choice.add(new CoverTooltipBehavior(descPaid, null));
		form.add(choice);

		form.add(new SubmitLink("savePack") {
			@Override
			public void onSubmit() {
				super.onSubmit();
				Long longIdRecipient = Long.valueOf(idRecipient.substring(0, idRecipient.indexOf(" ")));
				pack.setIdRecipient(recipientService.getRecipient(longIdRecipient));
				for (String s : listProd) {
					int indexProd = allNameProducts.indexOf(s);
					Product product = allProducts.get(indexProd);
					pack.getProducts().add(product);
				}
				if (packageService.getPackage(pack.getId()) != null) {
					Long longIdUser = Long.valueOf(idUser.substring(0, idUser.indexOf(" ")));
					pack.setIdUser(userProfileService.getUser(longIdUser));
					packageService.update(pack);
				} else {
					if (officer) {
						pack.setIdUser(AuthorizedSession.get().getUser());
					} else {
						Long longIdUser = Long.valueOf(idUser.substring(0, idUser.indexOf(" ")));
						pack.setIdUser(userProfileService.getUser(longIdUser));
					}
					packageService.register(pack);
				}
				setResponsePage(new PackagesPage());
			}
		}.add(new CoverTooltipBehavior(descSubmit, null)));

		Form<Integer> formProd = new Form<Integer>("formProd", Model.of(0));
		for (Product pr : allProducts) {
			allNameProducts.add(allProducts.indexOf(pr), pr.getNameProduct());
		}
		DropDownChoice<String> choiceProduct = new DropDownChoice<>("choiceProduct",
				new PropertyModel<String>(this, "inProducts"), allNameProducts);
		choiceProduct.add(new CoverTooltipBehavior(descProduct, null));
		formProd.add(choiceProduct);

		Button selProd = new Button("selectProduct") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				if (inProducts != null) {
					// Long longIdRecipient = Long.valueOf(
					// form.getString("recipients").substring(0,
					// form.getString("recipients").indexOf(" ")));
					// recipient =
					// recipientService.getRecipient(longIdRecipient);
					// int t = 0;
					// for (Package p : recipient.getPackages()) {
					// if (p.getDate().before(new
					// Date(System.currentTimeMillis() - (365l * 24 * 60 * 60 *
					// 1000)))) {
					// continue;
					// } else {
					// for (Product pr : p.getProducts()) {
					// if (pr.getNameProduct().equals(inProducts)) {
					// t++;
					// break;
					// }
					// }
					// }
					// if (t != 0) {
					// break;
					// }
					// }
					// if (t == 0 && !listProd.contains(inProducts)) {
					// spin--;
					// }
					for (int i = 0; i < spin; i++) {
						productFilter = new ProductFilter();
						productFilter.setNameProduct(inProducts);
						BigDecimal priceProduct = productService.find(productFilter).get(0).getPriceProduct();
						BigDecimal addPrice = pack.getPrice().add(priceProduct);
						pack.setPrice(addPrice);
						listProd.add(inProducts);
					}
				}

			}
		};
		selProd.add(new CoverTooltipBehavior(descSelectProd, null));
		formProd.add(selProd);

		ListView<String> addProd = new ListView<String>("addsProd", listProd) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<String> item) {
				item.add(new Label("addProd", item.getModel()));
				item.add(new Link<String>("deleteProd", item.getModel()) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						String nameProd = getModelObject();

						listProd.remove(nameProd);
					}
				});
			}
		};
		formProd.add(addProd);

		if (pack != null && pack.getProducts() != null) {
			for (Product p : pack.getProducts()) {
				existProducts.add(p.getNameProduct());
			}
		}
		ListView<String> existProd = new ListView<String>("existsProd", existProducts) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<String> item) {
				item.add(new Label("existProd", item.getModel()));
				item.add(new Link<String>("delExistProd", item.getModel()) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						String nameProd = getModelObject();

						for (int i = 0; i < pack.getProducts().size(); i++) {
							if (pack.getProducts().get(i).getNameProduct().equals(nameProd)) {
								pack.getProducts().remove(i);
								break;
							}
						}
						packageService.update(pack);
						setResponsePage(new PackRegPage(pack));
					}

				});
			}

		};
		formProd.add(existProd);

		add(formProd);
		add(form);

		add(new Link<PackagesPage>("BackToPackages") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new PackagesPage());

			}
		}.add(new CoverTooltipBehavior(descLink, null)));

		final AjaxSpinner<Integer> ajaxSpinner = new AjaxSpinner<Integer>("spinner", formProd.getModel(),
				Integer.class) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onSpin(AjaxRequestTarget target, Integer value) {
				spin = value;
			}

		};
		ajaxSpinner.add(new CoverTooltipBehavior(descSpinner, null));

		formProd.add(ajaxSpinner);

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
			Fragment fragment = new Fragment(markupId, "tooltip-fragment", RegistryPackPanel.this);
			fragment.add(new Label("name", Model.of(this.name)));
			// fragment.add(new ContextImage("cover", Model.of(this.url)));

			return fragment;
		}

	}

}
