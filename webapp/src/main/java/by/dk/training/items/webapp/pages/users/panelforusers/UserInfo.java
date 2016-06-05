package by.dk.training.items.webapp.pages.users.panelforusers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import by.dk.training.items.dataaccess.filters.ProductFilter;
import by.dk.training.items.dataaccess.filters.RecipientFilter;
import by.dk.training.items.dataaccess.filters.TypeFilter;
import by.dk.training.items.dataaccess.filters.UserFilter;
import by.dk.training.items.datamodel.Package;
import by.dk.training.items.datamodel.Product;
import by.dk.training.items.datamodel.Recipient;
import by.dk.training.items.datamodel.Type;
import by.dk.training.items.datamodel.UserProfile;
import by.dk.training.items.services.PackageService;
import by.dk.training.items.services.ProductService;
import by.dk.training.items.services.RecipientService;
import by.dk.training.items.services.TypeService;
import by.dk.training.items.services.UserProfileService;
import by.dk.training.items.webapp.pages.packages.panelforpackages.PackageInfo;

public class UserInfo extends Panel {

	private ModalWindow modalWindow;
	@Inject
	private UserProfileService userProfileService;
	@Inject
	private PackageService packageService;
	@Inject
	private ProductService productService;
	@Inject
	private TypeService typeService;
	@Inject
	private RecipientService recipientService;
	private UserProfile userProfile;
	private UserFilter userFilter;
	private String inPack;
	private ProductFilter productFilter;
	private TypeFilter typeFilter;
	private RecipientFilter recipientFilter;

	public UserInfo(ModalWindow modalWindow, UserProfile userProfile) {
		super(modalWindow.getContentId());
		userFilter = new UserFilter();
		userFilter.setFetchCredentials(true);
		userFilter.setFetchPackages(true);
		userFilter.setId(userProfile.getId());
		this.userProfile = userProfileService.find(userFilter).get(0);
		this.modalWindow = modalWindow;

		productFilter = new ProductFilter();
		productFilter.setUser(this.userProfile);
		typeFilter = new TypeFilter();
		typeFilter.setUser(userProfile);
		recipientFilter = new RecipientFilter();
		recipientFilter.setUser(userProfile);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize(); 
		final ModalWindow modal2 = new ModalWindow("modal2");
		modal2.setCssClassName("modal_window");
		modal2.setInitialHeight(500);
		modal2.setResizable(false);
		modal2.setWindowClosedCallback(new WindowClosedCallback() {

			@Override
			public void onClose(AjaxRequestTarget target) {
				target.add(UserInfo.this);

			}
		});
		this.setOutputMarkupId(true);
		add(modal2);

		add(new Label("id", userProfile.getId()));
		add(new Label("login", userProfile.getLogin()));
		add(new Label("fName", userProfile.getUserCredentials().getFirstName()));
		add(new Label("lName", userProfile.getUserCredentials().getLastName()));
		add(DateLabel.forDatePattern("usercreated", Model.of(userProfile.getUserCredentials().getCreated()),
				"dd-MM-yyyy"));
		add(new Label("status", userProfile.getUserCredentials().getStatus()));
		add(new Label("post", userProfile.getUserCredentials().getPost()));
		add(new Label("rank", userProfile.getUserCredentials().getRank()));
		add(new Label("mail", userProfile.getUserCredentials().getEmail()));

		List<String> listPack = new ArrayList<>();
		for (Package p : userProfile.getPackages()) {
			listPack.add(String.valueOf(p.getId()));
		}

		add(new DropDownChoice<String>("packs", new PropertyModel<String>(this, "inPack"), listPack)
				.add(new AjaxFormComponentUpdatingBehavior("onchange") {

					@Override
					protected void onUpdate(AjaxRequestTarget target) {

					}
				}).setOutputMarkupId(true));

		add(new AjaxLink<Void>("infoPackage") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				modal2.setContent(new PackageInfo(modal2, packageService.getPackage(Long.valueOf(inPack))));
				modal2.show(target);
			}
		});

		List<String> listPod = new ArrayList<>();
		for (Product p : productService.find(productFilter)) {
			listPod.add(String.format("%d %s", p.getId(), p.getNameProduct()));
		}
		add(new DropDownChoice<String>("listProd", listPod).setNullValid(true));

		List<String> listType = new ArrayList<>();
		for (Type t : typeService.find(typeFilter)) {
			listType.add(String.format("%d %s", t.getId(), t.getTypeName()));
		}
		add(new DropDownChoice<String>("listType", listType).setNullValid(true));

		List<String> listRec = new ArrayList<>();
		for (Recipient r : recipientService.find(recipientFilter)) {
			listRec.add(String.format("%d %s %s %s", r.getId(), r.getName(), r.getCity(), r.getAddress()));
		}
		add(new DropDownChoice<String>("listRec", listRec).setNullValid(true));

		add(new AjaxLink<Void>("back") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				modalWindow.close(target);
			}
		});

	}

}
