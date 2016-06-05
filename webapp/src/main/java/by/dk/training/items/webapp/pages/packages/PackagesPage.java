package by.dk.training.items.webapp.pages.packages;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.markup.html.link.Link;

import by.dk.training.items.webapp.app.AuthorizedSession;
import by.dk.training.items.webapp.pages.AbstractPage;
import by.dk.training.items.webapp.pages.packages.formforreg.PackRegPage;
import by.dk.training.items.webapp.pages.packages.panelforpackages.ListPackagesOfficer;
import by.dk.training.items.webapp.pages.packages.panelforpackages.ListPackagesPanel;
import by.dk.training.items.webapp.pages.packages.setting.SystemSettings;

@AuthorizeInstantiation(value = { "ADMIN", "OFFICER", "COMMANDER" })
public class PackagesPage extends AbstractPage {

	private static final long serialVersionUID = 1L;
	boolean admin = AuthorizedSession.get().getRoles().contains("ADMIN");
	boolean commander = AuthorizedSession.get().getRoles().contains("COMMANDER");
	boolean officer = AuthorizedSession.get().getRoles().contains("OFFICER");

	public PackagesPage() {
		super();

	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		if (admin || commander) {
			add(new ListPackagesPanel("list-panel"));
		} else {
			add(new ListPackagesOfficer("list-panel"));
		}

		add(new Link<PackRegPage>("CreatePack") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new PackRegPage());
			}
		});

		final ModalWindow modalSettings = new ModalWindow("modalSettings");
		modalSettings.setCssClassName("modal_window");
		modalSettings.setInitialHeight(500);
		modalSettings.setResizable(false);
		modalSettings.setWindowClosedCallback(new WindowClosedCallback() {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target) {
				target.add(PackagesPage.this);

			}
		});
		this.setOutputMarkupId(true);
		add(modalSettings);

		AjaxLink<Void> linkSettings = new AjaxLink<Void>("settings") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				modalSettings.setContent(new SystemSettings(modalSettings));
				modalSettings.show(target);
			}
		};
		if (admin || officer) {
			linkSettings.setVisible(false);
		}
		add(linkSettings);
	}
}
