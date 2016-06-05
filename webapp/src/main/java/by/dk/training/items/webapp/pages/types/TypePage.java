package by.dk.training.items.webapp.pages.types;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;

import by.dk.training.items.webapp.app.AuthorizedSession;
import by.dk.training.items.webapp.pages.AbstractPage;
import by.dk.training.items.webapp.pages.types.formforreg.RegistryTypePanel;
import by.dk.training.items.webapp.pages.types.panelfortypes.ListTypesPanel;

@AuthorizeInstantiation(value = { "ADMIN", "OFFICER", "COMMANDER" })
public class TypePage extends AbstractPage {

	private static final long serialVersionUID = 1L;
	boolean admin = AuthorizedSession.get().getRoles().contains("ADMIN");
	boolean commander = AuthorizedSession.get().getRoles().contains("COMMANDER");
	boolean officer = AuthorizedSession.get().getRoles().contains("OFFICER");

	public TypePage() {
		super();

	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		add(new ListTypesPanel("list-panel"));

		final ModalWindow modalCreate = new ModalWindow("modalCreate");
		modalCreate.setCssClassName("modal_window");
		modalCreate.setResizable(false);
		modalCreate.setWindowClosedCallback(new WindowClosedCallback() {

			@Override
			public void onClose(AjaxRequestTarget target) {
				target.add(TypePage.this);

			}
		});
		this.setOutputMarkupId(true);
		add(modalCreate);
		AjaxLink create = new AjaxLink<Void>("CreateType") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				modalCreate.setContent(new RegistryTypePanel(modalCreate));
				modalCreate.show(target);
			}
		};
		create.add(AttributeModifier.append("title", "Создать тип"));
		add(create);

		final ModalWindow modal2 = new ModalWindow("modal2");
		modal2.setTitle("Дерево типов");
		modal2.setWindowClosedCallback(new WindowClosedCallback() {

			@Override
			public void onClose(AjaxRequestTarget target) {
				target.add(TypePage.this);

			}
		});
		this.setOutputMarkupId(true);
		add(modal2);
	}
}
