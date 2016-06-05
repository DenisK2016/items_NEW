package by.dk.training.items.webapp.pages.recipients.formforreg;

import javax.inject.Inject;

import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;

import by.dk.training.items.datamodel.Recipient;
import by.dk.training.items.services.RecipientService;
import by.dk.training.items.webapp.app.AuthorizedSession;
import by.dk.training.items.webapp.pages.recipients.RecipientPage;

@AuthorizeAction(roles = { "ADMIN", "COMMANDER", "OFFICER" }, action = Action.RENDER)
public class RegistryRecipientPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private Recipient recipient;
	@Inject
	private RecipientService recipientService;
	private ModalWindow modalWindow;

	public RegistryRecipientPanel(ModalWindow modalWindow) {
		super(modalWindow.getContentId());
		this.modalWindow = modalWindow;
		recipient = new Recipient();
	}

	public RegistryRecipientPanel(ModalWindow modalWindow, Recipient recipient) {
		super(modalWindow.getContentId());
		this.modalWindow = modalWindow;
		this.recipient = recipient;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		Form<?> form = new Form<>("formRegRec", new CompoundPropertyModel<Recipient>(recipient));
		form.add(new FeedbackPanel("feedback"));

		TextField<String> name = new TextField<String>("name");
		name.setRequired(true);
		name.add(StringValidator.maximumLength(100));
		name.add(StringValidator.minimumLength(2));
		name.add(new PatternValidator("[А-Я а-я]+"));
		form.add(name);

		TextField<String> city = new TextField<String>("city");
		city.setRequired(true);
		city.add(StringValidator.maximumLength(100));
		city.add(StringValidator.minimumLength(2));
		city.add(new PatternValidator("[А-Я а-я]+"));
		form.add(city);

		TextField<String> address = new TextField<String>("address");
		address.setRequired(true);
		address.add(StringValidator.maximumLength(100));
		address.add(StringValidator.minimumLength(2));
		address.add(new PatternValidator("[А-Яа-я, ./0-9]+"));
		form.add(address);

		form.add(new SubmitLink("sendButton") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {

				if (recipient.getId() == null) {
					recipient.setIdUser(AuthorizedSession.get().getUser());
					recipientService.register(recipient);
				} else {
					recipientService.update(recipient);
				}
				setResponsePage(new RecipientPage());
			}
		});

		add(form);

		form.add(new Link<RecipientPage>("BackToRecipients") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new RecipientPage());
			}
		});

		if (recipient.getId() == null) {
			form.add(new Label("regOrUpdate", "Регистрация нового получателя."));
		} else {
			form.add(new Label("regOrUpdate", "Изменение получателя."));
		}

	}

}
