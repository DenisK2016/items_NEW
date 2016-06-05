package by.dk.training.items.webapp.pages.users;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;

import by.dk.training.items.webapp.pages.AbstractPage;
import by.dk.training.items.webapp.pages.users.formforreg.RegistryUserPanel;
import by.dk.training.items.webapp.pages.users.panelforusers.ListUsersPanel;

@AuthorizeInstantiation(value = { "ADMIN" })
public class UserPage extends AbstractPage {

	public UserPage() {
		super();

	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		add(new ListUsersPanel("list-panel"));
		final ModalWindow modalCreate = new ModalWindow("modalCreate");
		modalCreate.setCssClassName("modal_window");
		modalCreate.setResizable(false);
		modalCreate.setWindowClosedCallback(new WindowClosedCallback() {

			@Override
			public void onClose(AjaxRequestTarget target) {
				target.add(UserPage.this);

			}
		});
		this.setOutputMarkupId(true);
		add(modalCreate);

		AjaxLink create = new AjaxLink<Void>("createUser") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				modalCreate.setContent(new RegistryUserPanel(modalCreate));
				modalCreate.show(target);
			}
		};
		create.add(AttributeModifier.append("title", "Создать пользователя"));
		add(create);
	}
}
