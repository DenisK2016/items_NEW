package by.dk.training.items.webapp.pages.recipients;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;

import by.dk.training.items.webapp.pages.AbstractPage;
import by.dk.training.items.webapp.pages.recipients.formforreg.RegistryRecipientPanel;
import by.dk.training.items.webapp.pages.recipients.panelforrecipients.ListRecipientsPanel;

@AuthorizeInstantiation(value = { "ADMIN", "OFFICER", "COMMANDER" })
public class RecipientPage extends AbstractPage {

	private static final long serialVersionUID = 1L;

	public RecipientPage() {
		super();

	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		final ModalWindow modalCreate = new ModalWindow("modalCreate");
		modalCreate.setCssClassName("modal_window");
		modalCreate.setResizable(false);
		modalCreate.setWindowClosedCallback(new WindowClosedCallback() {

			@Override
			public void onClose(AjaxRequestTarget target) {
				target.add(RecipientPage.this);

			}
		});
		this.setOutputMarkupId(true);
		add(modalCreate);
		add(new ListRecipientsPanel("list-panel"));
		add(new AjaxLink<Void>("linkToReg") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				modalCreate.setContent(new RegistryRecipientPanel(modalCreate));
				modalCreate.show(target);
			}
		}.add(AttributeModifier.append("title", "Создать получателя")));
	}
}
