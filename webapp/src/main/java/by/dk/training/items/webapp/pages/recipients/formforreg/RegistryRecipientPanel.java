package by.dk.training.items.webapp.pages.recipients.formforreg;

import javax.inject.Inject;

import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;

import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.ui.widget.tooltip.CustomTooltipBehavior;

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

	private String descName = "Введите ФИО получателя";
	private String descCity = "Введите город получателя";
	private String descAddress = "Введите адрес получателя";
	private String descSubmit = "Сохранить получателя";
	private String descLink = "К списку получателей";

	public RegistryRecipientPanel(String id) {
		super(id);
		recipient = new Recipient();
	}

	public RegistryRecipientPanel(String id, Recipient recipient) {
		super(id);
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
		name.add(new PatternValidator("[А-Яа-я]+"));
		name.add(new CoverTooltipBehavior(descName, null));
		form.add(name);

		TextField<String> city = new TextField<String>("city");
		city.setRequired(true);
		city.add(StringValidator.maximumLength(100));
		city.add(StringValidator.minimumLength(2));
		city.add(new PatternValidator("[А-Яа-я]+"));
		city.add(new CoverTooltipBehavior(descCity, null));
		form.add(city);

		TextField<String> address = new TextField<String>("address");
		address.setRequired(true);
		address.add(StringValidator.maximumLength(100));
		address.add(StringValidator.minimumLength(2));
		address.add(new PatternValidator("[А-Яа-я, .-/0-9]+"));
		address.add(new CoverTooltipBehavior(descAddress, null));
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
		}.add(new CoverTooltipBehavior(descSubmit, null)));

		add(form);

		add(new Link<RecipientPage>("BackToRecipients") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new RecipientPage());
			}
		}.add(new CoverTooltipBehavior(descLink, null)));

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
			Fragment fragment = new Fragment(markupId, "tooltip-fragment", RegistryRecipientPanel.this);
			fragment.add(new Label("name", Model.of(this.name)));
			// fragment.add(new ContextImage("cover", Model.of(this.url)));

			return fragment;
		}

	}

}
