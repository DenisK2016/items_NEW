package by.dk.training.items.webapp.pages.packages.formforreg;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.RangeValidator;
import org.apache.wicket.validation.validator.StringValidator;

import com.googlecode.wicket.jquery.ui.form.spinner.AjaxSpinner;

import by.dk.training.items.datamodel.Package;
import by.dk.training.items.datamodel.Product;
import by.dk.training.items.datamodel.Recipient;
import by.dk.training.items.datamodel.UserProfile;
import by.dk.training.items.services.PackageService;
import by.dk.training.items.services.ProductService;
import by.dk.training.items.services.RecipientService;
import by.dk.training.items.services.UserProfileService;
import by.dk.training.items.webapp.pages.packages.PackagesPage;

public class RegistryPackPanel extends Panel {

	private static final long serialVersionUID = 1L;

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
	private Integer spin = 0;

	private List<Recipient> allRecipients = recipientService.getAll();
	private List<Long> allIdRecipients = new ArrayList<>();
	private Long idRecipient;

	private List<UserProfile> allUsers = userProfileService.getAll();
	private List<Long> allIdUsers = new ArrayList<>();
	private Long idUser;

	private static final List<Boolean> STATUS = Arrays.asList(new Boolean[] { true, false });

	public RegistryPackPanel(String id) {
		super(id);
		pack = new Package();

	}

	public RegistryPackPanel(String id, Package pack) {
		super(id);
		setPack(pack);

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

	public Long getIdRecipient() {
		return idRecipient;
	}

	public void setIdRecipient(Long idRecipient) {
		this.idRecipient = idRecipient;
	}

	public Long getIdUser() {
		return idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		Form<Package> form = new Form<Package>("formRegPack", new CompoundPropertyModel<Package>(pack));
		form.add(new FeedbackPanel("feedback"));

		TextField<Long> idField = new TextField<Long>("id");
		idField.setRequired(true);
		idField.add(RangeValidator.<Long> range(new Long(0), new Long(1_000_000_000_000_000_000l)));
		form.add(idField);

		for (Recipient rec : allRecipients) {
			allIdRecipients.add(allRecipients.indexOf(rec), rec.getId());
		}
		DropDownChoice<Long> recipient = new DropDownChoice<Long>("idRecipient",
				new PropertyModel<Long>(this, "idRecipient"), allIdRecipients);
		recipient.setNullValid(true);
		form.add(recipient);

		TextField<BigDecimal> price = new TextField<BigDecimal>("price");
		price.setRequired(true);
		price.add(RangeValidator.<BigDecimal> range(new BigDecimal(0), new BigDecimal(1_000_000_000)));
		form.add(price);

		TextField<Double> weight = new TextField<Double>("weight");
		weight.setRequired(true);
		weight.add(RangeValidator.<Double> range(0d, 1_000_000_000d));
		form.add(weight);

		for (UserProfile user : allUsers) {
			allIdUsers.add(allUsers.indexOf(user), user.getId());
		}
		DropDownChoice<Long> choiceUser = new DropDownChoice<Long>("idUser", new PropertyModel<Long>(this, "idUser"),
				allIdUsers);
		choiceUser.setNullValid(true);
		form.add(choiceUser);

		DateTextField receivedField = new DateTextField("date");
		pack.setDate(new Date());
		receivedField.add(new DatePicker());
		receivedField.setRequired(true);
		form.add(receivedField);

		TextArea<String> description = new TextArea<String>("description");
		description.setRequired(true);
		description.add(StringValidator.maximumLength(500));
		description.add(StringValidator.minimumLength(2));
		form.add(description);

		TextField<String> countrySender = new TextField<String>("countrySender");
		countrySender.setRequired(true);
		countrySender.add(StringValidator.maximumLength(500));
		countrySender.add(StringValidator.minimumLength(2));
		form.add(countrySender);

		TextField<String> paymentDeadline = new TextField<String>("paymentDeadline");
		paymentDeadline.setRequired(true);
		paymentDeadline.add(StringValidator.maximumLength(100));
		paymentDeadline.add(StringValidator.minimumLength(2));
		form.add(paymentDeadline);

		TextField<BigDecimal> fine = new TextField<BigDecimal>("fine");
		fine.setRequired(true);
		fine.add(RangeValidator.<BigDecimal> range(new BigDecimal(0), new BigDecimal(1_000_000_000)));
		form.add(fine);

		RadioChoice<Boolean> choice = new RadioChoice<Boolean>("paid", STATUS);
		choice.setRequired(true);
		form.add(choice);

		form.add(new SubmitLink("savePack") {
			@Override
			public void onSubmit() {
				super.onSubmit();
				pack.setIdRecipient(recipientService.getRecipient(idRecipient));
				pack.setIdUser(userProfileService.getUser(idUser));
				for (String s : listProd) {
					int indexProd = allNameProducts.indexOf(s);
					Product product = allProducts.get(indexProd);
					pack.getProducts().add(product);
				}
				if (pack.getId() != null) {
					packageService.update(pack);
				} else {
					packageService.register(pack);
				}
				setResponsePage(new PackagesPage());
			}
		});

		Form formProd = new Form<>("formProd");
		for (Product pr : allProducts) {
			allNameProducts.add(allProducts.indexOf(pr), pr.getNameProduct());
		}
		DropDownChoice<String> choiceProduct = new DropDownChoice<>("choiceProduct",
				new PropertyModel<String>(this, "inProducts"), allNameProducts);
		formProd.add(choiceProduct);

		formProd.add(new Button("selectProduct") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				if (inProducts != null) {
					for (int i = 0; i < spin; i++) {
						listProd.add(inProducts);
					}
				}

			}
		});

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
		});
		
		Form<Integer> formSpin = new Form<Integer>("formSpin", Model.of(0));
		add(formSpin);
		
		final AjaxSpinner<Integer> ajaxSpinner = new AjaxSpinner<Integer>("spinner", formSpin.getModel(),
				Integer.class) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onSpin(AjaxRequestTarget target, Integer value)
			{
				
			}
			
		};
	}

}
