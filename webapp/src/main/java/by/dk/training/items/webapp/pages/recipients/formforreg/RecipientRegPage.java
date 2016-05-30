package by.dk.training.items.webapp.pages.recipients.formforreg;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;

import by.dk.training.items.datamodel.Recipient;
import by.dk.training.items.webapp.pages.AbstractPage;

@AuthorizeInstantiation(value = { "ADMIN", "OFFICER", "COMMANDER" })
public class RecipientRegPage extends AbstractPage {

	private static final long serialVersionUID = 1L;
	private Recipient recipient;

	public RecipientRegPage() {
		super();

	}

	public RecipientRegPage(Recipient recipient) {
		super();
		this.recipient = recipient;

	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		if (recipient != null) {
			add(new RegistryRecipientPanel("regPanRec", recipient));
		} else {
			add(new RegistryRecipientPanel("regPanRec"));
		}

	}
}
