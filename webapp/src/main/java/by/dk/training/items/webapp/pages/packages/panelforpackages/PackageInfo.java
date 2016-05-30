package by.dk.training.items.webapp.pages.packages.panelforpackages;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.googlecode.wicket.jquery.ui.markup.html.link.AjaxLink;

import by.dk.training.items.dataaccess.filters.PackageFilter;
import by.dk.training.items.datamodel.Package;
import by.dk.training.items.datamodel.Product;
import by.dk.training.items.services.PackageService;

public class PackageInfo extends Panel {

	private ModalWindow modalWindow;
	@Inject
	private PackageService packageService;
	private Package pack;
	private PackageFilter packageFilter;

	public PackageInfo(ModalWindow modalWindow, Package pack) {
		super(modalWindow.getContentId());
		packageFilter = new PackageFilter();
		packageFilter.setFetchProduct(true);
		packageFilter.setFetchRecipient(true);
		packageFilter.setFetchUser(true);
		packageFilter.setId(pack.getId());
		this.pack = packageService.find(packageFilter).get(0);
		this.modalWindow = modalWindow;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		add(new Label("id", pack.getId()));
		add(new Label("idRec", pack.getIdRecipient().getId()));
		add(new Label("price", pack.getPrice()));
		add(new Label("weight", pack.getWeight()));
		add(new Label("idUser", pack.getIdUser().getId()));
		add(DateLabel.forDatePattern("date", Model.of(pack.getDate()), "dd-MM-yyyy"));
		add(new Label("descr", pack.getDescription()));
		add(new Label("country", pack.getCountrySender()));
		add(new Label("payment", pack.getPaymentDeadline()));
		add(new Label("fine", pack.getFine()));
		add(new Label("paid", pack.getPaid()));

		List<String> listProduct = new ArrayList<>();
		for (Product p : pack.getProducts()) {
			listProduct.add(p.getNameProduct());
		}
		add(new DropDownChoice<String>("products", listProduct).setNullValid(true));

		add(new AjaxLink<Void>("back") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				modalWindow.close(target);
			}
		});

	}

}
