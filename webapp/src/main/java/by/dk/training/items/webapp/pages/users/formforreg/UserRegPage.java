package by.dk.training.items.webapp.pages.users.formforreg;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;

import by.dk.training.items.datamodel.UserProfile;
import by.dk.training.items.webapp.pages.AbstractPage;

@AuthorizeInstantiation(value = { "ADMIN", "OFFICER", "COMMANDER" })
public class UserRegPage extends AbstractPage {

	private static final long serialVersionUID = 1L;
	private UserProfile userProfile;

	public UserRegPage() {
		super();

	}

	public UserRegPage(UserProfile userProfile) {
		super();
		this.userProfile = userProfile;

	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		if (userProfile != null) {
			add(new RegistryUserPanel("regPanUser", userProfile));
		} else {
			add(new RegistryUserPanel("regPanUser"));
		}

	}
}
